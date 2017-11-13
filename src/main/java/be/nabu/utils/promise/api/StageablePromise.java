package be.nabu.utils.promise.api;

public interface StageablePromise<T> extends ResolvablePromise<T> {
	public void stage(T result);
	public void resolve();
}
