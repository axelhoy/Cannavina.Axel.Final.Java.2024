/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import gestor.IVendible;
import modelo.Vehiculo;

public class Camion extends Vehiculo implements IVendible {
    private double capacidadCarga;
    private int numeroEjes;

    public Camion(String marca, String modelo, int año, double precio, 
                  EVehiculo estado, ETipoCombustible combustible,
                  double capacidadCarga, int numeroEjes) {
        super(marca, modelo, año, precio, estado, combustible);
        this.capacidadCarga = capacidadCarga;
        this.numeroEjes = numeroEjes;
    }

    public Camion(String marca, String modelo, int año, double precio, 
                  EVehiculo estado, double capacidadCarga) {
        super(marca, modelo, año, precio, estado);
        this.capacidadCarga = capacidadCarga;
        this.numeroEjes = 2;
    }

    public Camion(String marca, String modelo, int año) {
        super(marca, modelo, año);
        this.capacidadCarga = 1000.0;
        this.numeroEjes = 2;
    }

    @Override
    public double calcularMantenimiento() {
        return precio * 0.08 + (capacidadCarga * 0.01);
    }

    @Override
    public String obtenerTipoVehiculo() {
        return "Camión";
    }

    @Override
    public void acelerar() {
        System.out.println("El camión acelera con fuerza");
    }

    @Override
    public double calcularPrecioVenta() {
        double depreciacion = (2024 - año) * 0.08;
        return precio * (1 - Math.min(depreciacion, 0.7));
    }

    @Override
    public boolean estaDisponibleParaVenta() {
        return estado != EVehiculo.REPARACION;
    }

    // Getters y Setters
    public double getCapacidadCarga() { return capacidadCarga; }
    public void setCapacidadCarga(double capacidadCarga) { this.capacidadCarga = capacidadCarga; }
    
    public int getNumeroEjes() { return numeroEjes; }
    public void setNumeroEjes(int numeroEjes) { this.numeroEjes = numeroEjes; }
}
