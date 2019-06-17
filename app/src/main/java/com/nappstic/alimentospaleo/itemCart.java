package com.nappstic.alimentospaleo;

import java.io.Serializable;

/**
 * Created by uri on 28/3/18.
 */

public class itemCart extends Food implements Serializable {
    public String quantity;
    private String mesurementunit;
    private Boolean bought;


    public itemCart(){}

    itemCart(Food food, String quantity, String mesurementunit, Boolean bought) {
        super(food.getNombre_en(), food.getNombre_es(), food.getNombre_ca(), food.getDescripcio_en(), food.getDescripcio_es(), food.getDescripcio_ca(), food.getCategoria_en(), food.getCategoria_es(), food.getCategoria_ca(), food.getImg(), food.get_id(),food.getImg_firebase());
        this.quantity = quantity;
        this.mesurementunit = mesurementunit;
        this.bought = bought;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMesurementunit() {
        return mesurementunit;
    }

    public void setMesurementunit(String mesurementunit) {
        this.mesurementunit = mesurementunit;
    }

    public Boolean getBought() {
        return bought;
    }

    public void setBought(Boolean bought) {
        this.bought = bought;
    }

    public void changeBought(){
        if (getBought()){
            setBought(false);
        }else{
            setBought(true);
        }
    }
}
