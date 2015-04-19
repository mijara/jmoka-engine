package com.moka.ui;

import javax.swing.*;

public class Container extends JSplitPane
{
    private Hierarchy hierarchy;

    public Container()
    {
        super(JSplitPane.HORIZONTAL_SPLIT);

        hierarchy = Hierarchy.getInstance();
        JScrollPane scrollPane = new JScrollPane(hierarchy);
        setLeftComponent(scrollPane);

        ComponentContainer component = ComponentContainer.getInstance();
        JScrollPane scrollPane1 = new JScrollPane(component);
        setRightComponent(scrollPane1);

        setOneTouchExpandable(true);
        setDividerLocation(300);
    }

    public Hierarchy getHierarchy()
    {
        return hierarchy;
    }
}