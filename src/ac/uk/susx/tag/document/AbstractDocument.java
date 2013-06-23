package ac.uk.susx.tag.document;

public abstract class AbstractDocument <D,AT> implements Document<D,AT>{
	
	private D document;
	
	public AbstractDocument(D rawDoc){
		this.document = rawDoc;
	}
	
	public D getDocument(){
		return document;
	}
	
	public void setDocument(D docText) {
		this.document = docText;
	}

}
