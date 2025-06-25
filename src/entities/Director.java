package entities;

import TADS.list.linked.MyLinkedListImpl;

public class Director {
    int idMiembro;
    String nombre;
    MyLinkedListImpl<String> peliculasId;

    public Director(int idMiembro, String nombre) {
        this.idMiembro = idMiembro;
        this.peliculasId = new MyLinkedListImpl<String>();
        this.nombre = nombre;
    }

    public Director(int idMiembro) {
        this.idMiembro = idMiembro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdMiembro() {
        return idMiembro;
    }

    public void setIdMiembro(int idMiembro) {
        this.idMiembro = idMiembro;
    }

    public MyLinkedListImpl<String> getPeliculasId() {
        return peliculasId;
    }

    public void setPeliculasId(MyLinkedListImpl<String> peliculasId) {
        this.peliculasId = peliculasId;
    }




}
