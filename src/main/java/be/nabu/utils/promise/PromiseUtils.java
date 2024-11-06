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

import java.util.List;
import java.util.concurrent.Executor;

import be.nabu.utils.promise.api.Promise;
import be.nabu.utils.promise.api.StageablePromise;

public class PromiseUtils {
	public static <T> StageablePromise<T> defer(Executor executor) {
		return new SinglePromise<T>(executor);
	}
	public static <T> Promise<List<T>> combine(Executor executor, List<Promise<T>> promises) {
		return new CombinedPromise<T>(executor, promises);
	}
}
