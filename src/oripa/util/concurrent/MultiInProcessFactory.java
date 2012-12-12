package oripa.util.concurrent;



public interface MultiInProcessFactory<Value, Output> {
	public abstract MultiInProcess<Value, Output> create();
}
