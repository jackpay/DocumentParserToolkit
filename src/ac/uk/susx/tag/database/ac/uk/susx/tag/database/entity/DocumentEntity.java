package ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity;

import ac.uk.susx.tag.database.IEntity;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * Created by jp242 on 18/08/2014.
 */
@Entity
public class DocumentEntity implements IEntity {

    @PrimaryKey
    private final String name;
    @SecondaryKey(relate=Relationship.ONE_TO_ONE)
    private final Integer docId;

    public DocumentEntity(Integer docId, String name) {
        this.docId = docId;
        this.name = name;
    }

    public int getId() {
        return docId;
    }

    public String name() {
        return name;
    }

}
