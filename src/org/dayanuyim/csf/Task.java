package org.dayanuyim.csf;

import javax.jms.JMSException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.dayanuyim.utils.Utils.*;
import static org.apache.commons.lang3.ArrayUtils.*;
import static org.apache.commons.lang3.StringUtils.*;

enum ServiceStatus {
	SUCCESS,FAIL,WAITING,ERROR
}

 
@Path("/task")
public class Task {

	public static final String AMQ_URI = "failover:(tcp://localhost:61616)?randomize=false";
	public static final String AMQ_QUEUE = "csfResult";
	
	private static Logger logger = LoggerFactory.getLogger(Task.class);
	
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
    		final String taskId = part.getField("taskId").getValue();
    		new Thread(()->{
    				runPseudoProcess(taskId);
				})
    			.start();
    		return toJsonResult(ServiceStatus.SUCCESS, null, null, null, null, null);
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    		return toJsonResult(ServiceStatus.FAIL, null, null, null, null, ex.toString());
    	}
    }
    
    private void runPseudoProcess(String taskId)
    {
    	try {
    		Thread.sleep(1000);
    		logger.info("process task {}, waiting...", taskId);
			sendWaitingStatus(taskId);

    		Thread.sleep(30000);
    		logger.info("process task {}, success...", taskId);
			sendSuccessStatus(taskId);
		}
    	catch (JMSException e) {
			e.printStackTrace();
		}
    	catch(InterruptedException e){
    		System.out.println("pseudo process is interrupted!");
			e.printStackTrace();
    	}
    }

    private void sendWaitingStatus(String taskId) throws JMSException
    {
    	//data
		JSONObject vnc = new JSONObject();
		vnc.put("name", "a_vnc");
		vnc.put("type", "vnc");
		vnc.put("value", "http://192.168.211.136:7000/vnc_auto.html?password=lablab");
		vnc.put("desc", "VNC");
		vnc.put("chtDesc", "VNC\u9023\u7d50");

		String result = toJsonResult(ServiceStatus.WAITING, taskId, null, null, toArray(vnc), null);
    	sendToAmq(AMQ_URI, AMQ_QUEUE, result);
    }

    private void sendSuccessStatus(String taskId) throws JMSException
    {
    	//data
		String[] report_links = new String[]{
				"http://localhost:7080/sample-en.pdf",
				"http://localhost:7080/sample-cht.pdf"
		};
		String report_data = "rawdata";

		JSONObject log = new JSONObject();
		log.put("name", "a_log");
		log.put("type", "link");
		log.put("value", "http://localhost:7080/sample.7z");
		log.put("desc", "log link");
		log.put("chtDesc", "日誌連結");

		String result = toJsonResult(ServiceStatus.SUCCESS, taskId, report_links, report_data, toArray(log), null);
    	sendToAmq(AMQ_URI, AMQ_QUEUE, result);
    }
    
    private static String toJsonResult(ServiceStatus status, String taskId,
    		String[] result_links, String result_data,
    		JSONObject[] args, String message)
    {
		JSONObject json = new JSONObject();

		json.put("status", status.toString());

		//task id
		if(isNotBlank(taskId))
			json.put("taskId", taskId);
		
		//rport links
		int len = getLength(result_links);
		if(len == 1){
			json.put("link", result_links[0]);
		}
		else if(len > 1){
			JSONArray links_obj = new JSONArray();
			for(String link: result_links)
				links_obj.put(link);
			json.put("link", links_obj);
		}
		
		//args
		if(isNotEmpty(args)){
			for(JSONObject arg: args){
				String name = (String)arg.remove("name");
				json.put(name, arg);
			}
		}

		if(isNotBlank(result_data))
			json.put("data", buildResponseMsg(true, "2D6A5CF6EBAC5C13EF76EB99909D77E5", ""));

		if(isNotBlank(message))
			json.put("message", message);
		
		return json.toString();
    }
    
    //similar as CIA
    private static JSONObject buildResponseMsg(boolean state, String md5, String ec_msg) throws JSONException{
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
