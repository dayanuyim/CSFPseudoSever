package org.dayanuyim.utils;

import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.core.MediaType;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

public class Utils
{
 	public static void sendToAmq(String uri, String queue, String text) throws JMSException
	{
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(uri);
		Connection conn = factory.createConnection();
		conn.start();
		try{
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue(queue);
			MessageProducer producer = session.createProducer(dest);
			
			TextMessage msg = session.createTextMessage();
			msg.setText(text);
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


}
