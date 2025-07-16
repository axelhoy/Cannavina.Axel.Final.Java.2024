package gestor;

import modelo.EVehiculo;
import modelo.Vehiculo;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import util.AñoComparator;
import util.PrecioComparator;
import util.VehiculoNoEncontradoException;

public class GestorVehiculos implements ICrud<Vehiculo> {
    private List<Vehiculo> vehiculos;
    private static int contadorId = 1; // AGREGAR ESTA LÍNEA
    private static final String ARCHIVO_DATOS = "vehiculos.dat";
    private static final String ARCHIVO_CSV = "vehiculos.csv";
    private static final String ARCHIVO_JSON = "vehiculos.json";

    public GestorVehiculos() {
        this.vehiculos = new ArrayList<>();
    }

    @Override
    public void crear(Vehiculo vehiculo) {
        vehiculo.setId(contadorId++); // MODIFICAR ESTA LÍNEA
        vehiculos.add(vehiculo);
    }

    @Override
    public List<Vehiculo> leer() {
        return new ArrayList<>(vehiculos);
    }

    @Override
    public void actualizar(int index, Vehiculo vehiculo) {
        if (index >= 0 && index < vehiculos.size()) {
            vehiculos.set(index, vehiculo);
        }
    }

    @Override
    public void eliminar(int index) {
        if (index >= 0 && index < vehiculos.size()) {
            vehiculos.remove(index);
        }
    }

    @Override
    public Vehiculo obtenerPorId(int id) throws VehiculoNoEncontradoException { // AHORA DECLARA QUE PUEDE LANZARLA
        return vehiculos.stream()
                       .filter(v -> v.getId() == id)
                       .findFirst()
                       .orElseThrow(() -> new VehiculoNoEncontradoException("Vehículo con ID " + id + " no encontrado.")); // AHORA LANZA LA EXCEPCIÓN
    }

    // AGREGAR ESTE MÉTODO NUEVO
    private void actualizarContadorId() {
        if (!vehiculos.isEmpty()) {
            int maxId = vehiculos.stream()
                .mapToInt(Vehiculo::getId)
                .max()
                .orElse(0);
            contadorId = maxId + 1;
        }
    }

    // Método con wildcard upper bound
    public double calcularPrecioTotal(List<? extends Vehiculo> lista) {
        return lista.stream().mapToDouble(Vehiculo::getPrecio).sum();
    }

    // Método con wildcard lower bound
    public void agregarVehiculos(List<? super Vehiculo> lista, Vehiculo vehiculo) {
        lista.add(vehiculo);
    }
    
    public void editar(int index, Vehiculo vehiculoEditado) {
        if (index >= 0 && index < vehiculos.size()) {
            vehiculos.set(index, vehiculoEditado);
        }
    }

    // Ordenamiento
    public void ordenarPorCriterioNatural() {
        Collections.sort(vehiculos);
    }

    public void ordenarPorPrecio() {
            vehiculos.sort(new PrecioComparator());
    }

    public void ordenarPorAño() {
        vehiculos.sort(new AñoComparator());
    }

    // Filtrado
    public List<Vehiculo> filtrarPorEstado(EVehiculo estado) {
        return vehiculos.stream()
                       .filter(v -> v.getEstado() == estado)
                       .collect(Collectors.toList());
    }

    public List<Vehiculo> filtrarPorTipo(Class<? extends Vehiculo> tipo) {
        return vehiculos.stream()
                       .filter(tipo::isInstance)
                       .collect(Collectors.toList());
    }

    // Interfaces funcionales
    public void aplicarDescuento(Vehiculo vehiculo, double porcentaje) {
        Consumer<Vehiculo> aplicarDescuento = v -> 
            v.setPrecio(v.getPrecio() * (1 - porcentaje / 100));
        aplicarDescuento.accept(vehiculo);
    }

    public void actualizarPrecios(Function<Double, Double> funcion) {
        vehiculos.forEach(v -> v.setPrecio(funcion.apply(v.getPrecio())));
    }

    public void cambiarEstado(EVehiculo nuevoEstado, Predicate<Vehiculo> condicion) {
        vehiculos.stream()
                 .filter(condicion)
                 .forEach(v -> v.setEstado(nuevoEstado));
    }

    // Iterator personalizado
    public VehiculoIterator iterator() {
        return new VehiculoIterator(vehiculos);
    }
    
    private void crearDirectorioSiNoExiste() {
        File directorio = new File("data");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
    
    // Serialización - MODIFICAR ESTE MÉTODO
    public void guardarDatos() throws IOException {
        crearDirectorioSiNoExiste();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(vehiculos);
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDatos() throws IOException, ClassNotFoundException {
        File archivo = new File(ARCHIVO_DATOS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_DATOS))) {
                vehiculos = (List<Vehiculo>) ois.readObject();
                actualizarContadorId(); // AGREGAR ESTA LÍNEA
            }
        } else {
            System.out.println("Error al cargar los datos");
        }
    }

    // CSV
    public void exportarCSV() throws IOException {
        crearDirectorioSiNoExiste();
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_CSV))) {
            writer.println("ID,Tipo,Marca,Modelo,Año,Precio,Estado,Combustible");
            for (Vehiculo v : vehiculos) {
                writer.printf("%d,%s,%s,%s,%d,%.2f,%s,%s%n",
                    v.getId(), v.obtenerTipoVehiculo(), v.getMarca(), v.getModelo(),
                    v.getAño(), v.getPrecio(), v.getEstado(), v.getCombustible());
            }
        }
    }

    // JSON - MODIFICAR ESTE MÉTODO
    public void exportarJSON() throws IOException {
        crearDirectorioSiNoExiste();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(ARCHIVO_JSON), vehiculos);
    }
    
    public void importarJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File archivo = new File(ARCHIVO_JSON);
        if (archivo.exists()) {
            vehiculos = mapper.readValue(archivo, new TypeReference<List<Vehiculo>>() {});
            actualizarContadorId(); // AGREGAR ESTA LÍNEA
        }
    }

    // Exportar reporte
    public void exportarReporte(List<Vehiculo> vehiculosFiltrados, String nombreArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("=== REPORTE DE VEHÍCULOS ===");
            writer.println("Fecha: " + new Date());
            writer.println("Total de vehículos: " + vehiculosFiltrados.size());
            writer.println();
            
            for (Vehiculo v : vehiculosFiltrados) {
                writer.println(v.toString());
                writer.println("  - Mantenimiento: $" + String.format("%.2f", v.calcularMantenimiento()));
                if (v instanceof IVendible) {
                    writer.println("  - Precio venta: $" + String.format("%.2f", ((IVendible) v).calcularPrecioVenta()));
                }
                writer.println();
            }
        }
    }
}