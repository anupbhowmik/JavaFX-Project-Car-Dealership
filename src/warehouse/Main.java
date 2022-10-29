package warehouse;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {


    private Stage stage;
    private NetworkUtil networkUtil;
    private ObservableList<Car> observableList = FXCollections.observableArrayList();
    private List<Car> carList;


    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        connectToServer();

        carList = new ArrayList<>();
        networkUtil.write("Init");
        //  GETTING THE LATEST CARLIST FROM SERVER AT THE BEGINNING

        showLoginPage();
    }

    private void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 33333;
        networkUtil = new NetworkUtil(serverAddress, serverPort);
        new ReadThread(this);
    }


    public void showLoginPage() throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("layout/login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setMain(this);

        stage.setTitle("Login");
        stage.setScene(new Scene(root, 540, 330));

        stage.show();
    }

    public void showManuHomePage() throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("layout/manufacturer_home.fxml"));
        Parent root = loader.load();
        observableList.clear();

        ManufacturerHomeController controller = loader.getController();
        controller.setMain(this);
        controller.init("Welcome, Manufacturer!");

        stage.setTitle("Manufacturer Base");
        stage.setScene(new Scene(root, 1200, 600));

        stage.show();

    }

    public void showViewerHomePage() throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("layout/viewer_home.fxml"));
        Parent root = loader.load();
        observableList.clear();

        ViewerHomeController controller = loader.getController();

        controller.setMain(this);
        controller.init("Welcome, Viewer!");

        stage.setTitle("Viewer Base");
        stage.setScene(new Scene(root, 1200, 600));
        stage.show();
    }

    public void showAddCarPage() throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("layout/add_new_car.fxml"));
        Parent root = loader.load();

        AddNewCarController controller = loader.getController();

        controller.setMain(this);

        stage.setTitle("Add a new Car");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showEditCarPage(int selectedCarIndex) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("layout/edit_car.fxml"));
        Parent root = loader.load();

        EditCarController controller = loader.getController();

        controller.setMain(this);
        controller.init(selectedCarIndex);

        stage.setTitle("Edit Car");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        alert.setContentText("The username and password you provided is incorrect.");

        alert.showAndWait();
    }

    public List<Car> getCarList() {
        return carList;
    }

    public ObservableList<Car> getObservableList() {
        return observableList;
    }

    public void addNewCar(Car car) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                carList.add(car);
                observableList.add(car);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
