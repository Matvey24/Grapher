package model;

import java.io.IOException;
import java.util.Properties;

public interface Property {
    Properties getProperties(String fileName) throws IOException;
}
