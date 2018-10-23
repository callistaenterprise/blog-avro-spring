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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Avro deserialization.
 * 
 * @author Björn Beskow
 */
public class AvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AvroDeserializer.class);

  private final boolean useBinaryEncoding;
  
  public AvroDeserializer(boolean useBinaryEncoding) {
    this.useBinaryEncoding = useBinaryEncoding;
  }

  public boolean isUseBinaryEncoding() {
    return useBinaryEncoding;
  }

  @Override
  public T deserialize(Class<? extends T> clazz, byte[] data) throws SerializationException {
    try {
      T result = null;
      if (data != null) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("data='{}' ({})", DatatypeConverter.printHexBinary(data), new String(data));
        }
        Class<? extends SpecificRecordBase> specificRecordClass =
            (Class<? extends SpecificRecordBase>) clazz;
        Schema schema = specificRecordClass.newInstance().getSchema();
        DatumReader<T> datumReader =
            new SpecificDatumReader<>(schema);
        Decoder decoder = useBinaryEncoding ? DecoderFactory.get().binaryDecoder(data, null) : DecoderFactory.get().jsonDecoder(schema, new ByteArrayInputStream(data));;

        result = datumReader.read(null, decoder);
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("deserialized data={}:{}", clazz.getName(), result);
        }
      }
      return result;
    } catch (InstantiationException | IllegalAccessException | IOException e) {
      throw new SerializationException("Can't deserialize data '" + Arrays.toString(data) + "'", e);
    }
  }
}
