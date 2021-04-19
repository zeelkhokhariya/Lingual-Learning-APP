$(function () {
    var current = 0;
    setInterval(function () {
        current++;
        if (current >= $('.home-right img').length) {
            current = 0;
        }
        $('.home-right img').stop().eq(current).fadeIn(500).siblings().fadeOut(250);
    }, 5000);
})