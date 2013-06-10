package add.exam.poll.model;

import lombok.Getter;
import lombok.Setter;

public class VoteForm
{
    @Getter
    @Setter
    private Integer questionId;

    @Getter
    @Setter
    private Integer answerId;

}
