package org.test.order.application.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.test.order.domain.generic.output.OutputInterface;

public class GenericResponse {
    public ResponseEntity<Object> response(OutputInterface outputInterface) {
        if (outputInterface.getOutputStatus().getCode() == 200) {
            return ResponseEntity.status(HttpStatus.OK).body(outputInterface.getBody());
        }

        if (outputInterface.getOutputStatus().getCode() == 201) {
            return ResponseEntity.status(HttpStatus.CREATED).body(outputInterface.getBody());
        }

        if (outputInterface.getOutputStatus().getCode() == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(outputInterface.getBody());
        }

        if (outputInterface.getOutputStatus().getCode() == 204) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(outputInterface.getBody());
        }

        if (outputInterface.getOutputStatus().getCode() == 422) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(outputInterface.getBody());
        }

        if (outputInterface.getOutputStatus().getCode() == 400) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(outputInterface.getBody());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(outputInterface.getBody());
    }
}
