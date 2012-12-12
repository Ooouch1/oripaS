package oripa.util.concurrent;

import java.util.concurrent.Callable;

public abstract class SingleInProcess<Input, Output> {
	private Input value;
	
	/**
	 * defines an operation using given collection.
	 * @param value
	 * @return result of processing
	 */
	public abstract Output run(Input value);
	
	public final void setValue(Input value){
		this.value = value;
		
	}
	
	public final Runnable getRunnable(Input value){
		setValue(value);
		
		return new Runnable() {
			
			@Override
			public void run() {
				SingleInProcess.this.run(
						SingleInProcess.this.value);
			}
		};
	}
	
	public final Callable<Output> getCallable(Input value){
		setValue(value);
		
		return new Callable<Output>() {
			@Override
			public Output call() throws Exception {
				Input v = SingleInProcess.this.value;
				return SingleInProcess.this.run(v);
			}
		};
	}

}
