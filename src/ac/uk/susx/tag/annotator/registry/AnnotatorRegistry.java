package ac.uk.susx.tag.annotator.registry;

import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

public final class AnnotatorRegistry {
	
	private static final HashMap<Class<? extends IAnnotatorFactory<?,?>>,IAnnotator<?,?>> registry = Maps.newHashMap();
	private static final HashMap<Class<? extends IAnnotatorFactory<?,?>>,IAnnotatorFactory<?,?>> facRegistry = Maps.newHashMap();
	private static final HashMap<CommandLineOption,Class<IAnnotatorFactory<?,?>>> cmdRegistry = Maps.newHashMap();
	
	private AnnotatorRegistry(){}

	@SuppressWarnings("unchecked")
	public static synchronized <AT,ACT> IAnnotator<AT,ACT> getAnnotator(Class<? extends IAnnotatorFactory<AT,ACT>> cl) throws Exception {
		if(facRegistry.get(cl) != null) {
			if(registry.get(cl) == null){
				Class<? extends IAnnotatorFactory<?,?>> anomCl = cl;
				registry.put(anomCl, facRegistry.get(cl).create());
			}
			if(!registry.get(cl).modelStarted()){
				registry.get(cl).startModel();
			}
			return (IAnnotator<AT,ACT>) registry.get(cl); 
		}
		else {
			throw new Exception("There is no registered AbstractAnnotatorFactory of that class.");
		}
	}
	
	public static synchronized IAnnotator<?,?> getAnnotator(String cmd) throws Exception {
		if(cmdRegistry.get(cmd) != null) {
			if(registry.get(cmdRegistry.get(cmd)) == null){
				registry.put(cmdRegistry.get(cmd), facRegistry.get(cmdRegistry.get(cmd)).create());
			}
			if(!registry.get(cmdRegistry.get(cmd)).modelStarted()){
				registry.get(cmdRegistry.get(cmd)).startModel();
			}
			return registry.get(cmdRegistry.get(cmd)); 
		}
		else {
			throw new Exception("There is no registered AbstractAnnotatorFactory with that command line option.");
		}
	}
	
	public static Set<CommandLineOption> getOptions(){
		return cmdRegistry.keySet();
	}

	public static void registerAnnotator(IAnnotatorFactory<?,?> abstractAnnotatorFactory) {
		facRegistry.put((Class<IAnnotatorFactory<?,?>>) abstractAnnotatorFactory.getClass(), abstractAnnotatorFactory);
		cmdRegistry.put(abstractAnnotatorFactory.getCommandLineOption(), (Class<IAnnotatorFactory<?,?>>) abstractAnnotatorFactory.getClass());
	}

	public static void register() {
		Reflections reflections = new Reflections("ac.uk.susx.tag.annotator");
		Set<Class<?>> annotators = reflections.getTypesAnnotatedWith(AnnotatorFactory.class);
		for(Class<?> annotator : annotators) {
			IAnnotatorFactory<?,?> anf = null;
			try {
				anf = (IAnnotatorFactory<?,?>) annotator.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			registerAnnotator(anf);
		}
	}

}
