package framesLib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

public class MyFrame extends JFrame {
    private final Stack<Screen> backStack;
    public MyFrame() {
        this(false);
    }
    public MyFrame(boolean secondary){
        backStack = new Stack<>();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHideScreen();
                while (!backStack.empty())
                    backStack.pop().onDestroy();
                if(!secondary)
                    System.exit(0);
            }
        });
        setLocationRelativeTo(null);
    }

    public void changeScreen(Screen screen){
        onHideScreen();
        backStack.push(screen);
        setScreen(screen);
    }
    void back(){
        onHideScreen();
        if(!backStack.empty()) {
            backStack.pop().onDestroy();
        }else{
            dispose();
            return;
        }
        if(!backStack.empty()){
            setScreen(backStack.peek());
        }else{
            this.dispose();
        }
    }
    private void setScreen(Screen screen){
        getContentPane().removeAll();
        resize(screen);
        getContentPane().add(screen);
        screen.grabFocus();
        screen.show(this);
    }
    void resize(Screen screen){
        screen.onSetSize();
        Rectangle r = getBounds();
        Point p = r.getLocation();
        p.translate(r.width / 2, r.height/2);
        Dimension d = screen.getSize();
        setSize(d.width, d.height + 1);
        this.update(getGraphics());
        setSize(d);
        setLocation(r.x + (r.width- d.width) / 2, r.y  + (r.height - d.height) / 2);
    }
    private void onHideScreen(){
        if(!backStack.empty()){
            backStack.peek().onHide();
        }
    }
    public void clearStack(){
        backStack.clear();
    }
}
