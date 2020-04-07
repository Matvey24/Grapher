package model.help;

import framesLib.TextPanel;
import model.Language;

import java.util.HashMap;
import java.util.function.Consumer;

public class TextViewer {
    private static final HashMap<String, TextPanel> text_panels;
    public static final String[] help_names = {"help", "calc_help", "info"};
    static{
        text_panels = new HashMap<>();
        for(int i = 0; i < help_names.length; ++i)
            text_panels.put(help_names[i], new TextPanel(Language.HELPERS[i], Language.HELP_NAMES[i]));
    }
    public static TextPanel openText(String name){
        return text_panels.get(name);
    }
    public static void updateLanguage(){
        text_panels.values().forEach(new Consumer<TextPanel>() {
            int i = 0;
            @Override
            public void accept(TextPanel textPanel) {
                textPanel.updateLanguage(Language.HELPERS[i], Language.HELP_NAMES[i++], Language.BACK);
            }
        });
    }
}
