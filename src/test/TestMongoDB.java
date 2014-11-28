package test;

import java.lang.reflect.Field;
import java.net.UnknownHostException;

import org.jerry.mongo.conn.MongoDBFactory;
import org.jerry.mongo.model.Person;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class TestMongoDB {

	
	@Test
	public void testMongoDB(){
		try {
			DBCollection dbCollection = MongoDBFactory.getInstance().getMongoDBCollection("test", "test");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testClass() throws ClassNotFoundException{
		
		Class<Person> clazz  = Person.class;
		for(Field field : clazz.getDeclaredFields()){
			System.out.println(field.toString());
		}
	}
}
