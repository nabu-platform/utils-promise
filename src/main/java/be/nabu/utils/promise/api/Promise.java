package be.nabu.utils.promise.api;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface Promise<T> extends Future<T> {
	public Promise<T> then(final SuccessHandler<T> successHandler);
	public Promise<T> then(final ExceptionHandler exceptionHandler);
	public Promise<T> then(final CancelHandler cancelHandler);
	public Promise<T> then(final SuccessHandler<T> successHandler, final ExceptionHandler exceptionHandler);
	public Promise<T> then(final SuccessHandler<T> successHandler, final ExceptionHandler exceptionHandler, final CancelHandler cancelHandler);
	public Promise<T> timeout(long timeout, TimeUnit unit);
}
