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
 * The Register class represents the register activity.
 * It provides methods to register a user with an email and a password.
 */
public class Register extends AppCompatActivity {
    /**
     * The editTextEmail is the email text field and the editTextPassword is the password text field.
     */
    TextInputEditText editTextEmail, editTextPassword;
    /**
     * The registerButton is the register button.
     */
    Button registerButton;
    /**
     * The mAuth is the Firebase authentication instance.
     */
    FirebaseAuth mAuth;
    /**
     * The progressBar is the progress bar to show the progress of the registration.
     */
    ProgressBar progressBar;
    /**
     * The loginLink is the text view to login.
     */
    TextView loginLink;

    /**
     * Called when the activity is starting.
     * This method checks if the user is already logged in.
     * If the user is logged in, it starts the MainActivity and finishes the current activity.
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Called when the activity is starting.
     * This method sets the content view and initializes the editTextEmail, editTextPassword, registerButton, progressBar, and loginLink.
     * It also sets an event listener on the registerButton and the loginLink.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_register layout
        setContentView(R.layout.activity_register);
        // Initialize the editTextEmail, editTextPassword, registerButton, progressBar, and loginLink
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        loginLink = findViewById(R.id.loginLink);
        // Set an event listener on the loginLink
        loginLink.setOnClickListener(v -> {
            // When the loginLink is clicked, start the Login activity and finish the current activity
            Intent intent = new Intent(getApplicationContext(), Login.class);
            // The startActivity() method starts an instance of the Login activity.
            startActivity(intent);
            // The finish() method closes the current activity.
            finish();
        });
        // Set an event listener on the registerButton
        registerButton.setOnClickListener(v -> {
            // When the registerButton is clicked, validate the input, start the registration process
            // and handle the registration result
            progressBar.setVisibility(View.VISIBLE);
            String email = String.valueOf((editTextEmail).getText());
            String password = String.valueOf((editTextPassword).getText());
            //validate email input
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            }
            //validate password input
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            }
            //create user with email and password
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}