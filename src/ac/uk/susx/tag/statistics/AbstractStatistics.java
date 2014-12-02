package ac.uk.susx.tag.statistics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import ac.uk.susx.tag.writer.IOutputWriter;

public abstract class AbstractStatistics implements IStatistics{
	
	private final HashMap<String, AbstractStatistic<?>> stats;
	
	public AbstractStatistics(){
		this.stats = new HashMap<String, AbstractStatistic<?>>();
	}
	
	public void addStatistic(String field, AbstractStatistic<?> value){
		stats.put(field, value);
	}
	
	public Object getStatistic(String field){
		return stats.get(field);
	}
	
	public Set<String> getFields(){
		return stats.keySet();
	}
	
	public Collection<AbstractStatistic<?>> getStatistics(){
		return stats.values();
	}
	
	public abstract void combineStatistics();
	
	public abstract void writeStatistics(IOutputWriter writer);

}
