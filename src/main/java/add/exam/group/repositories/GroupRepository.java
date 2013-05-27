package add.exam.group.repositories;

import add.exam.common.repositories.CommonRepository;
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
    private static final String GET_STUDENT_GROUPS_NATIVE_QUERY = "select g.* from groups g left join students_in_groups sig on g.id = sig.group_id where sig.student_id = :studentId";

    //attributes
    private static final String USER_ID = "userId";
    private static final String GROUP_ID = "groupId";
    private static final String STUDENT_ID = "studentId";

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

    public List<Group> getStudentGroups(Integer userId)
    {
        return entityManager.createNativeQuery(GET_STUDENT_GROUPS_NATIVE_QUERY, Group.class)
                .setParameter(STUDENT_ID, userId)
                .getResultList();
    }
}
