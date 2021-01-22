package view.support_panels;

import controller.MyTimer;
import model.Language;
import controller.ModelUpdater;
import framesLib.Screen;
import model.FullModel;
import view.elements.ComboBoxParameter;
import view.elements.ElementsList;
import view.elements.Parameter;
import view.elements.TextElement;

import javax.swing.*;

import static view.elements.ElementsList.OFFSET;

public class TimerSettings extends Screen {
    private static final int HEIGHT = 4 * OFFSET + 2 * ComboBoxParameter.HEIGHT
            + TextElement.HEIGHT;

    private Parameter duration;
    private Parameter dimension;
    private MyTimer timer;
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
    private boolean dont_resize;
    private final JToggleButton start;
    private final JToggleButton timeDir;
    private final ModelUpdater updater;
    private Runnable timer_iteration;
    public TimerSettings(ModelUpdater updater) {
        setLayout(null);
        this.updater = updater;
        duration = new Parameter(Language.DURATION_FPS, (s) -> {
            String[] vars = s.split(":");
            if (vars.length == 0){
                duration.setDefault(dur + ":" + FPS);
                return;
            }
            double durat = Double.parseDouble(vars[0]);
            if(durat < 0){
                duration.setDefault(dur + ":" + FPS);
                return;
            }
            dur = durat;
            if(dur == 0){
                timer_iteration = this::timer_real_time;
                updater.setTime(0);
                updater.timerResize();
            }else{
                timer_iteration = this::timer_update;
            }
            if (vars.length == 1) {
                FPS = 60;
            } else {
                FPS = Double.parseDouble(vars[1]);
            }
            delay = 1 / FPS;
            int d = (int) (1000 * delay);
            d = Math.min(d, 10000);
            timer.setDelay(d);
        });
        duration.addTo(this);
        duration.setBounds(OFFSET, OFFSET, TextElement.WIDTH);
        dimension = new Parameter(Language.DIMENSION, (s) -> {
            String[] vars = s.split(":");
            if (vars.length == 0) {
                dimension.setDefault(startT + ":" + endT);
                return;
            }
            startT = Double.parseDouble(vars[0]);
            endT = Double.parseDouble(vars[1]);
            value = startT;
            if (!timer.isRunning() && !dont_resize && dur != 0) {
                updater.setTime(startT);
                updater.timerResize();
            }
            time = 0;
        });
        dimension.addTo(this);
        dimension.setBounds(OFFSET, 2 * OFFSET + ComboBoxParameter.HEIGHT,
                TextElement.WIDTH);
        timer = new MyTimer((int) (delay * 1000), () -> timer_iteration.run());
        timer_iteration = this::timer_update;

        start = new JToggleButton(Language.BEGIN);
        add(start);
        start.setBounds(OFFSET, 3 * OFFSET + 2 * ComboBoxParameter.HEIGHT,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        start.addActionListener(e -> start());
        timeDir = new JToggleButton(Language.BOOMERANG);
        add(timeDir);
        timeDir.setBounds(OFFSET + TextElement.WIDTH / 2 + OFFSET / 2,
                3 * OFFSET + 2 * ComboBoxParameter.HEIGHT,
                TextElement.WIDTH / 2 - OFFSET / 2, TextElement.HEIGHT);
        duration.setDefault(dur + ":" + FPS);
        dimension.setDefault(startT + ":" + endT);
        timeDir.addActionListener(e -> boomerang = timeDir.isSelected());
    }
    private void timer_update(){
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
    }

    private void timer_real_time(){
        long t = System.currentTimeMillis();
        double delta = (t - timeBefore) / 1000d;
        timeBefore = t;
        time += delta;
        value = time;
        updater.setTime(value);
        updater.timerResize();
    }
    private void start() {
        if (start.isSelected()) {
            timer.start();
            timeBefore = System.currentTimeMillis();
            updater.setTimerName(Language.TIMER + "(On)");
        } else {
            timer.stop();
            updater.setTimerName(Language.TIMER + "(Off)");
        }
    }
    public void stop(){
        if(!start.isSelected()) {
            timer.stop();
            updater.setTimerName(Language.TIMER + "(Off)");
        }
    }
    public void setDont_resize(boolean dont_resize) {
        this.dont_resize = dont_resize;
    }

    @Override
    public void onShow() {
        super.onShow();
        duration.setDefault(dur + ":" + FPS);
        dimension.setDefault(startT + ":" + endT);
        setTitle(Language.TIMER);
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
        m.timer_info = duration.getText() + "\n"
                + dimension.getText() + "\n" + timeDir.isSelected();
    }

    public void fromModel(FullModel m) {
        if (!m.timer_info.isEmpty()) {
            setDont_resize(true);
            String[] info = m.timer_info.split("\n");
            duration.setDefault(info[0]);
            duration.activate();
            dimension.setDefault(info[1]);
            dimension.activate();
            timeDir.setSelected(Boolean.parseBoolean(info[2]));
            boomerang = timeDir.isSelected();
            setDont_resize(false);
        }
    }

    @Override
    public void onSetSize() {
        setSize(ElementsList.WIDTH, HEIGHT);
    }
}
