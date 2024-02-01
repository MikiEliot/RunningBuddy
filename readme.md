# Application Architecture and Functionality

RunningBuddy follows the Model-View-Controller (MVC) architectural pattern, ensuring a modular and organized structure. It leverages Firebase services for real-time data management and user authentication, offering features like tracking running activities, user registration, and displaying a list of recorded activities.

Key Components

## 1: Login Activity:
        The Login class handles user authentication, allowing users to log in with their email and password credentials. It seamlessly integrates with Firebase Authentication to ensure a secure and efficient login process.

## 2: Register Activity:
        The Register class facilitates user registration, enabling users to create accounts with their email and password. It establishes a smooth onboarding experience for new users.

## 3: Main Activity:
        The MainActivity class serves as the central hub, orchestrating key functionalities such as starting and stopping running activities, updating user location on the map, and interacting with the Firebase Realtime Database to store and retrieve running results.

## 4: ListOfActivities Activity:
        The ListOfActivities class manages the display of a list of recorded running activities. It interacts with the Firebase Realtime Database to retrieve and present this data in a user-friendly manner.

## 5: RunningResult Class:
        The RunningResult class represents the data model for running activities. It encapsulates information such as time, speed, and distance. Instances of this class are stored in the Firebase database, ensuring seamless synchronization across connected clients.

## Login Class Overview

The Login class represents the login activity in the application, providing essential functionalities for user authentication. It utilizes Firebase Authentication to authenticate users with email and password credentials.
Class Structure
TextInputEditTexts and Buttons

    editTextEmail and editTextPassword: Instances of TextInputEditText representing user input fields for email and password.
    loginButton: A Button triggering the login process.

FirebaseAuth Instance

    mAuth: An instance of FirebaseAuth facilitating user authentication.

ProgressBar and TextView

    progressBar: A ProgressBar indicating the progress of the login process.
    registerLink: A TextView serving as a link to navigate to the registration activity.

Lifecycle Methods
onStart()

This method is called when the activity is starting. It checks if the user is already logged in. If so, it directly navigates to the MainActivity and finishes the current activity.
onCreate()

This method is called when the activity is first created. It initializes various components, sets up event listeners, and manages the login process.
Initialization and Setup

    FirebaseAuth Initialization:
        mAuth = FirebaseAuth.getInstance();
        Initializes the Firebase Authentication instance.

    UI Components Initialization:
        editTextEmail and editTextPassword are initialized with corresponding views from the layout.
        loginButton, progressBar, and registerLink are initialized.

    OnClickListener Setup:
        registerLink has an OnClickListener to start the Register activity and finish the current activity.
        loginButton has an OnClickListener triggering the login process.

Login Process

    Input Validation:
        Checks for empty email and password fields. Displays a toast message if either is empty.

    Firebase Authentication:
        Uses mAuth.signInWithEmailAndPassword(email, password) to sign in with the provided email and password.
        The addOnCompleteListener handles the result of the authentication process.

    Handling Authentication Result:
        If authentication is successful, a toast message is displayed, and the MainActivity is started.
        If authentication fails, a toast message indicates the failure.

Usage

    Launching the Activity:
        The Login activity is launched when users attempt to log in.

    Login Process:
        Users enter their email and password.
        Clicking the login button triggers the authentication process.

    Successful Login:
        If authentication is successful, users are directed to the MainActivity.

    Failed Login:
        If authentication fails, users receive a notification of the failure.

Recommendations

    Error Handling:
        Consider enhancing error handling for specific authentication failures.
    Security Considerations:
        Ensure secure handling of user credentials.
    User Feedback:
        Provide more informative messages to guide users during the login process.

# Register Class Overview

The Register class is the implementation of the register activity in the Fitness Tracker application. This class provides users with the ability to create accounts by registering with their email and password. The registration process is seamlessly integrated with Firebase Authentication for secure user account creation.
Class Structure
TextInputEditTexts and Buttons

    editTextEmail and editTextPassword: Instances of TextInputEditText representing user input fields for email and password.
    registerButton: A Button triggering the registration process.

FirebaseAuth Instance

    mAuth: An instance of FirebaseAuth facilitating user authentication.

ProgressBar and TextView

    progressBar: A ProgressBar indicating the progress of the registration process.
    loginLink: A TextView serving as a link to navigate to the login activity.

Lifecycle Methods
onStart()

