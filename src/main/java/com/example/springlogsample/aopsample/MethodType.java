package com.example.springlogsample.aopsample;

public enum MethodType {

    OTHER("OTHER"),
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    ;

    private String content;
    private MethodType(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
