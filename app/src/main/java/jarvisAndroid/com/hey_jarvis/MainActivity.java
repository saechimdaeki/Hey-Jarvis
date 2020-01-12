package jarvisAndroid.com.hey_jarvis;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.actions.ReserveIntents;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private ImageButton imgbutton;
    private TextView textView;
    boolean check=false;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 5);
            toast("음성 인식을 허용해 주어야 HEY-JARVIS를 이용할 수 있습니다.");
        }
        textView=(TextView)findViewById(R.id.textout);
        textView.setText("자비스 입니다 아래의 버튼을 누른뒤 명령을 해주세요");
        imgbutton=(ImageButton)findViewById(R.id.btnSpeak);
        imgbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(check==false)
                {
                    textView.setText("");
                    check=true;
                }
                Intent intent =new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA);
                try{
                    startActivityForResult(intent,200);
                }catch(ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"intent pool",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==200){
            if(resultCode==RESULT_OK && data!=null){
                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                textView.append("[나] "+result.get(0)+"\n");
                replyAnswer(result.get(0), textView);

            }
        }
    }

    private void replyAnswer(String input, TextView txt){
        try{
            if(input.equals("안녕")){
                txt.append("[자비스] 안녕하세요 주인님.\n");
                tts.speak("안녕하세요 주인님", TextToSpeech.QUEUE_FLUSH, null);
            }
            else if(input.equals("너는 누구니")){
                txt.append("[자비스]저는 자비스로 주인님의 집사입니다.\n");
                tts.speak("저는 자비스로 주인님의 집사입니다.", TextToSpeech.QUEUE_FLUSH, null);
            }
            else if(input.equals("택시 불러 줘")){
                txt.append("[자비스] 택시 불러드리겠습니다 주인님.\n");
                tts.speak("택시를 찾고있어요 .", TextToSpeech.QUEUE_FLUSH, null);
                callCar();
            }
            else if(input.equals("종료")){
                finish();
            }
            else {
                txt.append("[자비스] 아직 그런기능이 만들어져있지 않습니다\n");
                tts.speak("아직 그런기능이 만들어져있지 않습니다", TextToSpeech.QUEUE_FLUSH, null);
            }
        } catch (Exception e) {
            toast(e.toString());
        }
    }
    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    public void callCar() {
        Intent intent = new Intent(ReserveIntents.ACTION_RESERVE_TAXI_RESERVATION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            tts.speak("이용가능한 택시가 없거나 이 기능을 이용할수없어요 ",TextToSpeech.QUEUE_FLUSH,null);
            textView.append("[자비스] 현재 서비스는 이용 불가능 합니다..\n");
        }
    }
}