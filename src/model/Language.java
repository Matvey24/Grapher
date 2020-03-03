package model;

import model.help.Property;

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
    public static String SAVED;
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
    public static String SAVE_PRJ;
    public static String LOAD_PRJ;
    //
    //help
    public static String[][][] HELPERS;
    public static List<String> language_Names;
    public static int LANGUAGE_INDEX;
    private static Property property;
    public static final String DEFAULT_LANGUAGE = "English";
    public static void init(Property _property){
        property = _property;
        language_Names = new ArrayList<>();
        try {
            Properties properties = property.getProperties("settings.xml");
            Iterator<String> set = properties.stringPropertyNames().iterator();
            String def = DEFAULT_LANGUAGE;
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
        try {
            Properties properties = property.getProperties(name + ".l");
            String[] arr;
            GRAPHER = properties.getProperty("name");
            TYPE = properties.getProperty("type");
            TYPE_TITLES = properties.getProperty("titles").split("\n");
            arr = properties.getProperty("settings").split("\n");
            DISCRETIZATION = arr[0];
            SENSITIVITY = arr[1];
            DIMENSION = arr[2];
            DURATION_FPS = arr[3];
            BOOMERANG = arr[4];
            arr = properties.getProperty("states").split("\n");
            CONVERTING = arr[0];
            UPDATING = arr[1];
            FINE = arr[2];
            LOADED = arr[3];
            SAVED = arr[4];
            UPDATER_ERRORS = properties.getProperty("updater_errors").split("\n");
            CALCULATOR_ERRORS = properties.getProperty("calculator_errors").split("\n");
            PARSER_ERRORS = properties.getProperty("parser_errors").split("\n");
            arr = properties.getProperty("buttons").split("\n");
            BEGIN = arr[0];
            UPDATE = arr[1];
            MAIN_SETTINGS = arr[2];
            TIMER = arr[3];
            BACK = arr[4];
            SAVE_PRJ = arr[5];
            LOAD_PRJ = arr[6];
            arr = properties.getProperty("helpers").split("\n");
            HELP = arr[0];
            USER_HELP = arr[1];
            CALC_HELP = arr[2];
            RESIZERS = properties.getProperty("resizers").split("\n");
            arr = properties.getProperty("text_elements").split("\n");
            GRAPHICS = arr[0];
            FUNCTIONS = arr[1];
            CALCULATOR = arr[2];
            LANGUAGE = properties.getProperty("language");
            //////////////////////////////////////////////
            String s = properties.getProperty("help");
            List<String> list = Arrays.asList(s.split("\n"));
            Iterator<String> it = list.iterator();
            HELPERS = new String[2][][];
            for(int i = 0; i < 2; ++i){
                arr = it.next().split(" ");
                if(!arr[0].equals("help")) {
                    return "no part 'help'";
                }
                int n = Integer.parseInt(arr[1]);
                HELPERS[i] = new String[n][];
                for(int j = 0; j < n; ++j) {
                    arr = it.next().split(" ");
                    if (!arr[0].equals("part")) {
                        return "no part 'part'";
                    }
                    int m = Integer.parseInt(arr[1]);
                    HELPERS[i][j] = new String[m];
                    for (int k = 0; k < m; ++k) {
                        HELPERS[i][j][k] = it.next();
                    }
                }
            }
            LANGUAGE_INDEX = language_Names.indexOf(name);
            return name + " " + LOADED;
        }catch (Exception e){
            return "Error: " + e.getMessage();
        }
    }
}
