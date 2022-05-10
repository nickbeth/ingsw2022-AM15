package it.polimi.ingsw.eriantys.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class SerializationHelper {
  public static final int INIT_OBJECT_BUFFER_SIZE = 512;

  public static ByteBuffer serialize(Message message) throws IOException {
    ByteBuffer bytes;
    try (DynamicByteArrayOutputStream dbaos = new DynamicByteArrayOutputStream(INIT_OBJECT_BUFFER_SIZE);
         ObjectOutputStream oos = new ObjectOutputStream(dbaos)) {
      oos.writeObject(message);
      bytes = dbaos.getByteBuffer();
    }
    return bytes;
  }

  public static Message deserialize(ByteBuffer buffer) throws IOException {
    Message message;
    try (ByteBufferInputStream bais = new ByteBufferInputStream(buffer);
         ObjectInputStream ois = new ObjectInputStream(bais)) {
      message = (Message) ois.readObject();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return message;
  }
}
