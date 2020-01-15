package jarvisAndroid.com.hey_jarvis;



import android.app.Activity;
import android.widget.Toast;

public class BackButtonPressHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    public BackButtonPressHandler(Activity context) {
        this.activity = context;
    }
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            activity.finish(); toast.cancel();
        }
    }
    public void showGuide() {
        toast = Toast.makeText(activity, "Press the Backward button once more to exit the app.", Toast.LENGTH_SHORT);
        toast.show();
    }

}