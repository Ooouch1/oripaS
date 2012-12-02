package oripa.concurrent;

import java.util.Collection;
import java.util.concurrent.Callable;


public abstract class PartialCollectionProcess<Value, Output>{
	private Collection<Value> values;

	public PartialCollectionProcess(){
	}

	/**
	 * defines an operation using given collection, whose result is stored in a collection.
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
				PartialCollectionProcess.this.run(
						PartialCollectionProcess.this.values);
			}
		};
	}
	
	public final Callable<Output> getCallable(Collection<Value> values){
		setValues(values);
		
		return new Callable<Output>() {
			@Override
			public Output call() throws Exception {
				Collection<Value> v = PartialCollectionProcess.this.values;
				return PartialCollectionProcess.this.run(v);
			}
		};
	}

}