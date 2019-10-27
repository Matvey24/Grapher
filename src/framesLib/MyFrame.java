package framesLib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

public class MyFrame extends JFrame {
    private Stack<Screen> backStack;
    public MyFrame() {
        backStack = new Stack<>();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHideScreen();
                while (!backStack.empty())
                    backStack.pop().onDestroy();
                System.exit(0);
            }
        });

    }
    public void changeScreen(Screen screen){
        onHideScreen();
        backStack.push(screen);
        setScreen(screen);
    }
    void back(){
        onHideScreen();
        backStack.pop().onDestroy();
        setScreen(backStack.peek());
    }
    private void setScreen(Screen screen){
        getContentPane().removeAll();
        resize(screen);
        setLocationRelativeTo(null);
        getContentPane().add(screen);
        screen.grabFocus();
        screen.show(this);
    }
    void resize(Screen screen){
        screen.onSetSize();
        Dimension d = screen.getSize();
        setSize(d.width, d.height + 1);
        setSize(d);
    }
    private void onHideScreen(){
        if(!backStack.empty()){
            backStack.peek().onHide();
        }
    }
}
