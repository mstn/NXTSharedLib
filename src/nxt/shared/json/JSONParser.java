package nxt.shared.json;

/*
 * TODO 
 * considerare gli array [a,b,c]
 * vedi come definiti in JSON
 *
 */
public class JSONParser {

	private enum State {
		START, KEY, VALUE, END
	};

	public static final JSONObject parse(String input)
			throws JSONParseException {
		Tokenizer tokenizer = new Tokenizer(input, "{}\":,", " ", true);
		return parseJSONObject(tokenizer);
	}

	private static final JSONObject parseJSONObject(Tokenizer tokenizer)
			throws JSONParseException {
		JSONObject currentObject = null;
		String currentKey = null;
		Object currentValue = null;
		boolean isNumber = true;
		State state = State.START;

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if ("{".equals(token)) {
				if (state == State.START) {
					// creo un nuovo oggetto
					currentObject = new JSONObject();
					// mi aspetto di trovare succesivamente una key
					state = State.KEY;
				} else if (state == State.VALUE) {
					// il valore e' un oggetto
					// rimetto la { nello stream
					tokenizer.putBackToken(token);
					currentValue = parseJSONObject(tokenizer);
					isNumber = false;
				} else {
					throw new JSONParseException("expected JSON Object after {");
				}
			} else if ("}".equals(token)) {
				// creo una nuova copia key/valore
				if (currentKey != null && currentValue != null) {
					currentObject.put(currentKey, currentValue);
				} else {
					throw new JSONParseException("missing key or value");
				}
				// fine oggetto
				state = State.END;
				// ritorno al chiamante
				return currentObject;
			} else if ("\"".equals(token)) {
				if (state == State.VALUE) {
					// un valore numerico non e' racchiuso tra virgolette
					isNumber = false;
				}
			} else if (":".equals(token)) {
				if (state == State.KEY) {
					// mi aspetto di leggere un valore
					state = State.VALUE;
				} else {
					throw new JSONParseException("expected key before :");
				}
			} else if (",".equals(token)) {
				if (state == State.VALUE) {
					// creo una nuova coppia key/valore
					if (currentKey != null && currentValue != null) {
						currentObject.put(currentKey, currentValue);
					} else {
						throw new JSONParseException("missing key or value");
					}
					// dopo un valore c'e' una ulteriore key
					state = State.KEY;
				} else {
					throw new JSONParseException("unexpected ,");
				}
			} else {
				if (state == State.KEY) {
					currentKey = token;
				} else if (state == State.VALUE) {
					if (isNumber) {
						if ("true".equals(token) || "false".equals(token)) {
							currentValue = new Boolean(token);
						} else {
							// converto il valore in un numero (supponi intero
							// per adesso)
							try {
								currentValue = new Integer(token);
							} catch (NumberFormatException e) {
								throw new JSONParseException(
										"non number or non boolean value must be enclosed within \"");
							}
						}
					} else {
						currentValue = token;
					}
					// resetto il controllo sul numero
					isNumber = true;
				}
			}
		}

		if (state != State.END) {
			throw new JSONParseException("missing }");
		}

		return currentObject;
	}

	/*
	 * public static void main(String[] args){ // String test
	 * ="{\"action\":0,\"resource\":\"/motor/A\",\"content\":\"test\"}"; String
	 * test =
	 * "{\"action\":0,\"resource\":\"/motor/A\",\"content\":{\"speed\":360, \"power\":50}}"
	 * ; try { JSONObject obj = JSONParser.parse(test); return; } catch
	 * (JSONParseException e) { System.out.println(e.getMessage()); } }
	 */
}
