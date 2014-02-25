package ac.uk.susx.tag.annotator.registry;

import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;

public final class AnnotatorRegistry {
	
	private static final HashMap<Class<IAnnotatorFactory<?,?,?>>,IAnnotator<?,?,?>> registry = Maps.newHashMap();
	private static final HashMap<Class<IAnnotatorFactory<?,?,?>>,IAnnotatorFactory<?,?,?>> facRegistry = Maps.newHashMap();
	private static final HashMap<String,Class<IAnnotatorFactory<?,?,?>>> cmdRegistry = Maps.newHashMap();
	
	private AnnotatorRegistry(){}

	public static IAnnotator<?,?,?> getAnnotator(Class<IAnnotatorFactory<?,?,?>> cl) throws Exception {
		if(facRegistry.get(cl) != null) {
			if(registry.get(cl) == null){
				registry.put(cl, facRegistry.get(cl).create());
			}
			return registry.get(cl); 
		}
		else {
			throw new Exception("There is no registered AbstractAnnotatorFactory of that class.");
		}
	}
	
	public static IAnnotator<?,?,?> getAnnotator(String cmd) throws Exception {
		if(cmdRegistry.get(cmd) != null) {
			if(registry.get(cmdRegistry.get(cmd)) == null){
				registry.put(cmdRegistry.get(cmd), facRegistry.get(cmdRegistry.get(cmd)).create());
			}
			return registry.get(cmdRegistry.get(cmd)); 
		}
		else {
			throw new Exception("There is no registered AbstractAnnotatorFactory with that command line option.");
		}
	}

	public static void register(IAnnotatorFactory<?,?,?> abstractAnnotatorFactory) {
		facRegistry.put((Class<IAnnotatorFactory<?,?,?>>) abstractAnnotatorFactory.getClass(), abstractAnnotatorFactory);
		cmdRegistry.put(abstractAnnotatorFactory.getCommandLineOption(), (Class<IAnnotatorFactory<?, ?, ?>>) abstractAnnotatorFactory.getClass());
	}

	public static void register() {
		Reflections reflections = new Reflections();
		Set<Class<?>> annotators = reflections.getTypesAnnotatedWith(AnnotatorFactory.class);
		for(Class<?> annotator : annotators) {
			IAnnotatorFactory<?, ?, ?> anf = null;
			try {
				anf = (IAnnotatorFactory<?,?,?>) annotator.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			register(anf);
		}
	}

}
