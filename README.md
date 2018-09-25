# blog-avro-spring
Apache Avro is becoming a popular choice for Java Object Serialization in Event
Driven Architectures using Apache Kafka, due to its compact binary payloads and 
stringent schema support. If combining Event Notification using Kafka with traditional 
Request-Response, it is convenient to use the same serialization mechanism for the 
domain objects, regardless of if they are part of events emitted over Kafka or 
requested through a REST API. Here's how to do that in a Spring-MVC REST environment.
