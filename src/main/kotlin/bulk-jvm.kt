/*
 * Copyright (c) 2014-2015 Mark Platvoet<mplatvoet@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package nl.mplatvoet.komponents.kovenant

import java.util.ArrayList
import java.util.concurrent.atomic.AtomicInteger


private fun concreteAll<V, E>(vararg promises: Promise<V, E>): Promise<List<V>, E>{
    val results = ArrayList<V>(promises.size())
    (0..promises.size()-1).forEach { results.add(null as V) }
    val deferred = Kovenant.newDeferred<List<V>, E>()
    val successCount = AtomicInteger(promises.size())
    val failCount = AtomicInteger(0)
    promises.forEachIndexed {
        i, promise ->
            promise.success { v ->
                results[i] = v
                if (successCount.decrementAndGet() == 0) {
                    deferred.resolve(results)
                }
            }
            promise.fail { e ->
                if(failCount.incrementAndGet() == 1) {
                    deferred.reject(e)
                }
            }

    }

    return deferred.promise
}







