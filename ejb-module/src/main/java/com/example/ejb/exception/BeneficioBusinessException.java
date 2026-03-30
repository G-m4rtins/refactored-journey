package com.example.ejb.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BeneficioBusinessException extends RuntimeException {

    public BeneficioBusinessException(String message) {
        super(message);
    }
}
