package warehouse;

import javafx.application.Platform;
import util.LoginInfo;

import java.io.IOException;

public class ReadThread implements Runnable {
    private Thread thr;
    private Main main;

    public ReadThread(Main main) {
        this.main = main;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = main.getNetworkUtil().read();

                if (o != null) {
                    if (o instanceof LoginInfo) {
                        LoginInfo loginInfo = (LoginInfo) o;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (loginInfo.isStatus() == 1) {
                                    try {
                                        main.showManuHomePage();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (loginInfo.isStatus() == 2) {
                                    try {
                                        main.showViewerHomePage();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    main.showAlert();
                                }
                            }
                        });
                    }else if( o instanceof Car ){
                        main.addNewCar((Car) o);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                main.getNetworkUtil().closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



