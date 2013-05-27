<!-- show teacher report-->
function teacherReport(){
    var dateFrom = $('#dateFromInput').val();
    var dateTo = $('#dateToInput').val();
    $.ajax({
        type: "post",
        url : "reports/teacher.html",
        data: ({
            dateFrom: dateFrom,
            dateTo: dateTo
        }),
        success : function(data) {
            data = data.substring(data.indexOf('<body>') + 6);
            data = data.replace("</body></html>", "");
            $("#examsReportTableBody").html(data);
        }
    });
}

<!-- show student report-->
function studentReport(){
    var dateFrom = $('#dateFromInput').val();
    var dateTo = $('#dateToInput').val();
    $.ajax({
        type: "post",
        url : "reports/student.html",
        data: ({
            dateFrom: dateFrom,
            dateTo: dateTo
        }),
        success : function(data) {
            data = data.substring(data.indexOf('<body>') + 6);
            data = data.replace("</body></html>", "");

            $("#attemptsReportTableBody").html(data);
            var html = "";
            var averageScore = $("#averageScore").val();
            if (averageScore.length > 0){
                var averageTime = $("#averageTime").val();
                html = html + "<p><b>Average Score: </b> " + averageScore + "</p><p><b>Average Time: </b> " + averageTime + "</p>";
            }
            $("#totalStatsDiv").html(html);
        }
    });
}

<!-- shows teacher exam report in selected date range-->
function teacherExamReport(){
    var dateFrom = $('#dateFromInput').val();
    var dateTo = $('#dateToInput').val();
    var examId = $('#examIdInput').val();
    $.ajax({
        type: "post",
        url : "/reports/exam/" + examId +".html",
        data: ({
            dateFrom: dateFrom,
            dateTo: dateTo
        }),
        success : function(data) {
            data = data.substring(data.indexOf('<body>') + 6);
            data = data.replace("</body></html>", "");

            $("#examReportTableBody").html(data);
            var html = "";
            var averageScore = $("#averageScore").val();
            if (averageScore.length > 0){
                var averageTime = $("#averageTime").val();
                var bestScore = $("#bestScore").val();
                var bestStudent = $("#bestStudent").val();
                html = html + "<p><b>Best Score: </b> " + bestScore + "</p><p><b>Best Student: </b> " + bestStudent + "</p><br/>" +
                    "<p><b>Average Score: </b> " + averageScore + "</p><p><b>Average Time: </b> " + averageTime + "</p>";
            }
            $("#totalStatsDiv").html(html);
        }
    });
}