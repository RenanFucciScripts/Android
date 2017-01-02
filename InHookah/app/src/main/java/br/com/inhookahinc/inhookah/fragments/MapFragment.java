package br.com.inhookahinc.inhookah.fragments;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Renan Fucci on 08/11/2015.
 */
public class MapFragment extends SupportMapFragment
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;


    public MapFragment () {
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            getMapAsync(this);
        }
    }

    private void setUpMap() {

        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                //remove previously placed Marker
                if (marker != null) {
                    marker.remove();
                }

                //place marker where user just clicked
                marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap map) {
        LatLng guaru = new LatLng(-23.481701, -46.545417);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(guaru, 16));

        map.addMarker(new MarkerOptions()
                .title("Guarulhos")
                .snippet("13ª Cidade mais populosa do Brasil.")
                .position(guaru));
    }
}