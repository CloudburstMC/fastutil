/*
 * Copyright (C) 2017-2022 Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unimi.dsi.fastutil.doubles;

import static it.unimi.dsi.fastutil.BigArrays.copy;
import static it.unimi.dsi.fastutil.BigArrays.get;
import static it.unimi.dsi.fastutil.BigArrays.length;
import static it.unimi.dsi.fastutil.BigArrays.set;
import static it.unimi.dsi.fastutil.BigArrays.trim;
import static it.unimi.dsi.fastutil.BigArrays.wrap;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.MainRunner;

public class DoubleBigArraysTest {


	public static double[][] identity(final int n) {
		final double[][] perm = DoubleBigArrays.newBigArray(n);
		for(int i = n; i-- != 0;) set(perm, i , i);
		return perm;
	}

	@Test
	public void testQuickSort() {
		final double[] s = new double[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };

		Arrays.sort(s);
		final double[][] sorted = wrap(s.clone());

		double[][] a = wrap(s.clone());

		DoubleBigArrays.quickSort(a);
		assertArrayEquals(sorted, a);

		DoubleBigArrays.quickSort(a);
		assertArrayEquals(sorted, a);

		a = wrap(s.clone());

		DoubleBigArrays.quickSort(a, DoubleComparators.NATURAL_COMPARATOR);
		assertArrayEquals(sorted, a);

		DoubleBigArrays.quickSort(a, DoubleComparators.NATURAL_COMPARATOR);
		assertArrayEquals(sorted, a);

	}

	private void testCopy(final int n) {
		final double[][] a = DoubleBigArrays.newBigArray(n);
		for (int i = 0; i < n; i++) set(a, i, i);
		copy(a, 0, a, 1, n - 2);
		assertEquals(0, a[0][0], 0);
		for (int i = 0; i < n - 2; i++) assertEquals(i,  get(a, i + 1), 0);
		for (int i = 0; i < n; i++) set(a, i, i);
		copy(a, 1, a, 0, n - 1);
		for (int i = 0; i < n - 1; i++) assertEquals(i + 1, get(a, i) ,0);
		for (int i = 0; i < n; i++) set(a, i, i);
		final double[] b = new double[n];
		for (int i = 0; i < n; i++) b[i] = i;
		assertArrayEquals(a, wrap(b));
	}

	@Test
	public void testCopy10() {
		testCopy(10);
	}

	@Test
	public void testCopy1000() {
		testCopy(1000);
	}

	@Test
	public void testCopy1000000() {
		testCopy(1000000);
	}

	@Test
	public void testBinarySearch() {
		final double[] a = new double[] { 25, 32, 1, 3, 2, 0, 40, 7, 13, 12, 11, 10, -1, -6, -18, 2000 };

		Arrays.sort(a);
		final double[][] b = wrap(a.clone());

		for(int i = -1; i < 20; i++) {
			assertEquals(String.valueOf(i), Arrays.binarySearch(a, i), DoubleBigArrays.binarySearch(b, i));
			assertEquals(String.valueOf(i), Arrays.binarySearch(a, i), DoubleBigArrays.binarySearch(b, i, DoubleComparators.NATURAL_COMPARATOR));
		}

		for(int i = -1; i < 20; i++) {
			assertEquals(Arrays.binarySearch(a, 5, 13, i), DoubleBigArrays.binarySearch(b, 5, 13, i));
			assertEquals(Arrays.binarySearch(a, 5, 13, i), DoubleBigArrays.binarySearch(b, 5, 13, i, DoubleComparators.NATURAL_COMPARATOR));
		}
	}

	@Test
	public void testTrim() {
		final double[] a = new double[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final double[][] b = wrap(a.clone());

		for(int i = a.length; i-- != 0;) {
			final double[][] t = trim(b, i);
			final long l = length(t);
			assertEquals(i, l);
			for(int p = 0; p < l; p++) assertEquals(a[p], get(t, p), 0);

		}
	}

	@Test
	public void testEquals() {
		final double[] a = new double[] { 2, 1, 5, 2, 1, 0, 9, 1, 4, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		final double[][] b = wrap(a.clone());
		final double[][] c = wrap(a.clone());

		assertTrue(BigArrays.equals(b, c));
		b[0][0] = 0;
		assertFalse(BigArrays.equals(b, c));
	}

	@Test
	public void testRadixSort1() {
		double[][] t = wrap(new double[] { 2, 1, 0, 4 });
		DoubleBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = wrap(new double[] { 2, -1, 0, -4 });
		DoubleBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = DoubleBigArrays.shuffle(identity(100), new Random(0));
		DoubleBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = DoubleBigArrays.newBigArray(100);
		Random random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		DoubleBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));

		t = DoubleBigArrays.newBigArray(100000);
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		DoubleBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));
		for(long i = 100; i-- != 10;) set(t, i, random.nextInt());
		DoubleBigArrays.radixSort(t, 10, 100);
		for(long i = 99; i-- != 10;) assertTrue(get(t, i) <= get(t, i + 1));

		t = DoubleBigArrays.newBigArray(1000000);
		random = new Random(0);
		for(long i = length(t); i-- != 0;) set(t, i, random.nextInt());
		DoubleBigArrays.radixSort(t);
		for(long i = length(t) - 1; i-- != 0;) assertTrue(get(t, i) <= get(t, i + 1));
	}

	@Test
	public void testRadixSort2() {
		double d[][], e[][];
		d = DoubleBigArrays.newBigArray(10);
		for(long i = length(d); i-- != 0;) set(d, i, (int)(3 - i % 3));
		e = DoubleBigArrays.shuffle(identity(10), new Random(0));
		DoubleBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));

		d = DoubleBigArrays.newBigArray(100000);
		for(long i = length(d); i-- != 0;) set(d, i, (int)(100 - i % 100));
		e = DoubleBigArrays.shuffle(identity(100000), new Random(6));
		DoubleBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));

		d = DoubleBigArrays.newBigArray(10);
		for(long i = length(d); i-- != 0;) set(d, i, (int)(i % 3 - 2));
		Random random = new Random(0);
		e = DoubleBigArrays.newBigArray(length(d));
		for(long i = length(d); i-- != 0;) set(e, i, random.nextInt());
		DoubleBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));

		d = DoubleBigArrays.newBigArray(100000);
		random = new Random(0);
		for(long i = length(d); i-- != 0;) set(d, i, random.nextInt());
		e = DoubleBigArrays.newBigArray(length(d));
		for(long i = length(d); i-- != 0;) set(e, i, random.nextInt());
		DoubleBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));
		for(long i = 100; i-- != 10;) set(e, i, random.nextInt());
		DoubleBigArrays.radixSort(d, e, 10, 100);
		for(long i = 99; i-- != 10;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));

		d = DoubleBigArrays.newBigArray(1000000);
		random = new Random(0);
		for(long i = length(d); i-- != 0;) set(d, i, random.nextInt());
		e = DoubleBigArrays.newBigArray(length(d));
		for(long i = length(d); i-- != 0;) set(e, i, random.nextInt());
		DoubleBigArrays.radixSort(d, e);
		for(long i = length(d) - 1; i-- != 0;) assertTrue(Long.toString(i) + ": <" + get(d, i) + ", " + get(e, i) + ">, <" + get(d, i + 1) + ", " +  get(e, i + 1) + ">", get(d, i) < get(d, i + 1) || get(d, i) == get(d, i + 1) && get(e, i) <= get(e, i + 1));
	}


	@Test
	public void testShuffle() {
		final double[] a = new double[100];
		for(int i = a.length; i-- != 0;) a[i] = i;
		final double[][] b = wrap(a);
		DoubleBigArrays.shuffle(b, new Random());
		final boolean[] c = new boolean[a.length];
		for(long i = length(b); i-- != 0;) {
			assertFalse(c[(int)get(b, i)]);
			c[(int)get(b, i)] = true;
		}
	}

	@Test
	public void testShuffleFragment() {
		final double[] a = new double[100];
		for(int i = a.length; i-- != 0;) a[i] = -1;
		for(int i = 10; i < 30; i++) a[i] = i - 10;
		final double[][] b = wrap(a);
		DoubleBigArrays.shuffle(b, 10, 30, new Random());
		final boolean[] c = new boolean[20];
		for(int i = 20; i-- != 0;) {
			assertFalse(c[(int)get(b, i + 10)]);
			c[(int)get(b, i + 10)] = true;
		}
	}

	@Test
	public void testBinarySearchLargeKey() {
		final double[][] a = wrap(new double[] { 1, 2, 3 });
		DoubleBigArrays.binarySearch(a, 4);
	}

	@Test
	public void testRadixSortNaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final double[][] a = wrap(t.clone());
				DoubleBigArrays.radixSort(a, from, to);
				for (int i = to - 1; i-- != from;) assertTrue(Double.compare(get(a, i), get(a, i + 1)) <= 0);
			}

	}

	@Test
	public void testRadixSort2NaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final double[][] a = wrap(t.clone());
				final double[][] b = wrap(t.clone());
				DoubleBigArrays.radixSort(a, b, from, to);
				for(int i = to - 1; i-- != from;) {
					assertTrue(Double.compare(get(a, i), get(a, i + 1)) <= 0);
					assertTrue(Double.compare(get(b, i), get(b, i + 1)) <= 0);
				}
			}

	}

	@Test
	public void testQuickSortNaNs() {
		final double[] t = { Double.NaN, 1, 5, 2, 1, 0, 9, 1, Double.NaN, 2, 4, 6, 8, 9, 10, 12, 1, 7 };
		for(int to = 1; to < t.length; to++)
			for(int from = 0; from < to; from++) {
				final double[][] a = wrap(t.clone());
				DoubleBigArrays.quickSort(a, from, to);
				for (int i = to - 1; i-- != from;) assertTrue(Double.compare(get(a, i), get(a, i + 1)) <= 0);
			}

	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(DoubleBigArrays.class, "test", /*num=*/"10000", /*seed=*/"293843");
	}
}