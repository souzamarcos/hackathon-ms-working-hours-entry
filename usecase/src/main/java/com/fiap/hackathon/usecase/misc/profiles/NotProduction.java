package com.fiap.hackathon.usecase.misc.profiles;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Profile("!prod")
public @interface NotProduction {
}
