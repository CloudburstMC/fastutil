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

package it.unimi.dsi.fastutil.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class InspectableFileCachedInputStreamTest {

	private static final Random r = new Random(0);

	public static List<byte[]> byteArrays;
	static {
		byteArrays = new ArrayList<>();
		byte[] b;
		// Now generates byte buffers from 1 byte up to 64KiB; we shuffle them so that they are not increasing in size...
		for (int k = 0; k < 10; k++) {
			b = new byte[1 << k];
			r.nextBytes(b);
			byteArrays.add(b);
		}
		for (int k = 16; k >= 10; k--) {
			b = new byte[1 << k];
			r.nextBytes(b);
			byteArrays.add(b);
		}
		byteArrays.add(new byte[] {});
		byteArrays.add("This is a short\nnon empty and purely ASCII\nbyte sequence".getBytes());
	}


	@Test
	public void testSmall() throws IOException {
		InspectableFileCachedInputStream icis = new InspectableFileCachedInputStream(4);
		assertTrue(icis.isOpen());
		byte[] data = new byte[] { 1, 2 };
		icis.write(ByteBuffer.wrap(data));

		assertEquals(2, icis.length());
		assertEquals(1, icis.read());
		assertEquals(2, icis.read());
		assertEquals(-1, icis.read());

		icis.position(0);
		byte b[] = new byte[2];
		assertEquals(2, icis.read(b));
		assertArrayEquals(data, b);
		assertEquals(-1, icis.read());
		assertEquals(-1, icis.read(b, 0, b.length));
		assertEquals(0, icis.read(b, 0, 0));

		icis.clear();
		assertTrue(icis.isOpen());
		data = new byte[] { 1, 2, 3, 4, 5 };
		icis.write(ByteBuffer.wrap(data));

		assertEquals(5, icis.length());
		assertEquals(1, icis.read());
		assertEquals(2, icis.read());
		assertEquals(3, icis.read());
		assertEquals(4, icis.read());
		assertEquals(5, icis.read());
		assertEquals(-1, icis.read());

		icis.position(0);
		assertEquals(0, icis.position());
		b = new byte[5];
		assertEquals(5, icis.read(b));
		assertArrayEquals(data, b);

		icis.position(2);
		b = new byte[4];
		assertEquals(3, icis.read(b));
		assertArrayEquals(Arrays.copyOfRange(data, 2, 5), Arrays.copyOfRange(b, 0, 3));


		icis.position(0);
		assertEquals(1, icis.read());

		icis.position(4);
		assertEquals(1, icis.available());
		assertEquals(5, icis.read());
		assertEquals(5, icis.position());

		icis.position(0);
		assertEquals(2, icis.skip(2));
		assertEquals(2, icis.skip(2));
		assertEquals(5, icis.read());
		assertEquals(5, icis.position());

		icis.position(5);
		assertEquals(-1, icis.read());
		assertEquals(-1, icis.read(b, 0, b.length));

		icis.close();
		icis.dispose();
	}

	@Test
	public void test() throws IOException {
		for(int bufferSize: new int[] { 1, 2, 1024, 16384, 1024 * 1024 }) {
			InspectableFileCachedInputStream icis = new InspectableFileCachedInputStream(bufferSize);
			for(byte[] a: byteArrays) icis.write(ByteBuffer.wrap(a));
			for(byte[] a: byteArrays) {
				final byte[] buffer = new byte[a.length];
				icis.read(buffer);
				assertArrayEquals(a, buffer);
			}

			icis.position(0);
			icis.truncate(0);

			for(byte[] a: byteArrays)
				for(byte b: a) assertEquals(b, (byte)icis.read());

			icis.close();
			icis.dispose();
		}
	}

	@Test
	public void testWithSpecifiedFile() throws IOException {
		final InspectableFileCachedInputStream icis = new InspectableFileCachedInputStream(4, File.createTempFile(getClass().getSimpleName(), "overflow"));
		final byte[] data = new byte[] { 1, 2 };
		icis.write(ByteBuffer.wrap(data));

		assertEquals(2, icis.length());
		assertEquals(1, icis.read());
		assertEquals(2, icis.read());
		assertEquals(-1, icis.read());

		icis.close();
		icis.dispose();
	}

	@Test(expected=IOException.class)
	public void testClosed() throws IOException {
		final InspectableFileCachedInputStream icis = new InspectableFileCachedInputStream(4);
		final byte[] data = new byte[] { 1, 2 };
		icis.write(ByteBuffer.wrap(data));
		icis.close();
		assertFalse(icis.isOpen());
		icis.read();
	}

	@Test(expected=IOException.class)
	public void testDisposed() throws IOException {
		@SuppressWarnings("resource")
		final InspectableFileCachedInputStream icis = new InspectableFileCachedInputStream(4);
		final byte[] data = new byte[] { 1, 2 };
		icis.write(ByteBuffer.wrap(data));
		icis.dispose();
		assertFalse(icis.isOpen());
		icis.read();
	}

	@Test(expected=IOException.class)
	public void testClearDisposed() throws IOException {
		@SuppressWarnings("resource")
		final InspectableFileCachedInputStream icis = new InspectableFileCachedInputStream();
		final byte[] data = new byte[] { 1, 2 };
		icis.write(ByteBuffer.wrap(data));
		icis.dispose();
		icis.clear();
	}

	@Test(expected=IOException.class)
	public void testResetDisposed() throws IOException {
		@SuppressWarnings("resource")
		final InspectableFileCachedInputStream icis = new InspectableFileCachedInputStream();
		final byte[] data = new byte[] { 1, 2 };
		icis.write(ByteBuffer.wrap(data));
		icis.dispose();
		icis.reset();
	}

	@SuppressWarnings("resource")
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeBuffer() throws IOException {
		new InspectableFileCachedInputStream(-1);
	}

}