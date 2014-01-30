package ac.uk.susx.tag.annotator.registry;

import java.util.HashMap;

import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotator.IAnnotator;

public abstract class AbstractRegistry implements IRegistry{
	
	private static final HashMap<String,IAnnotator<?,?,?>> registry = Maps.newHashMap();

	public IAnnotator<?, ?, ?> getAnnotator(String id) throws Exception {
		if(registry.get(id) != null) {
			return registry.get(id); 
		}
		else {
			throw new Exception("There is no registered IAnnotator for the given id.");
		}
	}

	public void register(String key, IAnnotator<?, ?, ?> annotator) {
		registry.put(key, annotator);
	}

}
