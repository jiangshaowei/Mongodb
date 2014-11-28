package org.jerry.mongo.coder;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.jerry.mongo.conn.MongoDBFactory;
import org.jerry.mongo.model.Address;
import org.jerry.mongo.model.BaseBean;
import org.jerry.mongo.model.Cards;
import org.jerry.mongo.model.Person;
import org.jerry.mongo.util.MongoConstants;

import com.google.gson.Gson;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBEncoder;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoException;

public class DocumentManipulate<T extends BaseBean> extends BaseManipulate implements MongoConstants{
 
	private String collection;
	private static final String COLLECTION_NAME = "_collection";
	private Class<T> clazz;
	
	public DocumentManipulate(Class<T> clazz){
		this.clazz = clazz;
		collection = getField(COLLECTION_NAME);
	}
	
	/****
	 * return the first document in the collection
	 * @return
	 */
	public T findOne(){
		return findOne(null);
	}
	
	
	
	
	/****
	 * Find One document use documentId , show selected fields
	 * @param obj
	 * @param fields
	 * @return
	 */
	public T findOne(Object obj,BasicDBObject fields){
				DBObject dbObject = null;
				try {
					DBCollection dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
					if(obj == null){
						dbObject = dbCollection.findOne();
					}else if(fields == null ){
						dbObject = dbCollection.findOne(obj);
					}else{
						dbObject = dbCollection.findOne(obj, fields);
					}
					if(dbObject!=null && !dbObject.toString().isEmpty()){
						String json  = dbObject.toString();
				/*		json = json.replace("\\", "");
						json = json.replace("\"{", "{");
						json = json.replace("}\"", "}");*/
				//		T t = JSON.parseObject(json, clazz);
						T t = new Gson().fromJson(json, clazz);
						return t;
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return null;
	}
	
	public T findOne(Object obj){
		return findOne(obj, null);
	}
	
	/****
	 * Add documents to collection
	 * @param t
	 * @throws UnknownHostException 
	 */
	public void insert(List<T> t) throws UnknownHostException{
		/***
		 * Check t is null ?
		 */
		if(t != null && !t.isEmpty()){
			List<DBObject> documents = new ArrayList<DBObject>();
			BasicDBObject dbObject = null;
			for (T t2 : t) {
				dbObject = new BasicDBObject();
				generate(clazz, dbObject, t2);
				documents.add(dbObject);
			}
			try {
				DBCollection dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
				
				dbCollection.insert(documents);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
			//	MongoDBFactory.getInstance().closeMongo();
			}
			
		}
	}
	
	
	
	/****
	 * Add document to collection
	 * @param t
	 */
	public void insert(T t){
		/***
		 * Check t is null ?
		 */
		if(t != null){
			List<T> list = new ArrayList<T>();
			list.add(t);
			try {
				insert(list);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/****
	 * Return all the doc
	 * @return
	 */
	public List<T> queryFind(){
		return queryFind(null);
	}
	
	
	/****
	 * Return docs that matched query
	 * @param query
	 * @return
	 */
	public List<T> queryFind(BasicDBObject query){
		return queryFind(query, null);
	}
	
	
	
	/****
	 * Return docs's keys's specific fields that matched query
	 * @param query
	 * @return
	 */
	public List<T> queryFind(BasicDBObject query,BasicDBObject keys){
		DBCollection dbCollection = null;
		DBCursor cursor = null;
		List<T> results = null;
		try {
			 dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			 if(query == null ){
				 cursor = dbCollection.find();
			 }else if(keys == null ){
				 cursor = dbCollection.find(query);
			 }else{
				 cursor = dbCollection.find(query, keys);
			 }
			 results = new ArrayList<T>();
			 while(cursor.hasNext()){
					String json  = cursor.next().toString();
				/*	json = json.replace("\\", "");
					json = json.replace("\"{", "{");
					json = json.replace("}\"", "}");*/
			//		results.add(JSON.parseObject(json, clazz));
					results.add(new Gson().fromJson(json, clazz));
			 }
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			cursor.close();
		}
		return results;
	}
	
	
	
	/****
	 * remove with the default WriteConcern
	 * @param object
	 */
	public void remove(BasicDBObject object){
		if(object != null ){
			 DBCollection dbCollection = null;
			 try {
				dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
				dbCollection.remove(object);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Long count(){
		return  count(null);
	}
	
	/****
	 * returns the number of documents that match a query.
	 * @param query
	 * @return
	 */
	public Long count(DBObject query){
		DBCollection dbCollection = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			if(query == null ){
				return dbCollection.count();
			}
			return dbCollection.count(query);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return 0L;
	}
	
	
	public List distinct(String key){
		return distinct(key, null);
	}
	
	/****
	 *  find distinct values for a key use query
	 * @param key
	 * @param query
	 * @return
	 */
	public List distinct(String key,DBObject query){
		if(isParamIsNotValid(key)){
			return null;
		}
		DBCollection dbCollection = null;
		List list = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			if(query == null ){
				return  dbCollection.distinct(key);
				
			}
			return dbCollection.distinct(key,query);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}
	
	
	/****
	 * keys an object with a key set of the fields desired for the index
	 * Map<String,Object> map = new HashMap<String,Object>()
	 * map.put("name",1);
	 * map.put("age",1);
	 * BasicDBOject keys = new BasicDBObject(map)
	 * createIndex(keys)
	 * @param keys
	 */
	public void createIndex(DBObject keys ){
		createIndex(keys, null);
	}
	
	
	
	/****
	 * options : index name also
	 * @param keys
	 * @param options
	 */
	public void createIndex(DBObject keys,DBObject options){
		createIndex(keys, options, null);
	}
	
	/****
	 * Forces creation of an index on a set of fields, if one does not already exist. 
	 * @param keys
	 * @param options
	 * @param encoder
	 */
	public void createIndex(DBObject keys,DBObject options,DBEncoder encoder){
		DBCollection dbCollection = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			if(options == null ){
				dbCollection.createIndex(keys);
			}else if(encoder == null){
				dbCollection.createIndex(keys, options);
			}
			dbCollection.createIndex(keys, options, encoder);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	
	/***
	 * Creates an ascending index on a field with default options, if one does not already exist
	 * Index Type : Single Field Indexes
	 *              Compound Indexes   A compound index includes more than one field of the documents in a collection.
	 *              Multikey Indexes   A multikey index is an index on an array field, adding an index key for each value in the array.
	 *              Text Indexes     Text indexes support search of string content in documents.  ATTENTATION:MAY USE IN SEARCH PATTERN
	 * Example : ensureIndex("name") will creates an index on the name_field
	 * MongoDB supports single field indexes on fields at the top level of a document and on fields in sub-documents.
	 * @param name
	 */
	public void ensureIndex(String name){
		if(isParamIsNotValid(name)){
			return;
		}
		DBCollection dbCollection = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			dbCollection.ensureIndex(name);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	/****
	 *  an object with a key set of the fields desired for the index 
	 * @param keys
	 */
	public void ensureIndex(DBObject keys){
		ensureIndex(keys, null);
	}
	
	public void ensureIndex(DBObject keys,String name){
		ensureIndex(keys, name, false);
	}
	
	/****
	 * 
	 * @param keys
	 * @param name  name an identifier for the index
	 * @param unique
	 */
	public void ensureIndex(DBObject keys,String name,boolean unique){
		DBCollection dbCollection = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			if(name == null ){
				dbCollection.ensureIndex(keys);
			}
			dbCollection.ensureIndex(keys, name, unique);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	/****
	 * Saves an object to this collection (does insert or update based on the object _id).
	 * Do not work correctly
	 * @param object
	 */
	public void save(T t){
		if(t == null)
			return;
		DBCollection dbCollection = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			BasicDBObject dbObject = new BasicDBObject();
			dbObject.append(ID_FIELD,new ObjectId(t.get_id().get$oid()));
			generate(clazz, dbObject, t);
			dbCollection.save(dbObject,null);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	 /**
	  * DBObject query = new BasicDBObject("_id", new ObjectId(person.get_id().get$oid())
	  * update(query,person)  //means to update person object which _id match the query
	  * 
	  * DBObject query = new BasicDBObject("name","bob") 
	  * this query only update the fisrt object match the query
     * @param q search query for old object to update
     * @param o object with which to update <tt>q</tt>
     * @return
     * @throws MongoException
     * @dochub update
     */
	public void update(DBObject query,T t ){
		DBCollection dbCollection = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			BasicDBObject dbObject = new BasicDBObject();
			generate(clazz, dbObject, t);
			dbCollection.update(query, dbObject);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
	/****
	 * UPDATE T ,Use the _id field	
	 * @param t
	 */
	public void update(T t ){
		DBCollection dbCollection = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			DBObject query = new BasicDBObject("_id",new ObjectId(t.get_id().get$oid()));
			BasicDBObject dbObject = new BasicDBObject();
			generate(clazz, dbObject, t);
			dbCollection.update(query, dbObject);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

    /**
     * Similar to update ,only set multi is true
     * @param q search query for old object to update
     * @param o object with which to update <tt>q</tt>
     * @return
     * @throws MongoException
     * @dochub update
     */
	public void updateMulti(DBObject query,T t){
		DBCollection dbCollection = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			BasicDBObject dbObject = new BasicDBObject();
			dbCollection.updateMulti(query, dbObject);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
	
	/*****
	 * 	DBObject match = new BasicDBObject("$match", new BasicDBObject("name", "seinfield"));
    	DBObject fields = new BasicDBObject("name", 1);
    	fields.put("password", 1);
    	fields.put("_id", 0);
    	DBObject project = new BasicDBObject("$project", fields);
    	List<Person> persons = personCoder.aggregate(match, project);
	 * @return
	 */
	public List<T> aggregate(DBObject firstOp,DBObject... additionalOps){
		DBCollection dbCollection = null;
		List<T> results = null;
		try {
			 dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			 AggregationOutput output = dbCollection.aggregate(firstOp, additionalOps);
			 results = new ArrayList<T>();
			 if(output != null){
				 for(DBObject result : output.results()){
					 	String json  = result.toString();
					/*	json = json.replace("\\", "");
						json = json.replace("\"{", "{");
						json = json.replace("}\"", "}");*/
				//		results.add(JSON.parseObject(json, clazz));
						results.add(new Gson().fromJson(json, clazz));
				 }
			 }
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	/****
	 *  /**
     * Represents the command for a map reduce operation
     * Runs the command in REPLACE output type to a named collection
     * 
     * @param inputCollection
     *            the collection to read from   example : 	person collection
     * @param map
     *            map function in javascript code example:  function() {emit( this.cust_id,this_amout);	}
     * @param reduce
     *            reduce function in javascript code  example: function(key,values) {return Array.sum(values)}
     * @param outputCollection
     *            optional - leave null if want to get the result inline 
     * @param type
     *            the type of output
     *             /**
     * INLINE - Return results inline, no result is written to the DB server
    
     * 
     * REPLACE - Save the job output to a collection, replacing its previous content
     * MERGE - Merge the job output with the existing contents of outputTarget collection
     * REDUCE - Reduce the job output with the existing contents of outputTarget collection
     *  public static enum OutputType {
        REPLACE, MERGE, REDUCE, INLINE 
        };
     *            
     *            
     * @param query
     *            the query to use on input  example:new BasicDBObject("status","A")
     * @return
     * 
     * 
     */
	public MapReduceOutput  mapReduce( MapReduceCommand command ){
		DBCollection dbCollection = null;
		try {
			 dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			 MapReduceOutput output = dbCollection.mapReduce(command);
			 return output;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
	
	/*****
	 * Drop this collection
	 */
	private void drop(){
		DBCollection dbCollection = null;
		try{
			dbCollection = MongoDBFactory.getInstance().getMongoDBCollection(collection);
			dbCollection.drop();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	protected Field getFieldType(T bean,String fieldName) {
		try{
			return bean.getClass().getDeclaredField(fieldName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
    protected String getField(String fieldName){
        try{
            Object o = clazz.getField(fieldName).get(null) ;
            return o.toString();
        }catch (Exception e) {
        }
        return "";
    }

    protected String[] getFields(String fieldName){
        try{
            Object o = clazz.getField(fieldName).get(null) ;
            return o.toString().split(",");
        }catch (Exception e) {
        }
        return new String[]{};
    }    
    
    protected Object getField(Object bean,String opcolumn){
        try {
            Field field = bean.getClass().getDeclaredField(opcolumn);
            field.setAccessible(true);
            return field.get(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /******
     * GET DBObject through given obj
     * @param clazz
     * @param dbObject
     * @param obj
     */
    private void generate(Class clazz, BasicDBObject dbObject ,Object obj) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				Object object = getField(obj, field.getName());
				if(field.getName().equals(COLLECTION_NAME)){
					continue;
				}
				if(field.getType() == String.class){
					dbObject.append(field.getName(), object);
				}else if(field.getType() == Integer.class){
					dbObject.append(field.getName(), object);
				}else if(field.getType() == Long.class){
					dbObject.append(field.getName(), object);
				}else if(field.getType() == Date.class){
					dbObject.append(field.getName(), object);
				}else if(field.getType() == Number.class){
					dbObject.append(field.getName(), object);
				}else if(field.getType() == Character.class){
					dbObject.append(field.getName(), object);
				}else if(field.getType() == Map.class){
					Map map = (Map)object;
					Iterator<Map.Entry<Object, Object>> it = map.entrySet().iterator();
					BasicDBObject result = new BasicDBObject();
					BasicDBObject dbInnerMapObject = null;
					while(it.hasNext()){
						dbInnerMapObject = new BasicDBObject();
						Map.Entry<Object, Object> entry = it.next();
						generate(entry.getValue().getClass(),dbInnerMapObject, entry.getValue());
						result.append(entry.getKey().toString(), dbInnerMapObject);
					}
					dbObject.append(field.getName(), result);
				}else if(field.getType() == List.class){
					BasicBSONList listBson = new BasicBSONList();
					List<Object> list = (List)object;
					for(Object innerObject : list){
						BasicDBObject dbInnerObject = new BasicDBObject();
						generate(innerObject.getClass(), dbInnerObject, innerObject);
						listBson.add(dbInnerObject);
					}
					dbObject.append(field.getName(), listBson);
				}else if (field.getClass() instanceof Class ){
					BasicDBObject dbClassObject = new BasicDBObject();
					generate(object.getClass(), dbClassObject, object);
					dbObject.append(field.getName(), dbClassObject);
				}
			 } catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
    
    
    
    public static void main(String[] args){
    	
    	DocumentManipulate<Person> personCoder = new DocumentManipulate<Person>(Person.class); 
    
 //   	BasicDBObject basicObject = new  BasicDBObject("age",new BasicDBObject("$gt",1));
 //   	personCoder.distinct("name",basicObject);
    /*	Person person = new Person("Bob","456789",6);
    	List<Address> address =new ArrayList<Address>();
    	address.add(new Address("NUM","123"));
    	address.add(new Address("HELLO","Hello"));
    	person.setAddresses(address);*/
    	
    //	personCoder.insert(person);
     	Person person = personCoder.findOne();
     	person.setName("yiyayiya1");
     	Cards cards = person.getMapCards().get(1);
   // 	personCoder.update(new BasicDBObject("name", "Bob"), person);
     	System.exit(0);
    //  personCoder.save(person);
  //  	System.out.println(person.get_id());
 //     System.out.println(person.get$oid());
  /*  	BasicDBObject basicObject = new  BasicDBObject("_id", new ObjectId(person.get_id().get$oid()));
    	BasicDBObject keys = new BasicDBObject("_id",1).append("name", 1).append("password", 1).append("age", 1);
    	List<Person> persons = personCoder.queryFind(basicObject,keys);
    	 for (Person person1 : persons) {*/
//			System.out.println(person1.get$oid());
//			System.out.println(person1.getName() + person1.getPassword() + person1.getAge());
//		}
    // 	personCoder.remove(basicObject);
  //  	DBObject q  =new BasicDBObject();
  //  	q.put("_id", )
    	
    	
    	
    	
    }
}
