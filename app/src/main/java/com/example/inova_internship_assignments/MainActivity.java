package com.example.inova_internship_assignments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ImageView imageview;
    Button navigation_button;
    Button capture_image;


    private final static int MediaPermissionCode = 0;
    private final static int locationPermissionCode = 1;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("this is Permissions", "arr: " + Arrays.toString(permissions));
        Log.d("this is Granted results", "arr: " + Arrays.toString(grantResults));

        if(requestCode == MediaPermissionCode){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getImageFromAlbum();
            }else{
                Toast.makeText(this, "access media Permission denied",Toast.LENGTH_LONG).show();
            }

        }else if(requestCode == locationPermissionCode){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                addLocationListener();
            }else{
                Toast.makeText(this, "location Permission denied",Toast.LENGTH_LONG).show();
            }


        } else{
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }



    public boolean have_permission(String ManifestPermission,int permissioncode){
        // look at the media permission
        boolean per = ContextCompat.checkSelfPermission(this, ManifestPermission)
                == PackageManager.PERMISSION_GRANTED;

        // ask for read media permission  if not granted and save result in cam_per
        if (!per) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ManifestPermission}, permissioncode);
        }
        return per;
    }

    private void getImageFromAlbum(){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, MediaPermissionCode);
    }

    @SuppressLint("MissingPermission")
    private void addLocationListener() {
        Toast.makeText(this, "Opening Google maps ...",Toast.LENGTH_LONG).show();
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Uri gmmIntentUri = Uri.parse("geo:" + location.getLatitude() + "," + location.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                locationManager.removeUpdates(this);
//                startActivity(mapIntent);
                Log.d("TAG",gmmIntentUri.toString());
            }
        });

    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == MediaPermissionCode && resultCode == RESULT_OK && null!=data ) {
            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                imageview.setImageBitmap(selectedImage);

                imageview.setImageURI(data.getData());
//            } catch (FileNotFoundException e) {
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(MainActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        capture_image = findViewById(R.id.capture_image);
        navigation_button = findViewById(R.id.navigation_button);
        imageview = findViewById(R.id.imageView);


        capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("TAG","inside");
                    if(have_permission(Manifest.permission.READ_EXTERNAL_STORAGE,MediaPermissionCode)){
                        getImageFromAlbum();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Some error happen",Toast.LENGTH_LONG).show();
                }
            }
        });

        navigation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","before");
                try {
                    if(have_permission(Manifest.permission.ACCESS_FINE_LOCATION,locationPermissionCode)){
                        Log.d("TAG","inside");
                        addLocationListener();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Some error happen",Toast.LENGTH_LONG).show();
                }
                Log.d("TAG","after");
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        //TODO add log.d to see the lifecycle in logs
        Log.d("TAG","start");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        dispatchTakePictureIntent()
        //TODO add log.d to see the lifecycle in logs
        Log.d("TAG","resume");

    }

    @Override
    protected void onPause() {
        super.onPause();

        //TODO add log.d to see the lifecycle in logs
        Log.d("TAG","pause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        //TODO add log.d to see the lifecycle in logs
        Log.d("TAG","stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //TODO add log.d to see the lifecycle in logs
        Log.d("TAG","destroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //TODO add log.d to see the lifecycle in logs
        Log.d("TAG","restart");
    }

}