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
