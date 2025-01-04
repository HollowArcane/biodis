package com.toolkit.spring.model.stock;

import java.util.Arrays;
import java.util.List;


public enum Action
{
    WITHDRAW(0, "Sortie"),
    ENTRY(1, "Entrée");

    private int id;
    private String label;
    private Action(int id, String label)
    {
        this.id = id;
        this.label = label;
    }

    public static List<Action> all()
    { return Arrays.asList(ENTRY, WITHDRAW); }

    public static Action from(String action)
    {
        return switch (action.toLowerCase())
        {
            case "0" -> WITHDRAW;
            default -> ENTRY;
        };
    }

    public int getId()
    {
        return id;
    }

    public String getLabel()
    {
        return label;
    }
}