package com.example.firstlap;


public class CellItem
{
    private String text;
    private int backgroundColor;


    CellItem(String text, int backgroundColor)
    {
        this.text = text;
        this.backgroundColor = backgroundColor;
    }


    public String getText()
    {
        return text;
    }


    public int getBackgroundColor()
    {
        return backgroundColor;
    }
}
