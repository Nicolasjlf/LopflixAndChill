package com.ychill.nicolaslopezf.entregablefinal.model.Usuario;

/**
 * Created by Nicolas Lopez F on 12/6/2016.
 */

public class Like {

    private String id;
    private Boolean meGusta;

    public Like() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getMeGusta() {
        return meGusta;
    }

    public void setMeGusta(Boolean meGusta) {
        this.meGusta = meGusta;
    }
}
