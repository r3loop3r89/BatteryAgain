package com.shra1.batteryagain.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shra1.batteryagain.MainActivity;
import com.shra1.batteryagain.R;
import com.shra1.batteryagain.customviews.ShraTextView;
import com.shra1.batteryagain.utils.DirectionsJSONParser;
import com.shra1.batteryagain.utils.LongLog;
import com.shra1.batteryagain.utils.MyProgressDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment {

    public static final String TAG = "ShraX";
    private static MapFragment INSTANCE = null;
    SupportMapFragment map;
    GoogleMap mGoogleMap;
    ShraTextView stvDetails;
    Context mCtx;
    Location myCurrentLocation;

    boolean locationReady=false, mapReady=false;

    public static MapFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MapFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCtx = container.getContext();
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        stvDetails = (ShraTextView) v.findViewById(R.id.stvDetails);

        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;

                mapReady=true;
                goToMyLocationOnMap();

                displayMyLocationDetail();

                configureTheGoogleMap();

            }
        });


        return v;
    }

    @SuppressLint("MissingPermission")
    private void displayMyLocationDetail() {
        LocationManager lm = (LocationManager) mCtx.getSystemService(Context.LOCATION_SERVICE);
        lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationReady=true;
                myCurrentLocation = location;
                String detail = "My current location is (" + location.getLatitude() + ", " + location.getLongitude() + ")";
                stvDetails.setText(detail);

                goToMyLocationOnMap();

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
        }, Looper.getMainLooper());
    }

    private void goToMyLocationOnMap() {
        if (locationReady && mapReady){
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude()), 15));
        }
    }

    @SuppressLint("MissingPermission")
    private void configureTheGoogleMap() {
        mGoogleMap.setMyLocationEnabled(true);

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Add marker here
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                );

                LatLng origin = new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude());
                LatLng dest = latLng;

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);

                /*DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);*/

                MyProgressDialog.show(mCtx, "Please wait, getting direction data", false);
                Ion.with(mCtx)
                        .load(url)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                MyProgressDialog.dismiss();
                                if (e != null) {
                                    e.printStackTrace();
                                    return;
                                }

                                LongLog.d(TAG, result);
                                if (result.toLowerCase().contains("error")){
                                    if (MainActivity.getInstance()!=null){
                                        MainActivity.getInstance().showToast("Quota exeeded!");
                                    }
                                    return;
                                }

                                ParserTask parserTask = new ParserTask();
                                parserTask.execute(result);
                            }
                        });
            }
        });
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "AIzaSyAjLWtECbFLfPX1alKlEBSQsOMcRT2cN4g";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;// + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d(TAG, "DaTa: " + data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mGoogleMap.addPolyline(lineOptions);
            }
        }
    }

}
