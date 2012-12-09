package oripa.concurrent;

public interface SingleInProcessFactory<Input, Output> {

	public SingleInProcess<Input, Output> create();
}
