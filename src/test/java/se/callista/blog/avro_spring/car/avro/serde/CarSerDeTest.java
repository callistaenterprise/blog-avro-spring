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

import java.io.IOException;

import org.junit.Test;

import se.callista.blog.avro_spring.car.avro.Car;

/**
 * Tests for CarSerDe
 * 
 * @author Björn Beskow
 */
public class CarSerDeTest {

  @Test
  public void testCarSerDe() throws IOException {
    Car car1 = new Car();
    car1.setVIN("aVin");
    car1.setPlateNumber("aPlateNumer");

    CarSerDe serde = new CarSerDe();
    byte[] bytes = serde.serialize(car1);
    Car carCopy = serde.deserialize(bytes);

    assert car1 != carCopy;
    assert car1.equals(carCopy);
  }

}
