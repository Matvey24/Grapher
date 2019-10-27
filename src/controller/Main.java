package controller;

import framesLib.MyFrame;
import view.MainPanel;

public class Main {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        frame.changeScreen(new MainPanel());
    }
}
