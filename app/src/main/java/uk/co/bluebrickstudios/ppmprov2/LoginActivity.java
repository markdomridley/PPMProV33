package uk.co.bluebrickstudios.ppmprov2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.helper.HashGeneratorUtils;
import uk.co.bluebrickstudios.ppmprov2.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG;
    private TextView attemptsLeftTV;
    DatabaseHelper controller;
    private Button login;
    private TextView loginLockedTV;
    int numberOfRemainingLoginAttempts;
    private TextView numberOfRemainingLoginAttemptsTV;
    private EditText password;
    private EditText username;

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.LoginActivity.1 */
    class C03341 extends AsyncHttpResponseHandler {
        C03341() {
        }

        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            try {
                String responseString = new String(responseBody, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                Toast.makeText(LoginActivity.this.getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } catch (Exception e) {
            }
        }

        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            try {
                String responseString = new String(responseBody, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                Toast.makeText(LoginActivity.this.getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.numberOfRemainingLoginAttempts--;
                LoginActivity.this.attemptsLeftTV.setVisibility(View.VISIBLE);
                LoginActivity.this.numberOfRemainingLoginAttemptsTV.setVisibility(View.VISIBLE);
                LoginActivity.this.numberOfRemainingLoginAttemptsTV.setText(Integer.toString(LoginActivity.this.numberOfRemainingLoginAttempts));
                if (LoginActivity.this.numberOfRemainingLoginAttempts == 0) {
                    LoginActivity.this.login.setEnabled(false);
                    LoginActivity.this.loginLockedTV.setVisibility(View.VISIBLE);
                    LoginActivity.this.loginLockedTV.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                    LoginActivity.this.loginLockedTV.setText("LOGIN LOCKED!!!");
                }
            } catch (Exception e) {
            }
            if (statusCode == 404) {
                Toast.makeText(LoginActivity.this.getApplicationContext(), "Login Failed: Requested resource not found", Toast.LENGTH_LONG).show();
            } else if (statusCode == 500) {
                Toast.makeText(LoginActivity.this.getApplicationContext(), "Login Failed: Something went wrong at server end", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this.getApplicationContext(), "Login Failed: Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
            }
        }
    }

    public LoginActivity() {
        this.numberOfRemainingLoginAttempts = 3;
        this.controller = null;
    }

    static {
        LOG = LoginActivity.class.getName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);
        this.controller = DatabaseHelper.getInstance(this);
        setupVariables();
    }

    public void authenticateLogin(View view) {
        String passwordHash = BuildConfig.FLAVOR;
        try {
            passwordHash = HashGeneratorUtils.generateMD5(this.password.getText().toString());
        } catch (Exception e) {
        }

        Log.d(LOG, this.password.getText().toString());
        Log.d(LOG, passwordHash);

        if (this.controller.getAllUsers().size() == 0) {
            PPMProApp app = (PPMProApp) getApplication();
            app.setClient_id(3);
            app.setUsername("BAML Chester");
            app.setUserId(11);
            app.setIsAdmin(false);
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(getApplicationContext(), "Welcome BAML Chester", Toast.LENGTH_LONG).show();
            return;
        }

        User user = this.controller.login(this.username.getText().toString().trim().toLowerCase(), passwordHash);

        if (user == null || user.getEmail() == null) {

            user = this.controller.getUser(this.username.getText().toString());

            if(user != null){
                Log.d(LOG, user.getEmail() + ":" + user.getApp());
            }

            Toast.makeText(getApplicationContext(), "Access denied", Toast.LENGTH_SHORT).show();
            this.numberOfRemainingLoginAttempts--;
            this.attemptsLeftTV.setVisibility(View.VISIBLE);
            this.numberOfRemainingLoginAttemptsTV.setVisibility(View.VISIBLE);
            this.numberOfRemainingLoginAttemptsTV.setText(Integer.toString(this.numberOfRemainingLoginAttempts));
            if (this.numberOfRemainingLoginAttempts == 0) {
                this.login.setEnabled(false);
                this.loginLockedTV.setVisibility(View.VISIBLE);
                this.loginLockedTV.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                this.loginLockedTV.setText("LOGIN LOCKED!!!");
                return;
            }
            return;
        }
        PPMProApp app = (PPMProApp) getApplication();
        app.setClient_id(user.getClient_id());
        app.setEstate_id(user.getEstate_id());
        app.setUsername(user.getName());
        app.setUserId(user.getId());
        if (user.getIsAdmin() == 1) {
            app.setIsAdmin(true);
        } else {
            app.setIsAdmin(false);
        }
        startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(getApplicationContext(), "Welcome " + user.getName(), Toast.LENGTH_LONG).show();
    }

    private void setupVariables() {
        this.username = (EditText) findViewById(R.id.usernameET);
        this.password = (EditText) findViewById(R.id.passwordET);
        this.login = (Button) findViewById(R.id.loginBtn);
        this.loginLockedTV = (TextView) findViewById(R.id.loginLockedTV);
        this.attemptsLeftTV = (TextView) findViewById(R.id.attemptsLeftTV);
        this.numberOfRemainingLoginAttemptsTV = (TextView) findViewById(R.id.numberOfRemainingLoginAttemptsTV);
        this.numberOfRemainingLoginAttemptsTV.setText(Integer.toString(this.numberOfRemainingLoginAttempts));
    }

    public void remoteLoginCheck(String username, String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setAuthenticationPreemptive(true);
        client.setBasicAuth(username, password);
        RequestParams params = new RequestParams();
        params.add("email", username);
        params.add("password", password);
        client.post("https://ppm-pro.com/api/v1/user", params, new C03341());
    }
}
