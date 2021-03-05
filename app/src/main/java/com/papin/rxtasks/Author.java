package com.papin.rxtasks;

class Author {
    private int karma;
    private String name;

    public Author(int karma, String name) {
        this.karma = karma;
        this.name = name;
    }

    public Author() {
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
