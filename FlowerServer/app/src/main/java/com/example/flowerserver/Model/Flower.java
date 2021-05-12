package com.example.flowerserver.Model;

public class Flower {

    private String detail, imgUrl, name, type;
    private Long id,heart,like,price,star,idCategory;


    public Flower(String detail, String imgUrl, String name, String type, Long price, Long idCategory) {
        this.detail = detail;
        this.imgUrl = imgUrl;
        this.name = name;
        this.type = type;
        this.price = price;
        this.idCategory = idCategory;
    }

    public Flower() {
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHeart() {
        return heart;
    }

    public void setHeart(Long heart) {
        this.heart = heart;
    }

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getStar() {
        return star;
    }

    public void setStar(Long star) {
        this.star = star;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }
}

