package com.example.projet_java;

public class livreurbd {

    private Integer id_admin;
    private String nom;
    private String prenom;
    private Integer tele;

    public livreurbd(Integer id_admin, String nom, String prenom, Integer tele ){
        this.id_admin = id_admin;
        this.nom = nom;
        this.prenom = prenom;
        this.tele = tele;
    }
    public Integer getId_admin(){
        return id_admin;
    }
    public String getNom(){
        return nom;
    }
    public String getPrenom(){
        return prenom;
    }
    public Integer getTele(){
        return tele;
    }

}