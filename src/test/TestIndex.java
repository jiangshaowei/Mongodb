package test;

import org.jerry.mongo.coder.DocumentManipulate;
import org.jerry.mongo.model.Person;
import org.junit.Test;

import com.mongodb.BasicDBObject;

public class TestIndex {

	
	@Test
	public void testSingleFieldIndex(){
		DocumentManipulate<Person> personMani = new DocumentManipulate<Person>(Person.class);
//		personMani.ensureIndex("name");  //will create name index on person collection with order is asc order
		                                   //you can run db.person.find({"name":"jerry"}).explain() if use this index
		/****
		 * can use 
		 */
		personMani.ensureIndex(new BasicDBObject("street",1));
		
	}
	
	
	
}


