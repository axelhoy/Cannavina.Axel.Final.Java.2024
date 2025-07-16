package app;

import modelo.Auto;
import modelo.Camion;
import modelo.ETipoCombustible;
import modelo.EVehiculo;
import gestor.GestorVehiculos;
import modelo.Moto;
import modelo.Vehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Tooltip;
import java.util.ArrayList;
import java.util.List;

public class VehiculoController implements Initializable {
    
    @FXML private TableView<Vehiculo> tabla;
    @FXML private TableColumn<Vehiculo, Integer> colId;
    @FXML private TableColumn<Vehiculo, String> colTipo;
    @FXML private TableColumn<Vehiculo, String> colMarca;
    @FXML private TableColumn<Vehiculo, String> colModelo;
    @FXML private TableColumn<Vehiculo, Integer> colAño;
    @FXML private TableColumn<Vehiculo, Double> colPrecio;
    @FXML private TableColumn<Vehiculo, EVehiculo> colEstado;
    @FXML private TableColumn<Vehiculo, ETipoCombustible> colCombustible;
    @FXML private ComboBox<String> cbFiltro;
    
    private GestorVehiculos gestor;
    private ObservableList<Vehiculo> vehiculosObservable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gestor = new GestorVehiculos();
        vehiculosObservable = FXCollections.observableArrayList();
        
        // Configurar columnas de la tabla
        configurarTabla();
        
        // Configurar ComboBox
        cbFiltro.getItems().addAll("Todos", "Autos", "Camiones", "Motos", "Nuevos", "Usados");
        cbFiltro.setValue("Todos");
        
