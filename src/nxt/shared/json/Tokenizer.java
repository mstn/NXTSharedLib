package nxt.shared.json;

public class Tokenizer {

	private String input;
	private String delim;
	private String whites;
	private boolean includeDelim;
	private boolean eof;

	private int pos;

	public Tokenizer(String input, String delim, String whites,
			boolean includeDelim) {
		this.input = input;
		this.delim = delim;
		this.whites = whites;
		this.includeDelim = includeDelim;
		this.pos = 0;
		this.eof = false;
	}

	public String nextToken() {
		StringBuffer nextToken = new StringBuffer();
		char c = nextChar();
		// finche' non raggiungo la fine della stringa
		while (!eof) {
			// se il carattere non e' un delimitatore
			if (delim.indexOf(c) == -1) {
				nextToken.append(c);
			} else {
				// se stavo leggendo una parola
				// rimetti il char nel flusso e ritorna
				if (nextToken.length() > 0 || !includeDelim) {
					back();
				} else {
					// altrimenti il prossimo token e' il delim
					nextToken.append(c);
				}
				// esci dal loop
				break;
			}
			c = nextChar();
		}
		return nextToken.toString();
	}

	// TOFIX sbagliato solo per provare
	public boolean hasMoreTokens() {
		return (pos < input.length());
	}

	public void putBackToken(String token) {
		int length = token.length();
		if (pos - length > 0) {
			pos -= length;
		}

	}

	private char nextChar() {
		char c = ' ';
		// skip white chars
		while (whites.indexOf(c) != -1) {
			if (pos < input.length()) {
				c = input.charAt(pos);
				pos++;
			} else {
				// fine stringa
				eof = true;
			}
		}
		return c;
	}

	private void back() {
		if (pos > 0) {
			pos--;
		}
	}

}
