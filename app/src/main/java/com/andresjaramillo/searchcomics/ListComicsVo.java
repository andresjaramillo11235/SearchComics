package com.andresjaramillo.searchcomics;

public class ListComicsVo {
    private String name;
    private String resource;

    public ListComicsVo() {
    }

    public ListComicsVo(String name, String resource) {
        this.name = name;
        this.resource = resource;

    }

    public String getName() {
        return name;
    }

    public String getResource() {
        return resource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
