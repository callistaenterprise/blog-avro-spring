/**
 * Tests for CarSerDe
 * 
 * @author Björn Beskow
 */
package se.callista.blog.avro_spring.car.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import se.callista.blog.avro_spring.car.avro.Car;
import se.callista.blog.avro_spring.car.avro.serde.CarSerDe;
import se.callista.blog.avro_spring.car.persist.CarRepository;

/**
 * Tests for CarClient
 * 
 * @author Björn Beskow
 */
@RunWith(SpringRunner.class)
@RestClientTest(CarClient.class)
@ComponentScan("se.callista.blog.avro_spring.car")
public class CarClientTest {

  private static final String VIN = "123456789";
  private static final String PLATE_NUMBER = "ABC 123";

  private CarSerDe carSerDe = new CarSerDe(false);

  private Car car;
  private byte[] serializedCar;

  @Autowired
  private CarClient client;

  @Autowired
  private MockRestServiceServer server;

  @MockBean
  private CarRepository carRepository;

  @Before
  public void setUp() throws Exception {
    car = new Car(VIN, PLATE_NUMBER);
    serializedCar = carSerDe.serialize(car);
  }

  @Test
  public void testGetCar() throws Exception {
    given(carRepository.getCar(VIN)).willReturn(car);
    this.server.expect(requestTo("/car/" + VIN)).andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(serializedCar, new MediaType("application", "avro+json")));

    Car actualCar = this.client.getCar(VIN);

    assertThat(actualCar).isEqualTo(car);
  }

  @Test
  public void testUpdateCar() throws Exception {
    given(carRepository.updateCar(any(Car.class))).willReturn(car);
    this.server.expect(requestTo("/car/" + VIN)).andExpect(method(HttpMethod.PUT))
        .andRespond(withSuccess(serializedCar, new MediaType("application", "avro+json")));

    Car actualCar = this.client.updateCar(VIN, car);

    assertThat(actualCar).isEqualTo(car);
  }
}
