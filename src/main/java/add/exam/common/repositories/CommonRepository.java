package add.exam.common.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class is used for common save, update and retrieve hibernate models
 * Author Maksym Goroshkevych
 */
@Repository
public class CommonRepository
{
    //order by id
    public static final String ORDER_BY_ID = " order by id";

    //String formatter patterns
    public static final String TWO_STRING_PATTERN = "%s%s";
    public static final String THREE_STRING_PATTERN = "%s%s%s";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional (readOnly = true)
    public <T> T get(Class<T> entityClass, Integer id){
        return entityManager.find(entityClass, id);
    }

    @Transactional
    public <T> T create(T item){
        entityManager.persist(item);
        return item;
    }

    @Transactional
    public <T> T update(T item){
        item = entityManager.merge(item);
        entityManager.persist(item);
        return item;
    }

    @Transactional
    public <T> void delete(Class<T> entityClass, Integer id)
    {
        T item = get(entityClass, id);
        if (item != null){
            entityManager.remove(item);
        }
    }
}
