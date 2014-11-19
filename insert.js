// if (typeof define !== 'function') {
//     var define = require('amdefine')(module);
// }

// define(function(require, exports) {

var settings = {
    firebaseRoot: "https://studentchoice.firebaseio.com"
};

var Firebase = require('firebase');

var today = new Date();

//var nextweek = new Date(today.getFullYear(), today.getMonth(), today.getDate()+7); //announcement valid until next week

var data = {
	title: 'Name',
	// title: 'MyGroup!',
	admin: 'simplelogin:1',
	participants: {
		'simplelogin:1': true,
		'simplelogin:2': true,
		'simplelogin:3': true,
		'simplelogin:4': true
	},
    timestamp: new Date().getTime()
};

var firebaseRef = new Firebase(settings.firebaseRoot);
var announcementRef = firebaseRef.child("groups");
announcementRef.push(data);
announcementRef.push(data);
announcementRef.push(data);
announcementRef.push(data);
announcementRef.push(data);


// });