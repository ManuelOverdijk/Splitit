if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function(require, exports) {

var settings = {
    firebaseRoot: "https://studentchoice.firebaseio.com/"
};

var Firebase = require('firebase');

var today = new Date();

var nextweek = new Date(today.getFullYear(), today.getMonth(), today.getDate()+7); //announcement valid until next week

var data = {
    bg: "#ffff00",
    content: "<b>Welcome back #1!</b><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin tincidunt mollis nulla fringilla sollicitudin. Integer sit amet enim vel tellus dictum dignissim vitae non odio.</p>",
    validuntil: nextweek.getTime(),
    timestamp: new Date().getTime()
};

var firebaseRef = new Firebase(settings.firebaseRoot);
var announcementRef = firebaseRef.child("Announcements");
announcementRef.push(data);

});