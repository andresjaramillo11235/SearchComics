package com.andresjaramillo.searchcomics;

/**
 * Comic list interface.
 * @author Andres Jaramillo
 * @version 0.1
 */
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
