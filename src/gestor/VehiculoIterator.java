/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestor;

import modelo.Vehiculo;
import java.util.Iterator;
import java.util.List;

public class VehiculoIterator implements Iterator<Vehiculo> {
    private List<Vehiculo> vehiculos;
    private int posicion = 0;

    public VehiculoIterator(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    @Override
    public boolean hasNext() {
        return posicion < vehiculos.size();
    }

    @Override
    public Vehiculo next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        return vehiculos.get(posicion++);
    }
}