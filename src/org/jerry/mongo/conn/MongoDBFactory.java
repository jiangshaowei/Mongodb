package org.jerry.mongo.conn;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.jerry.mongo.coder.DocumentManipulate;
import org.jerry.mongo.util.MongoConstants;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoDBFactory implements MongoConstants{

	private static MongoDBFactory mongoDBFactory = new MongoDBFactory();
	private static Mongo mongo;
	
	private MongoDBFactory(){
		try {
			mongo = new Mongo(MONGO_HOST,MONGO_PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	 
	public static MongoDBFactory getInstance() throws UnknownHostException{
		 if(mongoDBFactory == null){
			 mongoDBFactory = new MongoDBFactory();
		 }
		 return mongoDBFactory;
	}
	
	
	/****
	 * GET THE DB OBJECT
	 * @param dbName
	 * @return
	 * @throws UnknownHostException
	 */
	public  DB getMongoDB(String dbName) throws UnknownHostException{
		 if(mongoDBFactory == null){
			 mongoDBFactory = new MongoDBFactory();
		 }
		 return mongo.getDB(dbName);
	}
	
	
	/****
	 * GET THE DB COLLECTION OBJECT
	 * @param collectionName
	 * @return
	 * @throws UnknownHostException
	 */
	public DBCollection  getMongoDBCollection(String dbName,String collectionName) throws UnknownHostException{
		 DB mongoDB  = getMongoDB(dbName);
		 return  mongoDB.getCollection(collectionName);
	}
	
	/****
	 * GET THE DB COLLECTION OBJECT
	 * @param collectionName
	 * @return
	 * @throws UnknownHostException
	 */
	public DBCollection  getMongoDBCollection(String collectionName) throws UnknownHostException{
		 DB mongoDB  = getMongoDB(MONGO_DB);
		 return  mongoDB.getCollection(collectionName);
	}
	
	/****
	 * close mongo
	 */
	public void closeMongo(){
		mongo.close();
		mongo = null;
	}
	
	
	
	public static void main(String[] args) throws UnknownHostException{
		DBCollection coll = MongoDBFactory.getInstance().getMongoDBCollection("aggregationExample");
		/*coll.insert(new BasicDBObjectBuilder()
		       .add("employee", 1)
		       .add("department", "Sales")
		       .add("amount", 71)
		       .add("type", "airfare")
		       .get());
		coll.insert(new BasicDBObjectBuilder()
		       .add("employee", 2)
		       .add("department", "Engineering")
		       .add("amount", 15)
		       .add("type", "airfare")
		       .get());
		coll.insert(new BasicDBObjectBuilder()
		       .add("employee", 4)
		       .add("department", "Human Resources")
		       .add("amount", 5)
		       .add("type", "airfare")
		       .get());
		coll.insert(new BasicDBObjectBuilder()
		       .add("employee", 42)
		       .add("department", "Sales")
		       .add("amount", 77)
		       .add("type", "airfare")
		       .get());
		*/
		// create our pipeline operations, first with the $match
		DBObject match = new BasicDBObject("$match", new BasicDBObject("type", "airfare"));

		// build the $projection operation
		DBObject fields = new BasicDBObject("department", 1);
		fields.put("amount", 1);
		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", "$department");
		groupFields.put("average", new BasicDBObject( "$avg", "$amount"));
		DBObject group = new BasicDBObject("$group", groupFields);

		// Finally the $sort operation
		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("amount", -1));

		// run aggregation
	//	List<DBObject> pipeline = Arrays.asList();
		AggregationOutput output = coll.aggregate(match,project, group, sort);
		for (DBObject result : output.results()) {
		    System.out.println(result);
		}
		
	}
	
}
