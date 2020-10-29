package framesLib.screenables;

import framesLib.Screen;
import framesLib.Screenable;

import java.util.Stack;

public class PanelFrameOperator implements Screenable {
    private MyFrame frame;
    private final InternalPanel panel;
    private final Stack<Boolean> who_own;

    public PanelFrameOperator(InternalPanel panel) {
        this.panel = panel;
        who_own = new Stack<>();
        panel.setMain(this);
    }

    public synchronized void changeScreenClearing(Screen screen, boolean clear) {
        if(getScreen() == screen)
            return;
        if (panel.resize(screen)) {
            if (clear) {
                clearStack();
            }
            who_own.push(true);
            panel.changeScreen(screen);
        } else {
            checkFrame();
            who_own.push(false);
            if (clear) {
                panel.clearStack();
                frame.changeScreenClearing(screen);
            } else {
                frame.changeScreen(screen);
            }
            frame.setFocusable(true);
            frame.setFocusableWindowState(true);
        }
    }

    @Override
    public synchronized void changeScreen(Screen screen) {
        changeScreenClearing(screen, false);
    }

    @Override
    public void back() {
        if (who_own.empty())
            return;
        if (who_own.pop()) {
            panel.back();
        } else {
            frame.back();
        }
    }

    @Override
    public void setTitle(String s) {
        if (who_own.peek()) {
            panel.setTitle(s);
        } else {
            frame.setTitle(s);
        }
    }

    @Override
    public boolean resize(Screen s) {
        if (who_own.peek()) {
            panel.resize(s);
        } else {
            frame.resize(s);
        }
        return true;
    }

    public void onPanelResize() {
        if (!panel.isValidSize()) {
            clearStack();
        }
    }

    @Override
    public void clearStack() {
        if (frame != null && frame.isVisible())
            frame.clearStack();
        panel.clearStack();
        who_own.clear();
    }

    @Override
    public Screen getScreen() {
        if (who_own.empty())
            return null;
        if (who_own.peek()) {
            return panel.getScreen();
        } else {
            return frame.getScreen();
        }
    }

    @Override
    public void setMain(Screenable s) {
    }

    private void checkFrame() {
        if (frame == null || !frame.isVisible()) {
            frame = new MyFrame(true);
            frame.setMain(this);
        }
    }
}
