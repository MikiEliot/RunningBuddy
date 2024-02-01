package fr.cda.running2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**
 * The ListOfActivities class displays a list of running activities.
 * It reads the activities from a Firebase database.
 */
public class ListOfActivities extends AppCompatActivity {

    /**
     * The activitiesListView displays the list of activities.
     */
    private ListView activitiesListView;
    /**
     * The activityList contains the list of activities.
     */
    private List<String> activityList;
    /**
     * The ArrayAdapter in Android is a bridge between the ListView (or any other view that extends AdapterView)
     * and the underlying data for that view. The ArrayAdapter provides access to the data items and is also
     * responsible for making a View for each item in the data set.
     */
    private ArrayAdapter<String> arrayAdapter;
    /**
     * The runningResultRef is a reference to the "runningResult" node in the Firebase database.
     */
    private DatabaseReference runningResultRef;

    /**
     * The onCreate method is called when the activity is starting.
     * It sets the content view and initializes the activity list, the array adapter, and the list view.
     * It also sets an event listener on the "runningResult" node in the Firebase database.
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_list_of_activities layout
        setContentView(R.layout.activity_list_of_activities);
        // Initialize the activity list, the array adapter, and the list view
        activitiesListView = findViewById(R.id.activitiesListView);
        activityList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activityList);
        activitiesListView.setAdapter(arrayAdapter);
        //The goBackButton is a button that allows the user to go back to the main activity.
        Button goBackButton = findViewById(R.id.backButton);
        // Set an event listener on the goBackButton
        goBackButton.setOnClickListener(v -> {
            // When the goBackButton is clicked, go back to the main activity
            Intent intent = new Intent(ListOfActivities.this, MainActivity.class);
            startActivity(intent);
        });
        // Initialize the runningResultRef, reference to the "runningResult" node in the Firebase database
        runningResultRef= FirebaseDatabase.getInstance("https://running2-ae995-default-rtdb.europe-west1.firebasedatabase.app").getReference("runningResult");
        // Read activities from Firebase
        readActivities();
    }


    /**
     * This method reads the running activities from the Firebase database.
     * It first checks if the runningResultRef is not null.
     * Then, it adds a ValueEventListener to the runningResultRef.
     * In the onDataChange method, it clears the activityList and iterates over the children of the dataSnapshot.
     * For each child, it gets the key and checks if the child has the expected nodes ("distance", "speed", and "time").
     * If the child has the expected nodes, it gets the values of these nodes and adds them to the activityList.
     * Finally, it notifies the arrayAdapter that the data set has changed.
     * In the onCancelled method, it handles any errors that occurred while reading the data.
     */
    private void readActivities() {
        if (runningResultRef != null) {
            runningResultRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    activityList.clear();
                    // Iterate over the children of the dataSnapshot.
                    // Each child in the dataSnapshot represents a running activity in the Firebase database.
                    for (DataSnapshot activitySnapshot : dataSnapshot.getChildren()) {
                        String activityId = activitySnapshot.getKey();

                        // Ensure that the activitySnapshot has the expected child nodes
                        if (activitySnapshot.hasChild("distance")
                                && activitySnapshot.hasChild("speed")
                                && activitySnapshot.hasChild("time")) {

                            // Extract values from the child nodes
                            Double distanceDouble = activitySnapshot.child("distance").getValue(Double.class);
                            Double speedDouble = activitySnapshot.child("speed").getValue(Double.class);
                            String time = activitySnapshot.child("time").getValue(String.class);

                            // Check for null values before converting
                            double distance = (distanceDouble != null) ? distanceDouble : 0.0;
                            double speed = (speedDouble != null) ? speedDouble : 0.0;

                            // Format the activity details as a string
                            String activityDetails = activityId + "\n" +
                                    "Distance: " + distance + "\n" +
                                    "Speed: " + speed + "\n" +
                                    "Time: " + time;

                            activityList.add(activityDetails);
                        }
                    }

                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Log.e("DatabaseError", databaseError.getMessage());
                    Toast.makeText(ListOfActivities.this, "Failed to read activities: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("DatabaseError", "runningResultRef is null");
            // toast message to inform the user that runningResultRef is null
            Toast.makeText(ListOfActivities.this, "Failed to read activities: runningResultRef is null", Toast.LENGTH_SHORT).show();        }
    }


}