// timer functions and initialization
function formatTime(time) {
    var hours = parseInt(time/3600),
        min = parseInt(time / 60) - (hours*60),
        sec = time - (min * 60) - (hours*3600);
    return (hours > 0 ? pad(hours, 2) : "00") + ":" + (min > 0 ? pad(min, 2) : "00") + ":" + pad(sec, 2);
}
function pad(number, length) {
    var str = '' + number;
    while (str.length < length) {str = '0' + str;}
    return str;
}