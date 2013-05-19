package add.exam.model.exam;

import java.util.ArrayList;
import java.util.List;

public enum ExamAccessType
{
    PRIVATE,
    PROTECTED,
    PUBLIC,
    PAID;

    public static List<ExamAccessType> getAccessTypes(){
        List<ExamAccessType> list = new ArrayList<ExamAccessType>();
        list.add(PUBLIC);
        list.add(PROTECTED);
        list.add(PRIVATE);
        return list;
    }
}
