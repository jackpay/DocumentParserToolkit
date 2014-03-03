package ac.uk.susx.tag.preparser;

public interface IJobFactory<T> {
	
	public IJob<T> createJob(T obj);

}
