package be.nabu.utils.promise.api;

public interface ResolvablePromise<T> extends Promise<T> {
	public void resolve(final T result);
	public void reject(final Exception exception);
}
