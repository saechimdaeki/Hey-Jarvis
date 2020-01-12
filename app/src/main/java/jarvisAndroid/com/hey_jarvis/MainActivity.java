package jarvisAndroid.com.hey_jarvis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
                inputVoice(textView);
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
            }
        });
    }
    public void inputVoice(final TextView txt) {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            final SpeechRecognizer stt = SpeechRecognizer.createSpeechRecognizer(this);
            stt.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    toast("음성 입력 시작...");
                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                }

                @Override
                public void onEndOfSpeech() {
                    toast("음성 입력 종료");
                }

                @Override
                public void onError(int error) {
                    toast("오류 발생 : " + error);
                    stt.destroy();
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> result = (ArrayList<String>) results.get(SpeechRecognizer.RESULTS_RECOGNITION);
                    txt.append("[나] "+result.get(0)+"\n");
                    replyAnswer(result.get(0), txt);
                    stt.destroy();
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
            stt.startListening(intent);
        } catch (Exception e) {
            toast(e.toString());
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