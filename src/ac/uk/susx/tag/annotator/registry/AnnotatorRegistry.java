package ac.uk.susx.tag.annotator.registry;

import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;

public final class AnnotatorRegistry {
	
	private static final HashMap<Class<? extends IAnnotatorFactory<?,?,?>>,IAnnotator<?,?,?>> registry = Maps.newHashMap();
	private static final HashMap<Class<? extends IAnnotatorFactory<?,?,?>>,IAnnotatorFactory<?,?,?>> facRegistry = Maps.newHashMap();
	private static final HashMap<String,Class<IAnnotatorFactory<?,?,?>>> cmdRegistry = Maps.newHashMap();
	
	private AnnotatorRegistry(){}

	@SuppressWarnings("unchecked")
	public static <AT,DT,ACT> IAnnotator<AT,DT,ACT> getAnnotator(Class<? extends IAnnotatorFactory<AT,DT,ACT>> cl) throws Exception {
		if(facRegistry.get(cl) != null) {
			if(registry.get(cl) == null){
				Class<? extends IAnnotatorFactory<?,?,?>> anomCl = cl;
				registry.put(anomCl, facRegistry.get(cl).create());
			}
			if(!registry.get(cl).modelStarted()){
				registry.get(cl).startModel();
			}
			return (IAnnotator<AT,DT,ACT>) registry.get(cl); 
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

	public static void registerAnnotator(IAnnotatorFactory<?,?,?> abstractAnnotatorFactory) {
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
			registerAnnotator(anf);
		}
	}

}
