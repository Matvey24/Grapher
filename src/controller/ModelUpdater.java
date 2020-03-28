package controller;

import model.Language;
import model.help.FullModel;
import view.MainPanel;
import view.elements.CalculatorView;
import view.elements.ElementsList;
import view.elements.FunctionsView;
import view.elements.TextElement;
import view.grapher.CoordinateSystem;
import view.grapher.graphics.Function;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Implicit;
import view.grapher.graphics.Parametric;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static java.awt.Color.*;

public class ModelUpdater {
    private static final List<Color> colors = Arrays.asList(BLUE, RED, GREEN, CYAN, magenta, GRAY, ORANGE, PINK, YELLOW, LIGHT_GRAY, BLACK);
    private static final List<String> func_names = Arrays.asList("f", "i", "j", "l", "m", "n", "o", "q", "r", "s", "bl");
    private static final double deltaScale = 1.2;
    private Calculator calculator;
    private final SupportFrameManager supportFrameManager;
    private final MainPanel mainPanel;
    private final DataBase dataBase;
    List<Graphic> graphics;
    ElementsList list;

    private double offsetX = -3;
    private double offsetY = 5.5;
    private double scaleX = 100;
    private double scaleY = 100;
    boolean dangerState = false;

    public ModelUpdater(Runnable repaint, MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        supportFrameManager = new SupportFrameManager(this);
        this.calculator = new Calculator(this, repaint);
        dataBase = new DataBase();
        new Thread(() -> {
            VersionController.UpdateInfo info = VersionController.checkUpdates();
            if (info.version_is_new) {
                supportFrameManager.openUpdaterFrame(info);
            }
        }).start();
    }

    public void addVRemove(ActionEvent e) {
        if (e.getActionCommand().equals("remove")) {
            supportFrameManager.close();
            remove(e);
        } else if (e.getActionCommand().equals("add")) {
            add(e);
        } else {
            System.out.println("error " + e.getActionCommand());
        }
        mainPanel.setGraphicsHeight();
    }

    private void add(ActionEvent e) {
        TextElement element = list.getElements().get(e.getID());
        Graphic graphic = new Function();
        graphics.add(graphic);
        element.addTextChangedListener((e1) -> calculator.recalculate());
        int id = findFreeId();
        graphic.setColor(colors.get(id));
        element.setColor(colors.get(id));
        element.setName(func_names.get(id) + "(x)");
        graphic.name = func_names.get(id);
        calculator.recalculate();
    }

    void add(String func, String params) {
        String[] arr = params.split("\n");
        String name = arr[0];
        int map_size = Integer.parseInt(arr[1]);
        String type = arr[2];
        Graphic gr;
        switch (type) {
            case "Function":
                gr = new Function(map_size);
                break;
            case "Parametric":
                gr = new Parametric(map_size);
                break;
            case "Implicit":
                gr = new Implicit(mainPanel, map_size);
                break;
            default:
                return;
        }
        int id = func_names.indexOf(name);
        gr.setColor(colors.get(id));
        graphics.add(gr);
        gr.name = name;
        list.addElement();
        TextElement e = list.getElements().get(list.getElements().size() - 1);
        e.setColor(colors.get(id));
        e.addTextChangedListener((e1) -> calculator.recalculate());
        e.setText(func);
        switch (type) {
            case "Function":
                e.setName(name + "(x)");
                break;
            case "Parametric":
                e.setName("xy(t)");
                String startEnd = arr[3];
                String[] st = startEnd.split(":");
                ((Parametric) gr).updateBoards(Double.parseDouble(st[0]), Double.parseDouble(st[1]));
                break;
            case "Implicit":
                e.setName(name + "(xy)");
                ((Implicit) gr).setSensitivity(Double.parseDouble(arr[3]));
                if(arr.length > 4)
                    ((Implicit) gr).setViewType(Integer.parseInt(arr[4]));
                break;
        }
    }

    private void remove(ActionEvent e) {
        graphics.remove(e.getID());
        calculator.recalculate();
    }

    public void startSettings(int id) {
        Graphic g = graphics.get(id);
        if (g instanceof Function) {
            supportFrameManager.openFunctionSettings((Function) g, list.getElements().get(id));
        } else if (g instanceof Parametric) {
            supportFrameManager.openParameterSettings((Parametric) g, list.getElements().get(id));
        } else if (g instanceof Implicit) {
            supportFrameManager.openImplicitSettings((Implicit) g, list.getElements().get(id));
        }
    }

    public void makeFunctionGUI(Graphic g, TextElement e) {
        if (g instanceof Function)
            return;
        int idx = graphics.indexOf(g);
        makeFunction(idx, e);
        calculator.recalculate();
        startSettings(idx);
    }
    public void makeParametricGUI(Graphic g, TextElement e) {
        if (g instanceof Parametric)
            return;
        int idx = graphics.indexOf(g);
        makeParametric(idx, e);
        calculator.recalculate();
        startSettings(idx);
    }
    public void makeImplicitGUI(Graphic g, TextElement e) {
        if (g instanceof Implicit)
            return;
        int idx = graphics.indexOf(g);
        makeImplicit(idx, e);
        calculator.recalculate();
        startSettings(idx);
    }

