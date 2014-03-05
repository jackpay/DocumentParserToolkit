package ac.uk.susx.tag.database;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class DocIndexEntity implements IEntity {
	
	@PrimaryKey
	String id;
	@SecondaryKey(relate=Relationship.ONE_TO_ONE)
	String docName;
	
	public DocIndexEntity() {}

	public DocIndexEntity(String docName, String id) {
		this.docName = docName;
		this.id = id;
	}
	
	public String getDocName() {
		return docName;
	}
	
	public String getId() {
		return id;
	}
	
}
