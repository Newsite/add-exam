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
function showEditAnswerModal(id, body, correct){
    if (id == undefined){
        id = '';
        body = '';
        correct = false;
    }
    $('#answer-id').val(id);
    if(correct == 'true'){
        $('#answerCorrect').prop('checked','checked');
    }else{
        $('#answerCorrect').removeAttr('checked');
    }
    $('#editQuestionAnswerModal').modal('show');

    //save question body content
    $("#answer-body").val(body);
}

<!-- close answer edit modal -->
function closeAnswerEditModal(){
    $('#editQuestionAnswerModal').modal('hide');
}

<!-- method for saving exam question answer -->
function saveAnswer(){
    $("#answer-form").submit();
}

<!-- method for saving question -->
function saveQuestion(){
    $("#answer-question").submit();
}

<!-- method for updating exam, question status -->
function updateStatus(){
    $.ajax({
        type: "POST",
        url : 'status.html',
        data:{},
        success: function (data){
            $('#warning-alert-div').html(data);
        }
    });
}

function updatePublishCheckbox(){
    var couldNotBePublished = $('#warning-alert-div').html().length > 0 ;
    $("#published").attr("disabled", couldNotBePublished);
}

function showSuccessSaveSettingsMessage(){
    var html = "<div class='alert alert-success' ><a class='close' data-dismiss='alert'>x</a> <span> Exam settings were saved! </span></div>";
    $("#settingsAlertsDiv").html(html);
}