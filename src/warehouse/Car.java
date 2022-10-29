package warehouse;

import java.io.Serializable;

public class Car implements Serializable {
    private String regNo, color1, color2, color3, carMake, carModel;
    private int yearMade, price, quantity;

    public String getRegNo() {
        return regNo;
    }

    public String getColor1() {
        return color1;
    }

    public String getColor2() {
        return color2;
    }

    public String getColor3() {
        return color3;
    }

    public String getCarMake() {
        return carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getYearMade() {
        return yearMade;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Car() {
    }

    public Car(String regNo, int yearMade, String color1, String color2, String color3, String carMake, String carModel, int price, int quantity) {
        this.regNo = regNo;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.carMake = carMake;
        this.carModel = carModel;
        this.yearMade = yearMade;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return regNo + "," + yearMade + "," + color1 + "," + color2 + "," + color3
                + "," + carMake + "," + carModel + "," + price + "," + quantity;
    }

}
