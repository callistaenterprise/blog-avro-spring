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

/**
 * Generic serialization/deserialization exception.
 * 
 * @author Björn Beskow
 */
public class SerializationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public SerializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public SerializationException(String message) {
    super(message);
  }

  public SerializationException(Throwable cause) {
    super(cause);
  }

  public SerializationException() {
    super();
  }

  /* avoid the expensive and useless stack trace for serialization exceptions */
  @Override
  public Throwable fillInStackTrace() {
    return this;
  }

}
