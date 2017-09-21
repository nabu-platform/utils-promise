package be.nabu.utils.promise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import be.nabu.utils.promise.api.CancelHandler;
import be.nabu.utils.promise.api.ExceptionHandler;
import be.nabu.utils.promise.api.Promise;
import be.nabu.utils.promise.api.SuccessHandler;

public class BasePromise<T> implements Promise<T> {
	private CountDownLatch latch = new CountDownLatch(1);
	private volatile T result;
	private volatile Exception exception;
	private List<CancelHandler> cancelHandlers = Collections.synchronizedList(new ArrayList<CancelHandler>());
	private List<SuccessHandler<T>> successHandlers = Collections.synchronizedList(new ArrayList<SuccessHandler<T>>());
	private List<ExceptionHandler> exceptionHandlers = Collections.synchronizedList(new ArrayList<ExceptionHandler>());
	private Executor executor;
	private boolean cancelled;
	
	public BasePromise(Executor executor) {
		this.executor = executor;
	}
	
	@Override
	public boolean isDone() {
		return result != null;
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		try {
			return get(365, TimeUnit.DAYS);
		}
		catch (TimeoutException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if (latch.await(timeout, unit)) {
			if (result == null) {
				throw new ExecutionException("No result found", exception);
			}
			else {
				return result;
			}
        }
		else {
            throw new TimeoutException();
    	}
	}

	protected void resolve(final T result) {
		// resolve only once
		if (this.result == null && this.exception == null) {
			this.result = result;
			latch.countDown();
			run(new Runnable() {
				public void run() {
					for (SuccessHandler<T> handler : successHandlers) {
						try {
							handler.handle(result);
						}
						catch (Exception e) {
							// ignore
						}
					}
				}
			});
		}
	}

	protected void reject(final Exception exception) {
		if (this.result == null && this.exception == null) {
			this.exception = exception;
			run(new Runnable() {
				public void run() {
					for (ExceptionHandler handler : exceptionHandlers) {
						try {
							handler.handle(exception);
						}
						catch (Exception e) {
							// ignore
						}
					}
				}
			});
		}
	}

	protected void run(Runnable runnable) {
		if (executor != null) {
			executor.execute(runnable);
		}
		else {
			runnable.run();
		}
	}
	
	@Override
	public boolean cancel(final boolean mayInterruptIfRunning) {
		if (!cancelled) {
			cancelled = true;
			if (!cancelHandlers.isEmpty()) {
				run(new Runnable() {
					public void run() {
						for (CancelHandler cancelHandler : cancelHandlers) {
							try {
								cancelHandler.cancel();
							}
							catch (Exception e) {
								// ignore
							}
						}
					}
				});
			}
		}
		return cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public BasePromise<T> then(final CancelHandler cancelHandler) {
		if (cancelled) {
			run(new Runnable() {
				public void run() {
					try {
						cancelHandler.cancel();
					}
					catch (Exception e) {
						// ignore
					}
				}
			});
		}
		this.cancelHandlers.add(cancelHandler);
		return this;
	}
	
	@Override
	public BasePromise<T> then(final SuccessHandler<T> successHandler) {
		// if the promise is already resolved, run it immediately
		if (result != null) {
			run(new Runnable() {
				public void run() {
					try {
						successHandler.handle(result);
					}
					catch (Exception e) {
						// ignore
					}
				}
			});
		}
		this.successHandlers.add(successHandler);
		return this;
	}
	
	@Override
	public BasePromise<T> then(final ExceptionHandler exceptionHandler) {
		if (exception != null) {
			run(new Runnable() {
				public void run() {
					try {
						exceptionHandler.handle(exception);
					}
					catch (Exception e) {
						// ignore
					}
				}
			});
		}
		this.exceptionHandlers.add(exceptionHandler);
		return this;
	}
	
	@Override
	public BasePromise<T> then(final SuccessHandler<T> successHandler, final ExceptionHandler exceptionHandler) {
		return then(successHandler, exceptionHandler, null);
	}
	
	@Override
	public BasePromise<T> then(final SuccessHandler<T> successHandler, final ExceptionHandler exceptionHandler, final CancelHandler cancelHandler) {
		if (successHandler != null) {
			then(successHandler);
		}
		if (exceptionHandler != null) {
			then(exceptionHandler);
		}
		if (cancelHandler != null) {
			then(cancelHandler);
		}
		return this;
	}
}
