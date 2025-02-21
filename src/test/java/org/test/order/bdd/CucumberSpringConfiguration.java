package org.test.order.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.test.order.OrderApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = OrderApplication.class)
public class CucumberSpringConfiguration {
}