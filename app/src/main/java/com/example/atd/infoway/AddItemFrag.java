package com.example.atd.infoway;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * Created by atd on 9/20/2016.
 */


    public class AddItemFrag extends Fragment {
    EditText ItemnameView;
    Button TakePicBut, AddBut, res;
    ImageView image;
    Item item;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout resource that'll be returned
            View rootView = inflater.inflate(R.layout.frag_add, container, false);

            // Get the arguments that was supplied when
            // the fragment was instantiated in the
            // CustomPagerAdapter
          //  Bundle args = getArguments();

        // create a new user
        User NewUser = User.getInstance();

            ItemnameView = (EditText) rootView.findViewById(R.id.EditItemName);
            TakePicBut = (Button) rootView.findViewById(R.id.buttonTakePic);
            AddBut = (Button) rootView.findViewById(R.id.buttonAdd);
            image = (ImageView) rootView.findViewById(R.id.imageView);

        TextView text = (TextView) rootView.findViewById(R.id.textView3);

        text.setText("Username" + NewUser.getUsername()+ " userF " +NewUser.getFirstName() +" userL " +NewUser.getLastName() +" userGender " +NewUser.getGender());
            // create a new item object
            item = new Item();
        item.user = User.getInstance();


        res = (Button) rootView.findViewById(R.id.res);
        // Take a picture button
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsersDBHelper.getInstance(getActivity()).getUserItems(item.user);

            }
        });


        // Take a picture button
        TakePicBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsersDBHelper.getInstance(getActivity()).getUserItems(item.user);
               dispatchTakePictureIntent();
            }
        });

        // Add new item button
        AddBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set item name
                item.name = ItemnameView.getText().toString();


               // dispatchTakePictureIntent();
                // call database add item
                // Get singleton instance of database
                UsersDBHelper.getInstance(getActivity()).addItem(item);

            }
        });

            return rootView;
        }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity( getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //Convert bitmap to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            //Encode base64 from byte array
            item.pic = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // set view thumbnail
            image.setImageBitmap(imageBitmap);
        }
    }


}



