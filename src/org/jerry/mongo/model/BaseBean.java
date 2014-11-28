package org.jerry.mongo.model;


public class BaseBean {
	/****
	 * 存取Mongo生成的ID
	 */
	private Oid _id;
	
	public BaseBean(){
		
	}
	
	
	public Oid get_id() {
		return _id;
	}


	public void set_id(Oid _id) {
		this._id = _id;
	}


	public class Oid{
		private String $oid;
		public String get$oid() {
			return $oid;
		}
 
		public void set$oid(String $oid) {
			this.$oid = $oid;
		}
	}
}
