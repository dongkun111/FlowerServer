package com.example.flowerserver.Model;

public class Category {
    private  String  id, name, imgUrl;


    public Category(){

    }

    public Category( String name, String image) {
        this.name = name;
        this.imgUrl = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String image) {
        this.imgUrl = image;
    }
}
