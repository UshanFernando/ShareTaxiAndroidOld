package com.example.ushanfernando.sharetaxi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ushanfernando.sharetaxi.Model.STApiRequest;
import com.example.ushanfernando.sharetaxi.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CompleteRegistration extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener, STApiRequest.OnRequestComplete {
    private static final String TAG = "CompleteRegistration";

//    private static final String urlComReg = "http://10.0.2.2:8080/server/information.php";

    //public server
    private static final String urlComReg = Constants.Domain+"/api/information.php";

    EditText bDay;
    EditText fName;
    EditText lName;
    EditText vehicel;
    EditText phoneNo;
    EditText city;

    CheckBox vehicelStatus;
    CheckBox iAgree;

    Button completeReg;

    TextView errorLabel;

    private int _day;
    private int _month;
    private int _birthYear;
    private Context _context;
    Spinner gender;
    Spinner province;

    SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_registration);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        LinearLayout cardView = findViewById(R.id.thankyouCard);

        fName = findViewById(R.id.fname_field);
        lName = findViewById(R.id.lname_field);
        vehicel = findViewById(R.id.vehicle_field);
        phoneNo = findViewById(R.id.phone_field);
        city = findViewById(R.id.city_field);

        errorLabel = (TextView) findViewById(R.id.error_label_complete_reg);

        vehicelStatus = (CheckBox) findViewById(R.id.field_vehicle_status);
        iAgree = findViewById(R.id.field_agree);

        completeReg = (Button) findViewById(R.id.btn_complete_reg);

        AnimationDrawable animationDrawable = (AnimationDrawable) cardView.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        bDayPicker(this, R.id.bDay_field);

        gender = (Spinner) findViewById(R.id.gender_field);
        province = (Spinner) findViewById(R.id.province_field);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.province, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(adapter2);

        completeReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeRegistration();
            }
        });

        mSessionManager = new SessionManager(this);


        vehicelStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    vehicel.setEnabled(true);
                }else {
                    vehicel.setEnabled(false);
                }
            }
        });
    }


    private void completeRegistration() {

        if (!isEmpty()) {

            Map<String, String> reqTags = new HashMap<>();
            reqTags.put("email", mSessionManager.getEmail());
            reqTags.put("password", mSessionManager.getPassword());
            reqTags.put("lname", lName.getText().toString());
            reqTags.put("fname", fName.getText().toString());
            reqTags.put("gender", gender.getSelectedItem().toString());
            reqTags.put("dob", bDay.getText().toString());
            reqTags.put("tel", phoneNo.getText().toString());
            reqTags.put("province", province.getSelectedItem().toString());
            reqTags.put("city", city.getText().toString());
            reqTags.put("vehicle", vehicel.getText().toString());

            Log.i(TAG, "completeRegistration: " + reqTags);

            STApiRequest req = new STApiRequest(CompleteRegistration.this, urlComReg, "req");
            req.newRequest(this, reqTags);

            Toast.makeText(this, "SignUp Sucessfull!", Toast.LENGTH_LONG).show();

        }


    }

    private boolean isEmpty() {
        String msg = "Please Fix Following errors to continue.";
        if (fName.getText().toString().length() == 0) {
            msg += "\nFirst Name Field is empty";
            fName.setError("This Field is Required!");
        } else if (lName.getText().toString().length() == 0) {
            msg += "\nLast Name Field is empty";
            lName.setError("This Field is Required!");
        } else if (bDay.getText().toString().length() == 0) {
            msg += "\nBirth day Field is empty";
            bDay.setError("This Field is Required!");
        } else if (gender.getSelectedItem().toString().equals("Gender") || gender.getSelectedItem() == null) {
            msg += "\nSelect your Gender";
            ((TextView) gender.getSelectedView()).setError("This Field is Required!");
        } else if (phoneNo.getText().toString().length() < 10) {
            msg += "\nPhone No Field is empty or Invalid";
            phoneNo.setError("This Field is Required!");
        } else if (vehicelStatus.isChecked() && vehicel.getText().toString().length() == 0) {
            msg += "\nVehicle Name Field is Empty";
            vehicel.setError("This Field is Required!");
        } else if (province.getSelectedItem().toString().equals("Select Your Province") || province.getSelectedItem() == null) {
            msg += "\nSelect your Province";
            ((TextView) province.getSelectedView()).setError("This Field is Required!");
        } else if (city.getText().toString().length() == 0) {
            msg += "\nCity Field is empty";
            city.setError("This Field is Required!");
        } else if (!iAgree.isChecked()) {
            msg += "\nYou have to agree the Terms and Conditions to continue Registration";
            iAgree.setError("This Field is Required!");
        } else {
            return false;
        }

        errorLabel.setText(msg);
        Toast.makeText(this, "Form not Complete can't submit! ", Toast.LENGTH_SHORT).show();

        return true;
    }

    public void bDayPicker(Context context, int editTextViewID) {
        Activity act = (Activity) context;
        this.bDay = act.findViewById(editTextViewID);
        this.bDay.setOnClickListener(this);
        this._context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _birthYear = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        bDay.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
        bDay.setError(null);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onSuccessRequest(JSONObject result, String instanceName) {

        Log.d(TAG, "onSuccessCompRegRequest: " + result);
        int status;
        String msg;

        if (instanceName.equals("register")) {
            try {
                status = result.getInt("status");
                msg = result.getString("message");

                switch (status) {
                    case 0:
                        errorLabel.setText("Connecting!");
                        mSessionManager.setIncompleteRegistrationStatus(false);
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        errorLabel.setText(msg);
                        break;
                    case 2:
                        errorLabel.setText(msg);
                        break;
                    case 3:
                        errorLabel.setText(msg);
                        break;
                }

            } catch (JSONException e) {
                Log.e(TAG, "onSuccessRequest: " + e);
            }

        }
    }
}


