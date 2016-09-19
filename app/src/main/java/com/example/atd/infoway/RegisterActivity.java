package com.example.atd.infoway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * Created by atd on 9/13/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private EditText mUsernameView;
    private EditText Password1View, Password2View, fnameView, lnameView, ageView;
    private RadioGroup radioGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the registration form.
        mUsernameView = (EditText) findViewById(R.id.user);
        Password1View = (EditText) findViewById(R.id.pass1);
        Password2View = (EditText) findViewById(R.id.pass2);
        fnameView = (EditText) findViewById(R.id.fiName);
        lnameView = (EditText) findViewById(R.id.laName);
        ageView = (EditText) findViewById(R.id.age);
        radioGender = (RadioGroup) findViewById(R.id.radioGen);


        Button mRigsterButton = (Button) findViewById(R.id.Cregister_button);
        mRigsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin(); go to register ACtivity
                attemptRegister();
            }
        });

    }

    private void attemptRegister() {

        // Reset errors.
        mUsernameView.setError(null);
        Password1View.setError(null);
        Password2View.setError(null);

        // Store values at the time of the login attempt.
        String user = mUsernameView.getText().toString();
        String password1 = Password1View.getText().toString();
        String password2 = Password2View.getText().toString();
        String fname = fnameView.getText().toString();
        String lname = lnameView.getText().toString();
        int age = Integer.parseInt(String.valueOf(ageView.getText()));

        int buttonId = radioGender.getCheckedRadioButtonId();
        RadioButton genderButton = (RadioButton) findViewById(buttonId);
        String gender = genderButton.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password1) && !isPasswordValid(password1)) {
            Password1View.setError(getString(R.string.error_invalid_password));
            focusView = Password1View;
            cancel = true;
        }
        // Check for matching passwords
        if (!password1.equals(password2)) {
            Password1View.setError(getString(R.string.error_match_password));
            focusView = Password1View;
            cancel = true;
        }

        // Check for a valid user address.
        if (TextUtils.isEmpty(user)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isuserValid(user)) {
            mUsernameView.setError(getString(R.string.error_invalid_usernme));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //     showProgress(true);
            //     mAuthTask = new UserLoginTask(user, password);
            Toast.makeText(getBaseContext(), "User created successfully", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // Get singleton instance of database
            UsersDBHelper databaseHelper = UsersDBHelper.getInstance(this);

            // Add sample post to the database
            databaseHelper.addUser(user, password1.hashCode(), fname, lname, gender, age);

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            // mAuthTask.execute((Void) null);
        }
    }

    private boolean isuserValid(String user) {
        //TODO: Replace this with your own logic
        return !user.contains(" ");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
