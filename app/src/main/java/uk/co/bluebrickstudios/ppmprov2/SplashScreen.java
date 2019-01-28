package uk.co.bluebrickstudios.ppmprov2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import uk.co.blubrickstudios.ppmprov2.R;

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT;

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.SplashScreen.1 */
    class C02081 implements Runnable {
        C02081() {
        }

        public void run() {
            if (((PPMProApp) SplashScreen.this.getApplicationContext()).isLoggedIn()) {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
            } else {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            }
            SplashScreen.this.finish();
        }
    }

    static {
        SPLASH_TIME_OUT = 3000;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new C02081(), (long) SPLASH_TIME_OUT);
    }
}
