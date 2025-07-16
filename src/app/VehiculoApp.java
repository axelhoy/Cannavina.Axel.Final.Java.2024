package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gestor.GestorVehiculos; 
import modelo.Auto;
import modelo.Camion;
import modelo.Moto;
import modelo.EVehiculo;
import modelo.ETipoCombustible;

import java.io.IOException;

public class VehiculoApp extends Application {

    private GestorVehiculos gestor;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicializar el gestor de vehículos
        gestor = new GestorVehiculos();

        // Cargar datos existentes primero
        try {
            gestor.cargarDatos();
            System.out.println("Datos cargados correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron datos previos, iniciando con datos de ejemplo.");
            // Agregar vehículos de ejemplo solo si no hay datos previos
            gestor.crear(new Auto("Toyota", "Corolla", 2020, 25000.0,
                                  EVehiculo.NUEVO, ETipoCombustible.GASOLINA, 4, false));
            gestor.crear(new Auto("BMW", "M3", 2021, 65000.0,
                                  EVehiculo.NUEVO, ETipoCombustible.GASOLINA, 2, true));
            gestor.crear(new Camion("Volvo", "FH16", 2019, 120000.0,
                                    EVehiculo.USADO, ETipoCombustible.DIESEL, 25000.0, 6));
            gestor.crear(new Moto("Yamaha", "R1", 2022, 18000.0,
                                  EVehiculo.NUEVO, ETipoCombustible.GASOLINA, 1000, true));
        }

        try {
            gestor.guardarDatos();
            System.out.println("Datos guardados correctamente después de la inicialización.");
        } catch (IOException e) {
            System.err.println("Error al guardar datos después de la inicialización: " + e.getMessage());
        }

        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VehiculoView.fxml"));
            Parent root = loader.load();

            VehiculoController controller = loader.getController();
            controller.setGestor(gestor);

            // Crear la escena
            Scene scene = new Scene(root, 1200, 800);

            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

            primaryStage.setTitle("Sistema de Gestión de Vehículos");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

            // Configurar el guardado de datos al cerrar la aplicación
            primaryStage.setOnCloseRequest(event -> {
                try {
                    gestor.guardarDatos();
                    System.out.println("Datos guardados al cerrar la aplicación.");
                } catch (IOException e) {
                    System.err.println("Error al guardar datos al cerrar: " + e.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la interfaz o iniciar la aplicación: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args); 
    }
}
