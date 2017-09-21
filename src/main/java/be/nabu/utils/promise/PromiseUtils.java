package be.nabu.utils.promise;

import java.util.List;
import java.util.concurrent.Executor;

import be.nabu.utils.promise.api.Promise;

public class PromiseUtils {
	public static <T> Promise<T> defer(Executor executor) {
		return new SinglePromise<T>(executor);
	}
	public static <T> Promise<List<T>> combine(Executor executor, List<Promise<T>> promises) {
		return new CombinedPromise<T>(executor, promises);
	}
}
