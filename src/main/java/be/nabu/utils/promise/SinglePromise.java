package be.nabu.utils.promise;

import java.util.concurrent.Executor;

public class SinglePromise<T> extends BasePromise<T> {
	
	public SinglePromise(Executor executor) {
		super(executor);
	}
	
	public void resolve(final T result) {
		super.resolve(result);
	}
	public void reject(final Exception exception) {
		super.reject(exception);
	}
}
