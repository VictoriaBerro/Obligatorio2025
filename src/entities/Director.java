package entities;

import TADS.list.linked.MyLinkedListImpl;

public class Director {
    String name;
    MyLinkedListImpl<String> peliculasId;

    public Director(String name) {
        this.name = name;
        this.peliculasId = new MyLinkedListImpl<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyLinkedListImpl<String> getPeliculasId() {
        return peliculasId;
    }

    public void setPeliculasId(MyLinkedListImpl<String> peliculasId) {
        this.peliculasId = peliculasId;
    }




}
