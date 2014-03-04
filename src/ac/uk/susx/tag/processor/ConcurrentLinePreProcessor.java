package ac.uk.susx.tag.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ac.uk.susx.tag.database.IDatabaseIndexer;
import ac.uk.susx.tag.database.IEntity;
import ac.uk.susx.tag.preparser.IJobFactory;

/**
 * 
 * @author jp242
 *
 * @param <PE> The class type of the primary index.
 * @param <ET> The class type of the IEntity being indexed
 */
public class ConcurrentLinePreProcessor<PE,ET extends IEntity> implements IProcessor {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 10;
	private final IDatabaseIndexer<PE,ET> indexer;
	private final IJobFactory<ET,String> jobFactory;
	private final ArrayBlockingQueue<Future<Boolean>> queue;
	private boolean complete;

	public ConcurrentLinePreProcessor(IDatabaseIndexer<PE,ET> indexer, IJobFactory<ET,String> jobFactory) { 
		this.indexer = indexer;
		this.jobFactory = jobFactory;
		this.queue = new ArrayBlockingQueue<Future<Boolean>>(NTHREADS*2);
	}
	
	public void processFiles(List<File> files) {
		ExecutorService producerPool = Executors.newSingleThreadExecutor();
		ExecutorService consumerPool = Executors.newSingleThreadExecutor();
		producerPool.submit(new Producer(queue,files));
		consumerPool.submit(new Consumer(queue));
		producerPool.shutdown();
		try {
			producerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
			queue.add(new ConsumerShutdownFuture());
			consumerPool.shutdown();
			consumerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private final class Producer implements Runnable {
		
		private final ArrayBlockingQueue<Future<Boolean>> queue;
		private final List<File> files;

		private Producer(ArrayBlockingQueue<Future<Boolean>> queue, List<File> files){
			this.queue = queue;
			this.files = files;
		}

		public void run() {
			ExecutorService executor;
			for(File file : files){
				try {
					executor = Executors.newFixedThreadPool(NTHREADS);
					BufferedReader br = new BufferedReader(new FileReader(file));
					String currLine;
					currLine = br.readLine();
					int lineCount = 0;
					while(currLine != null){
						String line = new String(currLine).trim();
						if(line.length() > 0) {
							JobCallable jc = new JobCallable(indexer,line);
							queue.put(executor.submit(jc));
						}
						currLine = br.readLine();
						lineCount++;
						if(lineCount%1000 == 0){
							System.err.println("Processing line: " + lineCount + " of File: " + file.getName());
						}
					}
				}
				catch (IOException e){
					e.printStackTrace();
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
	
	private final class Consumer implements Runnable {
		
		private final ArrayBlockingQueue<Future<Boolean>> queue;
		
		private Consumer(ArrayBlockingQueue<Future<Boolean>> queue){
			this.queue = queue;
		}

		public void run() {
			try {
				while(!complete){
					Future<Boolean> out = queue.take();
					if(out.get() == false){
						complete = true;
						return;
					}
					complete = out.getClass().equals(ConsumerShutdownFuture.class);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			} catch (ExecutionException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public void processFile(File file) {
		processFiles(new ArrayList<File>(Arrays.asList(file)));
	}
	
	private final class JobCallable implements Callable<Boolean> {
		
		private final IDatabaseIndexer<PE,ET> indexer;
		private final String document;
		
		public JobCallable(IDatabaseIndexer<PE,ET> indexer, String document) {
			this.indexer = indexer;
			this.document = document;
		}

		public Boolean call() throws Exception {
			try {
				indexer.index(jobFactory.createJob(document).process());
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
	}
	
	// Used as a poison pill to shut down the consumer object.
	private final class ConsumerShutdownFuture implements Future<Boolean> {

		public boolean cancel(boolean arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public Boolean get() throws InterruptedException, ExecutionException {
			// TODO Auto-generated method stub
			return null;
		}

		public Boolean get(long arg0, TimeUnit arg1) throws InterruptedException,
				ExecutionException, TimeoutException {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isCancelled() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isDone() {
			// TODO Auto-generated method stub
			return false;
		}
	}

}
