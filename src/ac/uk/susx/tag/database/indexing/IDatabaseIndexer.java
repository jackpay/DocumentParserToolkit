package ac.uk.susx.tag.database.indexing;

import java.util.List;

import ac.uk.susx.tag.database.DatabaseEntityStore;
import ac.uk.susx.tag.database.IEntity;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
/**
 * 
 * @author jp242
 *
 * @param <PE> Recognised input class
 * @param <ET> Entity Output entity class/type.
 */
public interface IDatabaseIndexer<PE,ET>{

	public DatabaseEntityStore entityStore();
	
	public <A> EntityIndex<A,ET> getIndex();
	
	public void index(int id, List<PE> entities);
	
	public void index(int id, PE entity);

}