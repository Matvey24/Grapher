package model.help;

import framesLib.TextPanel;
import model.Language;

import java.util.HashMap;

public class TextViewer {
    private static final HashMap<String, TextPanel> text_panels;
    static{
        text_panels = new HashMap<>();
        text_panels.put("calc_help", new TextPanel(Language.HELPERS[0], Language.CALC_HELP));
        text_panels.put("help", new TextPanel(Language.HELPERS[1], Language.USER_HELP));
    }
    public static TextPanel openText(String name){
        return text_panels.get(name);
    }
    public static void updateLanguage(){
        text_panels.get("calc_help").updateLanguage(Language.HELPERS[0], Language.CALC_HELP);
        text_panels.get("help").updateLanguage(Language.HELPERS[1], Language.USER_HELP);
    }
}