        // Cargar datos existentes
        try {
            gestor.cargarDatos();
        } catch (Exception e) {
            // Archivo no existe, continuar con lista vacía
        }
    }
    
    private void configurarTabla() {
        tabla.setItems(vehiculosObservable);
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipo.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().obtenerTipoVehiculo()));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAño.setCellValueFactory(new PropertyValueFactory<>("año"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCombustible.setCellValueFactory(new PropertyValueFactory<>("combustible"));
    }

    public void setGestor(GestorVehiculos gestor) {
    this.gestor = gestor;
    actualizarTabla();
}
    
    private void configurarCampoNumerico(TextField field, boolean permitirDecimales) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(permitirDecimales ? "\\d*\\.?\\d*" : "\\d*")) {
                field.setText(oldValue);
            }
        });
       }
    
    private List<String> validarCampos(String marca, String modelo, String año, String precio, String especifico1, String especifico2, String tipo) {
        List<String> errores = new ArrayList<>();

        if (marca.trim().isEmpty()) errores.add("La marca es obligatoria");
        if (modelo.trim().isEmpty()) errores.add("El modelo es obligatorio");
        if (año.trim().isEmpty()) errores.add("El año es obligatorio");
        else {
            try {
                int añoInt = Integer.parseInt(año);
                if (añoInt < 1900 || añoInt > 2025) errores.add("El año debe estar entre 1900 y 2025");
            } catch (NumberFormatException e) {
                errores.add("El año debe ser un número válido");
            }
        }

        if (precio.trim().isEmpty()) errores.add("El precio es obligatorio");
        else {
            try {
                double precioDouble = Double.parseDouble(precio);
                if (precioDouble <= 0) errores.add("El precio debe ser mayor a 0");
            } catch (NumberFormatException e) {
                errores.add("El precio debe ser un número válido");
            }
        }

        if (especifico1.trim().isEmpty()) errores.add("El campo específico 1 es obligatorio");
        else {
            try {
                if (tipo.equals("Auto")) {
                    int puertas = Integer.parseInt(especifico1);
                    if (puertas < 2 || puertas > 5) errores.add("El número de puertas debe estar entre 2 y 5");
                } else if (tipo.equals("Camión")) {
                    double capacidad = Double.parseDouble(especifico1);
                    if (capacidad <= 0) errores.add("La capacidad de carga debe ser mayor a 0");
                } else if (tipo.equals("Moto")) {
                    int cilindrada = Integer.parseInt(especifico1);
                    if (cilindrada <= 0) errores.add("La cilindrada debe ser mayor a 0");
                }
            } catch (NumberFormatException e) {
                errores.add("El campo específico 1 debe ser un número válido");
            }
        }

        if (tipo.equals("Camión") && especifico2.trim().isEmpty()) {
            errores.add("El número de ejes es obligatorio");
        } else if (tipo.equals("Camión")) {
            try {
                int ejes = Integer.parseInt(especifico2);
                if (ejes < 2 || ejes > 10) errores.add("El número de ejes debe estar entre 2 y 10");
            } catch (NumberFormatException e) {
                errores.add("El número de ejes debe ser un número válido");
            }
        }

        return errores;
    }   
    
    @FXML
    private void mostrarDialogoAgregar(ActionEvent event) {
        Dialog<Vehiculo> dialog = new Dialog<>();
        dialog.setTitle("Agregar Vehículo");
        dialog.setHeaderText("Ingrese los datos del vehículo");

        ButtonType btnAceptar = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAceptar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Auto", "Camión", "Moto");
        cbTipo.setValue("Auto");

        TextField tfMarca = new TextField();
        TextField tfModelo = new TextField();
        TextField tfAño = new TextField();
        TextField tfPrecio = new TextField();

        // Configurar campos numéricos
        configurarCampoNumerico(tfAño, false);
        configurarCampoNumerico(tfPrecio, true);

        ComboBox<EVehiculo> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll(EVehiculo.values());
        cbEstado.setValue(EVehiculo.NUEVO);

        ComboBox<ETipoCombustible> cbCombustible = new ComboBox<>();
        cbCombustible.getItems().addAll(ETipoCombustible.values());
        cbCombustible.setValue(ETipoCombustible.GASOLINA);

        TextField tfEspecifico1 = new TextField();
        CheckBox chkEspecifico2 = new CheckBox();
        TextField tfEjes = new TextField();
        Label lblEspecifico1 = new Label();
        Label lblEspecifico2 = new Label();

        // Configurar campo específico 1 como numérico
        configurarCampoNumerico(tfEspecifico1, false);
        configurarCampoNumerico(tfEjes, false);

        // Botón de errores con tooltip
        Button btnErrores = new Button("⚠");
        btnErrores.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold;");
        btnErrores.setPrefSize(30, 30);
        btnErrores.setVisible(false);

        Tooltip tooltipErrores = new Tooltip();
        btnErrores.setTooltip(tooltipErrores);

        cbTipo.setOnAction(e -> {
            String tipo = cbTipo.getValue();
            grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == 8 && GridPane.getColumnIndex(node) == 1);

            switch (tipo) {
                case "Auto":
                    lblEspecifico1.setText("Número de Puertas:");
                    lblEspecifico2.setText("Es Deportivo:");
                    chkEspecifico2.setText("Deportivo");
                    chkEspecifico2.setSelected(false);
                    grid.add(chkEspecifico2, 1, 8);
                    configurarCampoNumerico(tfEspecifico1, false);
                    break;
                case "Camión":
                    lblEspecifico1.setText("Capacidad de Carga:");
                    lblEspecifico2.setText("Número de Ejes:");
                    tfEjes.clear();
                    grid.add(tfEjes, 1, 8);
                    configurarCampoNumerico(tfEspecifico1, true);
                    break;
                case "Moto":
                    lblEspecifico1.setText("Cilindrada:");
                    lblEspecifico2.setText("Tiene Maletas:");
                    chkEspecifico2.setText("Con maletas");
                    chkEspecifico2.setSelected(false);
                    grid.add(chkEspecifico2, 1, 8);
                    configurarCampoNumerico(tfEspecifico1, false);
                    break;
            }
        });

        // Validación en tiempo real
        Runnable validarFormulario = () -> {
            String tipo = cbTipo.getValue();
            String especifico2 = tipo.equals("Camión") ? tfEjes.getText() : "";

            List<String> errores = validarCampos(
                tfMarca.getText(),
                tfModelo.getText(), 
                tfAño.getText(),
                tfPrecio.getText(),
                tfEspecifico1.getText(),
                especifico2,
                tipo
            );

            boolean hayErrores = !errores.isEmpty();
            btnErrores.setVisible(hayErrores);

            if (hayErrores) {
                tooltipErrores.setText(String.join("\n", errores));
            }

            dialog.getDialogPane().lookupButton(btnAceptar).setDisable(hayErrores);
        };

        // Agregar listeners a todos los campos
        tfMarca.textProperty().addListener((obs, old, nuevo) -> validarFormulario.run());
        tfModelo.textProperty().addListener((obs, old, nuevo) -> validarFormulario.run());
        tfAño.textProperty().addListener((obs, old, nuevo) -> validarFormulario.run());
        tfPrecio.textProperty().addListener((obs, old, nuevo) -> validarFormulario.run());
        tfEspecifico1.textProperty().addListener((obs, old, nuevo) -> validarFormulario.run());
        tfEjes.textProperty().addListener((obs, old, nuevo) -> validarFormulario.run());
        cbTipo.valueProperty().addListener((obs, old, nuevo) -> validarFormulario.run());

        // Inicializar con Auto por defecto
        cbTipo.fireEvent(new javafx.event.ActionEvent());

        grid.add(new Label("Tipo:"), 0, 0);
        grid.add(cbTipo, 1, 0);
        grid.add(new Label("Marca:"), 0, 1);
        grid.add(tfMarca, 1, 1);
        grid.add(new Label("Modelo:"), 0, 2);
        grid.add(tfModelo, 1, 2);
        grid.add(new Label("Año:"), 0, 3);
        grid.add(tfAño, 1, 3);
        grid.add(new Label("Precio:"), 0, 4);
        grid.add(tfPrecio, 1, 4);
        grid.add(new Label("Estado:"), 0, 5);
        grid.add(cbEstado, 1, 5);
        grid.add(new Label("Combustible:"), 0, 6);
        grid.add(cbCombustible, 1, 6);
        grid.add(lblEspecifico1, 0, 7);
        grid.add(tfEspecifico1, 1, 7);
        grid.add(lblEspecifico2, 0, 8);
        grid.add(btnErrores, 2, 0); // Botón de errores en la esquina

        dialog.getDialogPane().setContent(grid);

        // Validación inicial
        validarFormulario.run();

    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == btnAceptar) {
            try {
                String tipo = cbTipo.getValue();
                String marca = tfMarca.getText();
                String modelo = tfModelo.getText();
                int año = Integer.parseInt(tfAño.getText());
                double precio = Double.parseDouble(tfPrecio.getText());
                EVehiculo estado = cbEstado.getValue();
                ETipoCombustible combustible = cbCombustible.getValue();

                switch (tipo) {
                    case "Auto":
                        int puertas = Integer.parseInt(tfEspecifico1.getText());
                        boolean deportivo = chkEspecifico2.isSelected();
                        // QUITAR la línea vehiculo.setId() si existía
                        return new Auto(marca, modelo, año, precio, estado, combustible, puertas, deportivo);
                    case "Camión":
                        double capacidad = Double.parseDouble(tfEspecifico1.getText());
                        int ejes = Integer.parseInt(tfEjes.getText());
                        // QUITAR la línea vehiculo.setId() si existía
                        return new Camion(marca, modelo, año, precio, estado, combustible, capacidad, ejes);
                    case "Moto":
                        int cilindrada = Integer.parseInt(tfEspecifico1.getText());
                        boolean maletas = chkEspecifico2.isSelected(); 
                        // QUITAR la línea vehiculo.setId() si existía
                        return new Moto(marca, modelo, año, precio, estado, combustible, cilindrada, maletas);
                }
            } catch (Exception e) {
                mostrarAlerta("Error", "Datos inválidos: " + e.getMessage());
            }
        }
        return null;
    });

        dialog.showAndWait().ifPresent(vehiculo -> {
            gestor.crear(vehiculo);
            actualizarTabla();
        });
    }

    @FXML
    private void editarVehiculo(ActionEvent event) {
        Vehiculo seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un vehículo para editar");
            return;
        }

        Dialog<Vehiculo> dialog = new Dialog<>();
        dialog.setTitle("Editar Vehículo");
        dialog.setHeaderText("Modifique los datos del vehículo");

        ButtonType btnAceptar = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAceptar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Campos comunes
        TextField tfMarca = new TextField(seleccionado.getMarca());
        TextField tfModelo = new TextField(seleccionado.getModelo());
        TextField tfAño = new TextField(String.valueOf(seleccionado.getAño()));
        TextField tfPrecio = new TextField(String.valueOf(seleccionado.getPrecio()));

        configurarCampoNumerico(tfAño, false);
        configurarCampoNumerico(tfPrecio, true);

        ComboBox<EVehiculo> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll(EVehiculo.values());
        cbEstado.setValue(seleccionado.getEstado());

        ComboBox<ETipoCombustible> cbCombustible = new ComboBox<>();
        cbCombustible.getItems().addAll(ETipoCombustible.values());
        cbCombustible.setValue(seleccionado.getCombustible());

        // Campos específicos según el tipo
        TextField tfEspecifico1 = new TextField();
        CheckBox chkEspecifico2 = new CheckBox(); // Cambiado a CheckBox
        TextField tfEjes = new TextField(); // Para camión
        Label lblEspecifico1 = new Label();
        Label lblEspecifico2 = new Label();
        Label lblTipo = new Label();

        configurarCampoNumerico(tfEspecifico1, false);
        // Determinar el tipo y configurar campos específicos
        if (seleccionado instanceof Auto) {
            Auto auto = (Auto) seleccionado;
            lblTipo.setText("Tipo: Auto");
            lblEspecifico1.setText("Número de Puertas:");
            lblEspecifico2.setText("Es Deportivo:");
            tfEspecifico1.setText(String.valueOf(auto.getNumeroPuertas()));
            chkEspecifico2.setText("Deportivo");
            chkEspecifico2.setSelected(auto.isEsDeportivo()); // Usar setSelected()

            // Agregar controles al grid
            grid.add(lblTipo, 0, 0);
            grid.add(new Label("Marca:"), 0, 1);
            grid.add(tfMarca, 1, 1);
            grid.add(new Label("Modelo:"), 0, 2);
            grid.add(tfModelo, 1, 2);
            grid.add(new Label("Año:"), 0, 3);
            grid.add(tfAño, 1, 3);
            grid.add(new Label("Precio:"), 0, 4);
            grid.add(tfPrecio, 1, 4);
            grid.add(new Label("Estado:"), 0, 5);
            grid.add(cbEstado, 1, 5);
            grid.add(new Label("Combustible:"), 0, 6);
            grid.add(cbCombustible, 1, 6);
            grid.add(lblEspecifico1, 0, 7);
            grid.add(tfEspecifico1, 1, 7);
            grid.add(lblEspecifico2, 0, 8);
            grid.add(chkEspecifico2, 1, 8);

        } else if (seleccionado instanceof Camion) {
            Camion camion = (Camion) seleccionado;
            lblTipo.setText("Tipo: Camión");
            lblEspecifico1.setText("Capacidad de Carga:");
            lblEspecifico2.setText("Número de Ejes:");
            tfEspecifico1.setText(String.valueOf(camion.getCapacidadCarga()));
            tfEjes.setText(String.valueOf(camion.getNumeroEjes()));
            configurarCampoNumerico(tfEjes, false);
            // Agregar controles al grid
            grid.add(lblTipo, 0, 0);
            grid.add(new Label("Marca:"), 0, 1);
            grid.add(tfMarca, 1, 1);
            grid.add(new Label("Modelo:"), 0, 2);
            grid.add(tfModelo, 1, 2);
            grid.add(new Label("Año:"), 0, 3);
            grid.add(tfAño, 1, 3);
            grid.add(new Label("Precio:"), 0, 4);
            grid.add(tfPrecio, 1, 4);
            grid.add(new Label("Estado:"), 0, 5);
            grid.add(cbEstado, 1, 5);
            grid.add(new Label("Combustible:"), 0, 6);
            grid.add(cbCombustible, 1, 6);
            grid.add(lblEspecifico1, 0, 7);
            grid.add(tfEspecifico1, 1, 7);
            grid.add(lblEspecifico2, 0, 8);
            grid.add(tfEjes, 1, 8);

        } else if (seleccionado instanceof Moto) {
            Moto moto = (Moto) seleccionado;
            lblTipo.setText("Tipo: Moto");
            lblEspecifico1.setText("Cilindrada:");
            lblEspecifico2.setText("Tiene Maletas:");
            tfEspecifico1.setText(String.valueOf(moto.getCilindrada()));
            chkEspecifico2.setText("Con maletas");
            chkEspecifico2.setSelected(moto.isTieneMaletas()); // Usar setSelected()

            // Agregar controles al grid
            grid.add(lblTipo, 0, 0);
            grid.add(new Label("Marca:"), 0, 1);
            grid.add(tfMarca, 1, 1);
            grid.add(new Label("Modelo:"), 0, 2);
            grid.add(tfModelo, 1, 2);
            grid.add(new Label("Año:"), 0, 3);
            grid.add(tfAño, 1, 3);
            grid.add(new Label("Precio:"), 0, 4);
            grid.add(tfPrecio, 1, 4);
            grid.add(new Label("Estado:"), 0, 5);
            grid.add(cbEstado, 1, 5);
            grid.add(new Label("Combustible:"), 0, 6);
            grid.add(cbCombustible, 1, 6);
            grid.add(lblEspecifico1, 0, 7);
            grid.add(tfEspecifico1, 1, 7);
            grid.add(lblEspecifico2, 0, 8);
            grid.add(chkEspecifico2, 1, 8);
        }
        Button btnErroresEditar = new Button("⚠");
        btnErroresEditar.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold;");
        btnErroresEditar.setPrefSize(30, 30);
        btnErroresEditar.setVisible(false);

        Tooltip tooltipErroresEditar = new Tooltip();
        btnErroresEditar.setTooltip(tooltipErroresEditar);

        // Validación en tiempo real para editar
        Runnable validarFormularioEditar = () -> {
            String tipo = seleccionado instanceof Auto ? "Auto" : 
                         seleccionado instanceof Camion ? "Camión" : "Moto";
            String especifico2 = tipo.equals("Camión") ? tfEjes.getText() : "";

            List<String> errores = validarCampos(
                tfMarca.getText(),
                tfModelo.getText(), 
                tfAño.getText(),
                tfPrecio.getText(),
                tfEspecifico1.getText(),
                especifico2,
                tipo
            );

            boolean hayErrores = !errores.isEmpty();
            btnErroresEditar.setVisible(hayErrores);

            if (hayErrores) {
                tooltipErroresEditar.setText(String.join("\n", errores));
            }

            dialog.getDialogPane().lookupButton(btnAceptar).setDisable(hayErrores);
        };

        // Agregar listeners para editar
        tfMarca.textProperty().addListener((obs, old, nuevo) -> validarFormularioEditar.run());
        tfModelo.textProperty().addListener((obs, old, nuevo) -> validarFormularioEditar.run());
        tfAño.textProperty().addListener((obs, old, nuevo) -> validarFormularioEditar.run());
        tfPrecio.textProperty().addListener((obs, old, nuevo) -> validarFormularioEditar.run());
        tfEspecifico1.textProperty().addListener((obs, old, nuevo) -> validarFormularioEditar.run());
        if (seleccionado instanceof Camion) {
            tfEjes.textProperty().addListener((obs, old, nuevo) -> validarFormularioEditar.run());
        }

        // Y agregar el botón de errores al grid (después de agregar todos los controles al grid):
        grid.add(btnErroresEditar, 2, 0);

        // Validación inicial para editar
        validarFormularioEditar.run();
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnAceptar) {
                try {
                    String marca = tfMarca.getText();
                    String modelo = tfModelo.getText();
                    int año = Integer.parseInt(tfAño.getText());
                    double precio = Double.parseDouble(tfPrecio.getText());
                    EVehiculo estado = cbEstado.getValue();
                    ETipoCombustible combustible = cbCombustible.getValue();

                    // Crear nuevo vehículo según el tipo original
                    if (seleccionado instanceof Auto) {
                        int puertas = Integer.parseInt(tfEspecifico1.getText());
                        boolean deportivo = chkEspecifico2.isSelected(); // Usar isSelected()
                        Auto nuevoAuto = new Auto(marca, modelo, año, precio, estado, combustible, puertas, deportivo);
                        nuevoAuto.setId(seleccionado.getId());
                        return nuevoAuto;
                    } else if (seleccionado instanceof Camion) {
                        double capacidad = Double.parseDouble(tfEspecifico1.getText());
                        int ejes = Integer.parseInt(tfEjes.getText());
                        Camion nuevoCamion = new Camion(marca, modelo, año, precio, estado, combustible, capacidad, ejes);
                        nuevoCamion.setId(seleccionado.getId());
                        return nuevoCamion;
                    } else if (seleccionado instanceof Moto) {
                        int cilindrada = Integer.parseInt(tfEspecifico1.getText());
                        boolean maletas = chkEspecifico2.isSelected(); // Usar isSelected()
                        Moto nuevaMoto = new Moto(marca, modelo, año, precio, estado, combustible, cilindrada, maletas);
                        nuevaMoto.setId(seleccionado.getId());
                        return nuevaMoto;
                    }
                } catch (Exception e) {
                    mostrarAlerta("Error", "Datos inválidos: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(vehiculoEditado -> {
            int index = tabla.getSelectionModel().getSelectedIndex();
            gestor.editar(index, vehiculoEditado);
            actualizarTabla();
        });
    }

    @FXML
    private void eliminarVehiculo(ActionEvent event) {
        Vehiculo seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un vehículo para eliminar");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este vehículo?");
        alert.setContentText(seleccionado.toString());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                int index = tabla.getSelectionModel().getSelectedIndex();
                gestor.eliminar(index);
                actualizarTabla();
            }
        });
    }

    @FXML
    private void verDetalles(ActionEvent event) {
        Vehiculo seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un vehículo para ver sus detalles");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detalles del Vehículo");
        dialog.setHeaderText("Información completa del vehículo seleccionado");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Estilo para las etiquetas
        String estiloLabel = "-fx-font-weight: bold; -fx-text-fill: #333;";
        String estiloValor = "-fx-text-fill: #666;";

        // Información básica
        Label lblId = new Label("ID:");
        lblId.setStyle(estiloLabel);
        Label valorId = new Label(String.valueOf(seleccionado.getId()));
        valorId.setStyle(estiloValor);

        Label lblTipo = new Label("Tipo:");
        lblTipo.setStyle(estiloLabel);
        Label valorTipo = new Label(seleccionado.obtenerTipoVehiculo());
        valorTipo.setStyle(estiloValor);

        Label lblMarca = new Label("Marca:");
        lblMarca.setStyle(estiloLabel);
        Label valorMarca = new Label(seleccionado.getMarca());
        valorMarca.setStyle(estiloValor);

        Label lblModelo = new Label("Modelo:");
        lblModelo.setStyle(estiloLabel);
        Label valorModelo = new Label(seleccionado.getModelo());
        valorModelo.setStyle(estiloValor);

        Label lblAño = new Label("Año:");
        lblAño.setStyle(estiloLabel);
        Label valorAño = new Label(String.valueOf(seleccionado.getAño()));
        valorAño.setStyle(estiloValor);

        Label lblPrecio = new Label("Precio:");
        lblPrecio.setStyle(estiloLabel);
        Label valorPrecio = new Label(String.format("$%.2f", seleccionado.getPrecio()));
        valorPrecio.setStyle(estiloValor);

        Label lblEstado = new Label("Estado:");
        lblEstado.setStyle(estiloLabel);
        Label valorEstado = new Label(seleccionado.getEstado().toString());
        valorEstado.setStyle(estiloValor);

        Label lblCombustible = new Label("Combustible:");
        lblCombustible.setStyle(estiloLabel);
        Label valorCombustible = new Label(seleccionado.getCombustible().toString());
        valorCombustible.setStyle(estiloValor);

        // Agregar información básica al grid
        grid.add(lblId, 0, 0);
        grid.add(valorId, 1, 0);
        grid.add(lblTipo, 0, 1);
        grid.add(valorTipo, 1, 1);
        grid.add(lblMarca, 0, 2);
        grid.add(valorMarca, 1, 2);
        grid.add(lblModelo, 0, 3);
        grid.add(valorModelo, 1, 3);
        grid.add(lblAño, 0, 4);
        grid.add(valorAño, 1, 4);
        grid.add(lblPrecio, 0, 5);
        grid.add(valorPrecio, 1, 5);
        grid.add(lblEstado, 0, 6);
        grid.add(valorEstado, 1, 6);
        grid.add(lblCombustible, 0, 7);
        grid.add(valorCombustible, 1, 7);

        // Separador
        Separator separador = new Separator();
        separador.setStyle("-fx-padding: 10 0 10 0;");
        grid.add(separador, 0, 8, 2, 1);

        // Información específica según el tipo
        Label lblEspecificos = new Label("Características Específicas:");
        lblEspecificos.setStyle(estiloLabel + "-fx-font-size: 14px;");
        grid.add(lblEspecificos, 0, 9, 2, 1);

        int filaActual = 10;

        if (seleccionado instanceof Auto) {
            Auto auto = (Auto) seleccionado;

            Label lblPuertas = new Label("Número de Puertas:");
            lblPuertas.setStyle(estiloLabel);
            Label valorPuertas = new Label(String.valueOf(auto.getNumeroPuertas()));
            valorPuertas.setStyle(estiloValor);

            Label lblDeportivo = new Label("Es Deportivo:");
            lblDeportivo.setStyle(estiloLabel);
            Label valorDeportivo = new Label(auto.isEsDeportivo() ? "Sí" : "No");
            valorDeportivo.setStyle(estiloValor);

            grid.add(lblPuertas, 0, filaActual);
            grid.add(valorPuertas, 1, filaActual);
            grid.add(lblDeportivo, 0, filaActual + 1);
            grid.add(valorDeportivo, 1, filaActual + 1);

        } else if (seleccionado instanceof Camion) {
            Camion camion = (Camion) seleccionado;

            Label lblCapacidad = new Label("Capacidad de Carga:");
            lblCapacidad.setStyle(estiloLabel);
            Label valorCapacidad = new Label(String.format("%.2f toneladas", camion.getCapacidadCarga()));
            valorCapacidad.setStyle(estiloValor);

            Label lblEjes = new Label("Número de Ejes:");
            lblEjes.setStyle(estiloLabel);
            Label valorEjes = new Label(String.valueOf(camion.getNumeroEjes()));
            valorEjes.setStyle(estiloValor);

            grid.add(lblCapacidad, 0, filaActual);
            grid.add(valorCapacidad, 1, filaActual);
            grid.add(lblEjes, 0, filaActual + 1);
            grid.add(valorEjes, 1, filaActual + 1);

        } else if (seleccionado instanceof Moto) {
            Moto moto = (Moto) seleccionado;

            Label lblCilindrada = new Label("Cilindrada:");
            lblCilindrada.setStyle(estiloLabel);
            Label valorCilindrada = new Label(moto.getCilindrada() + " cc");
            valorCilindrada.setStyle(estiloValor);

            Label lblMaletas = new Label("Tiene Maletas:");
            lblMaletas.setStyle(estiloLabel);
            Label valorMaletas = new Label(moto.isTieneMaletas() ? "Sí" : "No");
            valorMaletas.setStyle(estiloValor);

            grid.add(lblCilindrada, 0, filaActual);
            grid.add(valorCilindrada, 1, filaActual);
            grid.add(lblMaletas, 0, filaActual + 1);
            grid.add(valorMaletas, 1, filaActual + 1);
        }

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setPrefWidth(400);

        dialog.showAndWait();
    }
    @FXML
    private void ordenarPorMarca(ActionEvent event) {
        gestor.ordenarPorCriterioNatural();
        actualizarTabla();
    }

    @FXML
    private void ordenarPorPrecio(ActionEvent event) {
        gestor.ordenarPorPrecio();
        actualizarTabla();
    }

    @FXML
    private void ordenarPorAño(ActionEvent event) {
        gestor.ordenarPorAño();
        actualizarTabla();
    }

    @FXML
    private void aplicarFiltro(ActionEvent event) {
        String filtro = cbFiltro.getValue();
        List<Vehiculo> vehiculosFiltrados;
        
        switch (filtro) {
            case "Autos":
                vehiculosFiltrados = gestor.filtrarPorTipo(Auto.class);
                break;
            case "Camiones":
                vehiculosFiltrados = gestor.filtrarPorTipo(Camion.class);
                break;
            case "Motos":
                vehiculosFiltrados = gestor.filtrarPorTipo(Moto.class);
                break;
            case "Nuevos":
                vehiculosFiltrados = gestor.filtrarPorEstado(EVehiculo.NUEVO);
                break;
            case "Usados":
                vehiculosFiltrados = gestor.filtrarPorEstado(EVehiculo.USADO);
                break;
            default:
                vehiculosFiltrados = gestor.leer();
        }
        
        vehiculosObservable.setAll(vehiculosFiltrados);
    }

    @FXML
    private void aplicarDescuento(ActionEvent event) {
        Vehiculo seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un vehículo para aplicar descuento");
            return;
        }

        gestor.aplicarDescuento(seleccionado, 10);
        actualizarTabla();
    }
    
    @FXML
    private void guardarDatos(ActionEvent event) {
        try {
            gestor.guardarDatos();
            mostrarAlerta("Éxito", "Datos guardados correctamente");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void exportarCSV(ActionEvent event) {
        try {
            gestor.exportarCSV();
            mostrarAlerta("Éxito", "Archivo CSV exportado correctamente");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al exportar CSV: " + e.getMessage());
        }
    }

    @FXML
    private void exportarJSON(ActionEvent event) {
        try {
            gestor.exportarJSON();
            mostrarAlerta("Éxito", "Archivo JSON exportado correctamente");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al exportar JSON: " + e.getMessage());
        }
    }

    @FXML
    private void generarReporte(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt")
        );
        
        Stage stage = (Stage) tabla.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stage);
        if (archivo != null) {
            try {
                gestor.exportarReporte(gestor.leer(), archivo.getAbsolutePath());
                mostrarAlerta("Éxito", "Reporte generado correctamente");
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al generar reporte: " + e.getMessage());
            }
        }
    }

    private void actualizarTabla() {
        vehiculosObservable.setAll(gestor.leer());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
}