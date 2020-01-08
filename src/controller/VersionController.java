package controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class VersionController {
    public static final int VERSION_CODE = 2;
    public static final String VERSION_NAME = getName(VERSION_CODE);
    private static final String URL_VERSION = "https://github.com/Matvey24/Grapher/raw/master/out/artifacts/Grapher_jar/Version.bin";
    private static final String URL_APP = "https://github.com/Matvey24/Grapher/raw/master/out/artifacts/Grapher_jar/Grapher.jar";
    private static int newVersion = -1;

    public static String getName(int code){
        int a = code / 10 + 1;
        int b = code % 10;
        return String.format("%d.%d", a, b);
    }
    public static int checkUpdates(){
        try {
            URL url = new URL(URL_VERSION);
            InputStream input = url.openStream();
            byte[] list = new byte[100];
            int i = 0;
            for(; i < list.length; ++i){
                int n = input.read();
                if(n == -1)
                    break;
                list[i] = (byte)n;
            }
            newVersion = Integer.parseInt(new String(list, 0, i));
            return (newVersion > VERSION_CODE)? newVersion : -1;
        }catch (Exception e){
            return -1;
        }
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
