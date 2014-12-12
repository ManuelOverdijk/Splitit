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

var data1 = {
	title: 'My Bill #1',
	admin: 'simplelogin:1',
	created: new Date().getTime(),
	participants: {
		'simplelogin:1': 20.1,
		'simplelogin:2': 19.2,
		'simplelogin:3': 11.12,
		'simplelogin:4': 8.20
	},
	completed: false,
	lastUpdated: ""
};

var firebaseRef = new Firebase(settings.firebaseRoot);
var announcementRef = firebaseRef.child("bills").child("-Jcxvruekex7EOMiIUZ_");
announcementRef.set(data1);
// announcementRef.push(data1);
// announcementRef.push(data);
// announcementRef.push(data);
// announcementRef.push(data);


// });