package warehouse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class ViewerHomeController {

    private Main main;
    private int selectedCarIndex;
    private boolean searched = false;

    @FXML
    private Label message;

    @FXML
    private TextField searchbyReg, searchbyCarMake, searchbyCarModel;

    @FXML
    private Button buyCarBtn;

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
    @FXML
    private Label selectedLabel;


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
            if (!searched)
                buyCarBtn.setVisible(true);
        }
    }

    public void showAllCars(ActionEvent actionEvent) {
        try {
            searched = false;
            main.getObservableList().clear();
            main.getCarList().clear();
            main.getNetworkUtil().write("ShowAllCars");
            buyCarBtn.setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh(ActionEvent actionEvent) {
        try {
            main.getObservableList().clear();
            main.getCarList().clear();
            main.getNetworkUtil().write("ShowAllCars");
            buyCarBtn.setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buyCar(ActionEvent actionEvent) {

        if (selectedCarIndex > -1) {
            selectedLabel.setText("");
            buyCarBtn.setVisible(true);
            if (main.getCarList().get(selectedCarIndex).getQuantity() > 0) {
                int prevQuantity = main.getCarList().get(selectedCarIndex).getQuantity();
                main.getCarList().get(selectedCarIndex).setQuantity(prevQuantity - 1);

                try {
                    main.getNetworkUtil().write("EditCar," + selectedCarIndex);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    main.getNetworkUtil().write(main.getCarList().get(selectedCarIndex));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showAlert(1);
            } else {
                showAlert(0);
            }
        }
    }


    public void searchByReg(ActionEvent event) {
        try {
            if (!searchbyReg.getText().equals("")) {
                buyCarBtn.setVisible(false);
                searched = true;
                main.getObservableList().clear();
                main.getNetworkUtil().write("SearchReg," + searchbyReg.getText());
                searchbyReg.clear();

            } else {
                showAlert(2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchByMakeModel(ActionEvent event) {
        try {
            if (!searchbyCarMake.getText().isEmpty() && !searchbyCarModel.getText().isEmpty()) {
                buyCarBtn.setVisible(false);
                searched = true;

                main.getObservableList().clear();
                main.getNetworkUtil().write("SearchMakeModel," + searchbyCarMake.getText() + "," + searchbyCarModel.getText());
                searchbyReg.clear();

            } else {
                showAlert(2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(int code) {
        if (code == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText("Failed to buy");
            alert.setContentText("Sorry, quantity not available.");
            buyCarBtn.setVisible(false);
            alert.showAndWait();
        } else if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Purchase successful!");
            alert.setContentText("You have bought the car!\n" +
                    "The quantity is reduced by 1\n" +
                    "Hit refresh to see changes!");
            buyCarBtn.setVisible(false);
            alert.showAndWait();
        } else if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Failed to search");
            alert.setContentText("Please make sure to type before hitting the search button.");
            buyCarBtn.setVisible(false);
            alert.showAndWait();
        }
    }
}

