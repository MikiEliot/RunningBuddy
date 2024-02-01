package fr.cda.running2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The MainActivity class represents the main activity of the app.
 * It provides methods to start and stop a running activity.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    /**
     * The constant REQUEST_PERMISSIONS_REQUEST_CODE is used as the request code passed to
     * ActivityCompat.requestPermissions(). This code is returned in the callback method
     * onRequestPermissionsResult() to identify the permission request.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    /**
     * The googleMap is the Google map.
     */
    private GoogleMap googleMap;
    /**
     * The mAuth is the Firebase authentication instance.
     */
    private FirebaseAuth mAuth;
    /**
     * The FusedLocationProviderClient is the main entry point for interacting with the fused location provider.
     * This class uses methods to request location updates and to access the last known location. The location
     * is computed from the fused location provider that manages the underlying location technologies, such as GPS
     * and Wi-Fi.
     * The FusedLocationProviderClient provides a simplified API that wraps the underlying location APIs in the
     * Google Play services Location APIs
     */
    private FusedLocationProviderClient fusedLocationClient;
    /**
     * The LocationCallback is used for receiving notifications from the FusedLocationProviderClient
     * when the location has changed. These location updates are the result of the
     * FusedLocationProviderClient.requestLocationUpdates() method being called.
     * The LocationCallback provides a way for the FusedLocationProviderClient to asynchronously
     * handle the location updates as they come in. This is done by overriding the onLocationResult()
     * method, which is called whenever the FusedLocationProviderClient has a new LocationResult
     * available.
     * The LocationResult object contains the new location data, which can be retrieved by calling
     * LocationResult.getLastLocation(). This returns a Location object representing the new
     * location.
     * In this application, the locationCallback is used to update the user's location on the map
     * as they move, and to calculate the total distance they have traveled during their run.
     */
    private LocationCallback locationCallback;
    /**
     * The pathPoints is the list of points on the path.
     */
    private List<LatLng> pathPoints;
    /**
     * The startTime is the start time of the running activity.
     */
    private long startTime;
    /**
     * In this application, the timerHandler is used to schedule a Runnable that updates the elapsed time
     * every second while the user is running.
     * This is done by posting the Runnable to be executed, which updates the timeTextView with the current elapsed time,
     * and then it schedules itself to be run again after a delay of one second. This continues until the user stops running.
     * */
    private Handler timerHandler;
    /**
     * The timeTextView is the text view to display the elapsed time.
     * speedTextView is the text view to display the speed.
     * DistanceTextView is the text view to display the distance.
     */
    private TextView timeTextView, speedTextView,distanceTextView;
    /**
     * The startButton is the button to start the running activity.
     * The stopButton is the button to stop the running activity.
     * The logoutButton is the button to logout.
     * The listOfActivitiesButton is the button to display the list of activities.
     */
    private Button startButton,stopButton, listOfActivitiesButton;

    /**
     * The isRunning is a boolean to check if the user is running.
     */
    private boolean isRunning = false;
    /**
     * The totalDistance is the total distance traveled by the user.
     */
    private double totalDistance;
    /**
     * The database is the Firebase database instance.
     */
    private FirebaseDatabase database;
    /**
     * The runningResult is a reference to the "runningResult" node in the Firebase database.
     */
    private DatabaseReference runningResult;
    /**
     * The elapsedTime is the elapsed time of the running activity.
     */
    private long elapsedTime;

    /**
     * Called when the activity is starting.
     * This method sets the content view and initializes the mAuth, database, runningResult, timeTextView,
     * listOfActivitiesButton, logoutButton, speedTextView, distanceTextView, startButton, stopButton,
     * fusedLocationClient, pathPoints, and timerHandler.
     * It also sets an event listener on the listOfActivitiesButton and the logoutButton.
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_main layout
        setContentView(R.layout.activity_main);
        // Initialize the mAuth, database, runningResult, timeTextView, listOfActivitiesButton, logoutButton, speedTextView, distanceTextView,
        // startButton, stopButton, fusedLocationClient, pathPoints, and timerHandler
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://running2-ae995-default-rtdb.europe-west1.firebasedatabase.app/");
        runningResult = database.getReference("runningResult");
        timeTextView = findViewById(R.id.timeTextView);
        listOfActivitiesButton = findViewById(R.id.listOfActivitiesButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        speedTextView = findViewById(R.id.speedTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        // Get the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Check if the mapFragment is not null
        if (mapFragment != null) {
            // If the mapFragment is not null, set up the map asynchronously
            mapFragment.getMapAsync(this);
        }
        // Set an event listener on the listOfActivitiesButton
        listOfActivitiesButton.setOnClickListener(v -> {
            // When the listOfActivitiesButton is clicked, start the ListOfActivities activity
            Intent intent = new Intent(MainActivity.this, ListOfActivities.class);
            startActivity(intent);
        });
        // Get the FusedLocationProviderClient.
        // This is the main entry point for interacting with the fused location provider.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Initialize the pathPoints list. This list will store the points on the path.
        pathPoints = new ArrayList<>();
        // Initialize the timerHandler. This Handler will be used to
        // schedule a Runnable that updates the elapsed time every second while the user is running.
        timerHandler = new Handler(Looper.getMainLooper());

        // Create a new LocationCallback. This is used for receiving notifications from the FusedLocationProviderClient
        // when the location has changed. These location updates are the result of the
        // FusedLocationProviderClient.requestLocationUpdates() method being called.
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Check if the last location is not null
                if (locationResult.getLastLocation() != null) {
                    // If the last location is not null, update the location
                    updateLocation(locationResult.getLastLocation());
                }
            }
        };
        // Request location updates. This method will request updates about the device's location
        // at the interval specified in the LocationRequest object (1000 milliseconds in this app).
        requestLocationUpdates();

        // Start the timer. This method will start a timer that updates the elapsed time every second
        // while the user is running. This is done by posting a Runnable to the timerHandler,
        // which updates the timeTextView with the current elapsed time, and then it schedules
        // itself to be run again after a delay of one second. This continues until the user stops running.
        startTimer();
    }

    /**
     * This method is called when the start button is clicked.
     * It first checks if the user is not already running.
     * If the user is not running, it sets the start time to the current time, resets the total distance to 0.0,
     * and sets the isRunning flag to true.
     * It then updates the UI by hiding the start button, showing the stop button, and making the speed and distance text views visible.
     * It also starts the timer, which updates the elapsed time every second while the user is running.
     *
     * @param view the view that was clicked
     */
    public void onStartButtonClick(View view) {
        if (!isRunning) {
            // Start button clicked
            startTime = System.currentTimeMillis();
            totalDistance = 0.0;
            isRunning = true;

            // Update UI
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            speedTextView.setVisibility(View.VISIBLE);
            distanceTextView.setVisibility(View.VISIBLE);
            startTime = System.currentTimeMillis();

            startTimer();
        }
    }
    /**
     * This method is called when the stop button is clicked.
     * It first checks if the user is currently running.
     * If the user is running, it calculates the elapsed time and the speed, and saves the running result.
     * It then updates the UI by showing the start button, hiding the stop button, and displaying the speed and distance text views.
     * It also sets the isRunning flag to false, indicating that the user is no longer running.
     *
     * @param view the view that was clicked
     */
    public void onStopButtonClick(View view) {
        if (isRunning) {
            // Stop button clicked
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            double speed = (totalDistance / elapsedTime) * 3600000; // Convert to km/h
            saveRunningResult(speed, totalDistance);
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.GONE);

            speedTextView.setText(String.format(getString(R.string.speed_format), speed));
            distanceTextView.setText(String.format(getString(R.string.distance_format), totalDistance));
            speedTextView.setVisibility(View.VISIBLE);
            distanceTextView.setVisibility(View.VISIBLE);

            isRunning = false;
        }
    }
    /**
     * This method requests location updates from the FusedLocationProviderClient.
     * It first checks if the app has the ACCESS_FINE_LOCATION permission.
     * If the app has the permission, it creates a LocationRequest object and sets the interval, fastest interval, and priority.
     * The interval is the rate at which the app will like to receive updates.
     * The fastest interval is the fastest rate at which the app can handle updates.
     * The priority is the level of accuracy desired for the location updates.
     * It then requests location updates from the FusedLocationProviderClient with the location request and the location callback.
     * If the app does not have the permission, it requests the ACCESS_FINE_LOCATION permission from the user.
     */
    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(1000); // Update interval in milliseconds
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * This method saves the running result to the Firebase database.
     * It first gets the email of the current user from the FirebaseAuth instance.
     * Then, it generates a unique activity ID by appending the current time in milliseconds to the string "activity".
     * It formats the elapsed time into a string.
     * It creates a RunningResult object with the formatted time, speed, and distance.
     * Finally, it adds the RunningResult object to the Firebase database under the generated activity ID.
     *
     * @param speed the speed of the running activity
     * @param distance the distance of the running activity
     */
    private void saveRunningResult(double speed, double distance) {
//            String userEmail = mAuth.getCurrentUser().getEmail();
            // Generate a unique activity ID
            String activityId = "activity" + System.currentTimeMillis();

            String formattedTime = getFormattedTime(elapsedTime);

            // Create a RunningResult object with speed, distance, and elapsed time
            RunningResult result = new RunningResult(formattedTime, speed, distance);
            runningResult.child(activityId).setValue(result) ;
    }

    /**
     * This method updates the user's location on the map and calculates the total distance they have traveled.
     * It first creates a LatLng object from the location's latitude and longitude.
     * If there are already points on the path, it calculates the distance from the last point to the new location and adds it to the total distance.
     * It then adds the new location to the pathPoints list and updates the map.
     * If the user is currently running, it updates the speed text view with the current speed.
     *
     * @param location the new location
     */
    private void updateLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (pathPoints.size() > 0) {
            // Update total distance based on the new location
            totalDistance += calculateDistance(pathPoints.get(pathPoints.size() - 1), latLng);
        }
        pathPoints.add(latLng);
        updateMap();
        if (isRunning) {
            updateSpeed(location.getSpeed());
        }
    }

    /**
     * This method calculates the distance between two points on the map.
     * It first creates a float array to store the results of the distance calculation.
     * It then calls the Location.distanceBetween() method to calculate the distance between the two points.
     * The distance is returned in meters, so it is divided by 1000 to convert it to kilometers.
     *
     * @param start the start point
     * @param end the end point
     * @return the distance between the two points
     */
    private double calculateDistance(LatLng start, LatLng end) {
        float[] results = new float[1];
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results);
        return results[0]/1000; // Convert to km
    }

    /**
     * This method updates the map with the points on the path.
     * It first checks if the googleMap is not null and if there are at least two points on the path.
     * If the googleMap is not null and there are at least two points on the path, it creates a PolylineOptions object
     * and adds all the points on the path to it.
     * It then adds the polyline to the map and animates the camera to the last point on the path.
     */
    private void updateMap() {
        if (googleMap != null && pathPoints.size() > 1) {
            PolylineOptions polylineOptions = new PolylineOptions().addAll(pathPoints);
            Polyline polyline = googleMap.addPolyline(polylineOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pathPoints.get(pathPoints.size() - 1), 15.0f));
        }
  }
    /**
     * This method updates the speed text view with the current speed.
     * It first checks if the user is currently running.
     * If the user is running, it updates the speed text view with the current speed.
     *
     * @param speed the current speed
     */
    private void updateSpeed(float speed) {
        if (isRunning) {
            speedTextView.setText(String.format(getString(R.string.speed_format), speed * 3.6));
        }
    }
    /**
     * This method starts a timer that updates the elapsed time every second while the user is running.
     * This is done by posting a Runnable to the timerHandler, which updates the timeTextView with the current elapsed time,
     * and then it schedules itself to be run again after a delay of one second. This continues until the user stops running.
     */
    private void startTimer() {
        timerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    long currentTime = System.currentTimeMillis();
                    elapsedTime = currentTime - startTime;
                    timeTextView.setText(getFormattedTime(elapsedTime));
                    timerHandler.postDelayed(this, 1000);
                }
            }
        });
    }
    /**
     * This method logs out the user and redirects to the login screen.
     * It first calls the signOut() method on the FirebaseAuth instance to sign out the user.
     * It then redirects to the login screen and finishes the current activity.
     *
     * @param view the view that was clicked
     */
    public void logoutUser(View view) {
        FirebaseAuth.getInstance().signOut();

        // Redirect to the login screen or any other desired screen
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    /**
     * This method formats the elapsed time into a string.
     * It first calculates the number of hours, minutes, and seconds from the elapsed time.
     * It then returns a string with the formatted time.
     *
     * @param milliseconds the elapsed time in milliseconds
     * @return the formatted time
     */
    private String getFormattedTime(long milliseconds) {
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = milliseconds / (1000 * 60 * 60);
        // formatting the elapsed time into a string.
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
    // The onMapReady() method is called when the map is ready to be used.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateMap();
    }
    /**
     * Called by the system when the activity is destroying.
     * This method stops the location updates, removes any callbacks and messages from the timerHandler,
     * and then calls the superclass's onDestroy() method.
     * This ensures that the app cleans up any resources it is using before the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        timerHandler.removeCallbacksAndMessages(null);
    }
    /**
     * This method stops the location updates.
     * It removes the location updates from the FusedLocationProviderClient.
     */
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    /**
     * This method is called by the system when the user has granted or denied the requested permissions.
     * It first checks if the request code is equal to REQUEST_PERMISSIONS_REQUEST_CODE, which is the code passed to
     * ActivityCompat.requestPermissions() when requesting the ACCESS_FINE_LOCATION permission.
     * If the request code is equal to REQUEST_PERMISSIONS_REQUEST_CODE, it checks if the permission was granted.
     * If the permission was granted, it calls the requestLocationUpdates() method to start requesting location updates.
     * If the permission was not granted, it shows a toast message informing the user that the permission was denied.
     *
     * @param requestCode the request code passed to ActivityCompat.requestPermissions()
     * @param permissions the requested permissions
     * @param grantResults the grant results for the corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
