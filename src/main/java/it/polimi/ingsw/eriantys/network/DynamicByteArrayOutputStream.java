package it.polimi.ingsw.eriantys.network;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class DynamicByteArrayOutputStream extends ByteArrayOutputStream {
  public DynamicByteArrayOutputStream() {
    super();
  }

  public DynamicByteArrayOutputStream(int size) {
    super(size);
  }

  /**
   * Gets the underlying byte array of this output stream.
   * Not all data might be valid so accesses must be checked against {@link #size()}.
   *
   * @return The underlying byte array
   */
  public byte[] getByteArray() {
    return buf;
  }

  /**
   * Gets a view over the underlying byte array of this output stream.
   *
   * @return A ByteBuffer over the underlying byte array, with position set to 0 and limit set to {@link #size()}.
   */
  public ByteBuffer getByteBuffer() {
    return ByteBuffer.wrap(buf, 0, size());
  }
}
