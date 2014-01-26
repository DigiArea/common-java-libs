// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// http://code.google.com/p/protobuf/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
/**
 * Copyright (c) 2009-2013 DigiArea, Inc. All rights reserved.
 * 
 * ZippyBuffer derived from Protocol Buffers - Google's data interchange format
 * 
 * @author norb 
 */
package com.digiarea.zippy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * The Class ZippyBuffer.
 */
public final class ZippyBuffer {

	/** The buffer. */
	private final byte[] buffer;

	/** The limit. */
	private final int limit;

	/** The position. */
	private int position;

	/** The mark. */
	private int mark;

	/**
	 * Instantiates a new zippy buffer reader.
	 * 
	 * @param buffer
	 *            the buffer
	 */
	public ZippyBuffer(final byte[] buffer) {
		this(buffer, 0, buffer.length);
	}

	/**
	 * Instantiates a new zippy buffer reader.
	 * 
	 * @param buffer
	 *            the buffer
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 */
	public ZippyBuffer(final byte[] buffer, final int offset, final int length) {
		super();
		this.buffer = buffer;
		limit = offset + length;
		position = offset;
	}

	/**
	 * Gets the buffer.
	 * 
	 * @return the buffer
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	// ------------------------ READER --------------------------

	/**
	 * Checks if WebSocket frame is a closing handshake
	 * 
	 * @return boolean
	 * @throws IOException
	 */
	public boolean isWsClosingHandshake() throws IOException {
		long frameSize = 0;
		byte type = readRawByte();
		byte b = readRawByte();
		frameSize <<= 7;
		frameSize |= b & 0x7f;
		// return back
		position -= 2;
		return type == 0xFF && frameSize == 0;
	}

