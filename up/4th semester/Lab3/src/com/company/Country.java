package com.company;

import javax.swing.*;

public class Country {

    private String name;
    private ImageIcon flag;

    public Country () {
        this.name = "";
        this.flag = null;
    }

    public Country (String name, ImageIcon flag) {
        this.name = name;
        this.flag = flag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlag(ImageIcon flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return name;
    }

}
