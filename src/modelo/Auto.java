/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import gestor.IVendible;

public class Auto extends Vehiculo implements IVendible {
    private int numeroPuertas;
    private boolean esDeportivo;

    public Auto(String marca, String modelo, int año, double precio, 
                EVehiculo estado, ETipoCombustible combustible,
                int numeroPuertas, boolean esDeportivo) {
        super(marca, modelo, año, precio, estado, combustible);
        this.numeroPuertas = numeroPuertas;
        this.esDeportivo = esDeportivo;
    }

    public Auto(String marca, String modelo, int año, double precio, 
                EVehiculo estado, int numeroPuertas) {
        super(marca, modelo, año, precio, estado);
        this.numeroPuertas = numeroPuertas;
        this.esDeportivo = false;
    }

    public Auto(String marca, String modelo, int año) {
        super(marca, modelo, año);
        this.numeroPuertas = 4;
        this.esDeportivo = false;
    }

    @Override
    public double calcularMantenimiento() {
        double base = precio * 0.05;
        return esDeportivo ? base * 1.5 : base;
    }

    @Override
    public String obtenerTipoVehiculo() {
        return "Auto";
    }

    @Override
    public void acelerar() {
        System.out.println("El auto acelera suavemente");
    }

    @Override
    public double calcularPrecioVenta() {
        double depreciacion = (2024 - año) * 0.1;
        return precio * (1 - Math.min(depreciacion, 0.8));
    }

    @Override
    public boolean estaDisponibleParaVenta() {
        return estado == EVehiculo.NUEVO || estado == EVehiculo.USADO;
    }

    // Getters y Setters
    public int getNumeroPuertas() { return numeroPuertas; }
    public void setNumeroPuertas(int numeroPuertas) { this.numeroPuertas = numeroPuertas; }
    
    public boolean isEsDeportivo() { return esDeportivo; }
    public void setEsDeportivo(boolean esDeportivo) { this.esDeportivo = esDeportivo; }
}