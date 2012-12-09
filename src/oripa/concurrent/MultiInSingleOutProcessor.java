package oripa.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiInSingleOutProcessor<Input, Output> {

	public interface Conquerer<Output>{
		public Output conquer(Output conquered, Output newElement);
	}

	
	private SingleInProcessFactory<Input, Output> processFactory;
	private Conquerer<Output> conquerer;
	
	public MultiInSingleOutProcessor(SingleInProcessFactory<Input, Output> processFactory, Conquerer<Output> conqerer) {
		this.processFactory = processFactory;
		this.conquerer = conqerer;
	}


	public Output execute(final Collection<Input> values) 
			throws IllegalAccessException, InterruptedException, ExecutionException{	

		Output result = null;
		final int processNum = 4;
		ExecutorService executor = Executors.newFixedThreadPool(processNum);
		CompletionService<Output> completion = new ExecutorCompletionService<>(executor);

		try{
			for(Input value : values){
				completion.submit( processFactory.create().getCallable(value) );
			}

			result = completion.take().get();
			
			for(int i = 1; i < values.size(); i++){
				// take the result of the finished process as soon as possible
				Output processed = completion.take().get();
				result = conquerer.conquer(result, processed);
				
			}
		}
		catch (Exception e) {

		}

		executor.shutdown();

		
		return result;
	}
	
	
}
