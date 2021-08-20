package com.theherdonline.ui.login;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.util.ActivityUtils;

public class AttentionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.dialog_attention_agent);

        findViewById(R.id.button_contact_same).setOnClickListener(l->{
            ActivityUtils.startCallingApp(this, getString(R.string.txt_sam_number));
        });

        findViewById(R.id.button_website).setOnClickListener(l->{
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(AppConstants.HERD_HOME_PAGE));
            startActivity(i);
        });

    }
}
