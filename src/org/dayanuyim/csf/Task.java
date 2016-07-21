package org.dayanuyim.csf;

import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 
@Path("/task")
public class Task {
 
    @GET
    public String sayHelloWorld() {
        return "[task] Echo";
    }   
 
    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String upload(FormDataMultiPart part){
    	try{
    		printMultiPart(part);
    		writeResultToAmq(part);
    		return toJsonResult("SUCCESS", null, null, null, null);
    	}
    	catch(Exception ex){
    		return toJsonResult("FAIL", null, null, null, ex.toString());
    	}
    }
    
    private void writeResultToAmq(FormDataMultiPart part) throws JMSException
    {
    	//config
    	String uri = "failover:(tcp://localhost:61616)?randomize=false";
    	String queue_name = "csfResult";
    	
    	//data
		String taskId = part.getField("taskId").getValue();
		String[] links = new String[]{
				"http://localhost:7080/sample-en.pdf",
				"http://localhost:7080/sample-cht.pdf"
		};
		String result = toJsonResult("SUCCESS", taskId, links, "rawdata", null);

		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(uri);
		Connection conn = factory.createConnection();
		conn.start();
		try{
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue(queue_name);
			MessageProducer producer = session.createProducer(dest);
			
			TextMessage msg = session.createTextMessage();
			msg.setText(result);
			producer.send(msg);
		}
		finally{
			conn.close();
		}
    }
    
	public static void printMultiPart(FormDataMultiPart part)
	{
			System.out.println(String.format("[%s]", part.getMediaType().toString()));
			for(Map.Entry<String, List<FormDataBodyPart>> field: part.getFields().entrySet()){

				String key = field.getKey();
				System.out.println(key);

				List<FormDataBodyPart> vals = field.getValue();
				for(FormDataBodyPart val: vals)
					System.out.println(String.format("  [%s]: %s",
							val.getMediaType().toString(),
							val.getMediaType().equals(MediaType.TEXT_PLAIN_TYPE)? val.getValue(): "(Not Text)"));
			}
	}

    public static String toJsonResult(String status, String taskId, String[] links, String data, String message)
    {
		JSONObject json = new JSONObject();
		json.put("status", "SUCCESS");
		if(taskId != null) json.put("taskId", taskId);
		
		if(links != null){
			if(links.length == 1){
				json.put("link", links[0]);
			}
			else if(links.length > 1){
				JSONArray links_obj = new JSONArray();
				for(String link: links)
					links_obj.put(link);
				json.put("link", links_obj);
			}
		}

		if(data != null) json.put("data", buildResponseMsg(true, "2D6A5CF6EBAC5C13EF76EB99909D77E5", ""));
		if(message != null) json.put("message", message);
		
		//test
		JSONObject arg = new JSONObject();
		arg.put("type", "link");
		arg.put("value", "http://localhost:7080/sample.7z");
		arg.put("desc", "log link");
		arg.put("chtDesc", "日誌連結");
		json.put("a_log", arg);

		return json.toString();
    }
    
    //similar as CIA
    public static JSONObject buildResponseMsg(boolean state, String md5, String ec_msg) throws JSONException{
		ec_msg = ec_msg.replaceAll("[\\s\"{}:\\\\]+", " "); 
		md5 = md5.toLowerCase();
		
		JSONObject result_obj = new JSONObject();
		result_obj.put("Response", ec_msg);
		result_obj.put("TYPE", "Result");

		JSONObject md5_obj = new JSONObject();
		md5_obj.put("Result", result_obj);

		JSONObject obj = new JSONObject();
		obj.put("serverstate", state? "Good": "Fail");
		obj.put("result", "Success");
		obj.put(md5, md5_obj);

		return obj;
		//String obj_msg = obj.toString();

		/*
		// **for CIA格式 (為什麼需要？)
		//再加一層 '\' 字元去跳脫, 所有 \ -> \\, " -> \", 頭尾加上 " 引號
		//注意：\\\\ 實際上是 \\, 而對 regex　來是一個 \
		obj_msg =  "\"" + obj_msg.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"") + "\"";
		*/

		//return obj_msg;
	}
}
