package oripa.util.concurrent;


public interface SingleInProcessFactory<Input, Output> {

	public SingleInProcess<Input, Output> create();
}
