package add.exam.common.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class PropertiesService
{
    private Properties properties = new Properties();

    public Properties getProperties(String fileName)
    {
        try{
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return properties;
    }

    public String getProperty(String fileName, String key)
    {
        return getProperties(fileName).getProperty(key);
    }
}
