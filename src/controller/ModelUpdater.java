package controller;

import calculator2.calculator.Parser;
import model.Language;
import model.FullModel;
import view.MainPanel;
import view.elements.*;
import view.grapher.CoordinateSystem;
import view.grapher.graphics.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static java.awt.Color.*;
import static view.MainPanel.GRAPH_WIDTH;
import static view.MainPanel.HEIGHT;

public class ModelUpdater {
    private static final List<Color> colors = Arrays.asList(
            BLUE,
            RED,
            new Color(0, 0xCC, 0),
            magenta,
            new Color(0x33, 0xCC, 0xFF),
            new Color(0x99, 0x33, 0xCC),
            new Color(0x99, 0x66, 0x33),
            new Color(0, 0x99, 0x66),
            BLACK
    );
    private static final List<String> func_names = Arrays.asList("f", "g", "i", "j", "l", "m", "n", "o", "bl");
    private static final double deltaScale = 1.2;
    private final Calculator calculator;
    private final SupportFrameManager supportFrameManager;
    private final MainPanel mainPanel;
    private final DataBase dataBase;
    List<Graphic> graphics;
    public ElementsList list;
    private Graphic lOG;
    private double offsetX = -3;
    private double offsetY = 5.5;
    private double scaleX = 100;
    private double scaleY = 100;
    public boolean dangerState = false;
    public boolean mousePressed;

