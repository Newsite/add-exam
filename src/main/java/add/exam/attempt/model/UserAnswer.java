package add.exam.attempt.model;

import lombok.Getter;
import lombok.Setter;

public class UserAnswer
{
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String body;

    @Getter
    @Setter
    private Boolean selected = false;

    @Getter
    @Setter
    private Boolean right;
}
