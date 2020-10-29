package framesLib.screenables;

import framesLib.Screen;
import framesLib.Screenable;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class InternalPanel extends JPanel implements Screenable {
    private final JLabel title;
    private final Stack<Screen> backStack;
    private Screenable main;

    public InternalPanel() {
        setLayout(null);
        title = new JLabel();
        title.setFont(new Font("arial", Font.BOLD, 20));
        add(title);
        backStack = new Stack<>();
    }

    public void onHideScreen() {
        if (!backStack.empty()) {
            backStack.peek().onHide();
            remove(backStack.peek());
        }
    }

    public boolean resize(Screen s) {
        s.onSetSize();
        return getHeight() >= s.getHeight() + 30 && getWidth() >= s.getWidth();
    }

    public boolean isValidSize() {
        if(backStack.empty())
            return true;
        Screen s = backStack.peek();
        return getHeight() >= s.getHeight() + 30 && getWidth() >= s.getWidth();
    }

    public void back() {
        if (backStack.empty()) {
            return;
        }
        onHideScreen();
        backStack.pop().onDestroy();
        if (!backStack.empty()) {
            setScreen(backStack.peek());
        } else {
            setTitle("");
        }
    }

    @Override
    public Screen getScreen() {
        if (backStack.empty())
            return null;
        return backStack.peek();
    }

    private void setScreen(Screen screen) {
        add(screen);
        screen.setLocation(0, 30);
        screen.grabFocus();
        screen.show(main);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        title.setBounds(5, 5, width - 10, 30);
        super.setBounds(x, y, width, height);
    }

    @Override
    public void changeScreen(Screen screen) {
        if (resize(screen)) {
            onHideScreen();
            backStack.push(screen);
            setScreen(screen);
        }
    }

    @Override
    public void setTitle(String s) {
        title.setText(s);
    }

    @Override
    public void setMain(Screenable s) {
        this.main = s;
    }

    public void clearStack() {
        while (!backStack.empty())
            back();
        repaint();
    }
}
