package jarvisAndroid.com.hey_jarvis;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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
    boolean checkloof=false;
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
                /*
                Intent intent =new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA);
                try{
                    startActivityForResult(intent,200);
                }catch(ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"intent pool",Toast.LENGTH_SHORT).show();
                }
                다른방식 하지만 이렇할시에는 google script가 저장된다는 toast message가뜬다.
                 */
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
                    stt.destroy();
                    toast("오류 발생 : " + error);
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
            checkloof=false;
            String cmp=input.split(" ")[0];
            String[] me={"안녕","너는 누구야","심심해"};
            String[] jarvis={"안녕하세요 주인님","저는 자비스로 주인님의 집사입니다","백준 문제푸는것 어떠세요?"};
            if(input.equals("종료")){
                tts.speak("서비스를 종료합니다 .", TextToSpeech.QUEUE_FLUSH, null);
                finish();
            }
            if(input.equals("택시 불러 줘")){
                callCar();
                checkloof=true;
            }
            for(int i=0; i<me.length; i++){
                if(input.equals(me[i])){
                    textView.append("[자비스]:"+jarvis[i]+'\n');
                    tts.speak(jarvis[i],TextToSpeech.QUEUE_FLUSH,null);
                    return;
                }
            }if(checkloof==true){
                ;
            }else {
                textView.append("[자비스] 아직 그기능은 구현되지 않았습니다\n");
                tts.speak("아직 그기능은 구현되지 않았습니다", TextToSpeech.QUEUE_FLUSH, null);
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
            textView.append("[자비스] 현재 택시 서비스는 이용 불가능 합니다..\n");
        }
    }
}