This method is called when the activity is starting. It checks if the user is already logged in. If so, it directly navigates to the MainActivity and finishes the current activity.
onCreate(Bundle savedInstanceState)

This method is called when the activity is first created. It initializes various components, sets up event listeners, and manages the registration process.
Initialization and Setup

    FirebaseAuth Initialization:
        mAuth = FirebaseAuth.getInstance();
        Initializes the Firebase Authentication instance.

    UI Components Initialization:
        editTextEmail and editTextPassword are initialized with corresponding views from the layout.
        registerButton, progressBar, and loginLink are initialized.

    OnClickListener Setup:
        loginLink has an OnClickListener to start the Login activity and finish the current activity.
        registerButton has an OnClickListener triggering the registration process.

Registration Process

    Input Validation:
        Checks for empty email and password fields. Displays a toast message if either is empty.

    Firebase Authentication:
        Uses mAuth.createUserWithEmailAndPassword(email, password) to create a user with the provided email and password.
        The addOnCompleteListener handles the result of the registration process.

    Handling Registration Result:
        If registration is successful, a toast message is displayed, and the Login activity is started.
        If registration fails, a toast message indicates the failure.

Usage

    Launching the Activity:
        The Register activity is launched when users attempt to register.

    Registration Process:
        Users enter their desired email and password.
        Clicking the register button triggers the account creation process.

    Successful Registration:
        If registration is successful, users receive a notification and are directed to the Login activity.

    Failed Registration:
        If registration fails, users receive a notification of the failure.

Recommendations

    Input Validation Enhancements:
        Consider implementing additional input validation checks for email format and password strength.

    User Feedback:
        Provide more informative messages to guide users during the registration process.

    Security Considerations:
        Ensure secure handling of user credentials during the registration process.

By understanding the structure and functionality of the Register class, developers can effectively integrate and customize user registration within the application.

# RunningResult Class Overview

The RunningResult class is a fundamental component in the Fitness Tracker application, representing a running activity with attributes such as time, speed, and distance. This class encapsulates methods to access and modify these properties, providing a flexible and structured way to manage running activity data. Additionally, it offers a method to push the running result to a Firebase database, ensuring seamless data synchronization.
Class Structure
Data Fields

    time: Represents the time of the running activity.
    speed: Represents the speed of the running activity.
    distance: Represents the distance covered during the running activity.

Constructors

    public RunningResult(String time, double speed, double distance)
        Constructs a new RunningResult with specified time, speed, and distance.

Getter and Setter Methods

    public String getTime()
        Returns the time of the running activity.

    public void setTime(String time)
        Sets the time of the running activity.

    public double getSpeed()
        Returns the speed of the running activity.

    public void setSpeed(double speed)
        Sets the speed of the running activity.

    public double getDistance()
        Returns the distance of the running activity.

    public void setDistance(double distance)
        Sets the distance of the running activity.

Database Interaction Method

    public void pushToDatabase()
        Pushes the running result to the Firebase database.
        Retrieves a reference to the 'runningResult' node in the database.
        Generates a unique key for the new running result.
        Sets the value at the generated key to this RunningResult object.

Usage

    Instantiation:
        Create a new instance of RunningResult by providing time, speed, and distance.

    Accessing and Modifying Data:
        Use getter and setter methods to access and modify time, speed, and distance attributes.

    Database Interaction:
        Call the pushToDatabase() method to store the running result in the Firebase Realtime Database.


# MainActivity ClassOverview

The MainActivity class serves as the central component in the Fitness Tracker application, managing the user's running activities, location updates, and interaction with the Firebase database. This activity is responsible for starting and stopping running sessions, displaying real-time data, updating the user's location on the map, and saving running results to Firebase.
Class Structure
Fields

    googleMap: Represents the Google Map for displaying the user's running path.
    mAuth: Firebase authentication instance for user authentication.
    fusedLocationClient: FusedLocationProviderClient for managing location updates.
    locationCallback: Callback for receiving location updates.
    pathPoints: List of points on the running path.
    startTime: Timestamp when the running activity started.
    timerHandler: Handler for updating elapsed time every second.
    timeTextView, speedTextView, distanceTextView: TextViews for displaying running information.
    startButton, stopButton, logoutButton, listOfActivitiesButton: Buttons for controlling running activities and navigation.
    isRunning: Boolean flag indicating whether the user is currently running.
    totalDistance: Accumulated distance during the running session.
    database: Firebase database instance.
    runningResult: Reference to the "runningResult" node in the Firebase database.
    elapsedTime: Elapsed time during the running session.

