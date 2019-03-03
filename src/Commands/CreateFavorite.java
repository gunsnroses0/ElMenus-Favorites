package Commands;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import Model.Favorite;


public class CreateFavorite extends Command {

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		HashMap<String, Object> props = parameters;
		Channel channel = (Channel) props.get("channel");
		JSONParser parser = new JSONParser();

		try {
			String url = ((JSONObject) parser.parse((String) props.get("body"))).get("uri").toString();
			url = url.substring(1);
			String[] parametersArray = url.split("/");
			System.out.println(parametersArray[1]);
			JSONObject messageBody = (JSONObject) parser.parse((String) props.get("body"));
//			HashMap<String, Object> requestBodyHash = jsonToMap((JSONObject) messageBody.get("body"));
			AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
			AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
			Envelope envelope = (Envelope) props.get("envelope");
			System.out.println("hereee");
			String createdMessage = Favorite.create("1",parametersArray[1]);
			
			JSONObject response = (JSONObject) parser.parse(createdMessage);
			
			channel.basicPublish("", properties.getReplyTo(), replyProps, response.toString().getBytes("UTF-8"));
			System.out.println("sasa");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}