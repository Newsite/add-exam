package add.exam.group.services;

import add.exam.common.repositories.CommonRepository;
import add.exam.exceptions.ResourceNotFoundException;
import add.exam.group.repositories.GroupRepository;
import add.exam.model.group.Group;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class GroupService
{

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private CommonRepository commonRepository;

    public List<Group> getTeacherGroups(Integer userId)
    {
        return groupRepository.getTeacherGroups(userId);

    }

    public Group getGroup(Integer groupId, Integer userId)
    {
        Group group = groupRepository.getGroup(groupId, userId);
        if ( group == null){
            throw new ResourceNotFoundException(String.format("Group with id = %s for user_id = %s", groupId, userId));
        }
        return group;
    }

    public void save(Group group)
    {
        if (group.getId() != null){
            commonRepository.update(group);
            return;
        }
        commonRepository.create(group);
    }

    public List<Group> getStudentGroups(Integer userId)
    {
        return groupRepository.getStudentGroups(userId);
    }
}
