package com.mobilalk.buszjegy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OwnedTicketListActivity extends AppCompatActivity {
    private static final String LOG_TAG = OwnedTicketListActivity.class.getName();
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private ArrayList<TicketItem> mItemList;
    private OwnedTicketItemAdapter mAdapter;

    private int gridNumber = 1;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private CollectionReference mOwnerships;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owned_ticket_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Could not authenticate user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new OwnedTicketItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        mOwnerships = mFirestore.collection("Ownerships");

        queryData();
    }

    private void queryData() {
        mItemList.clear();

        mOwnerships
            .whereEqualTo("email", user.getEmail())
            .get()
            .addOnSuccessListener(ownershipSnapshots -> {
                List<String> ownedItemIds = new ArrayList<>();

                for (QueryDocumentSnapshot ownership : ownershipSnapshots) {
                    String itemId = ownership.getString("itemId");
                    if (itemId != null) {
                        ownedItemIds.add(itemId);
                    }
                }

                if (ownedItemIds.isEmpty()) {
                    mAdapter.notifyDataSetChanged();
                    return;
                }

                for (String itemId : ownedItemIds) {
                    mItems.document(itemId).get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            TicketItem item = documentSnapshot.toObject(TicketItem.class);
                            item._setId(documentSnapshot.getId());
                            mItemList.add(item);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.owned_ticket_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.browseTickets) {
            finish();
            return true;
        } else if (itemId == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Sikeres kijelentkezés!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void deleteItem(TicketItem currentItem) {
        mOwnerships
            .whereEqualTo("email", user.getEmail())
            .whereEqualTo("itemId", currentItem._getId())
            .get()
            .addOnSuccessListener(querySnapshots -> {
                for (QueryDocumentSnapshot doc : querySnapshots) {
                    mFirestore.collection("Ownerships")
                            .document(doc.getId())
                            .delete();
                    queryData();
                    break;
                }
            });
    }
}