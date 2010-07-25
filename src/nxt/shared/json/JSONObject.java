package nxt.shared.json;

import java.util.Enumeration;
import java.util.Hashtable;

public class JSONObject {
	
	// utilizzo questo perche' sempre l'unico
	// messo a disposizione da Lejos
	Hashtable map = new Hashtable();;
	
	
	public Object get(String key){
		return map.get(key);
	}
	
	// uso l'enumerazione per evitare di copiare a destra e a manca
	// pero' se ci fosse un altro modo sarebbe benvenuto
	public Enumeration getKeys(){
		return map.keys();
	}

	public boolean hasKey(String key){
		return (map.get(key)!=null);
	}
	
	public JSONObject put(String key, Object value){
		map.put(key, value);
		return this;
	}
	
	public Object remove(String key){
		return null;
	}
	
	@Override
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		boolean first = true;
		Enumeration keys = map.keys();
		while ( keys.hasMoreElements() ){
			String key = (String) keys.nextElement();
			Object value = map.get(key);
			if (!first){
				buf.append(",");
			} 
			first = false;
			buf.append("\"").append(key).append("\"").append(":");
			if (value instanceof String){
				buf.append("\"").append(value.toString()).append("\"");
			} else {
				buf.append(value.toString());
			}
		}
		buf.append("}");
		return buf.toString();
	}
}
