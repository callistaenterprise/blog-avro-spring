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

package se.callista.blog.avro_spring.car.avro.serde;

import se.callista.blog.avro_spring.car.avro.Car;
import se.callista.blog.avro_spring.serde.AvroDeserializer;
import se.callista.blog.avro_spring.serde.AvroSerializer;
import se.callista.blog.avro_spring.serde.SerializationException;

/**
 * Avro serialization/deserialization for Cars.
 * 
 * @author Björn Beskow
 */
public class CarSerDe {

  private AvroSerializer<Car> serializer;
  private AvroDeserializer<Car> deserializer;

  private final boolean useBinaryEncoding;
  
  public CarSerDe(boolean useBinaryEncoding) {
    this.useBinaryEncoding = useBinaryEncoding;
    serializer = new AvroSerializer<>(useBinaryEncoding);
    deserializer = new AvroDeserializer<>(useBinaryEncoding);
  }

  public boolean isUseBinaryEncoding() {
    return useBinaryEncoding;
  }

  public Car deserialize(byte[] bytes) throws SerializationException {
    return deserializer.deserialize(Car.class, bytes);
  }

  public byte[] serialize(Car car) throws SerializationException {
    return serializer.serialize(car);
  }
}
