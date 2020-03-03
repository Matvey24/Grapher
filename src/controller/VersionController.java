package controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Properties;

public class VersionController {
    public static final int VERSION_CODE = 10;
    public static final String VERSION_NAME = getName(VERSION_CODE);
    private static final String URL_VERSION =
            "https://github.com/Matvey24/Grapher/raw/master/out/artifacts/Grapher_jar/VersionInfo.xml";
    private static int newVersion = -1;
    private static String URL_APP;
    public static class UpdateInfo{
        public String changes;
        public String version_name;
        public boolean version_is_new;
    }
    public static String getName(int code){
        int a = code / 10 + 1;
        int b = code % 10;
        return String.format("%d.%d", a, b);
    }
    public static UpdateInfo checkUpdates(){
        UpdateInfo u = new UpdateInfo();
        try {
            URL url = new URL(URL_VERSION);
            InputStream input = url.openStream();
            Properties properties = new Properties();
            properties.loadFromXML(input);
            URL_APP = properties.getProperty("file_path");
            newVersion = Integer.parseInt(properties.getProperty("version"));
            if(newVersion > VERSION_CODE){
                u.version_is_new = true;
                u.version_name = getName(newVersion);
                u.changes = properties.getProperty("changes");
            }
        }catch (Exception e){
            return u;
        }
        return u;
    }
    public static boolean update(){
        File newFile = findFileName();
        try{
            URL url = new URL(URL_APP);
            InputStream input = url.openStream();
            Files.copy(input, newFile.toPath());
        }catch (IOException e){
            return false;
        }
        return true;
    }
    private static File findFileName(){
        String mainName = "Grapher" + getName(newVersion);
        File f = new File(mainName + ".jar");
        int i = 0;
        while(f.exists()){
            ++i;
            f = new File(mainName + " (" + i + ").jar");
        }
        return f;
    }
}
