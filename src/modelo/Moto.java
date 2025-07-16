/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import gestor.IVendible;

public class Moto extends Vehiculo implements IVendible {
    private int cilindrada;
    private boolean tieneMaletas;

    public Moto(String marca, String modelo, int año, double precio, 
                EVehiculo estado, ETipoCombustible combustible,
                int cilindrada, boolean tieneMaletas) {
        super(marca, modelo, año, precio, estado, combustible);
        this.cilindrada = cilindrada;
        this.tieneMaletas = tieneMaletas;
    }

    public Moto(String marca, String modelo, int año, double precio, 
                EVehiculo estado, int cilindrada) {
        super(marca, modelo, año, precio, estado);
        this.cilindrada = cilindrada;
        this.tieneMaletas = false;
    }

    public Moto(String marca, String modelo, int año) {
        super(marca, modelo, año);
        this.cilindrada = 150;
        this.tieneMaletas = false;
    }

    @Override
    public double calcularMantenimiento() {
        return precio * 0.03 + (cilindrada * 0.1);
    }

    @Override
    public String obtenerTipoVehiculo() {
        return "Moto";
    }

    @Override
    public void acelerar() {
        System.out.println("La moto acelera rápidamente");
    }

    @Override
    public double calcularPrecioVenta() {
        double depreciacion = (2024 - año) * 0.12;
        return precio * (1 - Math.min(depreciacion, 0.85));
    }

    @Override
    public boolean estaDisponibleParaVenta() {
        return estado == EVehiculo.NUEVO || estado == EVehiculo.USADO;
    }

    // Getters y Setters
    public int getCilindrada() { return cilindrada; }
    public void setCilindrada(int cilindrada) { this.cilindrada = cilindrada; }
    
    public boolean isTieneMaletas() { return tieneMaletas; }
    public void setTieneMaletas(boolean tieneMaletas) { this.tieneMaletas = tieneMaletas; }
}
