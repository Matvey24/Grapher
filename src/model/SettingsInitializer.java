package model;

import model.help.Property;
import view.MainPanel;
import view.elements.ElementsList;
import view.elements.TextElement;
import view.grapher.graphics.Graphic;

import java.time.temporal.Temporal;
import java.util.Properties;

public class SettingsInitializer {
    public static void init(Property p){
        int main_width = MainPanel.WIDTH, main_height = MainPanel.HEIGHT;
        try {
            Language.onCreate(p);
            Properties pr = p.getProperties("settings.xml");

            for (String lan : pr.stringPropertyNames()) {
                String text = pr.getProperty(lan);
                int count;
                switch (lan) {
                    case "default":
                        Language.DEFAULT_LANGUAGE = text;
                        break;
                    case "width":
                        main_width = Integer.parseInt(text);
                        break;
                    case "height":
                        main_height = Integer.parseInt(text);
                        break;
                    case "graphics_count":
                        count = Integer.parseInt(text);
                        if (ElementsList.checkValidCount(count)) {
                            ElementsList.DEFAULT_MAX_SIZE = count;
                        }
                        break;
                    case "function_discretization":
                        count = Integer.parseInt(text);
                        if(Graphic.checkValidDiscretization(count)){
                            Graphic.FUNCTION_MAP_SIZE = count;
                        }
                        break;
                    case "parametric_discretization":
                        count = Integer.parseInt(text);
                        if(Graphic.checkValidDiscretization(count)){
                            Graphic.PARAMETRIC_MAP_SIZE = count;
                        }
                        break;
                    case "implicit_discretization":
                        count = Integer.parseInt(text);
                        if(Graphic.checkValidDiscretization(count)){
                            Graphic.IMPLICIT_MAP_SIZE = count;
                        }
                        break;
                    case "translation_discretization":
                        count = Integer.parseInt(text);
                        if(Graphic.checkValidDiscretization(count)){
                            Graphic.TRANSLATION_MAP_SIZE = count;
                        }
                        break;
                    case "default_offset":
                        count = Integer.parseInt(text);
                        if(count >= 0){
                            ElementsList.OFFSET = count;
                            ElementsList.setWidth();
                        }
                        break;
                    case "graphic_field_width":
                        count = Integer.parseInt(text);
                        if(count > 0){
                            TextElement.setTextWidth(count);
                        }
                    default:
                        if (lan.startsWith("l")) {
                            Language.language_Names.add(text);
                        }
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        MainPanel.rebounds(main_width, main_height);
        System.out.println(Language.loadLanguage(Language.DEFAULT_LANGUAGE));
    }
}
