function markQuestion(url) {
    var marked = $('#marked-checkbox').prop("checked");
    $.ajax({
        type: "post",
        url : url,
        data : ({
            marked : marked
        })
    });
}

function showSuccessSaveMessage(){
    var msg = $('#success-alert-div').html();
    $('#alert-div').html(msg);
}

function showWarning(){
    var msg = $('#warning-alert-div').html();
    $('#alert-div').html(msg);
}
