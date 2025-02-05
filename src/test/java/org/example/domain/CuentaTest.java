package org.example.domain;

import org.example.exceptions.SaldoInsuficienteException;
import org.example.exceptions.SaldoNegativoException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {
    Cuenta cuenta;
    private TestInfo info;
    private TestReporter testReporter;

    @BeforeEach
    void initTest(TestInfo info, TestReporter testReporter) {
        this.info = info;
        this.testReporter = testReporter;
        testReporter.publishEntry("iniciando el metodo");
        this.cuenta = new Cuenta("Andres", new BigDecimal("2500"));
        testReporter.publishEntry("Ejecutando: " + info.getDisplayName() + " " +
                info.getTestMethod().orElse(null).getName() + " con las etiquetas " +
                info.getTags());
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

    @Tag("cuenta")
    @Test
    @DisplayName("Probando el nombre de la cuenta")
    void testNombreCuenta() {
        if (info.getTags().contains("cuenta")) testReporter.publishEntry("hace algo en el metodo " + info.getTestMethod());

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

    @Tag("param")
    @Nested
    class PruebasParametrizadas{
        @ParameterizedTest(name = "numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "400", "500"})
        @DisplayName("Debitando de la cuenta parametrizado")
        void testDebitoCuentaParametrizado(String monto) {
            Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede nulo");
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvSource({"100,Daniel,Daniel", "200,Juan,Juan", "300,Pedro,Pedro"})
        @DisplayName("Debitando de la cuenta parametrizado csv source")
        void testDebitoCuentaParametrizadoCsvSource(String monto, String esperado, String actual) {
            Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
            cuenta.setPersona(actual);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede nulo");
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado,actual);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }


        @ParameterizedTest(name = "numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        @DisplayName("Debitando de la cuenta parametrizado csv")
        void testDebitoCuentaParametrizadoCsvFile(String monto) {
            Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede nulo");
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        @DisplayName("Debitando de la cuenta parametrizado csv2")
        void testDebitoCuentaParametrizadoCsvFile2(String monto, String esperado, String actual) {
            Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
            cuenta.setPersona(actual);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede nulo");
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado,actual);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @MethodSource("montoList")
        @DisplayName("Debitando de la cuenta parametrizado csv")
        void testDebitoCuentaParametrizadoMethodSource(String monto) {
            Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000"));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede nulo");
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        static List<String> montoList(){
            return Arrays.asList("100", "200", "300", "400", "500");
        }
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

    @Tag("properties")
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

    @Nested
    @Tag("timeout")
    class EjemploTimeOutTest {
        @Test
        @Timeout(6)
        void pruebaTimeOut() throws InterruptedException{
            TimeUnit.SECONDS.sleep(6) ;
        }

        @Test
        @Timeout(value = 6000, unit = TimeUnit.MILLISECONDS)
        void pruebaTimeOut2() throws InterruptedException{
            TimeUnit.SECONDS.sleep(6) ;
        }

        @Test
        void pruebaTimeOut3() throws InterruptedException {
            assertTimeout(Duration.ofSeconds(5000), () -> {
                    TimeUnit.SECONDS.sleep(5) ;
            });

        }
    }
}