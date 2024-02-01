package fr.cda.running2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/**
 * The RunningResult class represents a running activity with its time, speed, and distance.
 * It provides methods to get and set these properties.
 * It also provides a method to push the running result to a Firebase database.
 */
public class RunningResult {
    private String time;
    private double speed;
    private double distance;
    /**
     * Constructs a new RunningResult with the specified time, speed, and distance.
     *
     * @param time the time of the running activity
     * @param speed the speed of the running activity
     * @param distance the distance of the running activity
     */
    public RunningResult(String time, double speed, double distance) {
        this.time = time;
        this.speed = speed;
        this.distance = distance;
    }
    /**
     * @return the time of the running activity
     */
    public String getTime() {
        return time;
    }
    /**
     * Sets the time of the running activity.
     * @param time the new time of the running activity
     */
    public void setTime(String time) {
        this.time = time;
    }
    /**
     * @return the speed of the running activity
     */
    public double getSpeed() {
        return speed;
    }
    /**
     * Sets the speed of the running activity.
     * @param speed the new speed of the running activity
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    /**
     * @return the distance of the running activity
     */
    public double getDistance() {
        return distance;
    }
    /**
     * Sets the distance of the running activity.
     * @param distance the new distance of the running activity
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }


    /**
     * This method pushes the running result to the Firebase database.
     * It first gets a reference to the 'runningResult' node in the database.
     * Then, it generates a unique key for the new running result.
     * Finally, it sets the value at the generated key to this RunningResult object.
     */
    public void pushToDatabase() {
        DatabaseReference resultRef = FirebaseDatabase.getInstance("https://running2-ae995-default-rtdb.europe-west1.firebasedatabase.app/"
        ).getReference().child("runningResult");
        String key = resultRef.push().getKey();
        resultRef.child(key).setValue(this);
    }
}