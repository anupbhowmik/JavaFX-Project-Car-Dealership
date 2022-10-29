package server;

import warehouse.Car;
import util.NetworkUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Server {

    private static final String FILE_NAME = "cars.txt";

    private ServerSocket serverSocket;
    public HashMap<String, String> userMap;
    private List<Car> carList;

    public List<Car> getCarList() {
        return carList;
    }

    Server() {

        carList = new ArrayList<>();
        loadFromFile(carList);

        userMap = new HashMap<>();
        userMap.put("a", "a");
        userMap.put("admin", "admin");
        userMap.put("viewer", "");

        try {
            serverSocket = new ServerSocket(33333);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile(List<Car> carList) {
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            while (true) {
                line = br.readLine();
                if (line == null) break;

                String[] s = line.split(",");
                int year = Integer.parseInt(s[1]);
                int price = Integer.parseInt(s[7]);
                int quantity = Integer.parseInt(s[8]);
                carList.add(new Car(s[0], year, s[2], s[3], s[4], s[5], s[6], price, quantity));
                System.out.println("Loading program from " + FILE_NAME + "...");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serve(Socket clientSocket) throws IOException {
        NetworkUtil networkUtil = new NetworkUtil(clientSocket);
        new ReadThreadServer(userMap, networkUtil, this);

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
      /*  new Thread(){
            @Override
            public void run() {
                String message = null;
                do{
                    message = scanner.nextLine();
                }while(!message.equals("-1"));
                //System.out.println("write to file here");
            }
        }.start();*/
        new Server();

    }
}
