package view.elements;

import calculator2.calculator.Parser;
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
    private static int AREA_HEIGHT;
    public static int FUNC_HEIGHT;
    private final JLabel name;
    private final JTextArea area;
    private final JButton btn_update;
    private final JScrollPane scrollPane;
    private final StringBuilder sb;
    private final ModelUpdater updater;
    private final Parser.StringToken line;

    static {
        setSizes(160);
    }

    private static void setSizes(int height) {
        AREA_HEIGHT = height;
        FUNC_HEIGHT = AREA_HEIGHT + HEIGHT + OFFSET;
    }

    public FunctionsView(Runnable onUpdate, ModelUpdater updater) {
        this.updater = updater;
        sb = new StringBuilder();
        area = new JTextArea();
        area.setLineWrap(true);
        scrollPane = new JScrollPane(area);
        name = new JLabel();
        name.setFont(name_font);
        btn_update = new JButton();
        btn_update.addActionListener(e -> onUpdate.run());
        area.setTabSize(4);
        updateLanguage();
        line = new Parser.StringToken();
        registerActions(onUpdate);
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
    private void registerActions(Runnable onUpdate) {
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
        area.registerKeyboardAction((e) -> {
                    try {
                        String text = area.getSelectedText();
                        if(text != null){
                            int pos = area.getCaretPosition();
                            area.insert(text, pos);
                            area.setCaretPosition(pos + text.length());
                            return;
                        }
                        int pos = area.getCaretPosition();
                        int line = area.getLineOfOffset(pos);
                        int start = area.getLineStartOffset(line);
                        int end = area.getLineEndOffset(line);
                        text = area.getText(start, end - start);
                        if (text.length() > 0 && text.charAt(text.length() - 1) != '\n') {
                            area.append("\n");
                            end++;
                        }
                        area.insert(text, end);
                        area.setCaretPosition(pos + end - start);
                    } catch (Exception er) {
                        updater.setState(er.toString());
                    }
                },
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_D,
                        InputEvent.CTRL_DOWN_MASK
                ),
                JComponent.WHEN_FOCUSED);
        //resizes
        area.registerKeyboardAction(
                (e) -> resize_area(AREA_HEIGHT + 20),
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_DOWN,
                        InputEvent.CTRL_DOWN_MASK
                ),
                JComponent.WHEN_FOCUSED
        );
        area.registerKeyboardAction((e) -> {
                    if (AREA_HEIGHT < 40) {
                        return;
                    }
                    resize_area(AREA_HEIGHT - 20);

                },
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_UP,
                        InputEvent.CTRL_DOWN_MASK
                ),
                JComponent.WHEN_FOCUSED
        );
        area.registerKeyboardAction((e) -> {
                    try {
                        int pos = area.getCaretPosition();
                        int lineIdx = area.getLineOfOffset(pos);
                        int start = area.getLineStartOffset(lineIdx);
                        line.text = area.getText(start, pos - start);
                        line.replace = 0;
                        updater.findEndOf(line);
                        if (line.text.isEmpty())
                            return;
                        if (line.replace == 0) {
                            area.insert(line.text, pos);
                        } else {
                            area.replaceRange(line.text, pos - line.replace, pos);
                        }
                    } catch (Exception e1) {
                        updater.setState(e1.toString());
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),
                JComponent.WHEN_FOCUSED);
    }

    private void resize_area(int new_size) {
        setSizes(new_size);
        updater.getMainPanel().setGraphicsHeight();
        area.updateUI();
    }
}
