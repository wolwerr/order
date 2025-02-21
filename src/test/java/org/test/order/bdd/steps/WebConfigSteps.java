package org.test.order.bdd.steps;


import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.test.order.infra.config.WebConfig;

import static org.mockito.Mockito.*;

public class WebConfigSteps {

    private WebConfig webConfig;
    private CorsRegistry corsRegistry;
    private CorsRegistration corsRegistration;

    @Before
    public void setUp() {
        webConfig = new WebConfig();
        corsRegistry = mock(CorsRegistry.class);
        corsRegistration = mock(CorsRegistration.class);

        when(corsRegistry.addMapping(anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins(anyString(), anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders(anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(anyBoolean())).thenReturn(corsRegistration);
    }

    @Given("que o WebConfig está configurado")
    public void que_o_webconfig_está_configurado() {
        setUp();
    }

    @When("o CORS mappings é adicionado")
    public void o_cors_mappings_é_adicionado() {
        webConfig.addCorsMappings(corsRegistry);
    }

    @Then("deve adicionar o mapping {string} com origins {string} e {string}")
    public void deve_adicionar_o_mapping_com_origins(String mapping, String origin1, String origin2) {
        verify(corsRegistry).addMapping(mapping);
        verify(corsRegistration).allowedOrigins(origin1, origin2);
    }

    @Then("deve permitir métodos {string}, {string}, {string}, {string}, {string}")
    public void deve_permitir_métodos(String method1, String method2, String method3, String method4, String method5) {
        verify(corsRegistration).allowedMethods(method1, method2, method3, method4, method5);
    }

    @Then("deve permitir headers {string}")
    public void deve_permitir_headers(String headers) {
        verify(corsRegistration).allowedHeaders(headers);
    }

    @Then("deve permitir credenciais")
    public void deve_permitir_credenciais() {
        verify(corsRegistration).allowCredentials(true);
    }

    @Then("deve adicionar o mapping {string} com origins {string}")
    public void deveAdicionarOMappingComOrigins(String arg0, String arg1) {
    }
}
