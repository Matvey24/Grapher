package framesLib;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends Screen {
    private int height;
    public TextPanel(String[][] text) {
        setLayout(null);
        int offset = 10;
        for (String[] strings : text)
            offset = makeTheme(strings, offset);
        JButton btn_back = new JButton("Back");
        btn_back.setBounds(20, offset, 100,20);
        add(btn_back);
        btn_back.addActionListener((e)-> back());
        height = offset + 30;
        height = (int)((height + 30) * 1.1f);
    }
    private int makeTheme(String[] lines, int offsetY){
        JLabel main = new JLabel(lines[0]);
        main.setBounds(10,offsetY, 360,25);
        main.setFont(new Font("serial", Font.PLAIN, 20));
        add(main);
        offsetY += 20;
        for(int i = 1; i < lines.length; ++i){
            JLabel label = new JLabel(lines[i]);
            label.setBounds(30, offsetY, 340,20);
            offsetY += 20;
            add(label);
        }
        offsetY += 5;
        return offsetY;
    }

    @Override
    public void onShow() {
        setTitle("Help");
    }

    @Override
    public void onSetSize() {
        setSize(400,height);
    }

}
