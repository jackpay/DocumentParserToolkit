package ac.uk.susx.tag.indexing;

/**
 * Used as a means of retrieving items from a Map object. For this to be used correctly a sufficient equals(Object obj) method must be defined.
 * @author jp242
 *
 */
public interface IndexToken {
	
	public boolean equals(Object obj);

}
