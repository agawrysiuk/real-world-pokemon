package agawrysiuk.googlemapspokemonclone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import agawrysiuk.googlemapspokemonclone.model.MapManager;
import agawrysiuk.googlemapspokemonclone.views.TypeTextView;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private static final int LOCATION_REQUEST_CODE = 1000;
    //NETWORK_PROVIDER for real device
    //GPS_PROVIDER for emulator
    private static final String PROVIDER_FOR_GPS = LocationManager.GPS_PROVIDER;

    private MapManager mMapManager;

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private TypeTextView mTypeTextView;
    private ImageView mBubbleSpeechView;

    // == for the first location update
    private float rotation = 0;
    private boolean isFirstMapLoad = true;

    // == for the new activity to start ==
    private boolean isReadyToFight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //create a map manager for help purposes
        mMapManager = new MapManager(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // == setting up speech bubble ==
        mTypeTextView = findViewById(R.id.typeText);
        mTypeTextView.setOnClickListener(hideBubbleStartFight());
        mBubbleSpeechView = findViewById(R.id.bubbleSpeech);
        mBubbleSpeechView.setOnClickListener(hideBubbleStartFight());

        // == adding rotation sensors
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, rotationSensor,
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }

    private View.OnClickListener hideBubbleStartFight() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeTextView.setVisibility(View.INVISIBLE);
                mBubbleSpeechView.setVisibility(View.INVISIBLE);

                startActivity(new Intent(MapsActivity.this,FightActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                isReadyToFight = false;
            }
        };
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // == saving rotation sensor value ==
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            rotation = (float) Math.toDegrees(-event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // == when the map is ready ==
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // == set up map style ==
        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        if (!success) {
            Log.e("MAP STYLE", "Style parsing failed.");
        }

        // == map UI settings ==
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);

        // == setting up manager and listener ==
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // == we update player's location with location change
                updateYourLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // == request gps access ==
        if (Build.VERSION.SDK_INT < 23) {
            mLocationManager.requestLocationUpdates(PROVIDER_FOR_GPS, 0, 0, mLocationListener);
        } else if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                mLocationManager.requestLocationUpdates(PROVIDER_FOR_GPS, 0, 0, mLocationListener);
                Location location = mLocationManager.getLastKnownLocation(PROVIDER_FOR_GPS);
                updateYourLocation(location);
            }
        }

        // == test pokemon markers
        LatLng pkmnLocation = new LatLng(52.2310, 21.0067);
        mMap.clear();
        MarkerOptions marker = new MarkerOptions()
                .position(pkmnLocation)
                .title("Pokemon")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pokemon_icon));
        mMap.addMarker(marker);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mBubbleSpeechView.setVisibility(View.VISIBLE);
                // == animate text ==
                mTypeTextView
                        .setVisible(true)
                        .setTextAttr("POKEMON: AAARGH!\n...\n...")
                        .animateTypeText();
                isReadyToFight = true;
                // == return true for the event to consume the default behavior; false to make default behavior as well ==
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // == after request gps ==
        if (requestCode == 1000 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(PROVIDER_FOR_GPS, 0, 0, mLocationListener);
                Location location = mLocationManager.getLastKnownLocation(PROVIDER_FOR_GPS);
                updateYourLocation(location);
            }
        }
    }

    // == update players location ==
    private void updateYourLocation(Location location) {
        LatLng yourLocation = new LatLng(location.getLatitude(), location.getLongitude());
        float zoom = 18.5f;
        // == we use .moveCamera it so we can quickly load the map around us, for the rest we use .animateCamera ==
        if (isFirstMapLoad) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocation,zoom));
            isFirstMapLoad = false;
        }
        // == changing camera position ==
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(yourLocation)             // Sets the center of the map to current location
                .zoom(zoom)                       // Sets the zoom
                .bearing(rotation)                // Sets the orientation of the camera
                .tilt(20)                          // Sets the tilt of the camera to 0 degrees
                .build();                         // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 900, new GoogleMap.CancelableCallback() {
            // == we need to implement this interface because we want to use the overloaded version of .animateCamera ==
            // == if we use the regular one, the app will wait for the animateCamera to finish before it loads the background ==
            // == in many cases it doesn't have time to do it, so we need to set up animation time (second argument) ==
            // == there is also a third argument, so that's why we need to implement it; it can be empty ==
            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }
        });
        // == removed Marker adding because the player is displayed on the view now ==


    }
}
