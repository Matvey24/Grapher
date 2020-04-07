package view.support_panels;

import model.GraphType;
import model.Language;
import controller.ModelUpdater;
import model.Settings;
import view.elements.ComboBoxParameter;
import view.elements.Item;
import view.elements.Parameter;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;
import view.grapher.graphics.Implicit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static view.elements.ElementsList.OFFSET;

public class ImplicitSettings extends Settings {
    private Parameter mapSize;
    private final Parameter sensitivity;
    private ComboBoxParameter viewType;
    private Implicit imp;
    private TextElement el;
    private JButton btn_save;
    private FileChooser fileChooser;

    public ImplicitSettings(ModelUpdater updater) {
        setLayout(null);
        mapSize = new Parameter(Language.DISCRETIZATION, (s) -> {
            if (imp != null) {
                int n = Integer.parseInt(s);
                if (n < 1) {
                    mapSize.setDefault(String.valueOf(imp.MAP_SIZE));
                } else {
                    imp.setMAP_SIZE(Integer.parseInt(s));
                    updater.runResize();
                }
            }
        });
        mapSize.addTo(this);
        mapSize.setBounds(OFFSET, OFFSET, ComboBoxParameter.WIDTH);
        sensitivity = new Parameter(Language.SENSITIVITY, s -> {
            if (imp != null) {
                imp.setSensitivity(Double.parseDouble(s));
                updater.runResize();
            }
        });
        sensitivity.addTo(this);
        sensitivity.setBounds(OFFSET, 2 * OFFSET + ComboBoxParameter.HEIGHT, ComboBoxParameter.WIDTH);
        viewType = new ComboBoxParameter(Language.VIEW_COLOR, Language.COLORS);
        viewType.addTo(this);
        viewType.setBounds(OFFSET, 3 * OFFSET + 2 * ComboBoxParameter.HEIGHT);
        viewType.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (((Item) e.getItem()).name.equals(Language.COLORS[Implicit.INFRARED_IMAGER])) {
                    imp.setViewType(Implicit.INFRARED_IMAGER);
                } else if (((Item) e.getItem()).name.equals(Language.COLORS[Implicit.RAY_SPECTRUM])) {
                    imp.setViewType(Implicit.RAY_SPECTRUM);
                }
                updater.runResize();
            }
        });
        btn_save = new JButton(Language.SAVE_PICTURE);
        btn_save.setBounds(OFFSET, 4 * OFFSET + 3 * ComboBoxParameter.HEIGHT,
                TextElement.WIDTH, TextElement.HEIGHT);
        btn_save.addActionListener((e) -> {
            if (this.fileChooser == null) {
                this.fileChooser = new FileChooser();
                this.fileChooser.setSelectedFile(new File("img.jpg"));
                this.fileChooser.setActionListener((a) -> {
                    if (a.getActionCommand().equals(JFileChooser.CANCEL_SELECTION))
                        fileChooser.back();
                    if (a.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                        File f = fileChooser.getSelectedFile();
                        fileChooser.back();
                        updater.run(()-> {
                            try {
                                updater.setState(Language.CONVERTING);
                                BufferedImage image = imp.getData1();
                                updater.setState("Saving");
                                ImageIO.write(image, "gif", f);
                                updater.setState(Language.SAVED + " " + f.getName());
                            } catch (IOException ex) {
                                updater.setState(ex.toString());
                            }
                        });
                    }
                });
            }
            changeScreen(fileChooser);
        });
        add(btn_save);
    }

    public void setInfo(Implicit imp, TextElement e) {
        this.imp = imp;
        this.el = e;
        sensitivity.setDefault(imp.getSensitivity() + "");
        mapSize.setDefault(String.valueOf(imp.MAP_SIZE));
        viewType.setSelectedIndex(imp.viewType);
    }

    @Override
    public Graphic getGraphic() {
        return imp;
    }

    @Override
    public TextElement getTextElement() {
        return el;
    }

    @Override
    public void onSetSize() {
        setSize(ComboBoxParameter.WIDTH + 2 * OFFSET + 40,
                ComboBoxParameter.HEIGHT * 3 + 4 * OFFSET + 80);
    }
    @Override
    public void onShow() {
        setTitle(Language.TYPE_TITLES[GraphType.IMPLICIT.ordinal()]);
    }
    public void updateLanguage() {
        mapSize.setName(Language.DISCRETIZATION);
        sensitivity.setName(Language.SENSITIVITY);
        viewType.setName(Language.VIEW_COLOR);
        viewType.setElementNames(Language.COLORS);
        btn_save.setText(Language.SAVE_PICTURE);
    }
}
