package luongvo.com.todolistminimal.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import luongvo.com.todolistminimal.ToDoItem;

/**
 * Created by Redlor on 25/11/2017.
 * This class contains the methods need to update the Real-Time Database on Firebase.
 */

public class UpdateFirebase {

    // Get a reference to the Database
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = firebaseUser.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("toDoItems");

    public UpdateFirebase(){}

    // This method add a new item to Firebase Database
    public void addItem(ToDoItem toDoItem) {
        System.out.println("user " + uid);
        String itemId = databaseReference.push().getKey();
        System.out.println(itemId);
        databaseReference.child(itemId).setValue(toDoItem);
        toDoItem.setItemId(itemId);
    }

    // This method delete an item from Firebase Database
    public void deleteItem(final ToDoItem toDoItem) {
        String id = toDoItem.getItemId();
        System.out.println("the id is: " + id);
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    dataSnapshot1.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // This method delete all the checked items from Firebase
    public void deleteChecked() {
        Query query = databaseReference.orderByChild("done").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    dataSnapshot1.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}