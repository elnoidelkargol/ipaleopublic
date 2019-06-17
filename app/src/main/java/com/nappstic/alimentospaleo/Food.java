package com.nappstic.alimentospaleo;

import android.content.res.Resources;

import java.io.Serializable;

/**
 * Created by uri on 29/10/17.
 */

public class Food implements Serializable{
    public String nombre_en;
    public String nombre_es;
    public String nombre_ca;
    public String descripcio_en;
    public String descripcio_es;
    public String descripcio_ca;
    public String categoria_en;
    public String categoria_es;
    public String categoria_ca;
    public String img;
    public String _id;
    public String img_firebase;

    public Food(){}

    public Food(String nombre_en, String nombre_es, String nombre_ca, String descripcio_en, String descripcio_es, String descripcio_ca, String categoria_en, String categoria_es, String categoria_ca, String img, String _id, String img_firebase) {
        this.nombre_en = nombre_en;
        this.nombre_es = nombre_es;
        this.nombre_ca = nombre_ca;
        this.descripcio_en = descripcio_en;
        this.descripcio_es = descripcio_es;
        this.descripcio_ca = descripcio_ca;
        this.categoria_en = categoria_en;
        this.categoria_es = categoria_es;
        this.categoria_ca = categoria_ca;
        this.img = img;
        this.img_firebase = img_firebase;
        this._id = _id;
    }

    public String getImg_firebase() {
        return img_firebase;
    }

    public void setImg_firebase(String img_firebase) {
        this.img_firebase = img_firebase;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombre_en() {
        return nombre_en;
    }

    public void setNombre_en(String nombre_en) {
        this.nombre_en = nombre_en;
    }

    public String getNombre_es() {
        return nombre_es;
    }

    public void setNombre_es(String nombre_es) {
        this.nombre_es = nombre_es;
    }

    public String getNombre_ca() {
        return nombre_ca;
    }

    public void setNombre_ca(String nombre_ca) {
        this.nombre_ca = nombre_ca;
    }

    public String getDescripcio_en() {
        return descripcio_en;
    }

    public void setDescripcio_en(String descripcio_en) {
        this.descripcio_en = descripcio_en;
    }

    public String getDescripcio_es() {
        return descripcio_es;
    }

    public void setDescripcio_es(String descripcio_es) {
        this.descripcio_es = descripcio_es;
    }

    public String getDescripcio_ca() {
        return descripcio_ca;
    }

    public void setDescripcio_ca(String descripcio_ca) {
        this.descripcio_ca = descripcio_ca;
    }

    public String getCategoria_en() {
        return categoria_en;
    }

    public void setCategoria_en(String categoria_en) {
        this.categoria_en = categoria_en;
    }

    public String getCategoria_es() {
        return categoria_es;
    }

    public void setCategoria_es(String categoria_es) {
        this.categoria_es = categoria_es;
    }

    public String getCategoria_ca() {
        return categoria_ca;
    }

    public void setCategoria_ca(String categoria_ca) {
        this.categoria_ca = categoria_ca;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String obtainCorrectLanguageName(){
        String name;
        String lang;
        lang = Resources.getSystem().getConfiguration().locale.getLanguage();

        switch (lang){
            case "es":
                 name = getNombre_es();
                break;
            case "ca":
                name  = getNombre_ca();
                break;
            case "en":
                name = getNombre_en();
                break;
            default:
                name = getNombre_es();
        }
        return name;
    }
    public String obtainCorrectLanguageCategory(){
        String category;
        String lang;
        lang = Resources.getSystem().getConfiguration().locale.getLanguage();

        switch (lang){
            case "es":
                category = getCategoria_es();
                break;
            case "ca":
                category  = getCategoria_ca();
                break;
            case "en":
                category = getCategoria_en();
                break;
            default:
                category = getCategoria_es();
        }
        return category;
    }
    public String obtainCorrectLanguageDescription(){
        String description;
        String lang;
        lang = Resources.getSystem().getConfiguration().locale.getLanguage();

        switch (lang){
            case "es":
                description = getDescripcio_es();
                break;
            case "ca":
                description  = getDescripcio_ca();
                break;
            case "en":
                description = getDescripcio_en();
                break;
            default:
                description = getDescripcio_es();
        }
        return description;
    }
}
