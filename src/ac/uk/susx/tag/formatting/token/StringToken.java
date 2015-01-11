package ac.uk.susx.tag.formatting.token;

public class StringToken extends Token<String>{
	
	private final String token;

	public StringToken(String token) {
		super(token);
		this.token = token;
	}

	@Override
	public String toString() {
		return token;
	}

}
