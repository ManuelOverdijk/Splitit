# Student Choice: Design document

This is the design document for the SplitIt app.

## Description

The main purpose of the app is to give users the ability to split a bill in seperate payments among their friends. Users can create a chatroom where they can chat and add bills to be paid by all the group members. Anyone in the group can add bills and invite other friends by either email or username.
The main view of the app consits of a list of all the chats the users is currently active in. When clicking on a list item, the user is represented three swipable views, consiting of an overview of all the active bills, one view where users can chat with eachother, and another view with the group information.
The App has basic chat functionality, where users can partake in a chatroom and chat with eachother. In the Bill view, users see an overview of all active bills. At the top of the view users can create a new Bill. The history of the inactive/complete bills is represented at the bottom of the screen. The Info view lists the basic group information, like name and active participants.

## Design:

The app will use the Google Support Library V7 to port most of the material design and functionallity to devices with an android api < 21

## MVC

### Model

- Firebase Android api, for realtime connection with the Firebase database

### View
- LoginView: extends ActionBarActivity, implements GoogleApiClient
	- Facebook login button (Facebook Login Api)
	- Google+ login butotn (GoogleApiClient)
	- Login with usercredentials (Firebase SimpleLogin)
	- Register with usercredentials
	All login methods use Firebase Authentication Providers for handling user authentication
- GroupListView: extends MyListViewActivity, 
    - ListView of all bill groups of the user
    - In Action Bar: MyProfile button, Create Group Button

- GroupView:
	- 3 paged view with on the left: ListView of all the bills of the group, on the right: an ProfileView, and in the middle a ListView with the current Chat messages of the group. 

- BillView: 
	- CardView of the current bill, with a history of the old bills. 

### Controller
  - ChatListAdapter: For populating the chat view
  - GroupListAdapter: For population the group view
  - RegisterActivity: For handling the register view
  - LoginActivity: For handling the login view
  - GroupView: For handling the group view
  - ChatView: For handling the chat view
  - ObjectReference: For storing JSON data in SharedPreferences
  - MyListActivity: For extending the ListActivity
  - MyActionBarActiviyt: For extending the ActionBarActivity
  - GoogleOAuthActivity: For handling google+ login
  - User: User object for storing user data
  - Chat: Chat object for storing chat data
  - Group: Group object for storing group data

Future: find out whats needed to display bills

## Wireframe
![wireframe](https://github.com/jetsekoopmans/KentekenScan/blob/master/doc/wireframe.png)

## Optional
Implement an payment app for in-app paying of the bill. 