	/**
	 * Reads WebSocket frame length with the frame type first bite from the
	 * stream if first bite is {@code 0xFF} this is a closing handshake and the
	 * length equals zero, first bite equals {@code 0x00} then exception thrown
	 * as we do not support text protocols.
	 * 
	 * @return the length
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readWsFrameLength() throws IOException {
		byte type = readRawByte();
		if (type == 0xFF) {
			return 0;
		} else if ((type & 0x80) == 0x80) {
			// If the MSB on type is set, decode the frame length
			long frameSize = 0;
			int lengthFieldSize = 0;
			byte b;
			do {
				b = readRawByte();
				frameSize <<= 7;
				frameSize |= b & 0x7f;
				lengthFieldSize++;
				if (lengthFieldSize > 8) {
					// Perhaps a malicious peer?
					throw new IOException(
							"ZippyBufferReader to long WebSocket frame.");
				}
			} while ((b & 0x80) == 0x80);
			return (int) frameSize;
		} else {
			throw new IOException("ZippyBufferReader get text WebSocket frame.");
		}
	}

	/**
	 * Read a {@code double} field value from the stream.
	 * 
	 * @return the double
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readRawLittleEndianLong());
	}

	/**
	 * Read a {@code float} field value from the stream.
	 * 
	 * @return the float
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readRawLittleEndianInt());
	}

	/**
	 * Read an {@code Int} field value from the stream.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readInt() throws IOException {
		return readRawVarInt();
	}

	/**
	 * Read an {@code Long} field value from the stream.
	 * 
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public long readLong() throws IOException {
		return readRawVarLong();
	}

	/**
	 * Read a {@code FixedInt} field value from the stream.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readFixedInt() throws IOException {
		return readRawLittleEndianInt();
	}

	/**
	 * Read a {@code FixedLong} field value from the stream.
	 * 
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public long readFixedLong() throws IOException {
		return readRawLittleEndianLong();
	}

	/**
	 * Read a {@code UnsignedInt} field value from the stream.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readUnsignedInt() throws IOException {
		return readRawVarInt();
	}

	/**
	 * Read a {@code UnsignedLong} field value from the stream.
	 * 
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public long readUnsignedLong() throws IOException {
		return readRawVarLong();
	}

	/**
	 * Read an {@code SignedFixedInt} field value from the stream.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readUnsignedFixedInt() throws IOException {
		return readRawLittleEndianInt();
	}

	/**
	 * Read an {@code SignedFixedLong} field value from the stream.
	 * 
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public long readUnsignedFixedLong() throws IOException {
		return readRawLittleEndianLong();
	}

	/**
	 * Read an {@code SignedInt} field value from the stream.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readSignedInt() throws IOException {
		return decodeZigZagInt(readRawVarInt());
	}

	/**
	 * Read an {@code SignedLong} field value from the stream.
	 * 
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public long readSignedLong() throws IOException {
		return decodeZigZagLong(readRawVarLong());
	}

	/**
	 * Read an {@code SignedFixedInt} field value from the stream.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readSignedFixedInt() throws IOException {
		return readRawLittleEndianInt();
	}

	/**
	 * Read an {@code SignedFixedLong} field value from the stream.
	 * 
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public long readSignedFixedLong() throws IOException {
		return readRawLittleEndianLong();
	}

	/**
	 * Read a {@code bool} field value from the stream.
	 * 
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public boolean readBoolean() throws IOException {
		return readRawVarInt() != 0;
	}

	/**
	 * Read a {@code string} field value from the stream.
	 * 
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String readString() throws IOException {
		final int size = readRawVarInt();
		if (size <= (limit - position) && size > 0) {
			// Fast path: We already have the bytes in a contiguous buffer, so
			// just copy directly from it.
			final String result = new String(buffer, position, size, "UTF-8");
			position += size;
			return result;
		} else {
			// Slow path: Build a byte array first then copy it.
			return new String(readRawBytes(size), "UTF-8");
		}
	}

	/**
	 * Read a {@code bytes} field value from the stream.
	 * 
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public byte[] readBytes() throws IOException {
		final int size = readRawVarInt();
		return readRawBytes(size);
	}

	// -------------------------------------------------------------------

	/**
	 * Read a raw VarInt from the stream. If larger than 32 bits, discard the
	 * upper bits.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readRawVarInt() throws IOException {
		byte tmp = readRawByte();
		if (tmp >= 0) {
			return tmp;
		}
		int result = tmp & 0x7f;
		if ((tmp = readRawByte()) >= 0) {
			result |= tmp << 7;
		} else {
			result |= (tmp & 0x7f) << 7;
			if ((tmp = readRawByte()) >= 0) {
				result |= tmp << 14;
			} else {
				result |= (tmp & 0x7f) << 14;
				if ((tmp = readRawByte()) >= 0) {
					result |= tmp << 21;
				} else {
					result |= (tmp & 0x7f) << 21;
					result |= (tmp = readRawByte()) << 28;
					if (tmp < 0) {
						// Discard upper 32 bits.
						for (int i = 0; i < 5; i++) {
							if (readRawByte() >= 0) {
								return result;
							}
						}
						throw new IOException(
								"ZippyBuffer encountered a malformed varint.");
					}
				}
			}
		}
		return result;
	}

	/**
	 * Read a raw VarLong from the buffer.
	 * 
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public long readRawVarLong() throws IOException {
		int shift = 0;
		long result = 0;
		while (shift < 64) {
			final byte b = readRawByte();
			result |= (long) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				return result;
			}
			shift += 7;
		}
		throw new IOException("ZippyBuffer encountered a malformed varint.");
	}

	/**
	 * Read a 32-bit little-endian integer from the stream.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readRawLittleEndianInt() throws IOException {
		final byte b1 = readRawByte();
		final byte b2 = readRawByte();
		final byte b3 = readRawByte();
		final byte b4 = readRawByte();
		return (((int) b1 & 0xff)) | (((int) b2 & 0xff) << 8)
				| (((int) b3 & 0xff) << 16) | (((int) b4 & 0xff) << 24);
	}

	/**
	 * Read a 64-bit little-endian integer from the stream.
	 * 
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public long readRawLittleEndianLong() throws IOException {
		final byte b1 = readRawByte();
		final byte b2 = readRawByte();
		final byte b3 = readRawByte();
		final byte b4 = readRawByte();
		final byte b5 = readRawByte();
		final byte b6 = readRawByte();
		final byte b7 = readRawByte();
		final byte b8 = readRawByte();
		return (((long) b1 & 0xff)) | (((long) b2 & 0xff) << 8)
				| (((long) b3 & 0xff) << 16) | (((long) b4 & 0xff) << 24)
				| (((long) b5 & 0xff) << 32) | (((long) b6 & 0xff) << 40)
				| (((long) b7 & 0xff) << 48) | (((long) b8 & 0xff) << 56);
	}

	/**
	 * Decode a ZigZag-encoded 32-bit value. ZigZag encodes signed integers into
	 * values that can be efficiently encoded with varint. (Otherwise, negative
	 * values must be sign-extended to 64 bits to be varint encoded, thus always
	 * taking 10 bytes on the wire.)
	 * 
	 * @param n
	 *            An unsigned 32-bit integer, stored in a signed int because
	 *            Java has no explicit unsigned support.
	 * @return A signed 32-bit integer.
	 */
	public static int decodeZigZagInt(final int n) {
		return (n >>> 1) ^ -(n & 1);
	}

