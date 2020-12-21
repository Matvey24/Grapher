package model;

import calculator2.calculator.CalcLanguage;
import model.help.Property;
import view.MainPanel;

import java.io.IOException;
import java.util.*;

public class Language {
    public static String GRAPHER;
    //settings
    public static String DISCRETIZATION;
    public static String FEELS_TIME;
    public static String COLOR_CHOOSER;
    public static String[] TYPE_TITLES;
    //actions
    public static String MAKE;
    public static String APPLY;
    //implicit
    public static String SENSITIVITY;
    public static String VIEW_COLOR;
    public static String[] COLORS;
    public static String SAVE_PICTURE;
    //translation
    public static String LINES_PER_CELL;
    //parametric
    public static String DIMENSION;
    //timer
    public static String DURATION_FPS;
    public static String BEGIN;
    public static String BOOMERANG;
    //main settings
    public static String[] SETTINGS;
    //states
    public static String CONVERTING;
    public static String UPDATING;
    public static String FINE;
    public static String LOADED;
    public static String SAVED;
    //errors
    public static String[] UPDATER_ERRORS;
    public static String LOADING_LOG;
    //gui
    //help
    public static String HELP;
    public static String[] HELP_NAMES;
    public static String VERSION_LOG;
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
    public static String DEFAULT_LANGUAGE = "English";

    public static void onCreate(Property p){
        property = p;
        language_Names = new ArrayList<>();
    }

    public static String loadLanguage(String name) {
        try {
            Properties properties = property.getProperties(name + ".l");
            String[] arr;
            GRAPHER = properties.getProperty("name");
            TYPE_TITLES = properties.getProperty("titles").split("\n");
            arr = properties.getProperty("settings").split("\n");
            DISCRETIZATION = arr[0];
            SENSITIVITY = arr[1];
            VIEW_COLOR = arr[2];
            DIMENSION = arr[3];
            LINES_PER_CELL = arr[4];
            SAVE_PICTURE = arr[5];
            DURATION_FPS = arr[6];
            BOOMERANG = arr[7];
            FEELS_TIME = arr[8];
            COLOR_CHOOSER = arr[9];
            COLORS = properties.getProperty("colors").split("\n");
            arr = properties.getProperty("states").split("\n");
            CONVERTING = arr[0];
            UPDATING = arr[1];
            FINE = arr[2];
            LOADED = arr[3];
            SAVED = arr[4];
            UPDATER_ERRORS = properties.getProperty("updater_errors").split("\n");
            CalcLanguage.CALCULATOR_ERRORS = properties.getProperty("calculator_errors").split("\n");
            CalcLanguage.PARSER_ERRORS = properties.getProperty("parser_errors").split("\n");
            arr = properties.getProperty("buttons").split("\n");
            BEGIN = arr[0];
            MAIN_SETTINGS = arr[1];
            TIMER = arr[2];
            SAVE_PRJ = arr[3];
            LOAD_PRJ = arr[4];
            arr = properties.getProperty("actions").split("\n");
            BACK = arr[0];
            UPDATE = arr[1];
            MAKE = arr[2];
            APPLY = arr[3];
            arr = properties.getProperty("helpers").split("\n");
            HELP = arr[0];
            HELP_NAMES = new String[]{arr[1], arr[2], arr[3]};
            VERSION_LOG = arr[4];
            LOADING_LOG = properties.getProperty("version_controller");
            RESIZERS = properties.getProperty("resizers").split("\n");
            arr = properties.getProperty("text_elements").split("\n");
            GRAPHICS = arr[0];
            FUNCTIONS = arr[1];
            CALCULATOR = arr[2];
            SETTINGS = properties.getProperty("main_settings").split("\n");
            //////////////////////////////////////////////
            String s = properties.getProperty("help");
            List<String> list = Arrays.asList(s.split("\n"));
            Iterator<String> it = list.iterator();
            HELPERS = new String[3][][];
            for (int i = 0; i < 3; ++i) {
                arr = it.next().split(" ");
                if (!arr[0].equals("help")) {
                    return "no part 'help'";
                }
                int n = Integer.parseInt(arr[1]);
                HELPERS[i] = new String[n][];
                for (int j = 0; j < n; ++j) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.toString();
        }
    }
}
