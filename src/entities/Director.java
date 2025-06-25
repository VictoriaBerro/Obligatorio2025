package entities;

import TADS.list.linked.MyLinkedListImpl;

public class Director {
    int idMiembro;
    MyLinkedListImpl<String> peliculasId;

    public Director(int idMiembro) {
        this.idMiembro = idMiembro;
        this.peliculasId = new MyLinkedListImpl<String>();
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
