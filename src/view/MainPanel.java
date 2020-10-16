package view;

import model.Language;
import controller.ModelUpdater;
import controller.VersionController;
import framesLib.Screen;
import model.help.FullModel;
import view.elements.*;
import view.grapher.CoordinateSystem;
import view.grapher.GraphicsView;

import java.io.File;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.*;

import static view.elements.ElementsList.OFFSET;

public class MainPanel extends Screen {
    public static int WIDTH;
    public static int HEIGHT;
    public static int GRAPH_WIDTH;
    private final GraphicsView graphicsView;
    private final ModelUpdater updater;
    private final Point mousePosition;
    private int height;
    private int resizeType;
    private final ElementsList graphics;
    private final CalculatorView calculator;
    private final FunctionsView functions;
    private final JButton btn_help;
    private final JButton btn_resize;
    private final JButton btn_timer;
    private final JButton btn_settings;
    static{
        rebounds(1280, 720);
    }
    public MainPanel(){
        setLayout(null);
        btn_help = new JButton(Language.HELP);
        btn_help.setFocusPainted(false);
        add(btn_help);
        mousePosition = new Point();
        updater = new ModelUpdater(this::repaint, this);

        btn_help.addActionListener((e)-> updater.getSupportFrameManager().openHelperFrame());

        graphics = new ElementsList(0, 0, updater::addVRemove, updater::startSettings);
        graphics.setName(Language.GRAPHICS);
        graphics.addTo(this);
        graphicsView = new GraphicsView(graphics, updater);

        calculator = new CalculatorView(updater::recalculate, this::resize);
        calculator.addTo(this);

        functions = new FunctionsView(updater::recalculate);
        functions.addTo(this);

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
            updater.rescale(e.getPreciseWheelRotation(), e.getX() - ElementsList.WIDTH, e.getY(), line);
        });

        resizeType = 0;
        btn_resize = new JButton(Language.RESIZERS[resizeType]);
        btn_resize.setFocusPainted(false);
        btn_resize.addActionListener(e -> {
            switch (resizeType){
                case 0:
                    WIDTH = getWidth();
                    HEIGHT = getHeight();
                    GRAPH_WIDTH = WIDTH - ElementsList.WIDTH;
                    updater.recalculate();
                    break;
                case 3:
                    updater.rescaleBack();
                    break;
            }
        });
        btn_resize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    resizeType = (resizeType + 1) % 4;
                    btn_resize.setText(Language.RESIZERS[resizeType]);
                }
            }
        });
        add(btn_resize);

        btn_timer = new JButton(Language.TIMER);
        btn_timer.setFocusPainted(false);
        btn_timer.addActionListener(e ->updater.openTimer());
        btn_timer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3){
                   updater.getSupportFrameManager().getTimer().onClick();
                }
            }
        });
        add(btn_timer);
        btn_settings = new JButton(Language.MAIN_SETTINGS);
        btn_settings.setFocusPainted(false);
        btn_settings.addActionListener(e -> updater.getSupportFrameManager().openMainSettings());
        add(btn_settings);
        setGraphicsHeight();
        setDropTarget(new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable tr = dtde.getTransferable();
                for(DataFlavor df: tr.getTransferDataFlavors()){
                    try {
                        List<?> list = (List<?>)tr.getTransferData(df);
                        if(list.size() > 0) {
                            File f;
                            if(list.get(0) instanceof File)
                                f = (File) list.get(0);
                            else
                                f = new File(list.get(0).toString());
                            if(f.exists() && f.isFile())
                                updater.dosave(false, f);
                        }
                    }catch (Exception e){
                        updater.setState(e.toString());
                    }
                }
            }
        }));
    }
    public static void rebounds(int width, int height){
        WIDTH = width;
        HEIGHT = height;
        GRAPH_WIDTH = WIDTH - ElementsList.WIDTH;
    }
    public void setGraphicsHeight(){
        height = graphics.getHeight();
        height += OFFSET;
        functions.setBounds(0, height);
        height += 2*OFFSET + FunctionsView.FUNC_HEIGHT;
        calculator.setBounds(0,height);
        height += CalculatorView.CALC_HEIGHT;
        btn_help.setBounds(OFFSET,height,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        btn_settings.setBounds(3 * OFFSET / 2 + TextElement.WIDTH / 2,
                height,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        height += TextElement.HEIGHT + OFFSET;
        btn_resize.setBounds(OFFSET,
                height,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        btn_timer.setBounds(3 * OFFSET / 2 + TextElement.WIDTH / 2,
                height,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        height += TextElement.HEIGHT;
    }
    public void updateLanguage(){
        btn_help.setText(Language.HELP);
        graphics.setName(Language.GRAPHICS);
        btn_resize.setText(Language.RESIZERS[resizeType]);
        btn_timer.setText(Language.TIMER);
        btn_settings.setText(Language.MAIN_SETTINGS);
        functions.updateLanguage();
        calculator.updateLanguage();
        VersionController.updateLanguage();
        setTitle(VersionController.VERSION_NAME + " by Math_way");
    }
    public void resize(){
        WIDTH = TextElement.WIDTH + 30;
        GRAPH_WIDTH = WIDTH - TextElement.WIDTH;
        HEIGHT = height + 60;
        super.resize();
    }
    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
    @Override
    public void onShow() {
        setTitle(VersionController.VERSION_NAME + " by Math_way");
    }
    public CoordinateSystem getCoordinateSystem(){
        return graphicsView.getCoordinateSystem();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.translate(ElementsList.WIDTH, 0);
        graphicsView.paint(g);
    }
    public void makeModel(FullModel m){
        m.resize_idx = String.valueOf(resizeType);
    }
    public void fromModel(FullModel m){
        resizeType = Integer.parseInt(m.resize_idx);
        btn_resize.setText(Language.RESIZERS[resizeType]);
    }
    public void setTimerName(String name){
        btn_timer.setText(name);
    }
}
