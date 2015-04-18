package com.example.shanky.scuoncampusjobs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AssignShift extends Activity {

    Calendar calendar = Calendar.getInstance();
    Date date;
    String sTime ="";
    String eTime="";
    String itemSelectedInNameSpinner;
    String itemSelectedInIDSpinner;
    Spinner employeeNameSpinner, employeeIdSpinner;
    Button pickDate, pickStartTime, pickEndTime, Confirm;

    TextView result;
    ProgressBar pb;
    List<MyTask> tasks;
    List<assignShiftPlainOldJavaObjects> assignShiftPlainOldJavaObjectsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_shift);
        initializeWidgets();
        pb.setVisibility(View.INVISIBLE);
        createDatabase();
        spinnersUserInput();
        setListeners();
        checkOnlineInitializeAsyncTask();

    }

    private void setListeners() {
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AssignShift.this,dateListener,calendar.get(calendar.YEAR),calendar.get(calendar.MONTH),calendar.get(calendar.DAY_OF_MONTH)).show();
            }
        });

        pickStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AssignShift.this,startTimeListener,calendar.get(Calendar.HOUR),calendar.get(calendar.MINUTE),false).show();
            }
        });

        pickEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AssignShift.this,EndTimeListener,calendar.get(Calendar.HOUR),calendar.get(calendar.MINUTE),false).show();
            }
        });
    }


    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            calendar.set(year,monthOfYear,dayOfMonth);
            date = calendar.getTime();

        }
    };

    TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(calendar.MINUTE, minute);
            sTime = sTime + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

        }
    };

    TimePickerDialog.OnTimeSetListener EndTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(calendar.MINUTE, minute);
            eTime = eTime + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

        }
    };


    private void initializeWidgets() {
        pickDate = (Button) findViewById(R.id.AssignShiftPickDate);
        pickStartTime = (Button) findViewById(R.id.AssignShiftPickStartTime);
        pickEndTime = (Button) findViewById(R.id.AssignShiftPickEndTime);
        Confirm = (Button) findViewById(R.id.assignShiftConfirm );
        pb = (ProgressBar) findViewById(R.id.progressBar);
        result = (TextView) findViewById(R.id.result);
        result.setMovementMethod(new ScrollingMovementMethod());
        employeeNameSpinner = (Spinner) findViewById(R.id.EmployeeNameSpinner);
        employeeIdSpinner = (Spinner) findViewById(R.id.EmployeeIDSpinner);
        tasks = new ArrayList<>();
    }

    public void checkOnlineInitializeAsyncTask() {
        if (isOnline()) {
            requestData("http://10.0.2.2/assignShift.php");
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }
    protected void updateDisplay() {

        if (assignShiftPlainOldJavaObjectsList != null) {
            for (assignShiftPlainOldJavaObjects as_POJO : assignShiftPlainOldJavaObjectsList) {
                result.append(as_POJO.getEmployee_id() + " " + as_POJO.getEmployee_name() + " " +  as_POJO.getShift_date() + " " + as_POJO.getStart_time() + " " + as_POJO.getEnd_time() + "\n");
            }
        }
    }

    public void createDatabase(){

    }



    private void addItemsToEmployeeIDSpinner() {

        employeeIdSpinner = (Spinner) findViewById(R.id.EmployeeIDSpinner);
        List<String> labels = new ArrayList<String>();

        if (assignShiftPlainOldJavaObjectsList != null) {
            for (assignShiftPlainOldJavaObjects as_POJO : assignShiftPlainOldJavaObjectsList) {
                labels.add(as_POJO.getEmployee_id());
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> employeeIDSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        employeeIDSpinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        employeeIdSpinner.setAdapter(employeeIDSpinnerAdapter);

    }

    private void addItemsToEmployeeNameSpinner() {

        employeeNameSpinner = (Spinner) findViewById(R.id.EmployeeNameSpinner);
        List<String> labels = new ArrayList<String>();

        if (assignShiftPlainOldJavaObjectsList != null) {
            for (assignShiftPlainOldJavaObjects as_POJO : assignShiftPlainOldJavaObjectsList) {
                labels.add(as_POJO.getEmployee_name());
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> employeeIDSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        employeeIDSpinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        employeeNameSpinner.setAdapter(employeeIDSpinnerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assign_shift, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void spinnersUserInput(){

        employeeNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                itemSelectedInNameSpinner = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

                itemSelectedInNameSpinner = null;
            }
        });

        employeeIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                itemSelectedInIDSpinner = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                itemSelectedInIDSpinner=null;
            }
        });
    }

    public void assignShiftOnConfirm(View view) {
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            assignShiftPlainOldJavaObjectsList = assignShiftJSONParser.parseFeed(result);
            updateDisplay();
            addItemsToEmployeeIDSpinner();
            addItemsToEmployeeNameSpinner();

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(String... values)
        {

        }

    }


}