Methods
onCreate(Bundle savedInstanceState)

    Initializes various components, including Firebase instances, UI elements, location services, map setup, and timerHandler.
    Sets event listeners on buttons for controlling the app.

onStartButtonClick(View view)

    Handles the start button click event.
    Initiates a running session, sets start time, resets total distance, and updates UI accordingly.

onStopButtonClick(View view)

    Handles the stop button click event.
    Stops the running session, calculates elapsed time, speed, and saves running result to Firebase.
    Updates UI to display results.

requestLocationUpdates()

    Requests location updates from FusedLocationProviderClient.
    Checks for location permission and asks the user if not granted.

saveRunningResult(double speed, double distance)

    Saves running results to the Firebase database.
    Uses user's email, generates a unique activity ID, formats time, and creates a RunningResult object.

updateLocation(Location location)

    Updates user's location on the map and calculates total distance.
    Invoked by locationCallback when receiving location updates.

calculateDistance(LatLng start, LatLng end)

    Calculates distance between two points on the map using Location.distanceBetween().

updateMap()

    Updates the map with the running path using PolylineOptions.

updateSpeed(float speed)

    Updates the speed text view with the current speed during a running session.

startTimer()

    Starts a timer that updates the elapsed time every second during a running session.

getFormattedTime(long milliseconds)

    Formats elapsed time into a string (HH:MM:SS).

onMapReady(GoogleMap googleMap)

    Callback method when the map is ready for use.
    Invoked by the system to set up the Google Map asynchronously.

onDestroy()

    Called when the activity is destroying.
    Stops location updates, removes callbacks and messages from the timerHandler.

stopLocationUpdates()

    Stops location updates by removing them from FusedLocationProviderClient.

onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)

    Handles the result of the permission request.
    Checks if the location permission was granted or denied and takes appropriate action.

Usage

    Starting a Running Session:
        Click the "Start" button to initiate a running session.
        The app starts tracking the user's location, updating the map and displaying real-time information.

    Stopping a Running Session:
        Click the "Stop" button to end the running session.
        The app calculates elapsed time, speed, and saves the running result to Firebase.
        Displays the results on the UI.

    Viewing List of Activities:
        Click the "List of Activities" button to navigate to the ListOfActivities activity.

    Logging Out:
        Click the "Logout" button to sign out the user and navigate to the login screen.
    

# ListOfActivities Class Overview

The ListOfActivities class in the Fitness Tracker application is responsible for displaying a list of running activities retrieved from the Firebase database. It utilizes a ListView to present the activity details, such as distance, speed, and time, in a user-friendly manner. The class includes methods for reading and updating the list of activities, as well as an event listener for a button that allows the user to return to the main activity.
Class Structure
Fields

    activitiesListView: Displays the list of running activities.
    activityList: Contains the list of activities.
    arrayAdapter: Bridges between the ListView and the underlying data, responsible for creating a view for each item.
    runningResultRef: Reference to the "runningResult" node in the Firebase database.

Methods
onCreate(Bundle savedInstanceState)

    Sets the content view and initializes UI components, including the ListView, ArrayAdapter, and a button for returning to the main activity.
    Initializes the runningResultRef with a reference to the "runningResult" node in the Firebase database.
    Calls readActivities() to retrieve and display running activities.

readActivities()

    Reads running activities from the Firebase database using a ValueEventListener.
    Clears the activityList to refresh the data.
    Iterates over the children of the DataSnapshot representing running activities.
    Extracts key and checks for expected child nodes ("distance", "speed", "time").
    Formats activity details and adds them to the activityList.
    Notifies the ArrayAdapter that the data set has changed.
    Handles errors in case of data retrieval failures.

goBackButtonClick(View view)

    Event listener for the "Go Back" button.
    Navigates back to the main activity using an Intent.

Usage

    Viewing List of Activities:
        Upon entering the ListOfActivities activity, the list of running activities is displayed in the ListView.
        Each item in the list contains details such as activity ID, distance, speed, and time.

    Returning to Main Activity:
        Users can click the "Go Back" button to return to the main activity.
        This button triggers an Intent to navigate back to the main activity.

    Data Refresh:
        The list of activities is automatically refreshed when the ListOfActivities activity is created.
        This ensures that the latest activities from the Firebase database are displayed.