    public File last_used_file;

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
        int id = findFreeId();
        graphics.add(graphic);
        element.addTextChangedListener((e1) -> calculator.recalculate());
        graphic.color = colors.get(id);
        element.setColor(colors.get(id));
        setFuncName(graphic, func_names.get(id), element);
        graphic.name = func_names.get(id);
        calculator.recalculate();
    }

    void add(String func, String params) {
        String[] arr = params.split("\\n");
        String name;
        Color color = null;
        {
            String[] t = arr[0].split(" ");
            name = t[0];
            if(t.length > 1) {
                int val = Integer.parseUnsignedInt(t[1], 16);
                color = new Color(val);
            }
        }
        int map_size = Integer.parseInt(arr[1]);
        boolean feels_time = Boolean.parseBoolean(arr[2]);
        String type = arr[3];
        Graphic gr;
        switch (type) {
            case "Function":
                gr = new Function(map_size, feels_time);
                break;
            case "Parametric":
                gr = new Parametric(map_size, feels_time);
                break;
            case "Implicit":
                gr = new Implicit(mainPanel, this::isMousePressed, map_size, feels_time);
                break;
            case "Translation":
                gr = new Translation(getCoordinateSystem(), map_size, feels_time);
                break;
            default:
                return;
        }
        int id = func_names.indexOf(name);
        if(id == -1){
            id = func_names.size() - 1;
            name = func_names.get(id);
        }
        if(color == null){
            color = colors.get(id);
            gr.color = color;
        }else{
            gr.changeColor(color);
        }
        graphics.add(gr);
        gr.name = name;
        list.addElement();
        TextElement e = list.getElements().get(list.getElements().size() - 1);
        e.setColor(color);
        e.addTextChangedListener((e1) -> calculator.recalculate());
        e.setText(func);
        switch (type) {
            case "Function":
                e.setName(name + "(x)");
                break;
            case "Parametric":
                e.setName("xy(t)");
                String startEnd = arr[4];
                String[] st = startEnd.split(":");
                ((Parametric) gr).updateBoards(Double.parseDouble(st[0]), Double.parseDouble(st[1]));
                break;
            case "Implicit":
                e.setName(name + "(xy)");
                ((Implicit) gr).setSensitivity(Double.parseDouble(arr[4]));
                if (arr.length > 5)
                    ((Implicit) gr).setViewType(Integer.parseInt(arr[5]));
                break;
            case "Translation":
                e.setName("Tran");
                ((Translation) gr).setMultiplyer(Integer.parseInt(arr[4]));
                gr.setMAP_SIZE(gr.MAP_SIZE);
                break;
        }
    }

    private void remove(ActionEvent e) {
        int lo = start_make(e.getID());
        graphics.remove(e.getID());
        if (lo == e.getID()) {
            supportFrameManager.close();
        }
        if (e.getSource().equals(0)) {
            calculator.recalculate();
        }
    }

    public void run(Runnable r) {
        calculator.run(r);
    }

    public void startSettings(int id) {
        Graphic g = graphics.get(id);
        if (g instanceof Function) {
            supportFrameManager.openFunctionSettings((Function) g, list.getElements().get(id));
        } else if (g instanceof Parametric) {
            supportFrameManager.openParameterSettings((Parametric) g, list.getElements().get(id));
        } else if (g instanceof Implicit) {
            supportFrameManager.openImplicitSettings((Implicit) g, list.getElements().get(id));
        } else if (g instanceof Translation) {
            supportFrameManager.openTranslationSettings((Translation) g, list.getElements().get(id));
        }
        lOG = g;
    }

    public void makeFunction(int idx, TextElement e) {
        Graphic g = graphics.get(idx);
        Function function = new Function(Graphic.FUNCTION_MAP_SIZE, g.feelsTime);
        int lo = start_make(idx);
        function.setColor(g);
        graphics.set(idx, function);
        int id = func_names.indexOf(g.name);
        setFuncName(function, func_names.get(id), e);
        function.name = g.name;
        checkClosed(idx, lo);
    }

    public void makeParametric(int idx, TextElement e) {
        Graphic g = graphics.get(idx);
        Parametric parametric = new Parametric(Graphic.PARAMETRIC_MAP_SIZE, g.feelsTime);
        int lo = start_make(idx);
        parametric.setColor(g);
        graphics.set(idx, parametric);
        setFuncName(parametric, null, e);
        parametric.name = g.name;
        checkClosed(idx, lo);
    }

    public void makeImplicit(int idx, TextElement e) {
        Graphic g = graphics.get(idx);
        Implicit implicit = new Implicit(mainPanel, this::isMousePressed, Graphic.IMPLICIT_MAP_SIZE, g.feelsTime);
        int lo = start_make(idx);
        implicit.setColor(g);
        graphics.set(idx, implicit);
        int id = func_names.indexOf(g.name);
        setFuncName(implicit, func_names.get(id), e);
        implicit.name = g.name;
        checkClosed(idx, lo);
    }

    public void makeTranslation(int idx, TextElement e) {
        Graphic g = graphics.get(idx);
        Translation translation = new Translation(getCoordinateSystem(), Graphic.TRANSLATION_MAP_SIZE, g.feelsTime);
        translation.setMAP_SIZE(translation.MAP_SIZE);
        int lo = start_make(idx);
        translation.setColor(g);
        graphics.set(idx, translation);
        setFuncName(translation, null, e);
        translation.name = g.name;
        checkClosed(idx, lo);
    }
    private void setFuncName(Graphic g, String name, TextElement e){
        if(g instanceof Function){
            e.setName(name + "(x)");
        }else if(g instanceof Parametric){
            e.setName("xy(t)");
        }else if(g instanceof Implicit){
            e.setName(name + "(xy)");
        }else if(g instanceof Translation){
            e.setName("Tran");
        }
    }
    private int start_make(int idx) {
        int id = graphics.indexOf(lOG);
        graphics.get(idx).free();
        return id;
    }

    private void checkClosed(int idx, int lo) {
        if (supportFrameManager.isOpenedGraphic() && idx == lo) {
            startSettings(idx);
        }
    }

    private int findFreeId() {
        for (int i = 0; i < func_names.size() - 1; ++i) {
            String name = func_names.get(i);
            boolean hasName = false;
            for (Graphic element : graphics) {
                if (element.name.equals(name)) {
                    hasName = true;
                    break;
                }
            }
            if (!hasName)
                return i;
        }
        return func_names.size() - 1;
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

    public void openTimer() {
        supportFrameManager.openTimerSettings();
    }

    public void timerResize() {
        calculator.timerResize();
    }

    public void recalculate() {
        calculator.recalculate();
    }

    public void setStringElements(FunctionsView functions, CalculatorView calculator) {
        this.calculator.setElements(calculator, functions);
    }
    public void findEndOf(Parser.StringToken line){
        calculator.findEndOf(line);
    }
    public void setState(String text) {
        list.setState(text);
    }

    public void rebounds() {
        mainPanel.setGraphicsHeight();
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

    public void lookAtX(double x) {
        if (Double.isFinite(x))
            offsetX = x - GRAPH_WIDTH / scaleX / 2d;
    }

    public void lookAtY(double y) {
        if (Double.isFinite(y))
            offsetY = y + HEIGHT / scaleY / 2d;
    }
    public double getLookAtX(){
        return offsetX + GRAPH_WIDTH / scaleX / 2d;
    }
    public double getLookAtY(){
        return offsetY - HEIGHT / scaleY / 2d;
    }
    public double getMouseX(){
        int x = mainPanel.getMousePosition().x - ElementsList.WIDTH;
        return offsetX + x / scaleX;
    }
    public double getMouseY(){
        int y = mainPanel.getMousePosition().y;
        return offsetY - y / scaleY;
    }
    public void setScaleX(double x) {
        if (Double.isFinite(x) && x > 0) {
            double sX = offsetX + GRAPH_WIDTH / scaleX / 2d;
            scaleX = x;
            offsetX = sX - GRAPH_WIDTH / scaleX / 2d;
        }
    }

    public void setScaleY(double y) {
        if (Double.isFinite(y) && y > 0) {
            double sY = offsetY - HEIGHT / scaleY / 2d;
            scaleY = y;
            offsetY = sY + HEIGHT / scaleY / 2d;
        }
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

    public void setTimerName(String name) {
        mainPanel.setTimerName(name);
    }

    public CoordinateSystem getCoordinateSystem() {
        return mainPanel.getGraphicsView().getCoordinateSystem();
    }

    public void error(String message) {
        dangerState = true;
        setState(message);
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void updateLanguage() {
        mainPanel.updateLanguage();
        supportFrameManager.updateLanguage();
    }
    public void setColor(Graphic g, TextElement el, Color c){
        int id = colors.indexOf(c);
        if(id != -1) {
            g.color = c;
            String name = func_names.get(id);
            setFuncName(g, name, el);
            g.colorChanged = false;
            g.name = name;
        }else
            g.changeColor(c);
        if(g instanceof Implicit)
            ((Implicit) g).setC();
        el.setColor(c);
        calculator.repaint();
    }
    public void quick_save(boolean save){
        if(last_used_file != null){
            dosave(save, last_used_file);
        }else{
            supportFrameManager.openFileChooser(save);
        }
    }
    public void dosave(boolean save, File f) {
        if (save) {
            calculator.run(() -> {
                FullModel m = new FullModel();
                calculator.makeModel(m);
                m.view_params = getLookAtX() + "\n" + getLookAtY() + "\n" + scaleX + "\n" + scaleY;
                mainPanel.makeModel(m);
                supportFrameManager.getTimer().makeModel(m);
                supportFrameManager.getMainSettings().makeModel(m);
                setState(dataBase.save(m, f));
                last_used_file = f;
            });
        } else {
            calculator.run(() -> {
                try {
                    last_used_file = null;
                    supportFrameManager.getTimer().stop();
                    FullModel m = dataBase.load(f);
                    list.clear();
                    supportFrameManager.close();
                    calculator.fromModel(m);
                    list.updateGUI();
                    if (!m.view_params.isEmpty()) {
                        String[] view_params = m.view_params.split("\n");
                        scaleX = Double.parseDouble(view_params[2]);
                        scaleY = Double.parseDouble(view_params[3]);
                        lookAtX(Double.parseDouble(view_params[0]));
                        lookAtY(Double.parseDouble(view_params[1]));
                    }
                    mainPanel.fromModel(m);
                    supportFrameManager.getTimer().fromModel(m);
                    supportFrameManager.getMainSettings().fromModel(m);
                    mainPanel.setGraphicsHeight();
                    calculator.recalculate();
                    calculator.run(() -> setState(f.getName() + " " + Language.LOADED));
                    last_used_file = f;
                } catch (Exception e) {
                    setState(e.toString());
                }
            });
        }
    }
}
