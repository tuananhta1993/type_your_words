﻿var date = new Date();
date.setDate(date.getDate());

$('.date_picker').datetimepicker({ format: 'MM/DD/YYYY' });

$('.week_picker').datetimepicker({ viewMode: 'months' });