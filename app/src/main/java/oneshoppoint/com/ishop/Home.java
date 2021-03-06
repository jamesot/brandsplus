package oneshoppoint.com.ishop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stephineosoro on 04/09/16.
 */
public class Home extends AppCompatActivity {

    private RecyclerView mRecyclerView;


    private RecyclerView.Adapter mAdapter;

    private static final int ITEM_COUNT = 100;

    private List<Object> mContentItems = new ArrayList<>();
    public static View.OnClickListener myOnClickListener;
    private ProgressDialog pDialog;
    private List<Items> itemsList = new ArrayList<>();
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    //    private static RecyclerView recyclerView;
    JSONArray res = null;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_subcategory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Brands Plus");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        myOnClickListener = new MyOnClickListener(this);
//        {
//            for (int i = 0; i < ITEM_COUNT; ++i)
//                mContentItems.add(new Object());
//            mAdapter.notifyDataSetChanged();
//        }
        GetData();
    }

    private void GetData() {
        // Toast.makeText(getBaseContext(), "Inside function!", Toast.LENGTH_SHORT).show();
        // Tag used to cancel the request

        JSONObject js = new JSONObject();
        try {
            JSONObject jsonobject_one = new JSONObject();

            jsonobject_one.put("type", "event_and_offer");
            jsonobject_one.put("devicetype", "I");

            JSONObject jsonobject_TWO = new JSONObject();
            jsonobject_TWO.put("value", "event");
            JSONObject jsonobject = new JSONObject();

            jsonobject.put("requestinfo", jsonobject_TWO);
            jsonobject.put("request", jsonobject_one);


            js.put("data", jsonobject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONErrorin serializing", e.toString());
        }
        Log.e("JSON serializing", js.toString());
        String tag_string_req = "req_Categories";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, "https://www.oneshoppoint.com/api/category?page=1&parts=1", js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response from server is", response.toString());


                        String success = null;
                        try {


                            //successfully gotten matatu data
//                        String regno = jObj.getString("regno");

                            res = response.getJSONArray("data");
                            JSONArray children1 = null;
                            Log.e("result: ", res.toString());

                            // looping through All res
                            for (int i = 0; i < res.length(); i++) {
                                JSONObject c = res.getJSONObject(i);

                                // Storing each json item in variable

                                String name = c.getString("name");
                                children1 = c.getJSONArray("children");
                                Log.e("CategoryFragment", name);
                                Items items = new Items();
                                items.setTitle(name);
                                items.setTheID(c.getString("id"));
                                JSONObject a = c.getJSONObject("image");
                                items.setThumbnailUrl("https://www.oneshoppoint.com/images" + a.getString("path"));
                                itemsList.add(items);
//                                Group group = new Group(name,a.getString("path"));
//                                for (int j = 0; j < children1.length(); j++) {
//                                    JSONObject d = children1.getJSONObject(j);
//                                    JSONObject e = d.getJSONObject("image");
//
//                                    items.setTheID(d.getString("id"));
//                                }
//                                groups.append(i, group);
//
//                                ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
//                                HomeAdapter adapter = new HomeAdapter(context, groups);
//                                listView.setAdapter(adapter);
//
//                                String sacco = c.getString("sacco");
//                                String question = c.getString("question");
//                                items.setTitle("winner sacco: " + sacco);
//                                items.setDescription("Best in: " + question);
//                            items.setPrice("Total Rating is: " + rate);

//
                            }
//
                            adapter = new CategoryAdapter(getBaseContext(), itemsList);
                            mRecyclerView.setAdapter(adapter);


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
//                hideProgressDialog();
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
                headers= MyShortcuts.AunthenticationHeaders(getBaseContext());
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.e("request is", jsonObjReq.toString());
    }

    public class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Subcategories(v);
//            Toast.makeText(getBaseContext()(),  "has been added to the basket!", Toast.LENGTH_SHORT).show();


        }

        private void Subcategories(View v) {
            int selectedItemPosition = mRecyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = mRecyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            TextView textViewID
                    = (TextView) viewHolder.itemView.findViewById(R.id.theID);

            String selectedName = (String) textViewName.getText();
            String selectedID = (String) textViewID.getText();

            Log.e("NAME", selectedName);
            Log.e("PRICEs", selectedID);
            Intent intent = new Intent(getBaseContext(), SubCategory.class);
            intent.putExtra("category_id", selectedID);
            intent.putExtra("category_name", selectedName);

//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            recyclerView.setAdapter(null);
            startActivity(intent);


        }
    }
}
