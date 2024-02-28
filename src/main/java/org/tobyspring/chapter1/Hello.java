package org.tobyspring.chapter1;

import org.springframework.context.annotation.Bean;

// 메소드 주입 기능을 활용한 빈 전달 방식
public class Hello {

    private Person person;

    public void setPerson(Person person) {
        this.person = person;
    }

    @Bean
    public Hi hi() {
        Hi hi = new Hi();
        hi.setPerson(this.person);
        return hi;
    }

    @Bean
    public Hi hi2() {
        Hi hi = new Hi();
        hi.setPerson(this.person);
        return hi;
    }

    @Bean
    private Person person() {
        return new Person();
    }

}
