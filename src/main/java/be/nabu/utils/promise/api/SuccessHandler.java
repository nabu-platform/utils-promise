package be.nabu.utils.promise.api;

public interface SuccessHandler<T> {
	public void handle(T result);
}
