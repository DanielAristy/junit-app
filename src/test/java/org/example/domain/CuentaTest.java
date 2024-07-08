package org.example.domain;

import org.example.exceptions.SaldoInsuficienteException;
import org.example.exceptions.SaldoNegativoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setPersona("Daniel");
        assertEquals("Daniel", cuenta.getPersona());
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("0"));
        cuenta.setSaldo(new BigDecimal("1000.0"));
        assertEquals(1000.0, cuenta.getSaldo().doubleValue());
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
        assertEquals(900.0, cuenta.getSaldo().doubleValue());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        cuenta.credito(new BigDecimal("100.0"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100.0, cuenta.getSaldo().doubleValue());
    }

    @Test
    void testDineroInsuficienteException() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        Exception exception = assertThrows(SaldoInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal("2000.0"));
        });
        assertEquals("Saldo insuficiente", exception.getMessage());
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta = new Cuenta("Andres Gomez", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Jesus Ramirez", new BigDecimal("3500"));
        Banco banco = new Banco();
        banco.transferir(cuenta, cuenta2, new BigDecimal(500));
        assertEquals("2000", cuenta.getSaldo().toPlainString());
        assertEquals("4000", cuenta2.getSaldo().toPlainString());
    }

    @Test
    void testTranferirSaldoNegativo() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("3500"));
        Banco banco = new Banco();
        Exception exception = assertThrows(SaldoNegativoException.class, () -> {
            banco.transferir(cuenta, cuenta2, new BigDecimal(-500));
        });
        assertEquals("Saldo negativo", exception.getMessage());
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Jorge", new BigDecimal("3500"));
        Banco banco = new Banco();
        banco.setNombre("Banco de la republica");
        banco.add(cuenta);
        banco.add(cuenta2);
        assertEquals(2, banco.getCuentas().size());
        assertEquals("Banco de la republica", cuenta.getBanco().getNombre());
    }

    @Test
    void testRelacionPersonBanco() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Jorge", new BigDecimal("3500"));
        Banco banco = new Banco();
        banco.setNombre("Banco de la republica");
        banco.add(cuenta);
        banco.add(cuenta2);

        assertAll(
                () -> {
                    assertEquals("Andres", banco.getCuentas().stream()
                            .filter(c -> "Andres".equals(c.getPersona()))
                            .findFirst().get().getPersona()
                    );
                },
                () -> {
                    assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("Andres")));
                }
        );
    }
}