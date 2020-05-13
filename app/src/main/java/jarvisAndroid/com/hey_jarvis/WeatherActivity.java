package jarvisAndroid.com.hey_jarvis;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kwabenaberko.openweathermaplib.constants.Lang;
import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.ThreeHourForecastCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView content;


    GoogleApiClient apiClient;
    FusedLocationProviderClient providerClient;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);


        content=findViewById(R.id.content);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        apiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        providerClient=LocationServices.getFusedLocationProviderClient(this);

    }

    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    private void setLocationInfo(Location location){
        if(location != null){

            OpenWeatherMapHelper helper = new OpenWeatherMapHelper("4716c24d34633a1fb730d59f9519d65e"); //junseong apikey

            helper.setUnits(Units.IMPERIAL);
            helper.setLang(Lang.KOREAN);
            Log.v("latitude",String.valueOf(location.getLatitude()));
            helper.getThreeHourForecastByGeoCoordinates(Double.parseDouble(String.valueOf(location.getLatitude())), Double.parseDouble(String.valueOf(location.getLongitude())), new ThreeHourForecastCallback() {
                @Override
                public void onSuccess(ThreeHourForecast threeHourForecast) {
                    content.setText("City/Country: " + threeHourForecast.getCity().getName() + "/" + threeHourForecast.getCity().getCountry() + "\n"
                            + "Forecast Array Count: " + threeHourForecast.getCnt() + "\n"
                            //For this example, we are logging details of only the first forecast object in the forecasts array
                            + "First Forecast Date Timestamp: " + threeHourForecast.getList().get(0).getDt() + "\n"
                            + "First Forecast Weather Description: " + threeHourForecast.getList().get(0).getWeatherArray().get(0).getDescription() + "\n"
                            + "First Forecast Max Temperature: " + threeHourForecast.getList().get(0).getMain().getTempMax() + "\n"
                            + "First Forecast Wind Speed: " + threeHourForecast.getList().get(0).getWind().getSpeed() + "\n"
                    );
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.v(TAG, throwable.getMessage());
                }
            });
        }else {
            showToast("location null....");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        apiClient.connect();
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            providerClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    setLocationInfo(location);
                }
            });
            apiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        showToast("onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showToast("onConnectionFailed");
    }
}