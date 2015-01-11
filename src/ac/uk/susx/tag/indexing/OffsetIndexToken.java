package ac.uk.susx.tag.indexing;


/**
 * An IndexToken used to index by term offset position.
 * @author jp242
 *
 */
public class OffsetIndexToken implements IIndexToken{
	
	private final int startOffset; // Start position in document 
	private final int endOffset; // End position in document
	
	public OffsetIndexToken(int startOffset, int endOffset){
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}
	
	public int getStart(){
		return startOffset;
	}
	
	public int getEnd(){
		return endOffset;
	}
	
	public int hashCode(){
		int prime = 3;
		int hash = prime * startOffset;
		return hash;
	}

	/**
	 * Equals defined by the equality of the start and end offsets.
	 */
	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(!(obj instanceof OffsetIndexToken)){
			return false;
		}
		else {
			OffsetIndexToken pos = (OffsetIndexToken) obj;
			if(this.startOffset == pos.startOffset && this.endOffset == pos.endOffset){
				return true;
			}
		}
		return false;
	}
	
}
