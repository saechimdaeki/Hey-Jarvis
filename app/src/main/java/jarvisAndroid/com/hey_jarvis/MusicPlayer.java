package jarvisAndroid.com.hey_jarvis;

import android.app.Notification; //foreground
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.database.Cursor;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
public class MusicPlayer extends Service {

    MediaPlayer media;
    public static int variable=0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        variable++;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "my_channel_01";
        CharSequence name = "test";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = null;
        Intent resultIntent=new Intent(this,MainActivity.class);
        PendingIntent resultPendingIntent=PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
        }
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mNotificationManager.createNotificationChannel(mChannel);
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";
        Notification.Builder notice=new Notification.Builder(this);
        notice.setSmallIcon(R.drawable.jarvisicon); //https://makeappicon.com/
        notice.setContentTitle("jarvis뮤직");
        notice.setChannelId(CHANNEL_ID);
        notice.setContentText("뮤직플레이어 가동중");
        notice.build();
        notice.setAutoCancel(true);
        notice.setContentIntent(resultPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startForeground(1,notice.build());
            try{
                String[] musics=audio();
                media=new MediaPlayer();
                media.reset();
                int rand=(int)Math.floor(Math.random()*musics.length);
                media.setDataSource(musics[rand]);
                media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        try {
                            mediaPlayer.reset();
                            String[] music=audio();
                            int rand2=(int)Math.floor(Math.random()*music.length);
                            mediaPlayer.setDataSource(music[rand2]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }catch(Exception e){
                            tst(e.toString());
                        }
                    }
                });


                media.prepare();
                media.start();
            }catch(Exception e){
                tst(e.toString());
            }
        }// require >= JELLYBEAN
        return START_NOT_STICKY;
    }

    public String[] audio(){
        String[] ret=null;
        try{
            Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Media.DATA},null,null,null);
            if(cursor.getCount()==0){
                return null;
            }
            ret=new String[cursor.getCount()];
            cursor.moveToFirst();
            ret[cursor.getPosition()]=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            while(cursor.moveToNext()){
                ret[cursor.getPosition()]=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            }
            cursor.close();
        }catch(Exception e){
            tst(e.toString());
        }
        return ret;
    }
    private  void tst(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();

        media.stop(); // 음악 종료
        stopForeground(Service.STOP_FOREGROUND_DETACH);
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