	/**
	 * Decode a ZigZag-encoded 64-bit value. ZigZag encodes signed integers into
	 * values that can be efficiently encoded with varint. (Otherwise, negative
	 * values must be sign-extended to 64 bits to be varint encoded, thus always
	 * taking 10 bytes on the wire.)
	 * 
	 * @param n
	 *            An unsigned 64-bit integer, stored in a signed int because
	 *            Java has no explicit unsigned support.
	 * @return A signed 64-bit integer.
	 */
	public static long decodeZigZagLong(final long n) {
		return (n >>> 1) ^ -(n & 1);
	}

	/**
	 * Read one byte from the input.
	 * 
	 * @return the byte
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public byte readRawByte() throws IOException {
		if (position == limit) {
			// out of space
			throw new IOException("ZippyBuffer ran out of space.");
		}
		return buffer[position++];
	}

	/**
	 * Read a fixed size of bytes from the input.
	 * 
	 * @param size
	 *            the size
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public byte[] readRawBytes(final int size) throws IOException {
		if (size <= limit - position) {
			// We have all the bytes we need already.
			final byte[] bytes = new byte[size];
			System.arraycopy(buffer, position, bytes, 0, size);
			position += size;
			return bytes;
		} else {
			// out of space
			throw new IOException("ZippyBuffer ran out of space.");
		}
	}

	/**
	 * Attempt to read a sector, returning zero if we have reached EOF. Zippy
	 * protocol packet parsers use this to read sectors, since a zippy protocol
	 * packet may legally end wherever a sector occurs, and zero is not a valid
	 * sector number.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readSector() throws IOException {
		if (isEmpty()) {
			return 0;
		}
		return readRawVarInt();
	}

	/**
	 * Checks if buffer is empty.
	 * 
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return position == limit;
	}

	// ------------------------ SIZER --------------------------

	/**
	 * Compute the number of bytes that would be needed to encode WebSocket
	 * frame length
	 * 
	 * @param length
	 *            the length
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int sizeOfWsFrameLength(byte type, int length) throws IOException {
		// one bite for the type
		int size = 1;
		// Encode length.
		int b1 = length >>> 28 & 0x7F;
		int b2 = length >>> 14 & 0x7F;
		int b3 = length >>> 7 & 0x7F;
		if (b1 == 0) {
			if (b2 == 0) {
				if (b3 == 0) {
					size += 1;
				} else {
					size += 2;
				}
			} else {
				size += 3;
			}
		} else {
			size += 4;
		}
		return size;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a double.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code double} sector, including sector.
	 */
	public static int sizeOfDouble(final double value) {
		return LITTLE_ENDIAN_64_SIZE;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a float.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code float} sector, including sector.
	 */
	public static int sizeOfFloat(final float value) {
		return LITTLE_ENDIAN_32_SIZE;
	}

	/**
	 * Compute the number of bytes that would be needed to encode an
	 * UnsignedInt.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code UnsignedInt} sector.
	 */
	public static int sizeOfUnsignedInt(final int value) {
		return sizeOfRawVarInt(value);
	}

	/**
	 * Compute the number of bytes that would be needed to encode an
	 * UnsignedLong.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code UnsignedLong} sector, including sector.
	 */
	public static int sizeOfUnsignedLong(final long value) {
		return sizeOfRawVarLong(value);
	}

	/**
	 * Compute the number of bytes that would be needed to encode an Int.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code int} sector, excluding sector.
	 */
	public static int sizeOfInt(final int value) {
		if (value >= 0) {
			return sizeOfRawVarInt(value);
		} else {
			// Must sign-extend.
			return 10;
		}
	}

	/**
	 * Compute the number of bytes that would be needed to encode a Long.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code long} sector, excluding sector.
	 */
	public static int sizeOfLong(final long value) {
		return sizeOfRawVarLong(value);
	}

