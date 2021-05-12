package com.example.flowerserver.Model;

import java.util.List;

public class Request {
    private String name;
    private String phone;
    private String address;
    private String total;
    private String status;
    private List<Order> flowers;



    public Request(){}

    public Request(String name, String phone, String address, String total, List<Order> flowers) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.flowers = flowers;
        this.status = "0";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFlowers() {
        return flowers;
    }

    public void setFlowers(List<Order> flowers) {
        this.flowers = flowers;
    }
}