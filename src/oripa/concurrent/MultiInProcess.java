package oripa.concurrent;

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
	public abstract Output run(Collection<Value> values);
	
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
	
	public final Callable<Output> getCallable(Collection<Value> values){
		setValues(values);
		
		return new Callable<Output>() {
			@Override
			public Output call() throws Exception {
				Collection<Value> v = MultiInProcess.this.values;
				return MultiInProcess.this.run(v);
			}
		};
	}

}