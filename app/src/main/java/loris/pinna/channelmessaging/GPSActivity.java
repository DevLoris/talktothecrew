package loris.pinna.channelmessaging;

import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.net.ConnectException;

/**
 * Created by pinnal on 05/02/2018.
 */
public class GPSActivity extends ActionBarActivity  {
    public GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    public Location getLocation()  {
        if(mGoogleApiClient.isConnected()) {
            try {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                return lastLocation;
            } catch (SecurityException e) {
                return null;
            }
        }
        else  {
            try{
                throw new Exception("Pas connecté");
            }
            catch (Exception e) {
            }
            return null;
        }
    }

    public void updateLocation() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        //Correspond à l’intervalle moyen de temps entre chaque mise à jour des
        mLocationRequest.setFastestInterval(5000);
        //Correspond à l’intervalle le plus rapide entre chaque mise à jour des
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

       // LocationServices.FusedLocationApi.requestLocationUpdates(   this.mGoogleApiClient, mLocationRequest, this);

    }

}
