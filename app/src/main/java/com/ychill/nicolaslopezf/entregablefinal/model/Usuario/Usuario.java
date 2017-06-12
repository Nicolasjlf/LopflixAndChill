package com.ychill.nicolaslopezf.entregablefinal.model.Usuario;

import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class Usuario {

    private String id;
    private String foto;
    private String nombre;
    private String email;
    private String edad;
    private String descripcion;
    private Double longitudeCoordinate;
    private Double latitudeCoordinate;

//    private List<Like> likes;

//    private ArrayList<String> amigos;
//    private ArrayList<String> watchlist;


    public Usuario() {
    }

    public Usuario(String id, String foto, String nombre, String email) {
        this.id = id;
        this.foto = foto;
        this.nombre = nombre;
        this.email = email;
    }

    public Usuario(String id, String foto, String nombre, String email, String edad, String descripcion) {
        this.id = id;
        this.foto = foto;
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
        this.descripcion = descripcion;
    }

    public Usuario(String email, String id) {
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public void setLongitudeCoordinate(Double longitudeCoordinate) {
        this.longitudeCoordinate = longitudeCoordinate;
    }

    public void setLatitudeCoordinate(Double latitudeCoordinate) {
        this.latitudeCoordinate = latitudeCoordinate;
    }

    public Double getLongitudeCoordinate() {

        return longitudeCoordinate;
    }

//    public List<Like> getLikes() {
//        return likes;
//    }
//
//    public void setLikes(List<Like> likes) {
//        this.likes = likes;
//    }

    public Double getLatitudeCoordinate() {
        return latitudeCoordinate;
    }
}