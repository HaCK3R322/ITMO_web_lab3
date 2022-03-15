function updateClock() {
    let time = new Date();

    let month = time.toLocaleString('eng', {month: 'long'});
    let day = time.getDate();
    let hour = time.getHours();
    let min = time.getMinutes();

    let currentDate = month + "   " + day;

    if(min < 10) min = "0" + min;
    let currentTime = hour + ":" + min;

    let clockTime = document.getElementById("clockTime");
    let clockDate = document.getElementById("clockDate");

    clockTime.innerHTML = currentTime;
    clockDate.innerHTML = currentDate;
}
setTimeout(updateClock, 1);
setInterval(updateClock, 11000);