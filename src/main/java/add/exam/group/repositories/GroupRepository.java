package add.exam.group.repositories;

import add.exam.common.repository.CommonRepository;
import add.exam.model.group.Group;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class GroupRepository
{
    //queries
    private static final String GET_TEACHER_GROUPS_QUERY = "select g from Group g where user_id = :userId";
    private static final String GET_USER_GROUP_QUERY = "select g from Group g left join fetch g.exams left join fetch g.students where g.id = :groupId and g.user.id = :userId";

    //attributes
    private static final String USER_ID = "userId";
    private static final String GROUP_ID = "groupId";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Group> getTeacherGroups(Integer userId)
    {
        return entityManager.createQuery(String.format(CommonRepository.TWO_STRING_PATTERN, GET_TEACHER_GROUPS_QUERY, CommonRepository.ORDER_BY_ID), Group.class)
            .setParameter(USER_ID, userId)
            .getResultList();
    }

    @Transactional(readOnly = true)
    public Group getGroup(Integer groupId, Integer userId)
    {
        List<Group> groups = entityManager.createQuery(GET_USER_GROUP_QUERY, Group.class)
                .setParameter(GROUP_ID, groupId)
                .setParameter(USER_ID, userId)
                .getResultList();
        if (groups.isEmpty()){
            return null;
        }
        return groups.get(0);
    }
}