package org.example.domain;

import org.example.exceptions.SaldoInsuficienteException;
import org.example.exceptions.SaldoNegativoException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    void initTest(){
        System.out.println("iniciando el metodo");
        this.cuenta = new Cuenta("Andres", new BigDecimal("2500"));;
    }

    @AfterEach
    void finishTest(){
        System.out.println("finalizando el metodo de prueba");
    }

    @Test
    @DisplayName("Probando el nombre de la cuenta")
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setPersona("Daniel");
        assertEquals("Daniel", cuenta.getPersona(), "El nombre de la cuenta no es el que se esperaba");
    }

    @Test
    @DisplayName("Probando el saldo de la cuenta")
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("0"));
        cuenta.setSaldo(new BigDecimal("1000.0"));
        assertEquals(1000.0, cuenta.getSaldo().doubleValue());
    }

    @Test
    @DisplayName("Probando la referencia del objeto cuenta")
    void testRerefenciaCuenta() {
        Cuenta cuenta = new Cuenta("Andres Gomez", new BigDecimal("0"));
        Cuenta cuenta2 = new Cuenta("Andres Gomez", new BigDecimal("0"));
        assertEquals(cuenta, cuenta2);
    }

    @Test
    @DisplayName("Debitando de la cuenta")
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        cuenta.debito(new BigDecimal("100.0"));
        assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede nulo");
        assertEquals(900.0, cuenta.getSaldo().doubleValue(), "El saldo debe ser 900 despues del debito");
    }

    @Test
    @DisplayName("Realizando un credito en la cuenta")
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        cuenta.credito(new BigDecimal("100.0"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100.0, cuenta.getSaldo().doubleValue());
    }

    @Test
    @DisplayName("Validando excepcion, cuando la cuenta no tiene la cantidad a debitar")
    void testDineroInsuficienteException() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        Exception exception = assertThrows(SaldoInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal("2000.0"));
        });
        assertEquals("Saldo insuficiente", exception.getMessage());
    }

    @Test
    @DisplayName("Probando la transferencia entre cuentas")
    void testTransferirDineroCuentas() {
        Cuenta cuenta2 = new Cuenta("Jesus Ramirez", new BigDecimal("3500"));
        Banco banco = new Banco();
        banco.transferir(cuenta, cuenta2, new BigDecimal(500));
        assertEquals("2000", cuenta.getSaldo().toPlainString());
        assertEquals("4000", cuenta2.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Probando una transferencia negativa entre cuentas")
    void testTranferirSaldoNegativo() {
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("3500"));
        Banco banco = new Banco();
        Exception exception = assertThrows(SaldoNegativoException.class, () -> {
            banco.transferir(cuenta, cuenta2, new BigDecimal(-500));
        });
        assertEquals("Saldo negativo", exception.getMessage());
    }

    @Test
    @DisplayName("Probando relacion entre banco y cuentas")
    @Disabled
    void testRelacionBancoCuentas() {
        Cuenta cuenta2 = new Cuenta("Jorge", new BigDecimal("3500"));
        Banco banco = new Banco();
        banco.setNombre("Banco de la republica");
        banco.add(cuenta);
        banco.add(cuenta2);
        assertEquals(2, banco.getCuentas().size());
        assertEquals("Banco de la republica", cuenta.getBanco().getNombre());
    }

    @Test
    @DisplayName("Probando relacion entre las cuentas y banco")
    void testRelacionPersonBanco() {
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