
function stopPrg() {
    stop = true;
    $("#given").fadeOut("normal", function () {
        $(this).empty();
    });

    $("#given").fadeIn("normal", function () {
        $(this).append("<center><h1><span><span class='glyphicon glyphicon-thumbs-up'></span> Congratulation</span></h1><br>\n\
            <h2 class='text-info'>" + correctWords + "(wpm)</h2>\n\
            </br><h3>\n\
            <table class='table'><tr><td>Total of words</td><td>" + totalWords + "</td></tr><tr><td>Total of characters</td><td> " + numChar + "</td></tr></table>\n\
            </h3></center>");
    });

    $('#result1').val(correctWords);
    $('#result2').val(totalWords);
    $('#result3').val(numChar);

    // add 07032015
    $('#wpm').val(correctWords);

}

$(".sbmOpt").click(function () {
    $("#optForm").submit();
})

$(document).ready(function () {
    $("input[value='2_pb']").prop('checked', true);

    $('#typed').keyup(function (evt) {
        if ((evt.which === 32) && (stop === true)) {
            // set empty
            $('#typed').val("");
        }
    })
})

// upload file
$(function () {
    if ($("#publicRecord").length) {
        update_records_table(null);
    };

    if ($("#privateRecord").length) {
        var account_id=$("input[name='account']").val();
        update_records_table(account_id);
    };
})

format = function date2str(x, y) {
    var z = {
        M: x.getMonth() + 1,
        d: x.getDate(),
        h: x.getHours(),
        m: x.getMinutes(),
        s: x.getSeconds()
    };
    y = y.replace(/(M+|d+|h+|m+|s+)/g, function(v) {
        return ((v.length > 1 ? "0" : "") + eval('z.' + v.slice(-1))).slice(-2)
    });

    return y.replace(/(y+)/g, function(v) {
        return x.getFullYear().toString().slice(-v.length)
    });
}


function update_records_table(account_id)
{
    $("#records_tbl_body").html("");

    if (account_id!=null) {
        $.getJSON("/api/accounts/"+account_id, function(data){
            username=data.username;
        })

        $.ajax({
            method:"GET",
            dataType:"json",
            url:"/api/accounts/"+account_id+"/records?sort=WPM&name.dir=desc&page=0&size=10",
            success:function(jsonData)
            {
                append_record(jsonData._embedded.records,username);
            },
            error:function()
            {

            }
        })
    }else{
        // Public
        username="Anonynous";

        $.ajax({
            method:"GET",
            dataType:"json",
            url:"/api/records/search/findByisPublic?isPublic=true&sort=WPM,desc&page=0&size=10",
            success:function(jsonData)
            {
                append_record(jsonData._embedded.records,username);
            },
            error:function()
            {

            }
        })
    }
    


}

function append_record(jsonRecords,username)
{
    counter=0;
    for (var i = jsonRecords.length-1; i >=0; i--) {
        counter++;
        rec_date=jsonRecords[i].createdDate;
        rec_date=new Date(rec_date);
        rec_date=rec_date.getDate() + "/" + rec_date.getMonth() + "/" + rec_date.getFullYear();

        if (counter<=10) {
            $("#records_tbl_body").append("<tr><td><h6><span class=\"text-info\">"+(counter)+".</span></h6></td><td><h5>"+jsonRecords[i].wpm+"</h5></td><td><h6><i>"+username+"</i></h6></td><td><h6 class=\"text-muted\">"+rec_date+"</h6></td></tr>");
        };
    };
}