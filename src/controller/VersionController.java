package controller;

import model.Language;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class VersionController {
    public static final int VERSION_CODE = 34;
    public static String VERSION_NAME = getFullName(getName(VERSION_CODE));
    private static final String URL_VERSION =
            "https://github.com/Matvey24/Grapher/raw/master/out/artifacts/Grapher_jar/VersionInfo.xml";
    private static String URL_APP;
    private static String URL_LOG;
    public static class UpdateInfo{
        public String full_name;
        public boolean version_is_new;
    }
    public static String getName(int code){
        int a = code / 10 + 1;
        int b = code % 10;
        return String.format("%d.%d", a, b);
    }
    public static String getFullName(String versionName){
        return Language.GRAPHER + versionName + ".jar";
    }
    public static UpdateInfo checkUpdates(){
        UpdateInfo u = new UpdateInfo();
        try {
            URL url = new URL(URL_VERSION);
            InputStream input = url.openStream();
            Properties properties = new Properties();
            properties.loadFromXML(input);
            URL_APP = properties.getProperty("file_path", "");
            URL_LOG = properties.getProperty("log_path", "");
            int newVersion = Integer.parseInt(properties.getProperty("version"));
            if(newVersion > VERSION_CODE){
                u.full_name = getFullName(getName(newVersion));
                if(!new File(u.full_name).exists()){
                    u.version_is_new = true;
                }
            }
        }catch (Exception e){
            return u;
        }
        return u;
    }
    public static String[][] loadLog(){
        if(URL_LOG == null){
            checkUpdates();
        }
        if(URL_LOG == null){
            return null;
        }
        try{
            URL url = new URL(URL_LOG);
            InputStream input = url.openStream();
            Properties properties = new Properties();
            properties.loadFromXML(input);
            Set<String> names = properties.stringPropertyNames();
            String[][] log = new String[names.size()][];
            Iterator<String> s = names.stream().sorted().iterator();
            for(int i = 0; s.hasNext(); ++i){
                String text = s.next();
                log[names.size() - i - 1] = properties.getProperty(text).split("\\n");
            }
            return log;
        }catch (Exception e){
            return null;
        }
    }
    public static boolean update(UpdateInfo info){
        File newFile = new File(info.full_name);
        try{
            URL url = new URL(URL_APP);
            InputStream input = url.openStream();
            Files.copy(input, newFile.toPath());
        }catch (IOException e){
            return false;
        }
        return true;
    }
    public static void updateLanguage(){
        VERSION_NAME = null;
        VERSION_NAME = getFullName(getName(VERSION_CODE));
    }
}
