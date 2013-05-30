<!-- shows remove question modal -->
function showRemoveQuestionModal(id){
    $('#questionIdHiddenInput').val(id);
    $('#deleteExamQuestionModal').modal('show');
}

<!-- method for removing exam question-->
function removeQuestion(){
    var id = $('#questionIdHiddenInput').val();
    var url = 'question/' + id + '.html';
    var rowId = '#questionRow-' + id;
    $.ajax({
        type: "delete",
        url : url,
        success : function() {
            updateStatus();
            $('#deleteExamQuestionModal').modal('hide');
            $(rowId).remove();
            updatePublishCheckbox();
        }
    });
}

<!-- shows remove answer modal -->
function showRemoveAnswerModal(id){
    $('#answerIdHiddenInput').val(id);
    $('#deleteQuestionAnswerModal').modal('show');
}

<!-- method for removing exam question answer-->
function removeAnswer(){
    var id = $('#answerIdHiddenInput').val();
    var url = 'answer/' + id + '.html';
    var rowId = '#answerRow-' + id;
    $.ajax({
        type: "delete",
        url : url,
        success : function() {
            $(rowId).remove();
            var html = "<button class='btn btn-primary btn-large' onclick='showEditAnswerModal()'>Add answer</button>";
            $("#addAnswerButtonDiv").html(html);
            $('#deleteQuestionAnswerModal').modal('hide');
        }
    });
}

<!-- shows edit answer modal -->
function showEditAnswerModal(id, body){
    if (id == undefined){
        id = '';
        body = '';
    }
    $('#answer-id').val(id);
    $("#answer-body").val(body);
    $('#editQuestionAnswerModal').modal('show');
}

<!-- close answer edit modal -->
function closeAnswerEditModal(){
    $('#editQuestionAnswerModal').modal('hide');
}

<!-- method for saving exam question answer -->
function saveAnswer(){
    $("#answer-form").submit();
}
