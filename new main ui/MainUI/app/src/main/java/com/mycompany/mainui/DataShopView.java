package com.mycompany.mainui;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Mr.T on 4/9/2016.
 */
public class DataShopView {
    private String name;
    private String address;
    private String icon;
    private String distance;
    DataShopView(){
        name = "";
        address = "";
        icon = "";
        distance = "";
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public static ArrayList<DataShopView> createData(){
        ArrayList<DataShopView> data = new ArrayList<>();
        // read image from File
            String absolutePathSDCard = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
            String ImagePath = absolutePathSDCard + "/Example/image";
            File imageFolder = new File(ImagePath);
            File[] images = imageFolder.listFiles();


        for(int i = 0; i < 5; i++){
            DataShopView shop = new DataShopView();
            shop.setName("Say oh year" + i);
            shop.setAddress("Đại học bách khoa Hà Nội");
            shop.setDistance("Khoảng 3km");
            shop.setIcon(images[i].getAbsolutePath());
            data.add(shop);
        }
        return data;
    }
}
