package be.nabu.utils.promise;

import java.util.concurrent.Executor;

import be.nabu.utils.promise.api.StageablePromise;

public class SinglePromise<T> extends BasePromise<T> implements StageablePromise<T> {
	
	private volatile T staged;
	
	public SinglePromise(Executor executor) {
		super(executor);
	}
	
	@Override
	public void resolve(final T result) {
		super.resolve(result);
	}
	
	@Override
	public void reject(final Exception exception) {
		super.reject(exception);
	}
	
	@Override
	public void stage(T result) {
		this.staged = result;
	}

	@Override
	public void resolve() {
		if (staged == null) {
			throw new IllegalStateException("No value has been staged");
		}
		this.resolve(staged);
	}
}
