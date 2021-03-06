package oneshoppoint.com.ishop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by stephineosoro on 31/05/16.
 */

public class GetLocation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "SignupActivity";
    public static List<String> location = new ArrayList<String>();
    public static List<String> locationID = new ArrayList<String>();
    static String lID, selected_item;
    /* @Bind(R.id.inn_spinner)
     Spinner _Location;*/
    Spinner spinner2;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.error_message)
    TextView errormessage;
    ArrayAdapter<String> adapter = null;

    int succ = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_get_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Type you location");
        ButterKnife.bind(this);
        this.spinner2 = (Spinner) findViewById(R.id.inn_spinner);
        spinner2.setOnItemSelectedListener(this);
        GetLocations();
        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item, location);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
//                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
//                startActivity(intent);
            }
        });


    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

//        _signupButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(GetPrescription.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();


        // TODO: Implement your own signup logic here.
        GetRetailers();
//        saveResults();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
//                        onSignupSuccess();
                        // onSignupFailed();
//                        if (succ == 1) {
//                            Intent intent = new Intent(getBaseContext(), ShowPatients.class);
//                            startActivity(intent);
//                        }
//                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "adding prescription to cart failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        /*String location = _Location.getText().toString();


        if (location.isEmpty()) {
            _Location.setError("Input location");
            valid = false;
        } else {
            _Location.setError(null);
        }*/


        return valid;
    }

    private void GetRetailers() {
        // Toast.makeText(getBaseContext(), "Inside function!", Toast.LENGTH_SHORT).show();
        // Tag used to cancel the request

        JSONObject js = new JSONObject();
        try {


            String res = MyShortcuts.getDefaults("cartArray", getBaseContext());
            JSONArray jsonArray = new JSONArray(res);
            js.put("cart", jsonArray);
            js.put("locationId", lID);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        Log.e("JSON serializing", js.toString());
        String tag_string_req = "req_Categories";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "https://www.oneshoppoint.com/api/checkout/", js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response from server is", response.toString());
                        try {
                            JSONArray data = response.getJSONArray("data");
                            MyShortcuts.setDefaults("locationId", lID, getBaseContext());
                            Intent intent = new Intent(getBaseContext(), Retailers.class);
                            intent.putExtra("retailers", response.toString());
                            location.clear();
                            locationID.clear();
                            startActivity(intent);


                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
//                hideProgressDialog()
                Log.d("error volley", error.toString());
                errormessage.setVisibility(View.VISIBLE);
                errormessage.setText("Prescription code or location is not valid. Input a valid input");
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers = MyShortcuts.AunthenticationHeaders(getBaseContext());
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.e("request is", jsonObjReq.toString());
    }


    public void GetLocations() {
        Log.d("URL is", "https://www.oneshoppoint.com/api/location/");
        String tag_string_req = "req_inn";
        StringRequest strReq = new StringRequest(Request.Method.GET, "https://www.oneshoppoint.com/api/location/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response from server is", response.toString());


                String success = null;
                try {

                    JSONObject jObj = new JSONObject(response);
                    JSONArray data = jObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        String name = c.getString("name");
                        Log.e("Each data" + i, name);
                        location.add(name);
                        locationID.add(c.getString("id"));

                        Log.e("Each data" + i, c.getString("id"));
                    }

                   adapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("JSON ERROR", e.toString());
                }
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
//                hideProgressDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                headers = MyShortcuts.AunthenticationHeaders(getBaseContext());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
//                Log.e("category id", getIntent().getStringExtra("category_id"));
//                params.put("categoryId", 2 + "");


                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.e("request is", strReq.toString());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        parent.getItemAtPosition(position);
        lID = locationID.get(position);
        selected_item = parent.getItemAtPosition(position).toString();
        Log.e("INN ID", lID);
        Log.e("INN", selected_item);

//        Toast.makeText(getBaseContext(), "Seleccted an ITEM", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        lID = locationID.get(1);
    }
}