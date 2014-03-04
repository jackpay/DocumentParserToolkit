package ac.uk.susx.tag.database;

import java.util.List;

import com.sleepycat.persist.PrimaryIndex;
/**
 * 
 * @author jp242
 *
 * @param <PE> Primary index Entity type.
 * @param <ET> Entity Actual entity class/type.
 */
public interface IDatabaseIndexer<PE,ET>{

	public PrimaryIndex<PE,ET> getPrimaryIndex();

	public DatabaseEntityStore entityStore();
	
	public void index(List<ET> entities);
	
	public void index(ET entity);

}