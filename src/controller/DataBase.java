package controller;

import model.Language;
import model.FullModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataBase {
    public String save(FullModel m, File f){
        Properties properties = new Properties();
        properties.setProperty("graphs_count", "" + m.graphics.size());
        for(int i = 0; i < m.graphics.size(); ++i) {
            properties.setProperty("graphic" + i, m.graphics.get(i));
            properties.setProperty("graphic_info" + i, m.graphics_info.get(i));
        }
        properties.setProperty("functions", m.functions);
        properties.setProperty("calculator", m.calculator);
        properties.setProperty("timer_info", m.timer_info);
        properties.setProperty("resize_idx", m.resize_idx);
        properties.setProperty("view_params", m.view_params);
        properties.setProperty("main_settings", m.main_settings);
        try (FileOutputStream fos = new FileOutputStream(f)){
            properties.storeToXML(fos, "Edit it if you are a geek!");
        }catch (IOException e){
            return e.getMessage();
        }
        return Language.SAVED + " " + f.getName();
    }
    public FullModel load(File f) throws IOException{
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(f);
        properties.loadFromXML(fis);
        fis.close();
        FullModel m = new FullModel();
        int n = Integer.parseInt(properties.getProperty("graphs_count", "0"));
        List<String> graphs = new ArrayList<>();
        List<String> graph_info = new ArrayList<>();
        for(int i = 0; i < n; ++i){
            graphs.add(properties.getProperty("graphic" + i, ""));
            graph_info.add(properties.getProperty("graphic_info" + i, ""));
        }
        m.graphics = graphs;
        m.graphics_info = graph_info;
        m.functions = properties.getProperty("functions", "");
        m.calculator = properties.getProperty("calculator", "");
        m.timer_info = properties.getProperty("timer_info", "");
        m.main_settings = properties.getProperty("main_settings","");
        m.resize_idx = properties.getProperty("resize_idx", "");
        m.view_params = properties.getProperty("view_params", "");
        return m;
    }
}
