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
