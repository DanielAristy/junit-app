package org.example.exceptions;

public class SaldoNegativoException extends RuntimeException {
    public SaldoNegativoException(String message) {
        super(message);
    }
}
