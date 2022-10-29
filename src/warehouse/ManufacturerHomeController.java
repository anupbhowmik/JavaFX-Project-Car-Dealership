package warehouse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class ManufacturerHomeController {

    private Main main;
    private int selectedCarIndex;

    @FXML
    private Label message;

    @FXML
    private Button editCarBtn, deleteCarBtn;


    @FXML
    private Label selectedLabel;

    @FXML
    private TableView<Car> tableView;
    @FXML
    private TableColumn<Car, String> regCol;
    @FXML
    private TableColumn<Car, Integer> yearCol;
    @FXML
    private TableColumn<Car, String> carMakeCol;
    @FXML
    private TableColumn<Car, String> carModelCol;
    @FXML
    private TableColumn<Car, String> color1;
    @FXML
    private TableColumn<Car, String> color2;
    @FXML
    private TableColumn<Car, String> color3;
    @FXML
    private TableColumn<Car, Integer> quantity;
    @FXML
    private TableColumn<Car, Integer> price;


    public void init(String msg) {
        message.setText(msg);
        regCol = new TableColumn<>("Registration No");
        regCol.setMinWidth(150);
        regCol.setCellValueFactory(new PropertyValueFactory<Car, String>("regNo"));

        yearCol = new TableColumn<Car, Integer>("Year Made");
        yearCol.setMinWidth(100);
        yearCol.setCellValueFactory(new PropertyValueFactory<Car, Integer>("yearMade"));

        carMakeCol = new TableColumn<>("Car Make");
        carMakeCol.setMinWidth(100);
        carMakeCol.setCellValueFactory(new PropertyValueFactory<Car, String>("carMake"));

        carModelCol = new TableColumn<>("Car Model");
        carModelCol.setMinWidth(100);
        carModelCol.setCellValueFactory(new PropertyValueFactory<Car, String>("carModel"));

        color1 = new TableColumn<>("Color 1");
        color1.setMinWidth(100);
        color1.setCellValueFactory(new PropertyValueFactory<Car, String>("color1"));

        color2 = new TableColumn<>("Color 2");
        color2.setMinWidth(100);
        color2.setCellValueFactory(new PropertyValueFactory<Car, String>("color2"));

        color3 = new TableColumn<>("Color 3");
        color3.setMinWidth(100);
        color3.setCellValueFactory(new PropertyValueFactory<Car, String>("color3"));

        quantity = new TableColumn<>("Quantity");
        quantity.setMinWidth(100);
        quantity.setCellValueFactory(new PropertyValueFactory<Car, Integer>("quantity"));

        price = new TableColumn<>("Price (USD)");
        price.setMinWidth(100);
        price.setCellValueFactory(new PropertyValueFactory<Car, Integer>("price"));


        tableView.getColumns().addAll(regCol, yearCol, carMakeCol, carModelCol, color1, color2, color3,
                quantity, price);
        tableView.setItems(main.getObservableList());

    }

    @FXML
    void logoutAction(ActionEvent event) {
        try {
            main.showLoginPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void showCredit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Credits");

        alert.setHeaderText("Car Warehouse JavaFX Application");
        alert.setContentText("Created by\n" +
                "Anup Bhowmik\n" +
                "CSE - 18, BUET\n" +
                "ID: 1805082");
        alert.showAndWait();
    }

    public void updateLabel() {
        selectedCarIndex = tableView.getSelectionModel().getSelectedIndex();
        if (selectedCarIndex > -1) {
            selectedLabel.setText("");
            editCarBtn.setDisable(false);
            deleteCarBtn.setDisable(false);
        }
    }


    public void addNewCar(ActionEvent actionEvent) {
        try {
            main.showAddCarPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh(ActionEvent actionEvent) {
        //  todo: fetch data from server
        try {
            main.getObservableList().clear();
            main.getCarList().clear();
            main.getNetworkUtil().write("ShowAllCars");
            editCarBtn.setDisable(true);
            deleteCarBtn.setDisable(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAllCars(ActionEvent actionEvent) {
        try {
            main.getObservableList().clear();
            main.getCarList().clear();
            main.getNetworkUtil().write("ShowAllCars");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCar(ActionEvent actionEvent) {
        try {
            main.getNetworkUtil().write("DeleteCar," + selectedCarIndex);
            main.getObservableList().clear();
            main.getCarList().clear();
            deleteCarBtn.setDisable(true);
            main.getNetworkUtil().write("ShowAllCars");
            editCarBtn.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editCar(ActionEvent actionEvent) {
        try {
            main.showEditCarPage(selectedCarIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
