package add.exam.model.user;

import java.util.ArrayList;
import java.util.List;

public enum UserRole
{
    ROLE_TEACHER, ROLE_STUDENT, ROLE_ADMIN;

    public static List<UserRole> getUserRoles(){
        List<UserRole> list = new ArrayList<UserRole>();
        list.add(ROLE_TEACHER);
        list.add(ROLE_STUDENT);
        return list;
    }
}
