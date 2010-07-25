package nxt.shared.json;

public class JSONParseException extends Exception {

	private String msg;

	public JSONParseException(String msg){
		this.setMessage(msg);
	}

	public void setMessage(String msg) {
		this.msg = msg;
	}

	public String getMessage() {
		return msg;
	}
	
}
