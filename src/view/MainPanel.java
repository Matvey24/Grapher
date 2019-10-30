package view;

import controller.ModelUpdater;
import framesLib.Screen;
import view.elements.*;
import view.grapher.GraphicsView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static view.elements.ElementsList.OFFSET;

public class MainPanel extends Screen {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int GRAPH_WIDTH = WIDTH - ElementsList.WIDTH;
    private GraphicsView graphicsView;
    private Point mousePosition;
    public MainPanel(){
        setLayout(null);
        JButton btn_help = new JButton("Help");
        btn_help.setBounds(OFFSET,620, ElementsList.WIDTH - 2 * OFFSET, TextElement.HEIGHT);
        add(btn_help);
        btn_help.addActionListener((e)-> changeScreen(TextViewer.openText("Help")));
        JButton btn_calc_help = new JButton("Calculator help");
        btn_calc_help.setBounds(OFFSET, 620 + TextElement.HEIGHT + OFFSET, ElementsList.WIDTH - 2 * OFFSET, TextElement.HEIGHT);
        add(btn_calc_help);
        btn_calc_help.addActionListener((e)-> changeScreen(TextViewer.openText("Calc_Help")));
        mousePosition = new Point();

        ModelUpdater updater = new ModelUpdater(this::repaint);
        ElementsList graphics = new ElementsList("Graphics", 0, 0, updater::addVRemove);
        graphics.addTo(this);
        graphicsView = new GraphicsView(graphics, updater);

        CalculatorView calculator = new CalculatorView(updater::recalculate);
        calculator.addTo(this);
        calculator.setBounds(0, 620 - OFFSET - CalculatorView.CALC_HEIGHT);

        FunctionsView functions = new FunctionsView(updater::recalculate);
        functions.addTo(this);
        functions.setBounds(0, 620 - 3 * OFFSET - CalculatorView.CALC_HEIGHT - FunctionsView.FUNC_HEIGHT);

        updater.setStringElements(functions, calculator);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePosition.setLocation(e.getX(), e.getY());
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updater.translate(e.getX() - mousePosition.x, e.getY() - mousePosition.y);
                mousePosition.setLocation(e.getX(), e.getY());
            }
        });
        addMouseWheelListener(e -> updater.resize((int)e.getPreciseWheelRotation(), e.getX() - ElementsList.WIDTH, e.getY()));
        JButton btn_resize = new JButton("Resize");
        btn_resize.setBounds(1280, 720, 100, 40);
        btn_resize.addActionListener(e -> resize());
        add(btn_resize);
    }
    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
    @Override
    public void onShow() {
        setTitle("Grapher by Math_way");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        graphicsView.paint(g);
    }
}
