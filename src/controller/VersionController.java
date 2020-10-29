package controller;

import model.Language;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Properties;

public class VersionController {
    public static final int VERSION_CODE = 21;
    public static String VERSION_NAME = getFullName(getName(VERSION_CODE));
    private static final String URL_VERSION =
            "https://github.com/Matvey24/Grapher/raw/master/out/artifacts/Grapher_jar/VersionInfo.xml";
    private static String URL_APP;
    public static class UpdateInfo{
        public String changes;
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
            URL_APP = properties.getProperty("file_path");
            int newVersion = Integer.parseInt(properties.getProperty("version"));
            if(newVersion > VERSION_CODE){
                u.full_name = getFullName(getName(newVersion));
                if(!new File(u.full_name).exists()){
                    u.version_is_new = true;
                    u.changes = properties.getProperty("changes");
                }
            }
        }catch (Exception e){
            return u;
        }
        return u;
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
