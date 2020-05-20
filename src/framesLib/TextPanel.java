package framesLib;


import javax.swing.*;
import java.awt.*;

public class TextPanel extends Screen {
    private int height;
    private final int width = 400;
    private static final int MAX_HEIGHT = 500;
    private final JPanel internal;
    private String title;
    private final JButton btn_back;
    private final JScrollPane scrollPane;
    public TextPanel(String[][] text, String title, String back_name) {
        setLayout(null);
        this.title = title;
        internal = new JPanel();
        internal.setLayout(null);

        btn_back = new JButton(back_name);
        btn_back.setFocusPainted(false);
        add(btn_back);
        btn_back.addActionListener((e)-> back());

        scrollPane = new JScrollPane(internal);
        add(scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        makeText(text);
    }
    private void makeText(String[][] text){
        int offset = 10;
        for (String[] strings : text)
            offset = makeTheme(strings, offset);
        height = offset + 30;
        internal.setBounds(0,0, width - 40, height);
        internal.setPreferredSize(internal.getSize());
        scrollPane.setBounds(0,0,width - 20, Math.min(height, MAX_HEIGHT) - 30);
        btn_back.setBounds(20, Math.min(height, MAX_HEIGHT) - 20, 100,20);
    }
    private int makeTheme(String[] lines, int offsetY){
        JLabel main = new JLabel(lines[0]);
        main.setBounds(10,offsetY, width - 40,25);
        main.setFont(new Font("arial", Font.PLAIN, 20));
        internal.add(main);
        offsetY += 20;
        for(int i = 1; i < lines.length; ++i){
            JLabel label = new JLabel(lines[i]);
            label.setBounds(30, offsetY, width - 60,20);
            offsetY += 20;
            internal.add(label);
        }
        offsetY += 5;
        return offsetY;
    }

    @Override
    public void onShow() {
        setTitle(title);
    }

    @Override
    public void onSetSize() {
        int h = (int)((Math.min(height, MAX_HEIGHT) + 30) * 1.1f);
        setSize(width, h);
    }
    public void updateLanguage(String[][] text, String title, String back){
        this.title = title;
        btn_back.setText(back);
        internal.removeAll();
        makeText(text);
    }
}
