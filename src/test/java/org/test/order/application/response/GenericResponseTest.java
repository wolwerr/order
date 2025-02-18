package org.test.order.application.response;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.generic.output.OutputStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenericResponseTest {

    // Returns HTTP 200 OK with body when output status code is 200
    @Test
    public void test_returns_ok_status_with_body_when_output_status_is_200() {
        // Arrange
        OutputInterface outputInterface = mock(OutputInterface.class);
        OutputStatus outputStatus = new OutputStatus(200, "OK", "Success");
        String responseBody = "Test Response";
        when(outputInterface.getOutputStatus()).thenReturn(outputStatus);
        when(outputInterface.getBody()).thenReturn(responseBody);

        GenericResponse genericResponse = new GenericResponse();

        // Act
        ResponseEntity<Object> response = genericResponse.response(outputInterface);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

    @Test
    public void test_returns_created_status_with_body_when_output_status_is_201() {
        // Arrange
        OutputInterface outputInterface = mock(OutputInterface.class);
        OutputStatus outputStatus = new OutputStatus(201, "CREATED", "Success");
        String responseBody = "Test Response";
        when(outputInterface.getOutputStatus()).thenReturn(outputStatus);
        when(outputInterface.getBody()).thenReturn(responseBody);

        GenericResponse genericResponse = new GenericResponse();

        // Act
        ResponseEntity<Object> response = genericResponse.response(outputInterface);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

    @Test
    public void test_returns_not_found__status_with_body_when_output_status_is_404() {
        // Arrange
        OutputInterface outputInterface = mock(OutputInterface.class);
        OutputStatus outputStatus = new OutputStatus(404, "NOT_FOUND", "Not found");
        String responseBody = "Test Response";
        when(outputInterface.getOutputStatus()).thenReturn(outputStatus);
        when(outputInterface.getBody()).thenReturn(responseBody);

        GenericResponse genericResponse = new GenericResponse();

        // Act
        ResponseEntity<Object> response = genericResponse.response(outputInterface);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

    @Test
    public void test_returns_no_content_status_with_body_when_output_status_is_204() {
        // Arrange
        OutputInterface outputInterface = mock(OutputInterface.class);
        OutputStatus outputStatus = new OutputStatus(204, "NO_CONTENT", "No content");
        String responseBody = "No Content Response";
        when(outputInterface.getOutputStatus()).thenReturn(outputStatus);
        when(outputInterface.getBody()).thenReturn(responseBody);

        GenericResponse genericResponse = new GenericResponse();

        // Act
        ResponseEntity<Object> response = genericResponse.response(outputInterface);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

    @Test
    public void test_returns_unprocessable_entity_status_with_body_when_output_status_is_422() {
        // Arrange
        OutputInterface outputInterface = mock(OutputInterface.class);
        OutputStatus outputStatus = new OutputStatus(422, "UNPROCESSABLE_ENTITY", "Unprocessable entity");
        String responseBody = "Unprocessable Entity Response";
        when(outputInterface.getOutputStatus()).thenReturn(outputStatus);
        when(outputInterface.getBody()).thenReturn(responseBody);

        GenericResponse genericResponse = new GenericResponse();

        // Act
        ResponseEntity<Object> response = genericResponse.response(outputInterface);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

    @Test
    public void test_returns_bad_request_status_with_body_when_output_status_is_400() {
        // Arrange
        OutputInterface outputInterface = mock(OutputInterface.class);
        OutputStatus outputStatus = new OutputStatus(400, "BAD_REQUEST", "Bad request");
        String responseBody = "Bad Request Response";
        when(outputInterface.getOutputStatus()).thenReturn(outputStatus);
        when(outputInterface.getBody()).thenReturn(responseBody);

        GenericResponse genericResponse = new GenericResponse();

        // Act
        ResponseEntity<Object> response = genericResponse.response(outputInterface);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

    // Returns HTTP 500 INTERNAL_SERVER_ERROR for any unhandled status code
    @Test
    public void test_returns_internal_server_error_for_unhandled_status_code() {
        // Arrange
        OutputInterface outputInterface = mock(OutputInterface.class);
        OutputStatus outputStatus = new OutputStatus(999, "UNKNOWN", "Unknown status");
        String responseBody = "Error Response";
        when(outputInterface.getOutputStatus()).thenReturn(outputStatus);
        when(outputInterface.getBody()).thenReturn(responseBody);

        GenericResponse genericResponse = new GenericResponse();

        // Act
        ResponseEntity<Object> response = genericResponse.response(outputInterface);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

}