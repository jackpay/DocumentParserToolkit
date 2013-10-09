package ac.uk.susx.tag.statistics;

public abstract class AbstractStatistic<A> {

	private A stat;
	
	public AbstractStatistic(A stat){
		this.stat = stat;
	}
	
	public A getStat(){
		return stat;
	}
	
	public abstract String toString();
}
