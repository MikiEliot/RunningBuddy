package fr.cda.running2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The Login class represents the login activity.
 * It provides methods to login to the app.
 */
public class Login extends AppCompatActivity {
    /**
     * The TextInputEditText for the email and password.
     */
    TextInputEditText editTextEmail, editTextPassword;
    /**
     * The Button to login.
     */
    Button loginButton;
    /**
     * The FirebaseAuth instance.
     */
    FirebaseAuth mAuth;
    /**
     * The ProgressBar to show the progress of the login.
     */
    ProgressBar progressBar;
    /**
     * The TextView to register.
     */
    TextView registerLink;

    /**
     * Called when the activity is starting.
     * This method checks if the user is already logged in.
     * If the user is logged in, it starts the MainActivity and finishes the current activity.
     */
    @Override
    public void onStart() {
        super.onStart();
        //method to get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * Called when the activity is first created.
     * This method initializes the FirebaseAuth instance, TextInputEditText for email and password,
     * the login Button, ProgressBar, and the TextView for the register link.
     * It also sets onClickListeners for the register link and the login button.
     * The register link starts the Register activity and finishes the current activity.
     * The login button validates the input, starts the login process, and handles the login result.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Called when the activity is first created.
        //This method sets the user interface layout for this activity.
        //The layout file is defined in the project res/layout/activity_login.xml file.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize the TextInputEditText for email
        editTextEmail = findViewById(R.id.email);
        // Initialize the TextInputEditText for password
        editTextPassword = findViewById(R.id.password);
        // Initialize the login Button
        loginButton = findViewById(R.id.loginButton);
        // Initialize the ProgressBar
        progressBar = findViewById(R.id.progressBar);
        // Initialize the TextView for the register link
        registerLink = findViewById(R.id.registerLink);


        //Sets an OnClickListener for the register link.
        //When the register link is clicked, it starts the Register activity and finishes the current activity
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
            finish();
        });


        // Set an OnClickListener for the login button
        loginButton.setOnClickListener(v -> {
            // Make the progress bar visible
            progressBar.setVisibility(View.VISIBLE);
            // Get the email from the TextInputEditText and convert it to a string
            String email = String.valueOf((editTextEmail).getText());
            // Get the password from the TextInputEditText and convert it to a string
            String password = String.valueOf((editTextPassword).getText());

            // Check if the email field is empty
            if (TextUtils.isEmpty(email)) {
                // If it is, show a toast message asking the user to enter an email
                Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
            }
            // Check if the password field is empty
            if (TextUtils.isEmpty(password)) {
                // If it is, show a toast message asking the user to enter a password
                Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
            }
            // Sign in with email and password using FirebaseAuth
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        // Check if the sign in was successful
                        if (task.isSuccessful()) {
                            // If it was, show a toast message saying "Login Successful"
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            // Start the MainActivity
                            startActivity(intent);
                            // Finish the current activity
                            finish();
                        } else {
                            // If the sign in was not successful, show a toast message saying "Login Failed"
                            Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}