package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bson.Document;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import Commands.Command;

public class Favorite {
	
	private static final String COLLECTION_NAME = "favourites";
	private static MongoCollection<Document> collection = null;
	@SuppressWarnings("null")
	public static HashMap<String, Object> create(HashMap<String, Object>attributes,String user_id,String id) {

		String uri = "mongodb://localhost";

		MongoClient mongoClient = new MongoClient(uri);
		mongoClient = new MongoClient(new MongoClientURI(uri, MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true)));
		MongoDatabase database = mongoClient.getDatabase("El-Menus");
		
		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("favourites");
		Document newFavourite = new Document();
		try{
		newFavourite.put("fav_id", id);
		newFavourite.put("user_id", user_id);
		collection.insertOne(newFavourite);
		System.out.println("hshshssh");
		}
		catch (MongoException | ClassCastException e) {
			System.out.println(e.getMessage());
			System.out.println("ERROR");
		}
		System.out.println(collection.getNamespace());
		System.out.println(newFavourite.get("fav_id"));
		HashMap<String, Object> fav = new HashMap<String, Object>() ;
		fav.put("id", id);
		mongoClient.close();
		return fav;
		
	}


	public static ArrayList<HashMap<String, Object>> get(String favouriteId) {

		String uri = "mongodb://localhost";

		MongoClient mongoClient = new MongoClient(uri);
		mongoClient = new MongoClient(new MongoClientURI(uri, MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true)));
		MongoDatabase database = mongoClient.getDatabase("El-Menus");
		MongoCollection<Document> collection = database.getCollection("favourites");
		BasicDBObject query = new BasicDBObject();
		query.put("user_id", favouriteId);
		FindIterable<Document> docs = collection.find(query);
		JSONParser parser = new JSONParser(); 
		ArrayList<HashMap<String, Object>> favourites = new ArrayList<HashMap<String, Object>>();
		for (Document document : docs) {
			JSONObject json;
			try {
				json = (JSONObject) parser.parse(document.toJson());
				HashMap<String, Object> fav = Command.jsonToMap(json);	
				favourites.add(fav);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
			
		mongoClient.close();
        return favourites;
		
	}



}

