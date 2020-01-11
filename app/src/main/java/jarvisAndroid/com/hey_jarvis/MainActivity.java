package jarvisAndroid.com.hey_jarvis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private ImageButton imgbutton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textout);
        imgbutton=(ImageButton)findViewById(R.id.btnSpeak);
        imgbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
        }

        @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==200){
            if(resultCode==RESULT_OK && data!=null){
                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                textView.setText(result.get(0));
            }
        }
        }
}