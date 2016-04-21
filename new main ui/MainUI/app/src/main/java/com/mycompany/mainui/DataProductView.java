package com.mycompany.mainui;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Mr.T on 4/10/2016.
 */
public class DataProductView {
    String number_view;
    String address;
    String name;
    String image;
    String price;
    String sale;
    String oldPrice;

    DataProductView(int oldPrice, int sale, String image, String name, String address
            , String number_view){
        this.oldPrice = oldPrice + " đ";
        this.sale = "-" + sale + "%";
        int temp = (oldPrice*(100 - sale))/100;
        this.price = temp + " đ";
        this.image = image;
        this.name = name;
        this.address = address;
        this.number_view = number_view;

    }

    public String getNumber_view() {
        return number_view;
    }

    public void setNumber_view(String number_view) {
        this.number_view = number_view;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public static ArrayList<DataProductView> createDataProduct(){
        ArrayList<DataProductView> data = new ArrayList<>();
        // read image from File
        String absolutePathSDCard = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        String ImagePath = absolutePathSDCard + "/Example/image2";
        File imageFolder = new File(ImagePath);
        File[] images = imageFolder.listFiles();
        for (int i = 0; i < 5; i++){
            data.add(new DataProductView(1000000, 30,images[i].getAbsolutePath(),"say oh year",
                    "Back Khoa Ha Noi","Hơn 20.000 lượt xem tuần qua"));
        }
        return data;
    }
}
