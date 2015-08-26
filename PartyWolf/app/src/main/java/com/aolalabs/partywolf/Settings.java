package com.aolalabs.partywolf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;

import com.parse.ParseUser;

public class Settings extends Activity {

    private Boolean initialFirstCheck;
    private Boolean initialSecondCheck;
    private String userId;
    private Boolean onNew;
    private Boolean onStatus;
    private Integer onHype;
    private ParseUser currentUser = null;

    NumberPicker noPicker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        noPicker = (NumberPicker) findViewById(R.id.on_number);
        noPicker.setMaxValue(100);
        noPicker.setMinValue(0);
        noPicker.setWrapSelectorWheel(false);
        noPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        currentUser = ParseUser.getCurrentUser();
        userId = currentUser.getObjectId();
    }

    protected void onStart(){
        super.onStart();

        //To automatically set on the first and second switches if necessary
        onNew = currentUser.getBoolean("onNew");
        onStatus = currentUser.getBoolean("onStatus");
        onHype = currentUser.getInt("onHype");


        Switch firstSwitch = (Switch) findViewById(R.id.first_notification);
        Switch secondSwitch = (Switch) findViewById(R.id.second_notification);
        NumberPicker onNewHype = (NumberPicker) findViewById(R.id.on_number);


        if (onNew) {
            firstSwitch.setChecked(true);
        } else {
            firstSwitch.setChecked(false);
        }

        if (onStatus) {
            secondSwitch.setChecked(true);
        } else {
            secondSwitch.setChecked(false);
        }

        onNewHype.setValue(onHype);

        /*ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("User");
        query.getInBackground(userId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    String hello = (String) object.get("hello");
                    Toast.makeText(Settings.this, "hello" + hello,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Settings.this, "Wrong "+ e, Toast.LENGTH_LONG).show();// something went wrong
                }
            }
        });


        boolean initialFirstBoolean = currentUser.getBoolean("onNew");
        onHype = ParseUser.getCurrentUser().getInt("onHype");

        noPicker.setValue(onHype);

        boolean initialSecondBoolean = currentUser.getBoolean("onNew");


        if (initialFirstBoolean) {
            Switch firstSwitch = (Switch) findViewById(R.id.first_notification);
            firstSwitch.setChecked(true);
        } else {
            Switch firstSwitch = (Switch) findViewById(R.id.first_notification);
            firstSwitch.setChecked(false);
        }

        if (initialSecondBoolean) {
            Switch secondSwitch = (Switch) findViewById(R.id.second_notification);
            secondSwitch.setChecked(true);
        } else {
            Switch secondSwitch = (Switch) findViewById(R.id.second_notification);
            secondSwitch.setChecked(false);
        }*/


        //Check status of switches upon activity start up
        initialFirstCheck = firstSwitch.isChecked();
        initialSecondCheck = secondSwitch.isChecked();
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
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
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