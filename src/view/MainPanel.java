package view;

import controller.ModelUpdater;
import controller.VersionController;
import framesLib.Screen;
import view.elements.*;
import view.grapher.GraphicsView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static view.elements.ElementsList.OFFSET;

public class MainPanel extends Screen {
    private static final int WIDTH_0 = 1280;
    private static final int HEIGHT_0 = 720;
    public static int WIDTH = WIDTH_0;
    public static int HEIGHT = HEIGHT_0;
    public static int GRAPH_WIDTH = WIDTH - ElementsList.WIDTH;
    private GraphicsView graphicsView;

    private Point mousePosition;
    private int resizeType;
    private static final String[] resizers = {"Resize", "Abscissa", "Ordinate", "Return back"};
    public MainPanel(){
        setLayout(null);
        JButton btn_help = new JButton("Help");
        btn_help.setBounds(OFFSET,620, TextElement.WIDTH / 3 - OFFSET/2, TextElement.HEIGHT);
        add(btn_help);
        btn_help.addActionListener((e)-> changeScreen(TextViewer.openText("Help")));
        JButton btn_calc_help = new JButton("Calculator help");
        btn_calc_help.setBounds(3*OFFSET/2+TextElement.WIDTH / 3, 620, 2 * TextElement.WIDTH / 3 - OFFSET/2, TextElement.HEIGHT);
        add(btn_calc_help);
        btn_calc_help.addActionListener((e)-> changeScreen(TextViewer.openText("Calc_Help")));
        mousePosition = new Point();

        ModelUpdater updater = new ModelUpdater(this::repaint);
        ElementsList graphics = new ElementsList("Graphics", 0, 0, updater::addVRemove, updater::startSettings);
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
        addMouseWheelListener(e -> {
            int line = 0;
            if(resizeType == 1 || resizeType == 2)
                line = resizeType;
            updater.resize(e.getPreciseWheelRotation(), e.getX() - ElementsList.WIDTH, e.getY(), line);
        });

        JButton btn_resize = new JButton(resizers[0]);
        resizeType = 0;
        btn_resize.setBounds(OFFSET, 620 + TextElement.HEIGHT + OFFSET, TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        btn_resize.addActionListener(e -> {
            switch (resizeType){
                case 0:
                    WIDTH = getWidth();
                    HEIGHT = getHeight();
                    GRAPH_WIDTH = WIDTH - ElementsList.WIDTH;
                    updater.recalculate();
                    break;
                case 3:
                    updater.resizeBack();
                    break;
            }
        });
        btn_resize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    resizeType = (resizeType + 1) % 4;
                    btn_resize.setText(resizers[resizeType]);
                }
            }
        });
        add(btn_resize);

        JButton btn_timer = new JButton("Timer");
        btn_timer.setBounds(3 * OFFSET / 2 + TextElement.WIDTH / 2, 620 + TextElement.HEIGHT + OFFSET, TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        btn_timer.addActionListener(e -> updater.openTimer());
        add(btn_timer);
    }
    @Override
    public void onSetSize() {
        setSize(WIDTH_0, HEIGHT_0);
    }
    @Override
    public void onShow() {
        setTitle("Grapher" + VersionController.VERSION_NAME + " by Math_way");
    }
//    private long t = System.currentTimeMillis();
    @Override
    public void paint(Graphics g) {
//        long time = System.currentTimeMillis();
//        System.out.println(time - t);
//        t = time;
        super.paint(g);
        graphicsView.paint(g);
    }
}
