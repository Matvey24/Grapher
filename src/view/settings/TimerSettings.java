package view.settings;

import calculator2.calculator.executors.Variable;
import controller.ModelUpdater;
import framesLib.Screen;
import view.elements.Parameter;

import javax.swing.*;

public class TimerSettings extends Screen {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 300;

    private Parameter duration;
    private Parameter dimension;
    private Timer timer;
    private double FPS = 30;
    private double delta = 1/FPS;
    private double time;
    private double dur = 3;
    private double startT = -5;
    private double endT = 5;
    private boolean boomerang;
    private boolean fTimeDirection = true;
    private JToggleButton start;
    public TimerSettings(ModelUpdater updater){
        setLayout(null);
        duration = new Parameter("Duration:fps", (e)->{
            try {
                String[] vars = e.getSource().toString().split(":");
                if (vars.length == 0)
                    duration.setDefault(dur + ":" + FPS);
                dur = Double.parseDouble(vars[0]);
                if (vars.length == 1) {
                    FPS = 60;
                    delta = 1 / FPS;
                }else {
                    FPS = Double.parseDouble(vars[1]);
                    delta = 1 / FPS;
                }
            }catch (RuntimeException ex){
                onShow();
            }
        });
        duration.addTo(this);
        duration.setBounds(0,0, 150);
        dimension = new Parameter("Dimension", (e)->{
            try {
                String[] vars = e.getSource().toString().split(":");
                if (vars.length == 0)
                    dimension.setDefault(startT + ":" + endT);
                startT = Double.parseDouble(vars[0]);
                endT = Double.parseDouble(vars[1]);
                if(!timer.isRunning())
                for(Variable<Double> var: updater.getTimeVars()){
                    var.setValue(startT);
                }
                time = 0;
                updater.justResize();
            }catch (RuntimeException ex){
                onShow();
            }
        });
        dimension.addTo(this);
        dimension.setBounds(0,70,150);
        timer = new Timer((int)(delta * 1000), e -> {
            double len = endT - startT;
            double val = time / dur * len + startT;
            for(Variable<Double> var: updater.getTimeVars()){
                var.setValue(val);
            }
            if(fTimeDirection)
                time += delta;
            else
                time -= delta;
            if(time > dur) {
                if(boomerang) {
                    time = dur;
                    fTimeDirection = false;
                }else{
                    time -= dur;
                }
            }else if(time < 0){
                time = 0;
                fTimeDirection = true;
            }
            updater.justResize();
        });
        start = new JToggleButton("Run");
        add(start);
        start.setBounds(10,170,150, 40);
        start.addActionListener(e->{
            if(start.isSelected()){
                timer.start();
            }else{
                timer.stop();
            }
        });
        JToggleButton timeDir = new JToggleButton("Boomerang");
        add(timeDir);
        timeDir.setBounds(10,220, 150, 40);
        timeDir.addActionListener(e->{
            boomerang = timeDir.isSelected();
        });
    }

    @Override
    public void onShow() {
        duration.setDefault(dur + ":" + FPS);
        dimension.setDefault(startT + ":" + endT);
    }

    @Override
    public void onHide() {
        if(timer.isRunning()){
           timer.stop();
           start.setSelected(false);
        }

    }

    public double getStartT() {
        return startT;
    }

    @Override
    public void onSetSize() {
        setSize(WIDTH, HEIGHT);
    }
}
