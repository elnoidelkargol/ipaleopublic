package com.nappstic.alimentospaleo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShoppingListFragmentArrayAdapter extends ArrayAdapter<itemCart> {
    private Context context;
    List<itemCart> listItemsCart;
    itemCart mitemCart;
    ArrayList<String> checkedIds ;

    FirebaseDatabase mFirebaseDatabase;
    String uid;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mitemCardReference;

    private ItemClickListener mitemClickListener;


    public ShoppingListFragmentArrayAdapter(@NonNull Context context, int resource, List<itemCart> objects, ItemClickListener itemClickListener) {
        super(context, resource, objects);
            this.listItemsCart = objects;
        this.context = context;
        this.mitemClickListener = itemClickListener;
        checkedIds = new ArrayList<String>();


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.row_shopping_cart,parent,false);
        }
        Log.d("POSITION", "getView: " + position);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        uid = mFirebaseAuth.getUid().toString();
        mitemCardReference = mFirebaseDatabase.getReference().child("carts").child(uid);


        TextView foodName = (TextView) convertView.findViewById(R.id.txtViewNameShoppingList);
        final CircleImageView imgFood = (CircleImageView) convertView.findViewById(R.id.foodCardCircleView);
        final CheckBox checkBoxFood = (CheckBox) convertView.findViewById(R.id.chartFoodChecklist);
        checkBoxFood.setTag(position);
        final itemCart mitemCart2 = listItemsCart.get(position);
        Button btnClear = (Button) convertView.findViewById(R.id.btnClearShoppingList);
        Log.d("mitemcart", "getView: " + mitemCart2.obtainCorrectLanguageName());
        checkBoxFood.setChecked(mitemCart2.getBought());
        if (mitemCart2.getBought()){
            checkedIds.add(mitemCart2.get_id());
        }
        foodName.setText(mitemCart2.obtainCorrectLanguageName()+" "+mitemCart2.getQuantity()+" "+mitemCart2.getMesurementunit());
        Glide.with(imgFood.getContext())
                .load(mitemCart2.getImg_firebase())
                .into(imgFood);
        checkBoxFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("checkbox", " ");
                Integer position2 = (Integer) checkBoxFood.getTag();
                itemCart itemCart3 = listItemsCart.get(position2);
                Log.d("NOMBRE", "onClick: "+itemCart3.obtainCorrectLanguageName());
                itemCart3.changeBought();
                if(itemCart3.getBought()){
                    checkedIds.add(itemCart3.get_id());
                }else{
                    checkedIds.remove(itemCart3.get_id());
                }
                mitemCardReference.child(itemCart3.get_id()).child("bought").setValue(itemCart3.getBought());


            }
        });

        return convertView;
    }


    public ArrayList<String> getCheckedIds() {
        return checkedIds;
    }
}
