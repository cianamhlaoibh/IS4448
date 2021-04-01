package ie.app.a117362356_is4448_ca2.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import ie.app.a117362356_is4448_ca2.R;

/**
 * Reference
 *  - URL - //https://github.com/Ereza/CustomActivityOnCrash/blob/master/sample/src/main/java/cat/ereza/customactivityoncrash/sample/activity/CustomErrorActivity.java | https://github.com/Ereza/CustomActivityOnCrash
 *  - Creator - Eduard Ereza Martínez
 *  - Modified by Cian O Sullivan
 *
 */
public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Button restartButton = findViewById(R.id.restartApp);

        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());

        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
            return;
        }
        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            //restartButton.setText(R.string.restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.restartApplication(ErrorActivity.this, config);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(ErrorActivity.this, config);
                }
            });
        }
    }
}

