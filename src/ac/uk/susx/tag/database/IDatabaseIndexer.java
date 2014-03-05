package ac.uk.susx.tag.database;

import java.util.List;

import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
/**
 * 
 * @author jp242
 *
 * @param <PE> Primary index key type.
 * @param <ET> Entity Actual entity class/type.
 */
public interface IDatabaseIndexer<PE,ET extends IEntity>{

	public PrimaryIndex<PE,ET> getPrimaryIndex();
	
	public <SE> SecondaryIndex<PE,SE,ET> getSecondaryIndex();

	public DatabaseEntityStore entityStore();
	
	public void index(List<ET> entities);
	
	public void index(ET entity);

}