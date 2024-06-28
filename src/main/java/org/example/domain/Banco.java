package org.example.domain;

import org.example.exceptions.SaldoNegativoException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {
    List<Cuenta> cuentas;
    private String nombre;

    public Banco() {
        this.cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void add(Cuenta cuenta) {
        cuentas.add(cuenta);
        cuenta.setBanco(this);
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto){
        if(monto.compareTo(BigDecimal.ZERO) < 0){
            throw new SaldoNegativoException("Saldo negativo");
        }
        origen.debito(monto);
        destino.credito(monto);
    }
}
