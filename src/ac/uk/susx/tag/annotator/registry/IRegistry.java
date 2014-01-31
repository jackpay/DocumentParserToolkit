package ac.uk.susx.tag.annotator.registry;

import ac.uk.susx.tag.annotator.IAnnotator;

public interface IRegistry{
	
	public IAnnotator<?,?,?> get(String id) throws Exception;
	
	public void register(String id, IAnnotator<?,?,?> annotator);

}
