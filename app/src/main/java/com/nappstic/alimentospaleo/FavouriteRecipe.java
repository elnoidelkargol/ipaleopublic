package com.nappstic.alimentospaleo;

import android.content.res.Resources;

import java.io.Serializable;

public class FavouriteRecipe implements Serializable{

    private String recipeId;
    private String nombre_ca;
    private String nombre_es;
    private String nombre_en;
    private String recipePersons;
    private String recipeTime;
    private String recipeImage;




    public FavouriteRecipe(String recipeId, String nombre_ca, String nombre_es, String nombre_en, String recipePersons, String recipeTime, String recipeImage) {
        this.recipeId = recipeId;
        this.nombre_ca = nombre_ca;
        this.nombre_es = nombre_es;
        this.nombre_en = nombre_en;
        this.recipePersons = recipePersons;
        this.recipeTime = recipeTime;
        this.recipeImage = recipeImage;
    }

    public String getNombre_ca() {
        return nombre_ca;
    }

    public void setNombre_ca(String nombre_ca) {
        this.nombre_ca = nombre_ca;
    }

    public String getNombre_es() {
        return nombre_es;
    }

    public void setNombre_es(String nombre_es) {
        this.nombre_es = nombre_es;
    }

    public String getNombre_en() {
        return nombre_en;
    }

    public void setNombre_en(String nombre_en) {
        this.nombre_en = nombre_en;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }


    public String getRecipePersons() {
        return recipePersons;
    }

    public void setRecipePersons(String recipePersons) {
        this.recipePersons = recipePersons;
    }

    public String getRecipeTime() {
        return recipeTime;
    }

    public void setRecipeTime(String recipeTime) {
        this.recipeTime = recipeTime;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String obtainCorrectName(){
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
}
