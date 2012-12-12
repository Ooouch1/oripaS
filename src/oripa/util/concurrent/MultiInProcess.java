package oripa.util.concurrent;

import java.util.Collection;
import java.util.concurrent.Callable;


public abstract class MultiInProcess <Value, Output>{
	private Collection<Value> values;

	public MultiInProcess(){
	}

	/**
	 * defines an operation using given collection.
	 * @param values
	 * @return result of processing
	 */
	public abstract Output run(final Collection<Value> values);
	
	public final void setValues(Collection<Value> values){
		this.values = values;
		
	}
	
	public final Runnable getRunnable(Collection<Value> values){
		setValues(values);
		
		return new Runnable() {
			
			@Override
			public void run() {
				MultiInProcess.this.run(
						MultiInProcess.this.values);
			}
		};
		
	}
	
	public final Callable<Output> getCallable(final Collection<Value> values){
		setValues(values);
		
		return new Callable<Output>() {
			@Override
			public Output call() throws Exception {
				return MultiInProcess.this.run(MultiInProcess.this.values);
			}
		};
	}

}