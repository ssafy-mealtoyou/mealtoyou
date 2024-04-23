package com.mealtoyou.sample.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
public class Person {
    @Id
    private Long id;
    private String name;
}
