package org.example.domain;

import org.example.exceptions.SaldoInsuficienteException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta(){
        Cuenta cuenta = new Cuenta();
        cuenta.setPersona("Daniel");
        assertEquals("Daniel",cuenta.getPersona());
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("0"));
        cuenta.setSaldo(new BigDecimal("1000.0"));
        assertEquals(1000.0,cuenta.getSaldo().doubleValue());
    }

    @Test
    void testRerefenciaCuenta() {
        Cuenta cuenta = new Cuenta("Andres Gomez", new BigDecimal("0"));
        Cuenta cuenta2 = new Cuenta("Andres Gomez", new BigDecimal("0"));
        assertEquals(cuenta, cuenta2);
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        cuenta.debito(new BigDecimal("100.0"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900.0,cuenta.getSaldo().doubleValue());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        cuenta.credito(new BigDecimal("100.0"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100.0,cuenta.getSaldo().doubleValue());
    }

    @Test
    void testDineroInsuficienteException() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        Exception exception = assertThrows(SaldoInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal("2000.0"));
        });
        assertEquals("Saldo insuficiente", exception.getMessage());
    }
}