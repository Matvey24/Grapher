package framesLib;

import javax.swing.*;

public abstract class Screen extends JPanel {
    private Screenable screenable;
    public final void show(Screenable screenable){
        this.screenable = screenable;
        onShow();
        repaint();
    }
    protected final void changeScreen(Screen screen){
        screenable.changeScreen(screen);
    }
    public final void back(){
        screenable.back();
    }
    public abstract void onSetSize();
    public void onShow(){
        setTitle("");
    }
    @SuppressWarnings("EmptyMethod")
    public void onHide(){}
    @SuppressWarnings("EmptyMethod")
    public void onDestroy(){}

    protected final void setTitle(String title){
        screenable.setTitle(title);
    }
    protected void resize(){
        screenable.resize(this);
    }
}
