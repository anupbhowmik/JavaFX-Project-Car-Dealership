package warehouse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


public class AddNewCarController {

    private Main main;
    @FXML
    TextField color1, color2, color3;
    @FXML
    TextField regNo, carMake, carModel, yearMake, price, quantity;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    public void submitCar(ActionEvent actionEvent) throws Exception {
        String[] carDetails = new String[9];

        carDetails[0] = regNo.getText();
        carDetails[1] = carMake.getText();
        carDetails[2] = carModel.getText();
        carDetails[3] = yearMake.getText();
        carDetails[4] = price.getText();
        carDetails[5] = quantity.getText();
        carDetails[6] = color1.getText();
        carDetails[7] = color2.getText();
        carDetails[8] = color3.getText();

        boolean ready = true;
        boolean sameReg = false;
        for (int i = 0; i < main.getCarList().size(); i++) {
            if (main.getCarList().get(i).getRegNo().equals(carDetails[0])) {
                sameReg = true;
                ready = false;
                showAlert(-1);
                break;
            }
        }
        if (!sameReg) {
            for (int i = 0; i < 6; i++) {
                if (carDetails[i].equals("")) {
                    ready = false;
                    showAlert(0);
                    break;
                }
            }
        }

        if (ready) {
            int yearInt = Integer.parseInt(yearMake.getText());
            int priceInt = Integer.parseInt(price.getText());
            int quantityInt = Integer.parseInt(quantity.getText());

            Car car = new Car(regNo.getText(), yearInt, color1.getText(), color2.getText(), color3.getText(),
                    carMake.getText(), carModel.getText(), priceInt, quantityInt);

            main.getNetworkUtil().write("AddCar");
            main.getNetworkUtil().write(car);

            main.getObservableList().clear();
            main.getNetworkUtil().write("ShowAllCars");

            showAlert(1);
            main.showManuHomePage();
        }
    }

    public void showAlert(int code) {
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add car");
            alert.setHeaderText("Success!");
            alert.setContentText("Car Added Successfully");
            alert.show();
        } else if (code == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Failed");
            alert.setHeaderText("Failed to add car");
            alert.setContentText("Please make sure that none of the necessary fields are empty.");
            alert.show();
        } else if (code == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText("Failed to add car");
            alert.setContentText("There is already a car with the same registration number.");
            alert.show();
        }
    }

    public void cancelAdding(ActionEvent actionEvent) {
        try {
            main.showManuHomePage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
