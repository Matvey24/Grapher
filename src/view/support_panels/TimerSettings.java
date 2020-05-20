package view.support_panels;

import model.Language;
import controller.ModelUpdater;
import framesLib.Screen;
import model.help.FullModel;
import view.elements.Parameter;

import javax.swing.*;

public class TimerSettings extends Screen {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 300;

    private Parameter duration;
    private Parameter dimension;
    private Timer timer;
    private double FPS = 60;
    private double delay = 1 / FPS;
    private double dur = 30;
    private double startT = -5;
    private double endT = 5;
    private double time;
    private long timeBefore;
    private double value = startT;
    private boolean boomerang;
    private boolean fTimeDirection = true;
    private final JToggleButton start;
    private final JToggleButton timeDir;
    private final ModelUpdater updater;
    public TimerSettings(ModelUpdater updater) {
        setLayout(null);
        this.updater = updater;
        duration = new Parameter(Language.DURATION_FPS, (s) -> {
            String[] vars = s.split(":");
            if (vars.length == 0)
                duration.setDefault(dur + ":" + FPS);
            dur = Double.parseDouble(vars[0]);
            if (vars.length == 1) {
                FPS = 60;
            } else {
                FPS = Double.parseDouble(vars[1]);
            }
            delay = 1 / FPS;
            timer.setDelay((int) (1000 * delay));
        });
        duration.addTo(this);
        duration.setBounds(0, 0, 150);
        dimension = new Parameter(Language.DIMENSION, (s) -> {
            String[] vars = s.split(":");
            if (vars.length == 0)
                dimension.setDefault(startT + ":" + endT);
            startT = Double.parseDouble(vars[0]);
            endT = Double.parseDouble(vars[1]);
            if (!timer.isRunning()) {
                value = startT;
                updater.setTime(startT);
                updater.timerResize();
            }
            time = 0;
        });
        dimension.addTo(this);
        dimension.setBounds(0, 70, 150);
        timer = new Timer((int) (delay * 1000), e -> {
            long t = System.currentTimeMillis();
            double delta = (t - timeBefore) / 1000d;
            timeBefore = t;
            double len = endT - startT;
            value = time / dur * len + startT;
            updater.setTime(value);
            if (fTimeDirection)
                time += delta;
            else
                time -= delta;
            if (time > dur) {
                if (boomerang) {
                    time = dur;
                    fTimeDirection = false;
                } else {
                    time -= dur;
                }
            } else if (time < 0) {
                time = 0;
                fTimeDirection = true;
            }
            updater.timerResize();
        });
        start = new JToggleButton(Language.BEGIN);
        start.setFocusPainted(false);
        add(start);
        start.setBounds(10, 170, 150, 40);
        start.addActionListener(e -> start());
        timeDir = new JToggleButton(Language.BOOMERANG);
        timeDir.setFocusPainted(false);
        add(timeDir);
        timeDir.setBounds(10, 220, 150, 40);
        duration.setDefault(dur + ":" + FPS);
        dimension.setDefault(startT + ":" + endT);
        timeDir.addActionListener(e -> boomerang = timeDir.isSelected());
    }
    private void start(){
        if(start.isSelected()) {
            timer.start();
            timeBefore = System.currentTimeMillis();
            updater.setTimerName("Timer(On)");
        }else{
            timer.stop();
            updater.setTimerName("Timer(Off)");
        }
    }
    @Override
    public void onShow() {
        duration.setDefault(dur + ":" + FPS);
        dimension.setDefault(startT + ":" + endT);
    }

    public void onClick() {
        start.setSelected(!start.isSelected());
        start();
    }

    public void updateLanguage() {
        duration.setName(Language.DURATION_FPS);
        dimension.setName(Language.DIMENSION);
        start.setText(Language.BEGIN);
        timeDir.setText(Language.BOOMERANG);
    }

    public double getT() {
        return value;
    }

    public void makeModel(FullModel m) {
        m.timer_info = duration.getText() + "\n" + dimension.getText() + "\n" + timeDir.isSelected();
    }

    public void fromModel(FullModel m) {
        String[] info = m.timer_info.split("\n");
        duration.setDefault(info[0]);
        duration.activate();
        dimension.setDefault(info[1]);
        dimension.activate();
        timeDir.setSelected(Boolean.parseBoolean(info[2]));
        boomerang = timeDir.isSelected();
    }

    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
}
