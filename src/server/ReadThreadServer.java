package server;

import warehouse.Car;
import util.LoginInfo;
import util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadThreadServer implements Runnable {
    private Server server;
    private final Thread thr;
    private final NetworkUtil networkUtil;
    public HashMap<String, String> userMap;


    public ReadThreadServer(HashMap<String, String> map, NetworkUtil networkUtil, Server server) {
        this.userMap = map;
        this.server = server;
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {

            while (true) {
                Object o = networkUtil.read();

                if (o != null) {
                    if (o instanceof LoginInfo) {
                        LoginInfo loginInfo = (LoginInfo) o;
                        String password = userMap.get(loginInfo.getUserName());
                        if (loginInfo.getUserName().equals("viewer") && loginInfo.getPassword().equals(""))
                            loginInfo.setStatus(2);
                        else if (loginInfo.getPassword().equals(password))
                            loginInfo.setStatus(1);
                        networkUtil.write(loginInfo);
                    } else if (o instanceof String) {
                        String request = (String) o;
                        String[] commands = request.split(",");
                        if (commands[0].equals("ShowAllCars") || commands[0].equals("Init")) {
                            List<Car> carList = new ArrayList<>();
                            carList = server.getCarList();
                            for (int i = 0; i < carList.size(); i++) {
                                networkUtil.write(carList.get(i));
                            }
                        } else if (commands[0].equals("AddCar")) {
                            Car newCar = new Car();
                            Object car = networkUtil.read();
                            if (car instanceof Car) {
                                newCar = (Car) car;
                                List<Car> carList = new ArrayList<>();
                                carList = server.getCarList();
                                carList.add(newCar);
                            }
                        } else if (commands[0].equals("DeleteCar")) {
                            int carIndex = Integer.parseInt(commands[1]);
                            List<Car> carList = new ArrayList<>();
                            carList = server.getCarList();
                            carList.remove(carIndex);
                        } else if (commands[0].equals("EditCar")) {
                            int carIndex = Integer.parseInt(commands[1]);
                            Object car = networkUtil.read();

                            if (car instanceof Car) {
                                Car editedCar = (Car) car;
                                server.getCarList().set(carIndex, editedCar);
                            }
                        } else if (commands[0].equals("SearchReg")) {
                            String regNo = commands[1];
                            List<Car> carList = new ArrayList<>();
                            carList = server.getCarList();
                            for (int i = 0; i < carList.size(); i++) {
                                if (carList.get(i).getRegNo().equals(regNo))
                                    networkUtil.write(carList.get(i));
                            }

                        }
                        else if (commands[0].equals("SearchMakeModel")) {
                            String carMake = commands[1];
                            String carModel = commands[2];
                            List<Car> carList = new ArrayList<>();
                            carList = server.getCarList();
                            for (int i = 0; i < carList.size(); i++) {
                                if (carList.get(i).getCarMake().equals(carMake) &&
                                        carList.get(i).getCarModel().equals(carModel))
                                    networkUtil.write(carList.get(i));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                networkUtil.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



