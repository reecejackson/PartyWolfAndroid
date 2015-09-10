package com.aolalabs.partywolf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Settings extends Activity {

    private Boolean initialFirstCheck;
    private Boolean initialSecondCheck;
    private String userId;
    private Switch onNewSwitch;
    private Switch onStatusSwitch;
    private NumberPicker onHypePicker;
    private Boolean onNew;
    private Boolean onStatus;
    private Integer onHype;
    private ParseUser currentUser = null;
    private ImageView profilePicture = null;
    private String userCity = null;


    NumberPicker noPicker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        currentUser = ParseUser.getCurrentUser();
        currentUser.fetchInBackground();

        userCity = (String) getIntent().getExtras().get("city");
        noPicker = (NumberPicker) findViewById(R.id.on_number);
        noPicker.setMaxValue(100);
        noPicker.setMinValue(0);
        noPicker.setWrapSelectorWheel(false);
        noPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        userId = currentUser.getObjectId();

        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onHypePicker.getValue() == 0) {
                    currentUser.put("onHype", -1);
                } else {
                    currentUser.put("onHype", onHypePicker.getValue());
                }

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    protected void onStart(){
        super.onStart();

        //To automatically set on the first and second switches if necessary
        onNew = currentUser.getBoolean("onNew");
        onStatus = currentUser.getBoolean("onStatus");
        onHype = currentUser.getInt("onHype");


        onStatusSwitch = (Switch) findViewById(R.id.first_notification);
        onNewSwitch = (Switch) findViewById(R.id.second_notification);
        onHypePicker = (NumberPicker) findViewById(R.id.on_number);
        profilePicture = (ImageView) findViewById(R.id.userProfilePic);

        onNewSwitch.setChecked(onNew);
        onStatusSwitch.setChecked(onStatus);
        onHypePicker.setValue(onHype);

        setContent();
        setUpSwitches();
    }

    private void setUpSwitches() {

        onNewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("On status", "User changed on new notification to " + onNewSwitch.isChecked());
                currentUser.put("onNew", onNewSwitch.isChecked());
            }
        });

        onStatusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("On status", "User changed on status notification to " + onStatusSwitch.isChecked());
                currentUser.put("onStatus", onStatusSwitch.isChecked());
            }
        });

        onHypePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d("On hype", "User changed their hype number to " + onHypePicker.getValue());
                if(onHypePicker.getValue() == 0) {
                    currentUser.put("onHype", -1);
                } else {
                    currentUser.put("onHype", onHypePicker.getValue());
                }
            }
        });
    }

    private void setContent() {
        // Set the profile picture
        try {
            ParseFile profilePictureFile = (ParseFile) currentUser.get("profile_pic");

            profilePictureFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {

                    if (e == null)
                    {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        RoundImage roundImage = new RoundImage(bmp);
                        profilePicture.setImageBitmap(bmp);
                    }
                    else
                    {
                        // Set a default profile picture
                    }

                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }

        TextView location = (TextView) findViewById(R.id.userLocation);
        final TextView school = (TextView) findViewById(R.id.schoolName);
        TextView classYear = (TextView) findViewById(R.id.userClass);
        TextView name = (TextView) findViewById(R.id.username);

        final ParseObject universityPointer = (ParseObject) currentUser.get("university");

        universityPointer.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                school.setText(universityPointer.getString("name"));
            }
        });

        if(userCity.equals("")) {
            try{
                location.setText(currentUser.getString("currentCity"));
            } catch (Exception e) {
                e.printStackTrace();
                location.setText("Couldn't determine city");
            }
        } else {
            location.setText(userCity);
        }

        classYear.setText("Class of " + currentUser.getNumber("classOf").toString()) ;

        // Set the name
        name.setText(currentUser.getString("first_name") + " " + currentUser.getString("last_name"));
    }

    public void openHype(View view){
        Intent intent = new Intent(this, Hype.class);
        startActivity(intent);
    }

    public void openDate(View view){
        Intent intent = new Intent(this, Date.class);
        startActivity(intent);
    }

    public void openAdd(View view){
        Intent intent = new Intent(this, Add.class);
        startActivity(intent);
    }

    public void openSettings(View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void openPrivacy(View view){
        Intent intent = new Intent(this, Privacy.class);
        startActivity(intent);
    }

    public void openTermsOfService(View view){
        Intent intent = new Intent(this, TermsOfService.class);
        startActivity(intent);
    }

    public void openContactUs(View view){
        Intent intent = new Intent(this, ContactUs.class);
        startActivity(intent);
    }

    public void logOut (View view) {
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginA.class);
        setResult(2);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        setResult(2);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        setResult(2);
        super.onDestroy();
    }

    /*https://parse.com/questions/updating-a-field-without-retrieving-the-object-first
    @Override
    public void onStop(){
        super.onStop();

        //Check status of switches upon activity close
        Switch oneSwitch = (Switch) findViewById(R.id.first_notification);
        final Boolean finalFirstCheck = (Boolean) oneSwitch.isChecked();

        Switch twoSwitch = (Switch) findViewById(R.id.second_notification);
        final Boolean finalSecondCheck = (Boolean) twoSwitch.isChecked();

        if (initialFirstCheck != finalFirstCheck) {
            StringBuilder success = new StringBuilder(getResources().getString(R.string.success));
            Toast.makeText(Settings.this, success.toString(), Toast.LENGTH_LONG).show();
            String currentUser = new String(String.valueOf(ParseUser.getCurrentUser()));

            ParseQuery<ParseObject> querySetting1 = ParseQuery.getQuery("User");
            querySetting1.whereEqualTo("user", currentUser);
            querySetting1.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> setting1, ParseException e) {
                    if (e == null) {
                        for (ParseObject userObject : setting1) {
                            String userID = userObject.getObjectId();
                            ParseObject point = ParseObject.createWithoutData("User", userID);
                            point.put("onStatus", finalFirstCheck);
                        }
                    } else {
                    }
                }
            });
        }
    }*/
}