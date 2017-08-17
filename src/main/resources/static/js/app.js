$(document).ready(function () {

    function showDialog() {
        $("#inputIssueTitle").val('');
        $("#inputIssueBody").val('');
        $("#submitForm").prop('disabled', true);
        $("#inputIssueTitle").keyup(function() {
            $("#submitForm").prop('disabled',  isEmpty($("#inputIssueTitle").val()));
        });

        $("#myModal").modal('show');
    };

    function isEmpty(str) {
        return (!str || 0 === str.trim().length);
    }

    function createIssue() {
        var title = $("#inputIssueTitle").val();
        var body = $("#inputIssueBody").val();

        $.ajax('/api/issues', {
            data: JSON.stringify({title: title, body: body}),
            contentType: 'application/json',
            type: 'POST',
            success: function (data) {
                alert("Issue created")
            },
            error: function () {
                alert('Error occured');
            }

        });

        $("#myModal").modal('hide');
    }

    $("#submitForm").click(createIssue);
    $("#showModal").click(showDialog);
});


