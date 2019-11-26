package framesLib;

import javax.swing.*;
import java.awt.*;

public abstract class Screen extends JPanel {
    private MyFrame myFrame;
    public final void show(MyFrame myFrame){
        this.myFrame = myFrame;
        onShow();
    }
    protected final void changeScreen(Screen screen){
        myFrame.changeScreen(screen);
    }
    protected final void back(){
        myFrame.back();
    }
    public abstract void onSetSize();
    public void onShow(){}
    public void onHide(){}
    public void onDestroy(){}
    protected final void setTitle(String title){
        myFrame.setTitle(title);
    }
    protected void resize(){
        myFrame.resize(this);
    }
}
