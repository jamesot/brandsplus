package oneshoppoint.com.ishop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * Created by stephineosoro on 15/06/16.
 */
public class Plans extends AppCompatActivity {
    static CardGridArrayAdapter mCardArrayAdapter;
    protected ArrayList<Card> cards = new ArrayList<Card>();
    String carriers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_shipping);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Choose a plan");

        carriers = getIntent().getStringExtra("carrierPlans");
        JSONArray Res = null;
        try {

            Res = new JSONArray(carriers);

            for (int i = 0; i < Res.length(); i++) {
                JSONObject c = Res.getJSONObject(i);
                String id = c.getString("id");

                Log.e("id",id);
                String name = c.getString("name");

                GplayGridCard card = new GplayGridCard(getBaseContext());
                card.headerTitle = name ;
//                card.secondaryTitle="    total is Ksh. " + total;
                card.setId(id);
                card.setTitle(name);
                card.init();
                cards.add(card);
                Log.d("names", name );

            }

            mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);

            CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public class GplayGridCard extends Card {


    protected String url;

    protected String headerTitle;
    protected String secondaryTitle;
    protected float rating;

    public GplayGridCard(Context context) {
        super(context, R.layout.inner_content);
    }


    public GplayGridCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    private void init() {


        CardHeader header = new CardHeader(getContext());
        header.setButtonOverflowVisible(true);
        header.setTitle(headerTitle);
           /* header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
//                    Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    String selected = card.getId();
                    Toast.makeText(getBaseContext(), "Patient deleted", Toast.LENGTH_LONG).show();
//                    ID = card.getId();
//                    if (mCardArrayAdapter != null) {

                    cards.remove(card); //It is an example.
                    mCardArrayAdapter.notifyDataSetChanged();
//                    }

                }
            });*/

        addCardHeader(header);

        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                //Do something



                carriers = getIntent().getStringExtra("carrierPlans");
                JSONArray Res = null;
                try {


                    Res = new JSONArray(carriers);

                    for (int i = 0; i < Res.length(); i++) {
                        JSONObject c = Res.getJSONObject(i);
                        String id = c.getString("id");




                        if (card.getId().equals(id)){

                            Log.e("id is", id);

                            Intent intent =new Intent(getBaseContext(),CarrierCode.class);
                            intent.putExtra("paybill",getIntent().getStringExtra("paybill"));
                            intent.putExtra("name",getIntent().getStringExtra("name"));
                            intent.putExtra("id",id);
                            Log.e("paybill", getIntent().getStringExtra("paybill"));
                            Log.e("id",id);

                            intent.putExtra("planname",card.getTitle());
//                            intent.putExtra("totalretailer",c.getString("totalPrice"));
                            startActivity(intent);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        final TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
        subtitle.setText(secondaryTitle);

    }


}



}
