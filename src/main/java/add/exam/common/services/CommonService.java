package add.exam.common.services;

import add.exam.common.repositories.CommonRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class CommonService
{
    @Inject
    private CommonRepository repository;

    public <T>T get(Class<T> clazz, Integer id)
    {
        return repository.get(clazz, id);
    }

    public <T> void delete(Class<T> clazz, Integer id)
    {
        repository.delete(clazz, id);
    }

    public <T>T update(T item){
        return repository.update(item);
    }

    public <T>T create(T item){
        return repository.create(item);
    }

    public <T> List<T> getRandomSubList(List<T> list, Integer subListSize){
        int size = list.size();
        List<T> subList = new ArrayList<T>();
        Random randomGenerator = new Random();
        while (subList.size() < subListSize){
            int randomInt = randomGenerator.nextInt(size);
            subList.add(list.get(randomInt));
            list.remove(randomInt);
            size--;
        }
        return subList;
    }
}
