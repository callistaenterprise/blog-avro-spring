/*
 * Copyright 2018 the original author or authors.
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

package se.callista.blog.avro_spring.serde;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Avro serialization.
 * 
 * @author Björn Beskow
 */
public class AvroSerializer<T extends SpecificRecordBase> implements Serializer<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AvroSerializer.class);

  private final boolean useBinaryEncoding;
  
  public AvroSerializer(boolean useBinaryEncoding) {
    this.useBinaryEncoding = useBinaryEncoding;
  }

  public boolean isUseBinaryEncoding() {
    return useBinaryEncoding;
  }

  @Override
  public byte[] serialize(T data) throws SerializationException {
    try {
      byte[] result = null;

      if (data != null) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("data={}:{}", data.getClass().getName(), data);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Encoder encoder = useBinaryEncoding ?
            EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null) :
            EncoderFactory.get().jsonEncoder(data.getSchema(), byteArrayOutputStream);;

        DatumWriter<T> datumWriter = new SpecificDatumWriter<>(data.getSchema());
        datumWriter.write(data, encoder);

        encoder.flush();
        byteArrayOutputStream.close();

        result = byteArrayOutputStream.toByteArray();
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("serialized data='{}' ({})", DatatypeConverter.printHexBinary(result), new String(result));
        }
      }
      return result;
    } catch (IOException e) {
      throw new SerializationException("Can't serialize data='" + data + "'", e);
    }
  }
}
