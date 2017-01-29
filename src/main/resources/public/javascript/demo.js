
function dump(obj) {
    var out = '';
    for (var i in obj) {
        out += i + ": " + obj[i] + "\n";
    }

    alert(out);
}

$(document).ready(function () {
    var $calendar = $('#calendar');

    $calendar.weekCalendar({
        users: ['All reservations'],
        showAsSeparateUser: true,
        displayOddEven: true,
        timeslotsPerHour: 4,
        timeslotHeight: 20,
        allowCalEventOverlap: true,
        overlapEventsSeparate: true,
        firstDayOfWeek: 1,
        businessHours: {start: 0, end: 24, limitDisplay: true},
        daysToShow: 3,
        switchDisplay: {'1 Day': 1, '3 next days': 3, 'Work week': 5, 'Full week': 7},
        // switchDisplay: {'1 day': 1, '3 next days': 3, 'work week': 5, 'full week': 7},
        title: function (daysToShow) {
            return daysToShow == 1 ? '%date%' : '%start% - %end%';
        },
        height: function ($calendar) {
            return $(window).height() - $("h1").outerHeight() - 1;
        },
        eventRender: function (calEvent, $event) {
            if (calEvent.end.getTime() < new Date().getTime()) {
                $event.css("backgroundColor", "#aaa");
                $event.find(".wc-time").css({
                    "backgroundColor": "#999",
                    "border": "1px solid #888"
                });
            }
            // Tulevat ajat ja renderit
            if (calEvent.end.getTime() > new Date().getTime()) {
                $event.css("backgroundColor", calEvent.bgCol);
                $event.find(".wc-time").css({
                    "backgroundColor": "#999",
                    "border": "1px solid #888"
                });
            }
        },
        draggable: function (calEvent, $event) {
            return calEvent.readOnly != true;
        },
        resizable: function (calEvent, $event) {
            return calEvent.readOnly != true;
        },
        eventNew: function (calEvent, $event) {
            // $event.data('preventClick', true);

            if (isAuthenticated) {
                var $dialogContent = $("#event_edit_container");
                resetForm($dialogContent);
                var startField = $dialogContent.find("select[name='start']").val(calEvent.start);
                var endField = $dialogContent.find("select[name='end']").val(calEvent.end);
                var titleField = $dialogContent.find("input[name='title']");
                var bodyField = $dialogContent.find("textarea[name='body']");

                var startField2 = calEvent.start;
                var d = startField2.getDate();
                var m = startField2.getMonth();
                m++;
                var y = startField2.getFullYear();

                if (m < 10) {
                    m = "0" + m;
                }
                if (d < 10) {
                    d = "0" + d;
                }
                var hour1 = startField2.getHours();
                var min1 = startField2.getMinutes();
                if (min1 < 10) {
                    min1 = "0" + min1;
                }
                if (hour1 < 10) {
                    hour1 = "0" + hour1;
                }

                var ds = d + "." + m + "." + y;
                var hs = hour1 + "." + min1;

                var endField2 = calEvent.end;
                var d = endField2.getDate();
                var m = endField2.getMonth();
                m++;
                var y = endField2.getFullYear();

                if (m < 10) {
                    m = "0" + m;
                }
                if (d < 10) {
                    d = "0" + d;
                }
                var hour2 = endField2.getHours();
                var min2 = endField2.getMinutes();
                if (min2 < 10) {
                    min2 = "0" + min2;
                }
                if (hour2 < 10) {
                    hour2 = "0" + hour2;
                }

                var de = d + "." + m + "." + y;
                var he = hour2 + "." + min2;

                // window.location = "muokkaa-tilauslomake?ds=" + ds + "&hs=" + hs + "&de=" + de + "&he=" + he + "&h1=" + hour1 + "&m1=" + min1 + "&h2=" + hour2 + "&m2=" + min2;
                $dialogContent.find("input[name='start_date']").val(ds);
                $dialogContent.find("input[name='start_time']").val(hour1 + ":" + min1);
                $dialogContent.find("input[name='end_date']").val(de);
                $dialogContent.find("input[name='end_time']").val(hour2 + ":" + min2);
                $dialogContent.find("span[name='customer']").html(customer_name);
                        
                if (calEvent.end.getTime() > new Date().getTime()) {
                    $dialogContent.dialog({
                        modal: true,
                        // title: "Subject - " + calEvent.address,
                        title: "New reservation",
                        close: function () {
                            $dialogContent.dialog("destroy");
                            $dialogContent.hide();
                            $('#calendar').weekCalendar("removeUnsavedEvents");
                        },

                        buttons: {
                            'Create': function () {                                
                                $.ajax({
                                    type: "POST",
                                    url: "/reservations",
                                    cache: false,
                                    data: $("#event_edit_form").serialize(),
                                    success: function (data)
                                    {
                                        if (data.indexOf("OK") > -1) {
                                            calEvent.id
                                            calEvent.address = $dialogContent.find("textarea[name='subject']").val();
                                            calEvent.customer = customer_name;
                                            
                                            calEvent.title = "<br><b>Customer: </b>" + calEvent.customer + "<br><b>Purpose: </b>" + calEvent.address;
                                            calEvent.bgCol = "#1B7BB9";
                                            calEvent.userId = 0;
                                            calEvent.readOnly = true;
                                            // calEvent.css("backgroundColor", "#97273C");
                                            
                                            $dialogContent.dialog("close");
                                            $calendar.weekCalendar("updateEvent", calEvent);
                                            location.reload();
                                            
                                            // $dialogContent.dialog("close");
                                        } else
                                        {
                                            alert(data);
                                        }
                                    },
                                    error: function ()
                                    {
                                        alert("Data cannot be saved. Please try again!");
                                    }
                                })
                            },
                            'Close': function () {
                                $dialogContent.dialog("close");
                            }
                        }
                    }).show();
                } else
                {
                    $calendar.weekCalendar("removeEvent", calEvent.id);
                    simple_alert("Cannot create an event from the old time.");
                }
            } else
            {
                window.location = "/login";
            }
        },
        eventDrop: function (calEvent, $event) {

        },
        eventResize: function (calEvent, $event) {


        },
        eventClick: function (calEvent, $event) {
            var $dialogContent = $("#event_edit_container");
            resetForm($dialogContent);

            var startField2 = calEvent.start;
            var hour1 = startField2.getHours();
            var min1 = startField2.getMinutes();
            if (min1 < 10) {
                min1 = "0" + min1;
            }
            if (hour1 < 10) {
                hour1 = "0" + hour1;
            }
            var hsStart = hour1 + ":" + min1;

            var startField = calEvent.end;
            var hour = startField.getHours();
            var min = startField.getMinutes();
            if (min < 10) {
                min = "0" + min;
            }
            if (hour < 10) {
                hour = "0" + hour;
            }
            var hsEnd = hour + ":" + min;

            // Mod. Anh. 26012017
            // var startField = $dialogContent.find("span[name='start']").text(hsStart + ' - ' + hsEnd);
            // var endField = $dialogContent.find("select[name='end']").val(calEvent.end);
            // var addressField = $dialogContent.find("span[name='address']").text(calEvent.address);
            // var addressField = $dialogContent.find("input[name='address']").val(calEvent.address);
            // var customerField = $dialogContent.find("span[name='customer']").text(calEvent.customer);
            //var bodyField = $dialogContent.find("textarea[name='body']");
            //bodyField.val(calEvent.body);
            var d = calEvent.start.getDate();
            var m = calEvent.start.getMonth();
            m++;
            var y = calEvent.start.getFullYear();

            $dialogContent.find("input[name='start_date']").val(d + "/" + m + "/" + y);
            $dialogContent.find("input[name='start_time']").val(hsStart);

            var d = calEvent.end.getDate();
            var m = calEvent.end.getMonth();
            m++;
            var y = calEvent.end.getFullYear();

            $dialogContent.find("input[name='end_date']").val(d + "/" + m + "/" + y);
            $dialogContent.find("input[name='end_time']").val(hsEnd);
            $dialogContent.find("span[name='customer']").text(calEvent.customer);
            $dialogContent.find("textarea[name='subject']").val(calEvent.address);

            if (calEvent.end.getTime() > new Date().getTime()) {
                $dialogContent.dialog({
                    modal: true,
                    // title: "Subject - " + calEvent.address,
                    title: "Reservation info",
                    close: function () {
                        $dialogContent.dialog("destroy");
                        $dialogContent.hide();
                        $('#calendar').weekCalendar("removeUnsavedEvents");
                    },

                    buttons: {
                        "Delete": function () {
                            if (isAuthenticated) {
                                var confirmVal = confirm('Are you sure that you want to delete this event?');
                            if (confirmVal == true) {
                                $.ajax({
                                    type: "DELETE",
                                    url: "/reservations?id="+calEvent.id,
                                    cache: false,
                                    success: function (html) {
                                            if (html.indexOf("OK")>-1) {
                                                $calendar.weekCalendar("removeEvent", calEvent.id);
                                        $dialogContent.dialog("close");
                                            }
                                            else
                                            {
                                                simple_alert(html);
                                            }
                                        
                                    }
                                })
                            }
                            }else
                            {
                                window.location = "/login";
                            }
                        },
                        'Close': function () {
                            $dialogContent.dialog("close");
                        }
                    }
                }).show();
            } else {
                $dialogContent.dialog({
                    modal: true,
                    title: "Reservation info",
                    close: function () {
                        $dialogContent.dialog("destroy");
                        $dialogContent.hide();
                        $('#calendar').weekCalendar("removeUnsavedEvents");
                    },
                    buttons: {
                        cancel: function () {
                            $dialogContent.dialog("close");
                        }
                    }
                }).show();
            }

            var startField = $dialogContent.find("select[name='start']").val(calEvent.start);
            var endField = $dialogContent.find("select[name='end']").val(calEvent.end);
            $dialogContent.find(".date_holder").text($calendar.weekCalendar("formatDate", calEvent.start));

            setupStartAndEndTimeFields(startField, endField, calEvent, $calendar.weekCalendar("getTimeslotTimes", calEvent.start));

            $(window).resize().resize(); //fixes a bug in modal overlay size ??
        },
        eventMouseover: function (calEvent, $event) {
        },
        eventMouseout: function (calEvent, $event) {
        },
        noEvents: function () {

        },
        data: function (start, end, callback) {
            callback(getEventData());
        }
    });

    function resetForm($dialogContent) {
        $dialogContent.find("input").val("");
        $dialogContent.find("textarea").val("");
        $dialogContent.find("span").html("");
    }
    /*
     * Sets up the start and end time fields in the calendar event
     * form for editing based on the calendar event being edited
     */
    function setupStartAndEndTimeFields($startTimeField, $endTimeField, calEvent, timeslotTimes) {

        $startTimeField.empty();
        $endTimeField.empty();

        for (var i = 0; i < timeslotTimes.length; i++) {
            var startTime = timeslotTimes[i].start;
            var endTime = timeslotTimes[i].end;
            var startSelected = "";
            if (startTime.getTime() === calEvent.start.getTime()) {
                startSelected = "selected=\"selected\"";
            }
            var endSelected = "";
            if (endTime.getTime() === calEvent.end.getTime()) {
                endSelected = "selected=\"selected\"";
            }
            $startTimeField.append("<option value=\"" + startTime + "\" " + startSelected + ">" + timeslotTimes[i].startFormatted + "</option>");
            $endTimeField.append("<option value=\"" + endTime + "\" " + endSelected + ">" + timeslotTimes[i].endFormatted + "</option>");

            $timestampsOfOptions.start[timeslotTimes[i].startFormatted] = startTime.getTime();
            $timestampsOfOptions.end[timeslotTimes[i].endFormatted] = endTime.getTime();

        }
        $endTimeOptions = $endTimeField.find("option");
        $startTimeField.trigger("change");
    }

    var $endTimeField = $("select[name='end']");
    var $endTimeOptions = $endTimeField.find("option");
    var $timestampsOfOptions = {start: [], end: []};

    //reduces the end time options to be only after the start time options.
    $("select[name='start']").change(function () {
        var startTime = $timestampsOfOptions.start[$(this).find(":selected").text()];
        var currentEndTime = $endTimeField.find("option:selected").val();
        $endTimeField.html(
                $endTimeOptions.filter(function () {
                    return startTime < $timestampsOfOptions.end[$(this).text()];
                })
                );

        var endTimeSelected = false;
        $endTimeField.find("option").each(function () {
            if ($(this).val() === currentEndTime) {
                $(this).attr("selected", "selected");
                endTimeSelected = true;
                return false;
            }
        });

        if (!endTimeSelected) {
            //automatically select an end date 2 slots away.
            $endTimeField.find("option:eq(1)").attr("selected", "selected");
        }

    });
});

