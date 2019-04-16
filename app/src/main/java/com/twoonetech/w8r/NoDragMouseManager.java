package com.twoonetech.w8r;

import org.graphstream.ui.android_viewer.util.DefaultMouseManager;
import org.graphstream.ui.view.util.InteractiveElement;

import android.view.MotionEvent;
import java.util.EnumSet;

//The DefaultMouseManager but it doesn't allow dragging
public class NoDragMouseManager extends DefaultMouseManager {
    final private EnumSet<InteractiveElement> types;

    public NoDragMouseManager() {
        this(EnumSet.of(InteractiveElement.NODE, InteractiveElement.SPRITE));
    }

    public NoDragMouseManager(EnumSet<InteractiveElement> types) {
        this.types = types;
    }

    //The same as the defaultmousemanager onTouch, but without the parts that do the draggy selecty thing
    @Override
    public boolean onTouch(android.view.View v, MotionEvent event) {

        switch(event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                curElement = view.findGraphicElementAt(types,event.getX(), event.getY());

                if (curElement != null) {
                    mouseButtonPressOnElement(curElement, event);
                }  else {
                    mouseButtonPress(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (curElement != null) {
                    mouseButtonReleaseOffElement(curElement, event);
                    curElement = null;
                }
                break;
            default:
                break;
        }
        return true;
    }
}
