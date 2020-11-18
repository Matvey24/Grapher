package view.elements;

import controller.ModelUpdater;
import model.Language;
import model.ViewElement;

import javax.swing.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static view.elements.ElementsList.OFFSET;
import static view.elements.TextElement.HEIGHT;
import static view.elements.ElementsList.WIDTH;

public class FunctionsView extends ViewElement {
    private static final int AREA_HEIGHT = 150;
    public static final int FUNC_HEIGHT = AREA_HEIGHT + HEIGHT + OFFSET;
    private final JLabel name;
    private final JTextArea area;
    private final JButton btn_update;
    private final JScrollPane scrollPane;
    private final StringBuilder sb;

    public FunctionsView(Runnable onUpdate, ModelUpdater updater) {
        sb = new StringBuilder();
        area = new JTextArea();
        area.setLineWrap(true);
        scrollPane = new JScrollPane(area);
        name = new JLabel();
        name.setFont(name_font);
        btn_update = new JButton();
        btn_update.setFocusPainted(false);
        btn_update.addActionListener(e -> onUpdate.run());
        updateLanguage();
        registerActions(onUpdate, updater);
    }

    @Override
    public void addTo(Container c) {
        c.add(scrollPane);
        c.add(name);
        c.add(btn_update);
    }

    public String getText() {
        return area.getText();
    }

    public void setText(String s) {
        area.setText(s);
    }

    public void setBounds(int x, int y) {
        name.setBounds(x + OFFSET, y, WIDTH / 2 - OFFSET, HEIGHT);
        btn_update.setBounds(x + WIDTH / 2 + OFFSET, y, WIDTH / 2 - 2 * OFFSET, HEIGHT);
        scrollPane.setBounds(x + OFFSET, y + HEIGHT + OFFSET, TextElement.WIDTH, AREA_HEIGHT);
    }

    public void updateLanguage() {
        name.setText(Language.FUNCTIONS);
        btn_update.setText(Language.UPDATE);
    }

    private void registerActions(Runnable onUpdate, ModelUpdater updater) {
        area.registerKeyboardAction(
                (e) -> onUpdate.run(),
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_ENTER,
                        InputEvent.CTRL_DOWN_MASK
                ),
                JComponent.WHEN_FOCUSED
        );
        area.registerKeyboardAction(
                (e) -> {
                    try {
                        int pos = area.getCaretPosition();
                        int line = area.getLineOfOffset(pos);
                        if (line > 0) {
                            int startA = area.getLineStartOffset(line - 1);
                            int startB = area.getLineStartOffset(line);
                            int endB = area.getLineEndOffset(line);
                            sb.setLength(0);
                            String textA = area.getText(startA, startB - startA);
                            String textB = area.getText(startB, endB - startB);
                            sb.append(textB);
                            if (sb.length() == 0 || sb.charAt(sb.length() - 1) != '\n') {
                                sb.append('\n');
                                sb.append(textA);
                                sb.setLength(sb.length() - 1);
                            } else {
                                sb.append(textA);
                            }
                            area.replaceRange(sb.toString(), startA, startA + sb.length());
                            area.setCaretPosition(pos - startB + startA);
                        }
                    } catch (Exception er) {
                        updater.setState(er.toString());
                    }
                },
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_UP,
                        InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
                ),
                JComponent.WHEN_FOCUSED
        );
        area.registerKeyboardAction(
                (e) -> {
                    try {
                        int pos = area.getCaretPosition();
                        int line = area.getLineOfOffset(pos);
                        if (line < area.getLineCount() - 1) {
                            int startB = area.getLineStartOffset(line);
                            int endB = area.getLineEndOffset(line);
                            int endA = area.getLineEndOffset(line + 1);
                            sb.setLength(0);
                            String textA = area.getText(endB, endA - endB);
                            String textB = area.getText(startB, endB - startB);
                            sb.append(textA);
                            int deltaX;
                            if (sb.length() == 0 || sb.charAt(sb.length() - 1) != '\n') {
                                sb.append('\n');
                                sb.append(textB);
                                sb.setLength(sb.length() - 1);
                                deltaX = endA - endB + 1;
                            } else {
                                sb.append(textB);
                                deltaX = endA - endB;
                            }
                            area.replaceRange(sb.toString(), startB, startB + sb.length());
                            area.setCaretPosition(pos + deltaX);
                        }
                    } catch (Exception er) {
                        updater.setState(er.toString());
                    }
                },
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_DOWN,
                        InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
                ),
                JComponent.WHEN_FOCUSED
        );
        area.registerKeyboardAction((e) -> {
                    try {
                        int pos = area.getCaretPosition();
                        int line = area.getLineOfOffset(pos);
                        int start = area.getLineStartOffset(line);
                        int end = area.getLineEndOffset(line);
                        area.replaceRange("", start, end);
                    } catch (Exception er) {
                        updater.setState(er.toString());
                    }
                },
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_DELETE,
                        InputEvent.SHIFT_DOWN_MASK
                ),
                JComponent.WHEN_FOCUSED);

    }
}
