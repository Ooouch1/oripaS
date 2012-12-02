package oripa.concurrent;


public interface PartialCollectionProcessFactory<Value, Output> {
	public abstract PartialCollectionProcess<Value, Output> create();
}
