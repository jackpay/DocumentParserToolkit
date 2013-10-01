package ac.uk.susx.tag.statistics;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractStatistics implements Statistics{
	
	private final HashMap<String,Statistic<Object>> stats;
	
	public AbstractStatistics(){
		this.stats = new HashMap<String,Statistic<Object>>();
	}
	
	public <A> void addStatistic(String field, Statistic<A> value){
		stats.put(field, value);
	}
	
	public int getStatistic(String field){
		return stats.get(field);
	}
	
	public Set<String> getFields(){
		return stats.keySet();
	}
	
	public void combineStatistics(AbstractStatistics doc){
		for(String field : doc.getFields()){
			
		}
	}

}
