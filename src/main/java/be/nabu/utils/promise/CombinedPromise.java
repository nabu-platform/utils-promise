/*
* Copyright (C) 2017 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.utils.promise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import be.nabu.utils.promise.api.CancelHandler;
import be.nabu.utils.promise.api.ExceptionHandler;
import be.nabu.utils.promise.api.Promise;
import be.nabu.utils.promise.api.SuccessHandler;

public class CombinedPromise<T> extends BasePromise<List<T>> {

	private List<T> results = Collections.synchronizedList(new ArrayList<T>());
	private List<Exception> exceptions = Collections.synchronizedList(new ArrayList<Exception>());
	private List<Promise<T>> cancelled = Collections.synchronizedList(new ArrayList<Promise<T>>());
	private List<Promise<T>> promises;
	
	public CombinedPromise(Executor executor, final List<Promise<T>> promises) {
		super(executor);
		this.promises = promises;
		for (final Promise<T> promise : promises) {
			promise.then(new SuccessHandler<T>() {
				@Override
				public void handle(T result) {
					results.add(result);
					runIfFull();
				}
			}, new ExceptionHandler() {
				@Override
				public void handle(Exception exception) {
					exceptions.add(exception);
					runIfFull();
				}
			}, new CancelHandler() {
				@Override
				public boolean cancel() {
					cancelled.add(promise);
					runIfFull();
					return true;
				}
			});
		}
	}
	
	private void runIfFull() {
		if (results.size() + exceptions.size() == promises.size() - cancelled.size()) {
			if (exceptions.size() > 0) {
				for (int i = 1; i < exceptions.size(); i++) {
					exceptions.get(0).addSuppressed(exceptions.get(i));
				}
				reject(exceptions.get(0));
			}
			else {
				resolve(results);
			}
		}
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// normal cancel
		boolean result = super.cancel(mayInterruptIfRunning);
		// bubble to children
		for (Promise<T> promise : promises) {
			if (!cancelled.contains(promise)) {
				promise.cancel(mayInterruptIfRunning);
				cancelled.add(promise);
			}
		}
		return result;
	}

}
