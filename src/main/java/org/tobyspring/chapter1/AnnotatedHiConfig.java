package org.tobyspring.chapter1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnnotatedHiConfig {

    @Bean
    public Hi hi() {
        Person person = new Person();
        person.setName("Wonil");
        return new Hi(person);
    }

}
