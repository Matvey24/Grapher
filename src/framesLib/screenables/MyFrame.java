package framesLib.screenables;

import framesLib.Screen;
import framesLib.Screenable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

public class MyFrame extends JFrame implements Screenable {
    private final Stack<Screen> backStack;
    private Screenable main;

    public MyFrame() {
        this(false);
    }

    public MyFrame(boolean secondary) {
        backStack = new Stack<>();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHideScreen();
                while (!backStack.empty())
                    backStack.pop().onDestroy();
                if (!secondary)
                    System.exit(0);
            }
        });
        setMain(this);
        setLocationRelativeTo(null);
    }

    public void changeScreenClearing(Screen screen) {
        onHideScreen();
        while (!backStack.empty())
            backStack.pop().onDestroy();
        changeScreen(screen);
    }

    public void changeScreen(Screen screen) {
        onHideScreen();
        backStack.push(screen);
        setScreen(screen);
    }

    public void back() {
        onHideScreen();
        if (!backStack.empty()) {
            backStack.pop().onDestroy();
        } else {
            dispose();
            return;
        }
        if (!backStack.empty()) {
            setScreen(backStack.peek());
        } else {
            this.dispose();
        }
    }

    public Screen getScreen() {
        if (backStack.empty())
            return null;
        return backStack.peek();
    }

    private void setScreen(Screen screen) {
        getContentPane().removeAll();
        resize(screen);
        getContentPane().add(screen);
        screen.grabFocus();
        screen.show(main);
    }

    public boolean resize(Screen screen) {
        screen.onSetSize();
        Rectangle r = getBounds();
        Point p = r.getLocation();
        p.translate(r.width / 2, r.height / 2);
        Dimension d = screen.getSize();
        setSize(d.width + 15, d.height + 40);
        setLocation(r.x + (r.width - d.width - 15) / 2, r.y + (r.height - d.height - 40) / 2);
        return true;
    }

    private void onHideScreen() {
        if (!backStack.empty()) {
            backStack.peek().onHide();
        }
    }
    public void setMain(Screenable s) {
        this.main = s;
    }

    public void clearStack() {
        while (!backStack.empty())
            back();
    }
}
