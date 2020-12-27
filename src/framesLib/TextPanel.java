package framesLib;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TextPanel extends Screen {
    private int height = 500;
    private int width = 400;
    private final JPanel internal;
    private String title;
    private final JButton btn_back;
    private final JScrollPane scrollPane;
    private final JLabel[] labels;
    private final JTextArea[] areas;

    public TextPanel(String[][] text, String title, String back_name) {
        setLayout(null);
        this.title = title;
        internal = new JPanel();
        internal.setLayout(null);

        btn_back = new JButton(back_name);
        add(btn_back);
        btn_back.addActionListener((e) -> back());
        labels = new JLabel[text.length];
        areas = new JTextArea[text.length];
        scrollPane = new JScrollPane(internal);
        add(scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        makeText(text);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = getWidth();
                height = getHeight();
                setBounds();
            }
        });
    }

    private void makeText(String[][] text) {
        for (int i = 0; i < text.length; ++i) {
            String[] strings = text[i];
            if (strings == null)
                break;
            makeTheme(strings, i);
        }
    }

    private void makeTheme(String[] text, int idx) {
        JLabel main = new JLabel(text[0]);
        main.setFont(new Font("arial", Font.PLAIN, 20));
        internal.add(main);
        labels[idx] = main;
        JTextArea area = new JTextArea();
        areas[idx] = area;
        area.setFont(area.getFont().deriveFont(Font.BOLD));
        area.setText("");
        for (int i = 1; i < text.length; ++i) {
            area.append(text[i]);
            if (i != text.length - 1)
                area.append("\n");
        }
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBackground(getBackground());
        internal.add(area);
    }
    private void setBounds(){
        int offset = 10;
        for(int i = 0; i < labels.length; ++i){
            JLabel name = labels[i];
            JTextArea area = areas[i];
            name.setBounds(10, offset, width - 20, 25);
            offset += 20;
            area.setBounds(30, offset, width - 60, 50);
            area.setSize(area.getPreferredSize());
            offset += area.getHeight() + 5;
        }
        internal.setBounds(0, 0, width - 20, offset);
        internal.setPreferredSize(internal.getSize());
        scrollPane.setBounds(0, 0, width, height - 30);
        btn_back.setBounds(20, height - 25, 100, 20);
    }
    @Override
    public void onShow() {
        setTitle(title);
        internal.updateUI();
    }

    @Override
    public void onSetSize() {
        setSize(width, height);
    }

    public void updateLanguage(String[][] text, String title, String back) {
        this.title = title;
        btn_back.setText(back);
        internal.removeAll();
        makeText(text);
    }
}
