package onecorporation.one;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

import onecorporation.one.Data.PhotoEntry;
import onecorporation.one.Models.LoginModel;
import onecorporation.one.Models.PhotoModel;


public class RegisterActivity extends Activity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static LoginModel loginModel;

    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mLocationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginModel = new LoginModel(this);

        mUsernameView = (EditText) findViewById(R.id.profile_name);
        mPasswordView = (EditText) findViewById(R.id.profile_password);
        mLocationView = (EditText) findViewById(R.id.profile_location);

        final ImageButton profilePicButton = (ImageButton) findViewById(R.id.profile_picture);
        profilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: start up front facing camera if it exists on the phone
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "tmp.jpg")));

                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                //TODO: set the temp photo in the imagebutton
            }
        });

        final ImageButton maleAndroidButton = (ImageButton) findViewById(R.id.button_male);
        maleAndroidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                // TODO: do something with the state of the button (save it into local var)
                if (view.isSelected()) {
                    Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "False", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton femaleAndroidButton = (ImageButton) findViewById(R.id.button_female);
        maleAndroidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                // TODO: same thing as the male android button
                if (view.isSelected()) {
                    Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "False", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button newAccountButton = (Button) findViewById(R.id.button_register);
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: save the image to the users database
                createAccount();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Account creation */
    public void createAccount() {

        // TODO: save the location data into a variable
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String location = mLocationView.getText().toString();

        // Check for valid password, if the user entered one
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
        }

        // Check for valid email
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
        } else if (!isEmailValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_email));
        }

        // attempt the creation of account
        boolean access = loginModel.create(username, password);

        if (access) {
            Toast.makeText(getApplicationContext(), "Account created!", Toast.LENGTH_SHORT).show();
            //TODO: Go back to login so the user can verify that it works
        } else {
            Toast.makeText(getApplicationContext(), "Account failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
