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

package se.callista.blog.avro_spring.car.conf;

import java.util.List;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import se.callista.blog.avro_spring.serde.spring.AvroBinaryHttpMessageConverter;
import se.callista.blog.avro_spring.serde.spring.AvroJsonHttpMessageConverter;

/**
 * Configuration required to use the Avro{Binary,Json}HttpMessageConverter.
 * 
 * @author Björn Beskow
 */
@Configuration
public class ConverterConfig extends WebMvcConfigurerAdapter {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    super.configureMessageConverters(converters);
    converters.add(new AvroBinaryHttpMessageConverter<SpecificRecordBase>());
    converters.add(new AvroJsonHttpMessageConverter<SpecificRecordBase>());
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    // We need to add the AvroJsonHttpMessageConverter before any generic JSON converter,
    // since the pattern a generic JSON converter also may match. 
    RestTemplate restTemplate = builder.build();
    List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
    messageConverters.add(0, new AvroJsonHttpMessageConverter<SpecificRecordBase>());
    messageConverters.add(0, new AvroBinaryHttpMessageConverter<SpecificRecordBase>());
    restTemplate.setMessageConverters(messageConverters);
    return restTemplate;
  }

}
