package com.fiap.burger.application.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@ComponentScan(basePackages = "com.fiap.burger")
public class CucumberTestConfig { }