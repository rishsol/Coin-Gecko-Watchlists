package com.example.coingecko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://coingeckowatchlists-default-rtdb.firebaseio.com/");
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<Model> coinList;
    public String assetprice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button addBtn = findViewById(R.id.WatchListBtn);
        final EditText searchCrypto = findViewById(R.id.searchCrypto);
        final Button searchBtn = findViewById(R.id.SearchBtn);
        final TextView priceText = findViewById(R.id.priceText);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        coinList = new ArrayList<>();
        adapter = new MyAdapter(coinList, this);

        recyclerView.setAdapter(adapter);

        final String currEmail = getIntent().getExtras().get("email").toString();

        dbRef.child("users").child(currEmail).child("coins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coinList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String coin = postSnapshot.getValue(String.class);
                    coinList.add(new Model(coin));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.child("users").child(currEmail).child("coins").push().setValue(searchCrypto.getText().toString());
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();

                String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";

                Request request = new Request.Builder().url(url).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {

                            String searchCryptoString = searchCrypto.getText().toString();

                            JSONObject res = null;
                            try {
                                res = new JSONObject(response.body().string());
                                assetprice = ((JSONObject) res.get(searchCryptoString)).get("usd").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //String finalAssetprice = assetprice;
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    priceText.setText("Price of " + searchCryptoString + ": " + assetprice);
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}