// Added. Anh. 25012017
function reloadCal()
{
    // Mod. Anh. 26072016
    var a = 0;
    a = jQuery('#auto').val();

    var d = 0;
    d = jQuery('#driver').val();

    var o = 0;
    o = jQuery('#option').val();

    window.location = "tilauskalenteri?a=" + a + "&d=" + d + "&o=" + o;
}

function getEventData() {
    var returnObj = {
        events: []
    };

    var reservationsArray;
    var reservationObj;

    $.ajax({
        type: "GET",
        url: "/api/reservations",
        async: false,
        success: function (jsonObj)
        {
            reservationsArray = jsonObj._embedded.reservations;
            for (var i = 0; i < reservationsArray.length; i++) {
                reservationObj = new Object();
                reservationObj.id = reservationsArray[i].id;

                reservationObj.start = new Date(reservationsArray[i].reservationFrom).addHours(-2); // Time difference
                reservationObj.end = new Date(reservationsArray[i].reservationTo).addHours(-2); // Time difference
                
                // reservationObj.start2 = new Date(reservationsArray[i].reservationTo);
                // reservationObj.end2 = new Date(reservationsArray[i].reservationTo).addHours(1),
                var reservation_username = "";
                reservation_username=reservationsArray[i].username;
                
                if (reservation_username===customer_name) {
                    // this reservation belongs to the current user
                    reservationObj.customer = customer_name;
                    reservationObj.bgCol = "#1B7BB9";
                }
                else
                {
                    reservationObj.customer = "anonymous";
                    reservationObj.bgCol = "#EC4859";
                }
                
                reservationObj.title = "<br><b>Customer: </b>" + reservationObj.customer + "<br><b>Purpose: </b>" + reservationsArray[i].subject;
                reservationObj.address = reservationsArray[i].subject;
                
                reservationObj.userId = 0;
                reservationObj.readOnly = true;

                returnObj.events.push(reservationObj);
            }
        },
        error: function ()
        {

        }
    })

    return returnObj;
}

function trigger_is_public(val)
{
    $("input[name='isPublicText']").val(val);
}

function simple_alert(outputMsg, titleMsg, onCloseCallback) {
    if (!titleMsg)
        titleMsg = 'Alert';

    if (!outputMsg)
        outputMsg = 'No Message to Display.';

    $("<div></div>").html(outputMsg).dialog({
        title: titleMsg,
        resizable: false,
        modal: true,
        buttons: {
            "OK": function () {
                $(this).dialog("close");
            }
        },
        close: onCloseCallback
    });
}