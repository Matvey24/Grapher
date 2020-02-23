package model;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class Language {
    public static String GRAPHER;
    //settings
    public static String DISCRETIZATION;
    public static String TYPE;
    public static String[] TYPE_TITLES;
    //implicit
    public static String SENSITIVITY;
    public static String INEQUALITY;
    public static String SPECTRUM;
    //parametric
    public static String DIMENSION;
    //timer
    public static String DURATION_FPS;
    public static String BEGIN;
    public static String BOOMERANG;
    //main settings
    public static String LANGUAGE;
    //states
    public static String CONVERTING;
    public static String UPDATING;
    public static String FINE;
    public static String LOADED;
    //errors
    public static String[] UPDATER_ERRORS;
    public static String[] CALCULATOR_ERRORS;
    public static String[] PARSER_ERRORS;
    //gui
    //help
    public static String HELP;
    public static String USER_HELP;
    public static String CALC_HELP;
    //text elements
    public static String GRAPHICS;
    public static String FUNCTIONS;
    public static String CALCULATOR;
    //buttons
    public static String UPDATE;
    public static String MAIN_SETTINGS;
    public static String[] RESIZERS;
    public static String TIMER;
    public static String BACK;
    //
    //help
    public static String[][][] HELPERS;
    public static List<String> language_Names;
    public static int LANGUAGE_INDEX;
    private static Property property;
    public static void init(Property _property){
        property = _property;
        language_Names = new ArrayList<>();
        try {
            Properties properties = property.getProperties("settings.xml");
            Iterator<String> set = properties.stringPropertyNames().iterator();
            String def = "English";
            while(set.hasNext()){
                String lan = set.next();
                if(lan.equals("default")){
                    def = properties.getProperty(lan);
                    continue;
                }
                language_Names.add(properties.getProperty(lan));
            }
            System.out.println(loadLanguage(def));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static String loadLanguage(String name){
        List<String> list;
        try {
            Properties properties = property.getProperties(name + ".l");
            String s = properties.getProperty("main");
            list = Arrays.asList(s.split("\n"));
        }catch (IOException e){
            return "Error: " + e.getMessage();
        }
        for(int i = 0; i < list.size(); ++i){
            String s = list.get(i);
            s = s.trim();
            list.set(i, s);
        }
        Iterator<String> it = list.iterator();
        GRAPHER = it.next();
        if(!it.next().equals("settings:")){
            return "no part \"settings:\"";
        }
        DISCRETIZATION = it.next();
        TYPE = it.next();
        if(!it.next().equals("titles3:"))
            return "no part \"titles3:\"";
        TYPE_TITLES = new String[]{it.next(), it.next(), it.next()};
        SENSITIVITY = it.next();
        INEQUALITY = it.next();
        SPECTRUM = it.next();
        DIMENSION = it.next();
        DURATION_FPS = it.next();
        BEGIN = it.next();
        BOOMERANG = it.next();
        LANGUAGE = it.next();
        if(!it.next().equals("states:"))
            return "no part \"states:\"";
        CONVERTING = it.next();
        UPDATING = it.next();
        FINE = it.next();
        LOADED = it.next();
        if(!it.next().equals("updater_errors4:"))
            return "no part \"updater_errors4:\"";
        UPDATER_ERRORS = new String[]{it.next(), it.next(), it.next(), it.next()};
        if(!it.next().equals("calculator_errors6:"))
            return "no part \"calculator_errors6:\"";
        CALCULATOR_ERRORS = new String[]{it.next(), it.next(), it.next(), it.next(), it.next(), it.next()};
        if(!it.next().equals("parser_errors3:"))
            return "no part \"parser_errors3:\"";
        PARSER_ERRORS = new String[]{it.next(), it.next(), it.next()};
        if(!it.next().equals("help3:"))
            return "no part \"help3:\"";
        HELP = it.next();
        USER_HELP = it.next();
        CALC_HELP = it.next();
        if(!it.next().equals("text_elements3:"))
            return "no part \"text_elements3:\"";
        GRAPHICS = it.next();
        FUNCTIONS = it.next();
        CALCULATOR = it.next();
        if(!it.next().equals("buttons5:"))
            return "no part \"buttons5:\"";
        UPDATE = it.next();
        MAIN_SETTINGS = it.next();
        TIMER = it.next();
        BACK = it.next();
        if(!it.next().equals("resizers4:"))
            return "no part \"resizers4:\"";
        RESIZERS = new String[]{it.next(), it.next(), it.next(), it.next()};
        String[] s;
        HELPERS = new String[2][][];
        for(int i = 0; i < 2; ++i){
            s = it.next().split(" ");
            if(!s[0].equals("help"))
                return "no part 'help'";
            int n = Integer.parseInt(s[1]);
            HELPERS[i] = new String[n][];
            for(int j = 0; j < n; ++j) {
                s = it.next().split(" ");
                if (!s[0].equals("part"))
                    return "no part 'part'";
                int m = Integer.parseInt(s[1]);
                HELPERS[i][j] = new String[m];
                for (int k = 0; k < m; ++k) {
                    HELPERS[i][j][k] = it.next();
                }
            }
        }
        LANGUAGE_INDEX = language_Names.indexOf(name);
        return name + " " + LOADED;
    }
}
