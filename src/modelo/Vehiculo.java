/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Objects;

public abstract class Vehiculo implements Serializable, Comparable<Vehiculo> {
    protected static int contadorId = 1;
    protected int id;
    protected String marca;
    protected String modelo;
    protected int año;
    protected double precio;
    protected EVehiculo estado;
    protected ETipoCombustible combustible;

    // Constructor completo
    public Vehiculo(String marca, String modelo, int año, double precio, 
                   EVehiculo estado, ETipoCombustible combustible) {
        this.id = contadorId++;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.precio = precio;
        this.estado = estado;
        this.combustible = combustible;
    }

    // Constructor con un parámetro menos
    public Vehiculo(String marca, String modelo, int año, double precio, 
                   EVehiculo estado) {
        this(marca, modelo, año, precio, estado, ETipoCombustible.GASOLINA);
    }

    // Constructor personalizado
    public Vehiculo(String marca, String modelo, int año) {
        this(marca, modelo, año, 0.0, EVehiculo.NUEVO);
    }

    // Métodos abstractos
    public abstract double calcularMantenimiento();
    public abstract String obtenerTipoVehiculo();
    public abstract void acelerar();

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    
    public int getAño() { return año; }
    public void setAño(int año) { this.año = año; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public EVehiculo getEstado() { return estado; }
    public void setEstado(EVehiculo estado) { this.estado = estado; }
    
    public ETipoCombustible getCombustible() { return combustible; }
    public void setCombustible(ETipoCombustible combustible) { this.combustible = combustible; }

    @Override
    public int compareTo(Vehiculo otro) {
        return this.marca.compareTo(otro.marca);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) obj;
        return id == vehiculo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s (%d) - $%.2f - %s", 
                           obtenerTipoVehiculo(), marca, modelo, año, precio, estado);
    }
}

