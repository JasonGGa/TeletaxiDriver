package com.colossus.teletaxidriver;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private EditText fName, fPhone, fPlate;
    private Button bSave;
    private ImageView iProfileImage;

    private String userID;
    private String dName;
    private String dPhone;
    private String dPlate;
    private String dProfileImageUrl;

    private FirebaseAuth auth;
    private DatabaseReference driverDatabase;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar aBar = getSupportActionBar();
        if (aBar != null) aBar.setDisplayHomeAsUpEnabled(true);

        fName = findViewById(R.id.name);
        fPhone = findViewById(R.id.phone);
        fPlate = findViewById(R.id.plate);
        iProfileImage = findViewById(R.id.profileImage);
        bSave = findViewById(R.id.save);

        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        driverDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("Driver").child(userID);

        getUserInfo();

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });
    }

    private void getUserInfo() {
        driverDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        dName = map.get("name").toString();
                        fName.setText(dName);
                    }
                    if (map.get("phone") != null) {
                        dPhone = map.get("phone").toString();
                        fPhone.setText(dPhone);
                    }
                    if (map.get("car") != null) {
                        dPlate = map.get("car").toString();
                        fPlate.setText(dPlate);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void saveUserInfo() {
        dName = fName.getText().toString();
        dPhone = fPhone.getText().toString();
        dPlate = fPlate.getText().toString();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", dName);
        userInfo.put("phone", dPhone);
        userInfo.put("car", dPlate);
        driverDatabase.updateChildren(userInfo);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }
}
