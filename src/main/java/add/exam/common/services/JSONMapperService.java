package add.exam.common.services;

import add.exam.attempt.model.UserAnswer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class JSONMapperService
{
    ObjectMapper mapper = new ObjectMapper();

    public <T> String toJSONString(T item){
        try
        {
            return mapper.writeValueAsString(item);
        }
        catch (IOException e)
        {
            return "";
        }
    }

    public <T>T toObject(Class<T> clazz, String json){
        try
        {
            return mapper.readValue(json, clazz);
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public List<UserAnswer> toUserAnswerList(String json){
        try
        {
            return mapper.readValue(json, new TypeReference<List<UserAnswer>>(){});
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
