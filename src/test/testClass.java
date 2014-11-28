package test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.jerry.mongo.coder.DocumentManipulate;
import org.jerry.mongo.model.Address;
import org.jerry.mongo.model.BaseBean;
import org.jerry.mongo.model.Card;
import org.jerry.mongo.model.Cards;
import org.jerry.mongo.model.Person;
import org.jerry.mongo.model.ZipCode;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class testClass {

	@Test
	public void testClass(){
		Person person = new Person();
		person.setName("jiangshaowei");
		person.setPassword("123456");
		person.setAge(10);
		ZipCode zipCode = new ZipCode();
		zipCode.setCity("Beijing");
		zipCode.setState("Beijing");
		zipCode.setZipcode(100000);
		person.setZipcode(zipCode);
		Cards  cards = new Cards();
		List<Card> cardss = new ArrayList<Card>();
		Card card = new Card();
		card.setCardnumber("123456");
		card.setBank("Commercial");
		card.setBalance("10000000");
		cardss.add(card);
		card = new Card();
		card.setCardnumber("456789");
		card.setBank("Commercial");
		card.setBalance("222222");
		cardss.add(card);
		cards.setCards(cardss);
		person.setCards(cards);
		List<Address> address = new ArrayList<Address>();
    	address.add(new Address("NUM","123"));
    	address.add(new Address("HELLO","Hello"));
    	person.setAddresses(address);
    	Map<Integer,Cards> maps = new HashMap<Integer, Cards>();
    	maps.put(1, cards);
    	person.setMapCards(maps);
		Class clazz = person.getClass();
		BasicDBObject dbObject = new BasicDBObject();
		System.out.println(dbObject.toString());
		DocumentManipulate<Person> personManipulate = new  DocumentManipulate<Person>(Person.class);
		personManipulate.insert(person);
		
	}

	private void generate(Class clazz, BasicDBObject dbObject ,Object obj) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				Object object = getField(obj, field.getName());
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
					dbObject.append(field.getName(), object);
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
}
