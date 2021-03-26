

    function initparticles() {
        bubbles();
        hearts();
        lines();
        confetti();
        fire();
        sunbeams();
    }




function bubbles() {
    $.each($(".particletext.bubbles"), function(){
        var bubblecount = ($(this).width()/50)*10;
        for(var i = 0; i <= bubblecount; i++) {
            var size = ($.rnd(40,80)/10);
            $(this).append('<span class="particle" style="top:' + $.rnd(20,80) + '%; left:' + $.rnd(0,95) + '%;width:' + size + 'px; height:' + size + 'px;animation-delay: ' + ($.rnd(0,30)/10) + 's;"></span>');
        }
    });
}

function hearts() {
    $.each($(".particletext.hearts"), function(){
        var heartcount = ($(this).width()/50)*5;
        for(var i = 0; i <= heartcount; i++) {
            var size = ($.rnd(60,120)/10);
            $(this).append('<span class="particle" style="top:' + $.rnd(20,80) + '%; left:' + $.rnd(0,95) + '%;width:' + size + 'px; height:' + size + 'px;animation-delay: ' + ($.rnd(0,30)/10) + 's;"></span>');
        }
    });
}

function lines() {
    $.each($(".particletext.lines"), function(){
        var linecount = ($(this).width()/50)*10;
        for(var i = 0; i <= linecount; i++) {
            $(this).append('<span class="particle" style="top:' + $.rnd(-30,30) + '%; left:' + $.rnd(-10,110) + '%;width:' + $.rnd(1,3) + 'px; height:' + $.rnd(20,80) + '%;animation-delay: -' + ($.rnd(0,30)/10) + 's;"></span>');
        }
    });
}

function confetti() {
    $.each($(".particletext.confetti"), function(){
        var confetticount = ($(this).width()/50)*10;
        for(var i = 0; i <= confetticount; i++) {
            $(this).append('<span class="particle c' + $.rnd(1,2) + '" style="top:' + $.rnd(10,50) + '%; left:' + $.rnd(0,100) + '%;width:' + $.rnd(6,8) + 'px; height:' + $.rnd(3,4) + 'px;animation-delay: ' + ($.rnd(0,30)/10) + 's;"></span>');
        }
    });
}

function fire() {
    $.each($(".particletext.fire"), function(){
        var firecount = ($(this).width()/50)*20;
        for(var i = 0; i <= firecount; i++) {
            var size = $.rnd(8,12);
            $(this).append('<span class="particle" style="top:' + $.rnd(40,70) + '%; left:' + $.rnd(-10,100) + '%;width:' + size + 'px; height:' + size + 'px;animation-delay: ' + ($.rnd(0,20)/10) + 's;"></span>');
        }
    });
}

function sunbeams() {
    $.each($(".particletext.sunbeams"), function(){
        var linecount = ($(this).width()/50)*10;
        for(var i = 0; i <= linecount; i++) {
            $(this).append('<span class="particle" style="top:' + $.rnd(-50,0) + '%; left:' + $.rnd(0,100) + '%;width:' + $.rnd(1,3) + 'px; height:' + $.rnd(80,160) + '%;animation-delay: -' + ($.rnd(0,30)/10) + 's;"></span>');
        }
    });
}

jQuery.rnd = function(m,n) {
    m = parseInt(m);
    n = parseInt(n);
    return Math.floor( Math.random() * (n - m + 1) ) + m;
}


initparticles();


export { //很关键
    initparticles,
bubbles,
hearts,
lines,
confetti,
fire,
sunbeams,

}