	/**
	 * Compute the number of bytes that would be needed to encode a FixedInt.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code FixedInt} sector.
	 */
	public static int sizeOfUnsignedFixedInt(final int value) {
		return LITTLE_ENDIAN_32_SIZE;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a FixedLong.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code FixedLong} sector.
	 */
	public static int sizeOfUnsignedFixedLong(final long value) {
		return LITTLE_ENDIAN_64_SIZE;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a {@code bool}
	 * sector.
	 * 
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int sizeOfBoolean(final boolean value) {
		return 1;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a String.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code String} sector.
	 */
	public static int sizeOfString(final String value) {
		try {
			final byte[] bytes = value.getBytes("UTF-8");
			return sizeOfRawVarInt(bytes.length) + bytes.length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 not supported.", e);
		}
	}

	/**
	 * Compute the number of bytes that would be needed to encode a.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code bytes} sector.
	 */
	public static int sizeOfBytes(final byte[] value) {
		return sizeOfRawVarInt(value.length) + value.length;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a single byte
	 * sector.
	 * 
	 * @param b
	 *            the b
	 * @return the int
	 */
	public static int sizeOfRawByte(byte b) {
		return 1;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a
	 * SignedFixedInt.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code SignedFixedInt} sector.
	 */
	public static int sizeOfSignedFixedInt(final int value) {
		return LITTLE_ENDIAN_32_SIZE;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a
	 * SignedFixedLong.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code SignedFixedLong} sector.
	 */
	public static int sizeOfSignedFixedLong(final long value) {
		return LITTLE_ENDIAN_64_SIZE;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a SignedInt.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code SignedInt} sector.
	 */
	public static int sizeOfSignedInt(final int value) {
		return sizeOfRawVarInt(encodeZigZagInt(value));
	}

	/**
	 * Compute the number of bytes that would be needed to encode a SignedLong.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code SignedLong} sector.
	 */
	public static int sizeOfSignedLong(final long value) {
		return sizeOfRawVarLong(encodeZigZagLong(value));
	}

	/**
	 * Compute the number of bytes that would be needed to encode a sector.
	 * 
	 * @param sectorNumber
	 *            the sector number
	 * @return the int
	 */
	public static int sizeOfSector(final int sectorNumber) {
		return sizeOfRawVarInt(sectorNumber);
	}

	/**
	 * Compute the number of bytes that would be needed to encode a VarInt.
	 * 
	 * @param value
	 *            the value
	 * @return the int {@code value} is treated as unsigned, so it won't be
	 *         sign-extended if negative.
	 */
	public static int sizeOfRawVarInt(final int value) {
		if ((value & (0xffffffff << 7)) == 0) {
			return 1;
		}
		if ((value & (0xffffffff << 14)) == 0) {
			return 2;
		}
		if ((value & (0xffffffff << 21)) == 0) {
			return 3;
		}
		if ((value & (0xffffffff << 28)) == 0) {
			return 4;
		}
		return 5;
	}

	/**
	 * Compute the number of bytes that would be needed to encode a VarLong.
	 * 
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int sizeOfRawVarLong(final long value) {
		if ((value & (0xffffffffffffffffL << 7)) == 0) {
			return 1;
		}
		if ((value & (0xffffffffffffffffL << 14)) == 0) {
			return 2;
		}
		if ((value & (0xffffffffffffffffL << 21)) == 0) {
			return 3;
		}
		if ((value & (0xffffffffffffffffL << 28)) == 0) {
			return 4;
		}
		if ((value & (0xffffffffffffffffL << 35)) == 0) {
			return 5;
		}
		if ((value & (0xffffffffffffffffL << 42)) == 0) {
			return 6;
		}
		if ((value & (0xffffffffffffffffL << 49)) == 0) {
			return 7;
		}
		if ((value & (0xffffffffffffffffL << 56)) == 0) {
			return 8;
		}
		if ((value & (0xffffffffffffffffL << 63)) == 0) {
			return 9;
		}
		return 10;
	}

	/** The Constant LITTLE_ENDIAN_32_SIZE. */
	private static final int LITTLE_ENDIAN_32_SIZE = 4;
	/** The Constant LITTLE_ENDIAN_64_SIZE. */
	private static final int LITTLE_ENDIAN_64_SIZE = 8;

	// ------------------------ WRITER --------------------------

	/**
	 * Write WebSocket frame length
	 * 
	 * @param length
	 *            the length
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeWsFrameLength(byte type, int length) throws IOException {
		writeRawByte(type);
		// Encode length.
		int b1 = length >>> 28 & 0x7F;
		int b2 = length >>> 14 & 0x7F;
		int b3 = length >>> 7 & 0x7F;
		int b4 = length & 0x7F;
		if (b1 == 0) {
			if (b2 == 0) {
				if (b3 == 0) {
					writeRawByte(b4);
				} else {
					writeRawByte(b3 | 0x80);
					writeRawByte(b4);
				}
			} else {
				writeRawByte(b2 | 0x80);
				writeRawByte(b3 | 0x80);
				writeRawByte(b4);
			}
		} else {
			writeRawByte(b1 | 0x80);
			writeRawByte(b2 | 0x80);
			writeRawByte(b3 | 0x80);
			writeRawByte(b4);
		}
	}

	/**
	 * Write a {@code double} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeDouble(final double value) throws IOException {
		writeRawLittleEndianLong(Double.doubleToRawLongBits(value));
	}

	/**
	 * Write a {@code float} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeFloat(final float value) throws IOException {
		writeRawLittleEndianInt(Float.floatToRawIntBits(value));
	}

	/**
	 * Write an {@code long} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeLong(final long value) throws IOException {
		writeRawVarLong(value);
	}

	/**
	 * Write an {@code int} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeInt(final int value) throws IOException {
		if (value >= 0) {
			writeRawVarInt(value);
		} else {
			// Must sign-extend.
			writeRawVarLong(value);
		}
	}

	/**
	 * Write a {@code FixedInt} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeUnsignedFixedInt(final int value) throws IOException {
		writeRawLittleEndianInt(value);
	}

	/**
	 * Write a {@code FixedLong} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeUnsignedFixedLong(final long value) throws IOException {
		writeRawLittleEndianLong(value);
	}

	/**
	 * Write a {@code boolean} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeBoolean(final boolean value) throws IOException {
		writeRawByte(value ? 1 : 0);
	}

	/**
	 * Write a {@code string} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeString(final String value) throws IOException {
		// Unfortunately there does not appear to be any way to tell Java to
		// encode UTF-8 directly into our buffer, so we have to let it create
		// its own byte array and then copy.
		final byte[] bytes = value.getBytes("UTF-8");
		writeRawVarInt(bytes.length);
		writeRawBytes(bytes);
	}

	/**
	 * Write a {@code bytes} sector to the buffer.
	 * 
	 * @param bytes
	 *            the bytes
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeBytes(final byte[] bytes) throws IOException {
		writeRawVarInt(bytes.length);
		writeRawBytes(bytes);
	}

	/**
	 * Write a {@code UnsignedInt} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeUnsignedInt(final int value) throws IOException {
		writeRawVarInt(value);
	}

	/**
	 * Write a {@code UnsignedLong} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeUnsignedLong(final long value) throws IOException {
		writeRawVarLong(value);
	}

	/**
	 * Write an {@code SignedFixedInt} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeSignedFixedInt(final int value) throws IOException {
		writeRawLittleEndianInt(value);
	}

	/**
	 * Write an {@code SignedFixedLong} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeSignedFixedLong(final long value) throws IOException {
		writeRawLittleEndianLong(value);
	}

	/**
	 * Write an {@code SignedInt} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeSignedInt(final int value) throws IOException {
		writeRawVarInt(encodeZigZagInt(value));
	}

	/**
	 * Write an {@code SignedLong} sector to the buffer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeSignedLong(final long value) throws IOException {
		writeRawVarLong(encodeZigZagLong(value));
	}

	// =================================================================

	/**
	 * Write a single byte.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeRawByte(final byte value) throws IOException {
		if (position == limit) {
			// ZippyBufferWriter ran out of space
			throw new IOException("ZippyBufferWriter ran out of space.");
		}
		buffer[position++] = value;
	}

	/**
	 * Write a single byte, represented by an integer value.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeRawByte(final int value) throws IOException {
		writeRawByte((byte) value);
	}

	/**
	 * Write an array of bytes.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeRawBytes(final byte[] value) throws IOException {
		writeRawBytes(value, 0, value.length);
	}

	/**
	 * Write part of an array of bytes.
	 * 
	 * @param value
	 *            the value
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeRawBytes(final byte[] value, int offset, int length)
			throws IOException {
		if (limit - position >= length) {
			// We have room in the current buffer.
			System.arraycopy(value, offset, buffer, position, length);
			position += length;
		} else {
			// ZippyBufferWriter ran out of space
			throw new IOException("ZippyBufferWriter ran out of space.");
		}
	}

	/**
	 * Encode and write a VarInt. {@code value} is treated as unsigned, so it
	 * won't be sign-extended if negative.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeRawVarInt(int value) throws IOException {
		while (true) {
			if ((value & ~0x7F) == 0) {
				writeRawByte(value);
				return;
			} else {
				writeRawByte((value & 0x7F) | 0x80);
				value >>>= 7;
			}
		}
	}

	/**
	 * Encode and write a VarLong.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeRawVarLong(long value) throws IOException {
		while (true) {
			if ((value & ~0x7FL) == 0) {
				writeRawByte((int) value);
				return;
			} else {
				writeRawByte(((int) value & 0x7F) | 0x80);
				value >>>= 7;
			}
		}
	}

	/**
	 * Write a little-endian 32-bit integer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeRawLittleEndianInt(final int value) throws IOException {
		writeRawByte((value) & 0xFF);
		writeRawByte((value >> 8) & 0xFF);
		writeRawByte((value >> 16) & 0xFF);
		writeRawByte((value >> 24) & 0xFF);
	}

	/**
	 * Write a little-endian 64-bit integer.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeRawLittleEndianLong(final long value) throws IOException {
		writeRawByte((int) (value) & 0xFF);
		writeRawByte((int) (value >> 8) & 0xFF);
		writeRawByte((int) (value >> 16) & 0xFF);
		writeRawByte((int) (value >> 24) & 0xFF);
		writeRawByte((int) (value >> 32) & 0xFF);
		writeRawByte((int) (value >> 40) & 0xFF);
		writeRawByte((int) (value >> 48) & 0xFF);
		writeRawByte((int) (value >> 56) & 0xFF);
	}

	/**
	 * Encode a ZigZag-encoded 32-bit value. ZigZag encodes signed integers into
	 * values that can be efficiently encoded with varint. (Otherwise, negative
	 * values must be sign-extended to 64 bits to be varint encoded, thus always
	 * taking 10 bytes on the wire.)
	 * 
	 * @param n
	 *            A signed 32-bit integer.
	 * @return An unsigned 32-bit integer, stored in a signed int because Java
	 *         has no explicit unsigned support.
	 */
	public static int encodeZigZagInt(final int n) {
		// Note: the right-shift must be arithmetic
		return (n << 1) ^ (n >> 31);
	}

	/**
	 * Encode a ZigZag-encoded 64-bit value. ZigZag encodes signed integers into
	 * values that can be efficiently encoded with varint. (Otherwise, negative
	 * values must be sign-extended to 64 bits to be varint encoded, thus always
	 * taking 10 bytes on the wire.)
	 * 
	 * @param n
	 *            A signed 64-bit integer.
	 * @return An unsigned 64-bit integer, stored in a signed int because Java
	 *         has no explicit unsigned support.
	 */
	public static long encodeZigZagLong(final long n) {
		// Note: the right-shift must be arithmetic
		return (n << 1) ^ (n >> 63);
	}

	/**
	 * Sets this buffer's position.
	 * 
	 * @param i
	 * @return
	 */
	public ZippyBuffer position(int i) {
		position = i;
		return this;
	}

	/**
	 * Returns this buffer's position.
	 * 
	 * @return
	 */
	public int position() {
		return position;
	}

	/**
	 * Returns this buffer's limit. Remember: the buffer's limit is final!
	 * 
	 * @return
	 */
	public int limit() {
		return limit;
	}

	/**
	 * Sets this buffer's mark at its position.
	 * 
	 * @return
	 */
	public ZippyBuffer mark() {
		mark = position;
		return this;
	}

	/**
	 * Resets this buffer's position to the previously-marked position. Invoking
	 * this method neither changes nor discards the mark's value.
	 * 
	 * @return
	 */
	public ZippyBuffer reset() {
		position = mark;
		return this;
	}

	/**
	 * Rewinds this buffer. The position and the mark are set to zero.
	 * 
	 * @return
	 */
	public ZippyBuffer rewind() {
		position = 0;
		mark = 0;
		return this;
	}
}
