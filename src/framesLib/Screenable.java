package framesLib;

public interface Screenable {
    void changeScreen(Screen screen);
    void back();
    void setTitle(String s);
    boolean resize(Screen s);
    void clearStack();
    void setMain(Screenable s);
    Screen getScreen();
}
