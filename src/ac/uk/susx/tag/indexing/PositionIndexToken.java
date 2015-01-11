package ac.uk.susx.tag.indexing;

/**
 * An IndexToken used to index by document position.
 * @author jp242
 *
 */
public class PositionIndexToken implements IIndexToken {
	
	private final int docPosition; // The index position within the document
	
	public PositionIndexToken(int docPosition){
		this.docPosition = docPosition;
	}
	
	public int getPosition(){
		return docPosition;
	}
	
	public int hashCode(){
		int prime = 3;
		int hash = prime * docPosition;
		return hash;
	}

	/**
	 * Equals defined by the equality of the doc position.
	 */
	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(!(obj instanceof PositionIndexToken)){
			return false;
		}
		else {
			PositionIndexToken pos = (PositionIndexToken) obj;
			if(this.docPosition == pos.docPosition){
				return true;
			}
		}
		return false;
	}
	
}
