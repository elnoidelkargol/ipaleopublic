package com.nappstic.alimentospaleo;

import android.content.res.Resources;

import java.io.Serializable;

public class Recipe implements Serializable {

    private String ingredientes_txt_ca;
    private String ingredientes_txt_es;
    private String ingredientes_txt_en;
    private Integer categoria_ppal;
    private Integer categoria_secun;
    private String ingredientes_obj;
    private String nombre_ca;
    private String nombre_es;
    private String nombre_en;
    private String preparacion_ca;
    private String preparacion_es;
    private String preparacion_en;
    private Integer personas;
    private Integer dificultat;
    private Integer tiempo;
    private String foto;
    private String fuente;
    private Integer id;
    private Boolean isFavourite;

    public Recipe(){}

    public Recipe(Boolean isFavourite,String ingredientes_txt_ca, String ingredientes_txt_es, String ingredientes_txt_en, Integer categoria_ppal, Integer categoria_secun, String ingredientes_obj, String nombre_ca, String nombre_es, String nombre_en, String preparacion_ca, String preparacion_es, String preparacion_en, Integer personas, Integer dificultat, Integer tiempo, String foto, String fuente, Integer id) {
        this.ingredientes_txt_ca = ingredientes_txt_ca;
        this.ingredientes_txt_es = ingredientes_txt_es;
        this.ingredientes_txt_en = ingredientes_txt_en;
        this.categoria_ppal = categoria_ppal;
        this.categoria_secun = categoria_secun;
        this.ingredientes_obj = ingredientes_obj;
        this.nombre_ca = nombre_ca;
        this.nombre_es = nombre_es;
        this.nombre_en = nombre_en;
        this.preparacion_ca = preparacion_ca;
        this.preparacion_es = preparacion_es;
        this.preparacion_en = preparacion_en;
        this.personas = personas;
        this.dificultat = dificultat;
        this.tiempo = tiempo;
        this.foto = foto;
        this.fuente = fuente;
        this.id = id;
        this.isFavourite = isFavourite;
    }


    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public String getIngredientes_txt_ca() {
        return ingredientes_txt_ca;
    }

    public void setIngredientes_txt_ca(String ingredientes_txt_ca) {
        this.ingredientes_txt_ca = ingredientes_txt_ca;
    }

    public String getIngredientes_txt_es() {
        return ingredientes_txt_es;
    }

    public void setIngredientes_txt_es(String ingredientes_txt_es) {
        this.ingredientes_txt_es = ingredientes_txt_es;
    }

    public String getIngredientes_txt_en() {
        return ingredientes_txt_en;
    }

    public void setIngredientes_txt_en(String ingredientes_txt_en) {
        this.ingredientes_txt_en = ingredientes_txt_en;
    }

    public Integer getCategoria_ppal() {
        return categoria_ppal;
    }

    public void setCategoria_ppal(Integer categoria_ppal) {
        this.categoria_ppal = categoria_ppal;
    }

    public Integer getCategoria_secun() {
        return categoria_secun;
    }

    public void setCategoria_secun(Integer categoria_secun) {
        this.categoria_secun = categoria_secun;
    }

    public String getIngredientes_obj() {
        return ingredientes_obj;
    }

    public void setIngredientes_obj(String ingredientes_obj) {
        this.ingredientes_obj = ingredientes_obj;
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

    public String getPreparacion_ca() {
        return preparacion_ca;
    }

    public void setPreparacion_ca(String preparacion_ca) {
        this.preparacion_ca = preparacion_ca;
    }

    public String getPreparacion_es() {
        return preparacion_es;
    }

    public void setPreparacion_es(String preparacion_es) {
        this.preparacion_es = preparacion_es;
    }

    public String getPreparacion_en() {
        return preparacion_en;
    }

    public void setPreparacion_en(String preparacion_en) {
        this.preparacion_en = preparacion_en;
    }

    public Integer getPersonas() {
        return personas;
    }

    public void setPersonas(Integer personas) {
        this.personas = personas;
    }

    public Integer getDificultat() {
        return dificultat;
    }

    public void setDificultat(Integer dificultat) {
        this.dificultat = dificultat;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String obtainCorrectPreparation(){
        String name;
        String lang;
        lang = Resources.getSystem().getConfiguration().locale.getLanguage();

        switch (lang){
            case "es":
                name = getPreparacion_es();
                break;
            case "ca":
                name  = getPreparacion_ca();
                break;
            case "en":
                name = getPreparacion_en();
                break;
            default:
                name = getPreparacion_es();
        }
        return name;
    }
    public String obtainCorrectIngredients(){
        String name;
        String lang;
        lang = Resources.getSystem().getConfiguration().locale.getLanguage();

        switch (lang){
            case "es":
                name = getIngredientes_txt_es();
                break;
            case "ca":
                name  = getIngredientes_txt_ca();
                break;
            case "en":
                name = getIngredientes_txt_en();
                break;
            default:
                name = getIngredientes_txt_es();
        }
        return name;
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
