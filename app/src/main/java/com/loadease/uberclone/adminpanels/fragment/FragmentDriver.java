package com.loadease.uberclone.adminpanels.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentActivity;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kusu.library.LoadingButton;
import com.loadease.uberclone.adminpanels.Common.Common;
import com.loadease.uberclone.adminpanels.Interfaces.locationListener;
import com.loadease.uberclone.adminpanels.Messages.DriverRequestReceived;
import com.loadease.uberclone.adminpanels.Model.LocationUtils;
import com.loadease.uberclone.adminpanels.Model.NotifyToRiderEvent;
import com.loadease.uberclone.adminpanels.Model.RiderModel;
import com.loadease.uberclone.adminpanels.Model.Token;
import com.loadease.uberclone.adminpanels.Model.TripPlaneModel;
import com.loadease.uberclone.adminpanels.Model.User;
import com.loadease.uberclone.adminpanels.R;
import com.loadease.uberclone.adminpanels.Retrofit.IGoogleApi_sep;
import com.loadease.uberclone.adminpanels.Retrofit.RetrofitClient_sep;
import com.loadease.uberclone.adminpanels.Util.Location;
import com.loadease.uberclone.adminpanels.Util.UsersUtill;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentDriver extends FragmentActivity implements OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{


    private GoogleMap mMap;
    Location location;
    private Marker currentLocationMarket;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount account;

    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    boolean isLoggedInFacebook = accessToken != null && !accessToken.isExpired();


    GeoFire pickupGeoFire,destinationGeoFire;
    GeoQuery pickupGeoQuery,destinationGeoQuery;


    GeoQueryEventListener pickupGeoQueryListner=new GeoQueryEventListener() {
        @Override
        public void onKeyEntered(String key, GeoLocation location) {

            btn_start_uber.setEnabled(true);
            UsersUtill.sendNotifyToRider(FragmentDriver.this,snackbarView,key);


            if (pickupGeoQuery!=null)
            {
                pickupGeoFire.removeLocation(key);
                pickupGeoFire=null;
                pickupGeoQuery.removeAllListeners();
            }
        }

        @Override
        public void onKeyExited(String key) {


            btn_start_uber.setEnabled(false);
        }

        @Override
        public void onKeyMoved(String key, GeoLocation location) {

        }

        @Override
        public void onGeoQueryReady() {

        }

        @Override
        public void onGeoQueryError(DatabaseError error) {

        }



    };

    GeoQueryEventListener destinationGeoQueryListner=new GeoQueryEventListener() {
        @Override
        public void onKeyEntered(String key, GeoLocation location) {

            Toast.makeText(FragmentDriver.this, "destination enter", Toast.LENGTH_SHORT).show();
            
            btn_complete_trip.setEnabled(true);

            if (destinationGeoQuery!=null)
            {
                destinationGeoFire.removeLocation(key);
                destinationGeoFire=null;
                destinationGeoQuery.removeAllListeners();
            }
        }

        @Override
        public void onKeyExited(String key) {

        }

        @Override
        public void onKeyMoved(String key, GeoLocation location) {

        }

        @Override
        public void onGeoQueryReady() {

        }

        @Override
        public void onGeoQueryError(DatabaseError error) {

        }
        
    };




    String city_name;
    private DatabaseReference drivers, onlineRef, currentUserRef;
    private GeoFire geoFire;
    ValueEventListener onlineValueEventLisner=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.exists() && currentUserRef!=null)
            {
                currentUserRef.onDisconnect().removeValue();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };





    private static final int REQUEST_CODE_PERMISSION=100;
    private static final int PLAY_SERVICES_REQUEST_CODE=2001;
    DriverRequestReceived driverRequestReceived;
    Disposable countDownEvent;
    View snackbarView;




    Chip chip_decline;
    CardView layout_accept;
    CircularProgressBar circularProgressBar;
    TextView txt_estimate_time,txt_estimate_distance;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    IGoogleApi_sep iGoogleApi_sep;
    Polyline blackPolyLine,greyPolyLine;
    List<LatLng> polyLineList;
    PolylineOptions polylineOptions,blackPolyLineOption;



    TextView txt_rating,txt_type_uber,txt_rider_name,txt_start_uber_estimate_distance
            ,txt_start_uber_estimate_time;
    ImageView img_round,img_phone_call;
    CardView layout_start_uber;
    LoadingButton btn_start_uber,btn_complete_trip;
    String tripNumberId="";
    boolean  isTripStart=false,onlineSystemAlreadyRegister=false;


    LinearLayout layout_notify_rider;
    TextView txt_notify_rider;
    ProgressBar progress_notify;






    CountDownTimer waiting_timer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_driver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        snackbarView=findViewById(R.id.snackbarView);
        iGoogleApi_sep = RetrofitClient_sep.getInstance().create(IGoogleApi_sep.class);

        txt_rating=findViewById(R.id.txt_rating);
        txt_type_uber=findViewById(R.id.txt_type_uber);
        txt_rider_name=findViewById(R.id.txt_rider_name);
        txt_start_uber_estimate_distance=findViewById(R.id.txt_start_uber_estimate_distance);
        txt_start_uber_estimate_time=findViewById(R.id.txt_start_uber_estimate_time);
        img_round=findViewById(R.id.img_round);
        img_phone_call=findViewById(R.id.img_phone_call);
        layout_start_uber=findViewById(R.id.layout_start_ubber);
        btn_start_uber=findViewById(R.id.btn_start_uber);
        btn_complete_trip=findViewById(R.id.btn_complete_trip);
        layout_notify_rider=findViewById(R.id.layout_notify_rider);
        txt_notify_rider=findViewById(R.id.txt_notify_rider);
        progress_notify=findViewById(R.id.progress_notify);

        chip_decline=findViewById(R.id.chip_decline);
        circularProgressBar=findViewById(R.id.circularProgressBar);
        layout_accept=findViewById(R.id.layout_accept);
        txt_estimate_time=findViewById(R.id.txt_estimate_time);
        txt_estimate_distance=findViewById(R.id.txt_estimate_distance);





        verifyGoogleAccount();


        init();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






        add_PricingValue();








     updateFirebaseToken();

    }




    public void add_PricingValue()
    {

        DatabaseReference db=FirebaseDatabase.getInstance().getReference("pricingValue");
        HashMap hashMap=new HashMap();
        hashMap.put("baseFare","1");
        hashMap.put("timeRate","2");
        hashMap.put("distanceRate","2");

        db.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });


    }

    private void init() {



        onlineRef= FirebaseDatabase.getInstance().getReference(".info/connected");





        location=new Location(this, new locationListener() {
            @Override
            public void locationResponse(LocationResult response) {
                // Add a icon_marker in Sydney and move the camera
                Common.currentLat=response.getLastLocation().getLatitude();
                Common.currentLng=response.getLastLocation().getLongitude();

                LatLng currentLocation = new LatLng(Common.currentLat, Common.currentLng);

                if (currentLocationMarket != null) currentLocationMarket.remove();

                currentLocationMarket = mMap.addMarker(new MarkerOptions().position(currentLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))
                        .title("Your Location"));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Common.currentLat,
                        Common.currentLng), 15.0f));


                if (pickupGeoFire!=null)
                {
                    pickupGeoQuery=pickupGeoFire.queryAtLocation(new GeoLocation(Common.currentLat,Common.currentLng),

                            0.05);

                    pickupGeoQuery.addGeoQueryEventListener(pickupGeoQueryListner);
                }





                if (destinationGeoFire!=null)
                {
                    destinationGeoQuery=destinationGeoFire.queryAtLocation(new GeoLocation(Common.currentLat,Common.currentLng),

                            0.05);

                    destinationGeoQuery.addGeoQueryEventListener(destinationGeoQueryListner );
                }












            }
        });







        location.getLocation();





    }

    private void makeDriverOnline() {



        String saveCityName=city_name;
        city_name= LocationUtils.getAddressFromLocation(getApplicationContext());

        if (!city_name.equals(saveCityName))
        {
            if (currentUserRef !=null)
                currentUserRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        updateDriverLocation();
                    }
                });
        }
        else
        {
            updateDriverLocation();

        }



    }

    private void updateDriverLocation() {

        if (!TextUtils.isEmpty(city_name))
        {
                drivers = FirebaseDatabase.getInstance().getReference("Drivers")
                    .child(Common.currentUser.getCarType())
                    .child(city_name);


            currentUserRef = drivers.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            geoFire = new GeoFire(drivers);


            ///update loc

            geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    new GeoLocation(Common.currentLat
                            ,Common.currentLng),

                    (key, error) -> {


                        if (error!=null)
                        {
                            Toast.makeText(FragmentDriver.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(FragmentDriver.this, "you are online", Toast.LENGTH_SHORT).show();
                        }


                    }
            );

            registerOnlineSystem();

        }
        else
        {
            Toast.makeText(this, "service unavailable here ", Toast.LENGTH_SHORT).show();
        }


    }


    private void loadDriverInformation(){
        FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl)
                .child(Common.userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Common.currentUser = dataSnapshot.getValue(User.class);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



    private void verifyGoogleAccount()
    {
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        OptionalPendingResult<GoogleSignInResult> opr=Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()){
            GoogleSignInResult result= opr.get();
            handleSignInResult(result);
        }else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>()
            {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }



    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess())
        {
            account = result.getSignInAccount();
            Common.userID=account.getId();

            loadDriverInformation();
        }
        else if(isLoggedInFacebook)
        {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            String id=object.optString("id");
                            Common.userID=id;

                            loadDriverInformation();

                        }
                    });
            request.executeAsync();
        }

        else
            {

            Common.userID=FirebaseAuth.getInstance().getCurrentUser().getUid();

            loadDriverInformation();
        }
    }



    private void updateFirebaseToken() {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        final DatabaseReference tokens=db.getReference(Common.token_tbl);

        final Token token=new Token(FirebaseInstanceId.getInstance().getToken());
        if(FirebaseAuth.getInstance().getUid()!=null) tokens.child(FirebaseAuth.getInstance().getUid()).setValue(token);
        else if(account!=null) tokens.child(account.getId()).setValue(token);
        else{
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            String id = object.optString("id");
                            tokens.child(id).setValue(token);
                        }
                    });
            request.executeAsync();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        location.remove_location();
        geoFire.removeLocation(FirebaseAuth.getInstance().getCurrentUser().getUid());
        onlineRef.removeEventListener(onlineValueEventLisner);


        if (EventBus.getDefault().hasSubscriberForEvent(DriverRequestReceived.class))
            EventBus.getDefault().removeStickyEvent(DriverRequestReceived.class);

        if (EventBus.getDefault().hasSubscriberForEvent(NotifyToRiderEvent.class))
            EventBus.getDefault().removeStickyEvent(NotifyToRiderEvent.class);




        EventBus.getDefault().unregister(this );
        compositeDisposable.clear();
        onlineSystemAlreadyRegister=false;



    }

    @Override
    protected void onResume() {
        super.onResume();

            registerOnlineSystem();

    }

    @Override
    protected void onStart() {
        super.onStart();
        location.inicializeLocation();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    private void registerOnlineSystem() {


        if (!onlineSystemAlreadyRegister)
        {
            onlineRef.addValueEventListener(onlineValueEventLisner);

            onlineSystemAlreadyRegister=true;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);



        try {

            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle
                    (getApplicationContext(), R.raw.uber_style_map));

            if (!success)
            {
                Toast.makeText(this, "load map fail", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "load map ", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e)
        {

        }




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        location.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {

        super.onStop();
        location.stopUpdateLocation();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        location.inicializeLocation();


    }

    @Override
    public void onConnectionSuspended(int i)
    {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}