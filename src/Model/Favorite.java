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
	private static int DbPoolCount = 4;
	public static int getDbPoolCount() {
		return DbPoolCount;
	}
	public static void setDbPoolCount(int dbPoolCount) {
		DbPoolCount = dbPoolCount;
	}
	static Favorite instance = new Favorite();
	@SuppressWarnings("null")
	public static HashMap<String, Object> create(HashMap<String, Object>attributes,String id) throws ParseException {
		HashMap<String, Object> returnValue = null ;
		MongoClientOptions.Builder options = MongoClientOptions.builder()
	            .connectionsPerHost(DbPoolCount);
		MongoClientURI uri = new MongoClientURI(
				"mongodb://localhost",options);

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("El-Menus");
		
		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("favourites");
		Document newFavourite = new Document();
		try{
		newFavourite.put("fav_id", id);
		collection.insertOne(newFavourite);
		JSONParser parser = new JSONParser();

		returnValue = Command.jsonToMap((JSONObject) parser.parse(newFavourite.toJson()));
		

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
		return returnValue;
	}


	public static ArrayList<HashMap<String, Object>> get(String favouriteId) {

		MongoClientOptions.Builder options = MongoClientOptions.builder()
	            .connectionsPerHost(DbPoolCount);
		MongoClientURI uri = new MongoClientURI(
				"mongodb://localhost",options);
		MongoClient mongoClient = new MongoClient(uri);
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