    public void makeFunction(int idx, TextElement e){
        Function function = new Function();
        function.setColor(e.getColor());
        graphics.set(idx, function);
        int id = colors.indexOf(e.getColor());
        e.setName(func_names.get(id) + "(x)");
        function.name = func_names.get(id);
    }
    public void makeParametric(int idx, TextElement e){
        Parametric parametric = new Parametric();
        parametric.setColor(e.getColor());
        graphics.set(idx, parametric);
        e.setName("xy(t)");
        parametric.name = func_names.get(colors.indexOf(e.getColor()));
    }
    public void makeImplicit(int idx, TextElement e){
        Implicit implicit = new Implicit(mainPanel);
        implicit.setColor(e.getColor());
        graphics.set(idx, implicit);
        int id = colors.indexOf(e.getColor());
        e.setName(func_names.get(id) + "(xy)");
        implicit.name = func_names.get(id);
    }
    private int findFreeId() {
        for (int i = 0; i < colors.size() - 1; ++i) {
            Color c = colors.get(i);
            boolean hasColor = false;
            for (TextElement element : list.getElements()) {
                if (element.getColor() == c) {
                    hasColor = true;
                    break;
                }
            }
            if (!hasColor)
                return i;
        }
        return colors.size() - 1;
    }

    public void translate(int dScreenX, int dScreenY) {
        if (dangerState)
            return;
        double dOffsetX = dScreenX / scaleX;
        double dOffsetY = dScreenY / scaleY;
        offsetX -= dOffsetX;
        offsetY += dOffsetY;
        calculator.runResize();
    }

    public void rescale(double delta, int x, int y, int line) {
        if (dangerState)
            return;
        double deltaX = x / scaleX;
        double deltaY = y / scaleY;
        if (line == 0) {
            scaleX /= Math.pow(deltaScale, delta);
            scaleY /= Math.pow(deltaScale, delta);
            offsetX += -x / scaleX + deltaX;
            offsetY += y / scaleY - deltaY;
        } else if (line == 1) {
            scaleX /= Math.pow(deltaScale, delta);
            offsetX += -x / scaleX + deltaX;
        } else if (line == 2) {
            scaleY /= Math.pow(deltaScale, delta);
            offsetY += y / scaleY - deltaY;
        }
        calculator.runResize();
    }

    public void rescaleBack() {
        if (dangerState)
            return;
        double yc = offsetY * scaleY;
        scaleY = 1 * scaleX;
        offsetY = yc / scaleY;

        calculator.runResize();
    }

    public void runResize() {
        calculator.runResize();
    }
    public void runRepaint(){calculator.runRepaint();}
    public void openTimer() {
        supportFrameManager.openTimerSettings();
    }

    public void frameResize() {
        calculator.frameResize();
    }

    public void recalculate() {
        calculator.recalculate();
    }

    public void setStringElements(FunctionsView functions, CalculatorView calculator) {
        this.calculator.setElements(calculator, functions);
    }

    public void setState(String text) {
        list.setState(text);
    }

    public void setResize(Runnable resize) {
        calculator.setResize(resize);
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setList(ElementsList list) {
        this.list = list;
    }

    public void setGraphics(ArrayList<Graphic> graphics) {
        this.graphics = graphics;
    }

    public SupportFrameManager getSupportFrameManager() {
        return supportFrameManager;
    }

    public void setTime(double time) {
        calculator.resetConstant("tm", time);
    }
    public CoordinateSystem getCoordinateSystem(){
        return mainPanel.getCoordinateSystem();
    }
    public void error(String message) {
        dangerState = true;
        setState(message);
    }

    public void updateLanguage() {
        mainPanel.updateLanguage();
        supportFrameManager.updateLanguage();
    }

    public void dosave(boolean selection, java.io.File f) {
        if (selection) {
            calculator.run(() -> {
                FullModel m = new FullModel();
                calculator.makeModel(m);
                m.view_params = offsetX + "\n" + offsetY + "\n" + scaleX + "\n" + scaleY;
                mainPanel.makeModel(m);
                supportFrameManager.getTimer().makeModel(m);
                setState(dataBase.save(m, f));
            });
        } else {
            calculator.run(() -> {
                try {
                    FullModel m = dataBase.load(f);
                    if (m.graphics.size() != 0) {
                        list.clear();
                    }
                    calculator.fromModel(m);
                    String[] view_params = m.view_params.split("\n");
                    offsetX = Double.parseDouble(view_params[0]);
                    offsetY = Double.parseDouble(view_params[1]);
                    scaleX = Double.parseDouble(view_params[2]);
                    scaleY = Double.parseDouble(view_params[3]);
                    mainPanel.fromModel(m);
                    supportFrameManager.getTimer().fromModel(m);
                    if (m.language != null && Language.language_Names.contains(m.language) && Language.language_Names.indexOf(m.language) != Language.LANGUAGE_INDEX) {
                        supportFrameManager.getMainSettings().setLanguage(Language.language_Names.indexOf(m.language));
                    }
                    supportFrameManager.close();
                    mainPanel.setGraphicsHeight();
                    calculator.recalculate();
                    calculator.run(() -> setState(f.getName() + " " + Language.LOADED));
                } catch (Exception e) {
                    setState(e.toString());
                }
            });
        }
    }
}
