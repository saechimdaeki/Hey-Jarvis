package jarvisAndroid.com.hey_jarvis;



import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


import com.bumptech.glide.Glide;


public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ImageView imageView=findViewById(R.id.image_loading);
        Glide.with(getBaseContext()).load(R.raw.jarvis).into(imageView);
        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 9000); //어차피 백버튼으로 넘어갈수있으니까 ;;
    }
}