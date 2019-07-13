package com.gkt.browse.dictionary;

public class DataGenerater {

private String definition;
private String domain;
private String example;
private String etymology;
private String text;
private String type;


    public DataGenerater(String definition, String domain, String example,String etymology,String text,String type) {
        this.definition = definition;
        this.domain = domain;
        this.example = example;
        this.etymology = etymology;
        this.text = text;
        this.type = type;
    }


    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getEtymology() {
        return etymology;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
