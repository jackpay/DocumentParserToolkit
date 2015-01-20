package ac.uk.susx.tag.annotator.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.factory.CommandLineOption;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;

public final class AnnotatorRegistry {
	
	private static final HashMap<Class<? extends IAnnotatorFactory<?,?>>,IAnnotator<?,?>> registry = Maps.newHashMap();
	private static final HashMap<Class<? extends IAnnotatorFactory<?,?>>,IAnnotatorFactory<?,?>> facRegistry = Maps.newHashMap();
	private static final HashMap<String,Class<IAnnotatorFactory<?,?>>> cmdRegistry = Maps.newHashMap();
	private static final ArrayList<CommandLineOption> commands = new ArrayList<CommandLineOption>();
	private static final HashMap<String,IAnnotatorFactory<?,?>> nameRegistry = Maps.newHashMap();
	
	private AnnotatorRegistry(){}

	/**
	 * @param cl
	 * @return The annotator which is produced by this class of AnnotatorFactory.
	 * @throws Exception
	 */
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
	
	/**
	 * @param name Name of the annotator as defined by its AnnotatorFactory.
	 * @return The annotator registered with the given name
	 * @throws Exception
	 */
	public static synchronized IAnnotator<?,?> getAnnotator(String name) throws Exception {
		if(nameRegistry.get(name) != null) {
			if(registry.get(nameRegistry.get(name).getClass()) == null) {
				registry.put((Class<? extends IAnnotatorFactory<?, ?>>) nameRegistry.get(name).getClass(), nameRegistry.get(name).create());
			}
			if(!registry.get(nameRegistry.get(name).getClass()).modelStarted()) {
				registry.get(nameRegistry.get(name).getClass()).startModel();
			}
			return registry.get(nameRegistry.get(name).getClass());
		}
		else {
			throw new Exception("There is no registered annotator with that name.");
		}
	}
	
	/**
	 * Primary used to retrieve or create an annotator from command line prompts
	 * @param cmd The command line parameter prompt
	 * @param params Any additional params for that specific parser. 
	 * @return The returned or newly instantiated Annotator
	 * @throws Exception
	 */
	public static synchronized IAnnotator<?,?> getAnnotator(String cmd, String[] params) throws Exception {
		if(cmdRegistry.get(cmd) != null) {
			if(registry.get(cmdRegistry.get(cmd)) == null){
				registry.put(cmdRegistry.get(cmd), facRegistry.get(cmdRegistry.get(cmd)).create(params));
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
	
	public static List<CommandLineOption> getOptions(){
		return commands;
	}

	/**
	 * Add a new annotator to the registry via the factory which will produce when required.
	 * @param abstractAnnotatorFactory
	 * @throws Exception
	 */
	public  static void registerAnnotator(IAnnotatorFactory<?,?> abstractAnnotatorFactory) throws Exception {
		facRegistry.put((Class<IAnnotatorFactory<?,?>>) abstractAnnotatorFactory.getClass(), abstractAnnotatorFactory);
		cmdRegistry.put(abstractAnnotatorFactory.getCommandLineOption().getCommand(), (Class<IAnnotatorFactory<?,?>>) abstractAnnotatorFactory.getClass());
		commands.add(abstractAnnotatorFactory.getCommandLineOption());
		if(nameRegistry.containsKey(abstractAnnotatorFactory.name())) {
			throw new Exception("An AnnotatoFactory with that name already exists.");
		}
		else {
			nameRegistry.put(abstractAnnotatorFactory.name(), abstractAnnotatorFactory);
		}
	}

	/**
	 * Searches the classpath and registers any AnnotatorFactory classes.
	 */
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
			try {
				registerAnnotator(anf);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
