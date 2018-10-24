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

package se.callista.blog.avro_spring.car.client;

import java.nio.charset.Charset;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import se.callista.blog.avro_spring.car.avro.Car;

/**
 * Rest Client example, using Avro Serializer/Deserializer.
 * 
 * @author Björn Beskow
 */
@Component
public class CarClient {

  private static final MediaType APPLICATION_AVRO_JSON =
      new MediaType("application", "avro+json", Charset.forName("UTF-8"));

  @Autowired
  private RestTemplate restTemplate;

  public Car getCar(String VIN) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(APPLICATION_AVRO_JSON));
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<Car> result =
        restTemplate.exchange("/car/" + VIN, HttpMethod.GET, entity, Car.class);
    return result.getBody();
  }

  public Car updateCar(String VIN, Car car) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(APPLICATION_AVRO_JSON));
    headers.setContentType(APPLICATION_AVRO_JSON);
    HttpEntity<Car> entity = new HttpEntity<>(car, headers);

    ResponseEntity<Car> result =
        restTemplate.exchange("/car/" + VIN, HttpMethod.PUT, entity, Car.class);
    return result.getBody();
  }

}
