<!-- configuration for exam autocomplete -->
$('#examSearchInput').typeahead({
    source: function (query, process) {
        map = {};
        groupId = $("#groupId").val();
        return $.ajax({
            type: "post",
            url: "/search/exams.html",
            data:({
                groupId: groupId,
                pattern: query
            }),
            success: function(data){
                var list = [];
                $("#hiddenInputs").html(data);
                $("#hiddenInputs option").each(function(){
                    map[$(this).text()] = $(this).val();
                    list.push($(this).text());
                });
                $("#hiddenInputs").html('');
                return process(list);
            }

        });
    },
    updater: function (obj) {
        $("#examSearchInput").attr('disabled', true);
        $("#addExamButton").attr('disabled', false);
        $("#examIdHiddenInput").val(map[obj]);
        return obj;
    }
});

<!-- configuration for poll autocomplete -->
$('#pollSearchInput').typeahead({
    source: function (query, process) {
        map = {};
        groupId = $("#groupId").val();
        return $.ajax({
            type: "post",
            url: "/search/polls.html",
            data:({
                groupId: groupId,
                pattern: query
            }),
            success: function(data){
                var list = [];
                $("#hiddenInputs").html(data);
                $("#hiddenInputs option").each(function(){
                    map[$(this).text()] = $(this).val();
                    list.push($(this).text());
                });
                $("#hiddenInputs").html('');
                return process(list);
            }

        });
    },
    updater: function (obj) {
        $("#pollSearchInput").attr('disabled', true);
        $("#addPollButton").attr('disabled', false);
        $("#pollIdHiddenInput").val(map[obj]);
        return obj;
    }
});

<!-- configuration for student autocomplete -->
$('#studentSearchInput').typeahead({
    source: function (query, process) {
        map = {};
        groupId = $("#groupId").val();
        return $.ajax({
            type: "post",
            url: "/search/students.html",
            data:({
                groupId: groupId,
                pattern: query
            }),
            success: function(data){
                var list = [];
                $("#hiddenInputs").html(data);
                $("#hiddenInputs option").each(function(){
                    map[$(this).text()] = $(this).val();
                    list.push($(this).text());
                });
                $("#hiddenInputs").html('');
                return process(list);
            }

        });
    },
    updater: function (obj) {
        $("#studentSearchInput").attr('disabled', true);
        $("#addStudentButton").attr('disabled', false);
        $("#studentIdHiddenInput").val(map[obj]);
        return obj;
    }
});

function showAddExamModal(){
    clearExamSearchInput();
    $("#addExamModal").modal('show');
}

function clearExamSearchInput(){
    $("#examSearchInput").attr('disabled', false);
    $("#addExamButton").attr('disabled', true);
    $("#examSearchInput").val('');
}

function addExam(){
    var examId = $("#examIdHiddenInput").val();
    var groupId = $("#groupId").val();
    $.ajax({
        type: "post",
        url: "/group/" + groupId + "/add/exam.html",
        data:({
            examId: examId
        }),
        success: function(data){
            data = data.substring(data.indexOf('<body>') + 1);
            data = data.replace("</body></html>", "");
            $('#examsTable tr:last').after(data);
            $("#addExamModal").modal('hide');
        }
    });
}

function showAddPollModal(){
    clearPollSearchInput();
    $("#addPollModal").modal('show');
}

function clearPollSearchInput(){
    $("#pollSearchInput").attr('disabled', false);
    $("#addPollButton").attr('disabled', true);
    $("#pollSearchInput").val('');
}

function addPoll(){
    var pollId = $("#pollIdHiddenInput").val();
    var groupId = $("#groupId").val();
    $.ajax({
        type: "post",
        url: "/group/" + groupId + "/add/poll.html",
        data:({
            pollId: pollId
        }),
        success: function(data){
            data = data.substring(data.indexOf('<body>') + 6);
            data = data.replace("</body></html>", "");
            $('#pollsTable tr:last').after(data);
            $("#addPollModal").modal('hide');
        }
    });
}

function showRemoveExamModal(id){
    $('#examIdHiddenInput').val(id);
    $("#deleteExamModal").modal('show');
}

<!-- method for removing exam from group-->
function removeExam(){
    var examId = $('#examIdHiddenInput').val();
    var groupId = $("#groupId").val();
    var url = '/group/' + groupId + '/exam/'+ examId + '.html';
    var rowId = '#examRow-' + examId;
    $.ajax({
        type: "delete",
        url : url,
        success : function() {
            $('#deleteExamModal').modal('hide');
            $(rowId).remove();
        }
    });
}

function showRemovePollModal(id){
    $('#pollIdHiddenInput').val(id);
    $("#deletePollModal").modal('show');
}

<!-- method for removing exam from group-->
function removePoll(){
    var pollId = $('#pollIdHiddenInput').val();
    var groupId = $("#groupId").val();
    var url = '/group/' + groupId + '/poll/'+ pollId + '.html';
    var rowId = '#pollRow-' + pollId;
    $.ajax({
        type: "delete",
        url : url,
        success : function() {
            $(rowId).remove();
            $('#deletePollModal').modal('hide');
        }
    });
}

function showAddStudentModal(){
    clearStudentSearchInput();
    $("#addStudentModal").modal('show');
}

function clearStudentSearchInput(){
    $("#studentSearchInput").attr('disabled', false);
    $("#addStudentButton").attr('disabled', true);
    $("#studentSearchInput").val('');
}

function addStudent(){
    var studentId = $("#studentIdHiddenInput").val();
    var groupId = $("#groupId").val();
    $.ajax({
        type: "post",
        url: "/group/" + groupId + "/add/student.html",
        data:({
            studentId: studentId
        }),
        success: function(data){
            data = data.substring(data.indexOf('<body>') + 1);
            data = data.replace("</body></html>", "");
            $('#studentsTable tr:last').after(data);
            $("#addStudentModal").modal('hide');
        }
    });
}

<!-- method for showing remove student from group modal -->
function showRemoveStudentModal(id){
    $('#studentIdHiddenInput').val(id);
    $("#deleteStudentModal").modal('show');
}

<!-- method for removing student from group-->
function removeStudent(){
    var studentId = $('#studentIdHiddenInput').val();
    var groupId = $("#groupId").val();
    var url = '/group/' + groupId + '/student/'+ studentId + '.html';
    var rowId = '#studentRow-' + studentId;
    $.ajax({
        type: "delete",
        url : url,
        success : function() {
            $('#deleteStudentModal').modal('hide');
            $(rowId).remove();
        }
    });
}