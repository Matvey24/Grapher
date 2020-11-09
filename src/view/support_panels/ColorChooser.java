package view.support_panels;

import framesLib.Screen;
import model.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static view.elements.ElementsList.OFFSET;

public class ColorChooser extends Screen {
    private final JColorChooser jcc;
    private final JButton btn_done;
    private final JButton btn_apply;
    private final JButton btn_back;
    private ActionListener onDone;
    public ColorChooser(){
        setLayout(null);
        jcc = new JColorChooser();
        add(jcc);
        jcc.setBounds(OFFSET, OFFSET, 600, 350);
        btn_apply = new JButton(Language.APPLY);
        add(btn_apply);
        btn_apply.setBounds(OFFSET + 300, 2*OFFSET + 350, 150, 20);
        btn_apply.addActionListener(this::set);
        btn_back = new JButton(Language.BACK);
        add(btn_back);
        btn_back.setBounds(OFFSET, 2*OFFSET + 350, 150, 20);
        btn_back.addActionListener((e)->back());
        btn_done = new JButton(Language.MAKE);
        add(btn_done);
        btn_done.setBounds(OFFSET + 450, 2*OFFSET + 350, 150, 20);
        btn_done.addActionListener(this::done);

    }
    public void set(Color c, ActionListener onDone){
        jcc.setColor(c);
        this.onDone = onDone;
    }
    public Color getColor(){
        return jcc.getColor();
    }
    private void done(ActionEvent e){
        set(e);
        back();
    }
    private void set(ActionEvent e){
        if(onDone != null)
            onDone.actionPerformed(e);
    }
    @Override
    public void onShow() {
        setTitle(Language.COLOR_CHOOSER);
    }
    public void updateLanguage(){
        btn_done.setText(Language.MAKE);
        btn_apply.setText(Language.APPLY);
        btn_back.setText(Language.BACK);
    }
    @Override
    public void onSetSize() {
        setSize(600 + 2 * OFFSET,370 + 3 * OFFSET);
    }
}
