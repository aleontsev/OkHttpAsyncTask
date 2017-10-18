package doitnow.fivecards;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "Logs";
    final String ip_key = "ip";
    final String date_key = "date";
    final String time_key = "time";
    final String echo_second_key = "one";
    final String json_validation_key = "validate";
    String echo_first_key = "";

    TextView ipTextView;
    TextView timeTextView;
    EditText firstParameterEcho;
    EditText secondParameterEcho;
    TextView firstParameter;
    TextView secondParameter;
    TextView echoUrl;
    TextView validationResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ipTextView   = (TextView) findViewById(R.id.text_ip_address);
        timeTextView = (TextView) findViewById(R.id.text_time);

        String url_ip    = "http://ip.jsontest.com/";
        String url_time  = "http://date.jsontest.com";

        //Setting IP
        OkHttpHandler ipHttpHandler = new OkHttpHandler();
        ipHttpHandler.execute(url_ip);

        //Setting Date and Time
        OkHttpHandler dateHttpHandler = new OkHttpHandler();
        dateHttpHandler.execute(url_time);

   }

    //on click listener for Contacts Button
    public void onContactsButtonClick(View view){
        Log.d(LOG_TAG, "onContactsButtonClick");

        Intent i = new Intent(this, ContactActivity.class);
        startActivity(i);
    }

    //on click listener for Submit_Echo Button
    public void onSubmitButtonClick(View view){
        Log.d(LOG_TAG, "onSubmitButtonClick");

        String parameter1 = null;
        String parameter2 = null;
        //http://echo.jsontest.com/key/value/one/two
        String url_echo_json  = "http://echo.jsontest.com";

        firstParameterEcho =(EditText)findViewById(R.id.editTextKey);
        secondParameterEcho =(EditText)findViewById(R.id.editTextValue);

        firstParameter = (TextView)findViewById(R.id.text_result_json1);
        secondParameter = (TextView)findViewById(R.id.text_result_json2);

        echoUrl =(TextView)findViewById(R.id.text_url_echo);

        parameter1 =  firstParameterEcho.getText().toString();
        parameter2 =  secondParameterEcho.getText().toString();

        echo_first_key = firstParameterEcho.getText().toString();

        if((parameter1!=null)&&(parameter2!=null)){
            url_echo_json = url_echo_json + "/"+parameter1+"/"+parameter2+"/one/two";
            echoUrl.setText(url_echo_json);

            OkHttpHandler echoHttpHandler = new OkHttpHandler();
            echoHttpHandler.execute(url_echo_json);
        }
    }

    //onSubmitValidationButtonClick
    public void onSubmitValidationButtonClick(View view) {
        Log.d(LOG_TAG, "onSubmitValidationButtonClick");

        String url_json_validation = "http://validate.jsontest.com/?json=";
        String json_request = null;
        validationResult = (TextView)findViewById(R.id.result_validation);

        EditText v = (EditText)findViewById(R.id.validation_request);
        TextView validationView = (TextView)findViewById(R.id.result_validation);
        json_request = v.getText().toString();
        url_json_validation = url_json_validation + json_request;
        OkHttpHandler validationHttpHandler = new OkHttpHandler();
        validationHttpHandler.execute(url_json_validation);

    }

    //parsing JSON response
    public String JSONParse(String JSONResponse, String key){
        Log.d(LOG_TAG, JSONResponse.toString());

        String ipAddressString = null;
        JSONObject ipAddressJSON = null;
        try {
            ipAddressJSON = new JSONObject(JSONResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ipAddressString = ipAddressJSON.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ipAddressString==null)
            return "no data";
        else
            return ipAddressString;
    }

    //okhttp call using async task
    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                //TimeUnit.SECONDS.sleep(0);
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (JSONParse(s, ip_key) != "no data") {
                ipTextView.setText("IP: " + JSONParse(s, ip_key));
            }
            if ((JSONParse(s, date_key) != "no data") && (JSONParse(s, time_key) != "no data")) {
                timeTextView.setText("date&time: " + JSONParse(s, date_key) + " " + JSONParse(s, time_key));
            }
            if(JSONParse(s, echo_second_key) != "no data"){
                secondParameter.setText(echo_second_key + ": " + JSONParse(s, echo_second_key));
            }
            if(JSONParse(s, echo_first_key) != "no data"){
                firstParameter.setText(echo_first_key+ ": " + JSONParse(s, echo_first_key));
            }
            if(JSONParse(s, json_validation_key) != "no data") {
                validationResult.setText(JSONParse(s, json_validation_key));
            }

        }
    }
    ///Another example of using okhttp request Asynchronously
    //import android.os.AsyncTask;
    //import android.support.v7.app.AppCompatActivity;
    //import android.os.Bundle;
    //import android.util.Log;
    //import android.view.View;
    //import android.widget.Button;
    //import android.widget.TextView;
    //
    //import org.json.JSONException;
    //import org.json.JSONObject;
    //
    //import java.io.IOException;
    //
    //import okhttp3.Call;
    //import okhttp3.Callback;
    //import okhttp3.MediaType;
    //import okhttp3.OkHttpClient;
    //import okhttp3.Request;
    //import okhttp3.RequestBody;
    //import okhttp3.Response;
    //
    //    public class MainActivity extends AppCompatActivity {
    //
    //        TextView txtString;
    //        public String url= "https://reqres.in/api/users/2";
    //
    //        @Override
    //        protected void onCreate(Bundle savedInstanceState) {
    //            super.onCreate(savedInstanceState);
    //            setContentView(R.layout.activity_main);
    //
    //            txtString= (TextView)findViewById(R.id.txtString);
    //
    //            try {
    //                run();
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //
    //        void run() throws IOException {
    //
    //            OkHttpClient client = new OkHttpClient();
    //
    //            Request request = new Request.Builder()
    //                    .url(url)
    //                    .build();
    //
    //            client.newCall(request).enqueue(new Callback() {
    //                @Override
    //                public void onFailure(Call call, IOException e) {
    //                    call.cancel();
    //                }
    //
    //                @Override
    //                public void onResponse(Call call, Response response) throws IOException {
    //
    //                    final String myResponse = response.body().string();
    //
    //                    MainActivity.this.runOnUiThread(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            txtString.setText(myResponse);
    //                        }
    //                    });
    //
    //                }
    //            });
    //        }
    //
    //    }

}


