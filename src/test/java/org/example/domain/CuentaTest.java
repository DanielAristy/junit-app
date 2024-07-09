package org.example.domain;

import org.example.exceptions.SaldoInsuficienteException;
import org.example.exceptions.SaldoNegativoException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    void initTest() {
        System.out.println("iniciando el metodo");
        this.cuenta = new Cuenta("Andres", new BigDecimal("2500"));
    }

    @AfterEach
    void finishTest() {
        System.out.println("finalizando el metodo de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finalizando el test");
    }

    @Test
    @DisplayName("Probando el nombre de la cuenta")
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setPersona("Daniel");
        assertEquals("Daniel", cuenta.getPersona(), "El nombre de la cuenta no es el que se esperaba");
    }

    @RepeatedTest(value = 2, name = "Repeticion numero {currentRepetition} de {totalRepetitions}")
    @DisplayName("Probando el nombre de la cuenta repetido")
    void testNombreCuentaRepetido(RepetitionInfo info) {
        if (info.getCurrentRepetition() == 2){
            System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
        }
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


    @Nested
    class PropiedadesSistemaTest {

        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((key, value) -> System.out.println(key + ":" + value));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "17.0.11")
        void testJavaVersion() {
        }

        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((key, value) -> System.out.println(key + ":" + value));
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-21.0.2.*")
        void testJavaHome() {
        }

        @Test
        @DisplayName("Probando el nombre de la cuenta en el ambiente Dev")
        void testNombreCuentaDev() {
            Boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(esDev);
            Cuenta cuenta = new Cuenta();
            cuenta.setPersona("Daniel");
            assertEquals("Daniel", cuenta.getPersona(), "El nombre de la cuenta no es el que se esperaba");
        }

        @Test
        @DisplayName("Probando el nombre de la cuenta en el ambiente Dev 2")
        void testNombreCuentaDev2() {
            Boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumingThat(esDev, () -> {
                Cuenta cuenta = new Cuenta();
                cuenta.setPersona("Daniel");
                assertEquals("Daniel", cuenta.getPersona(), "El nombre de la cuenta no es el que se esperaba");
            });

        }
    }
}