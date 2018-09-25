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

package se.callista.blog.avro_spring.car.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import se.callista.blog.avro_spring.car.avro.Car;
import se.callista.blog.avro_spring.car.avro.serde.CarSerDe;
import se.callista.blog.avro_spring.car.persist.CarRepository;

/**
 * Tests for CarController
 * 
 * @author Björn Beskow
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
@AutoConfigureWebClient
public class CarControllerTest {

  private static final String VIN = "123456789";
  private static final String PLATE_NUMBER = "ABC 123";

  private CarSerDe carSerDe = new CarSerDe();

  private Car car;
  private byte[] serializedCar;

  @Autowired
  private MockMvc mvc;

  @MockBean
  private CarRepository carRepository;

  @Before
  public void setUp() throws Exception {
    car = new Car(VIN, PLATE_NUMBER);
    serializedCar = carSerDe.serialize(car);

    given(carRepository.getCar(VIN)).willReturn(car);
    given(carRepository.updateCar(any(Car.class))).willReturn(car);
  }

  @Test
  public void testGetCar() throws Exception {

    mvc.perform(get("/car/" + VIN).accept("application/avro")).andExpect(status().isOk())
        .andExpect(content().contentType("application/avro"))
        .andExpect(content().bytes(serializedCar));
  }

  @Test
  public void testUpdateCar() throws Exception {

    mvc.perform(put("/car/" + VIN).accept("application/avro").contentType("application/avro")
        .content(serializedCar)).andExpect(status().isOk())
        .andExpect(content().contentType("application/avro"))
        .andExpect(content().bytes(serializedCar));
  }
}
