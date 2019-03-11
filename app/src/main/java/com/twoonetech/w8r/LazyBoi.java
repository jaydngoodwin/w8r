package com.twoonetech.w8r;

import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.InteractiveElement;
import org.graphstream.ui.view.util.MouseManager;

import java.util.EnumSet;


// "...choose a lazy person to do a difficult job..." - Bill Gates, probably
// "This boi doesn't do anything" - me
// "He's perfect!" - Also Bill Gates, I think.
public class LazyBoi implements MouseManager {
    @Override
    public void init(GraphicGraph graphicGraph, View view) {

    }

    @Override
    public void release() {

    }

    @Override
    public EnumSet<InteractiveElement> getManagedTypes() {
        return null;
    }
}