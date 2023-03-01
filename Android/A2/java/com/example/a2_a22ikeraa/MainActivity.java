package com.example.a2_a22ikeraa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private final int RECORD_AUDIO = 103;
    private final int CAMERA = 104;
    private String PATH_AUDIOS;
    private String PATH_FOTO;

    Spinner spMusic;
    Button bttnPlay;
    Button bttnRecord;
    Button bttnPic;
    ImageView ivShowPicture;

    MediaPlayer player;
    MediaRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PATH_AUDIOS = getExternalFilesDir(null).getAbsolutePath() + File.separator + "UD-A2A"+ File.separator +"MUSICA"+ File.separator+"a22ikeraa" + File.separator;
        PATH_FOTO = getExternalFilesDir(null).getAbsolutePath() + File.separator + "UD-A2A"+ File.separator +"FOTO"+ File.separator+"a22ikeraa" + File.separator;
        setUp();

        bttnPlay.setOnClickListener(view ->  playAudio());

        bttnRecord.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) recordAudio();
               else requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO} , RECORD_AUDIO);
            } else recordAudio();
        });

        bttnPic.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) takePick();
                else requestPermissions(new String[]{Manifest.permission.CAMERA} , CAMERA);
            } else takePick();
        });
    }

    private void playAudio() {
        String audioName = spMusic.getSelectedItem().toString();
        File audio = new File(PATH_AUDIOS + audioName);

        if (player.isPlaying() && player != null) player.stop();

        player.reset();
        try {
            player.setDataSource(audio.getAbsolutePath());
            player.prepare();
            player.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String msgToast = requestCode == RECORD_AUDIO ? getString(R.string.toastDeniedRecord) : getString(R.string.toastDeniedCamera);

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            switch (requestCode){
                case RECORD_AUDIO:
                    recordAudio();
                    break;
                case CAMERA:
                    takePick();
                    break;
            }
        }
        else Toast.makeText(this, msgToast, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File fotoFile = new File(PATH_FOTO + "foto.png");
        if (resultCode == RESULT_OK) {
            if(data == null) return;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivShowPicture.setImageBitmap(bitmap);

            try {
                FileOutputStream out = new FileOutputStream(fotoFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100 , out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }

    //Preparar el escenario para que funcione
    private void setUp() {
        this.spMusic = findViewById(R.id.spMusic);
        this.bttnPlay = findViewById(R.id.bttnPlay);
        this.bttnRecord = findViewById(R.id.bttnRecord);
        this.bttnPic = findViewById(R.id.bttnPick);
        this.ivShowPicture = findViewById(R.id.ivViewPicture);
        this.player = new MediaPlayer();
        this.recorder = new MediaRecorder();

        cpSounds();
        readSounds();

    }


    //Le pasa al spiner los nombres de las audios que están en la SD
    private void readSounds() {
        File audiosDir = new File(PATH_AUDIOS);
        String[] audioNames = audiosDir.list();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item , audioNames);
        spMusic.setAdapter(adapter);
    }

    //Metodo que copia los sonidos desde RAW a la SD
    private void cpSounds() {
        File audiosFiles = new File(PATH_AUDIOS);
        if (!audiosFiles.exists()) audiosFiles.mkdirs();

        File audio1 = new File(audiosFiles.getAbsolutePath() + "/audio1.mp3");
        File audio2 = new File(audiosFiles.getAbsolutePath() + "/audio2.mp3");
        try {
            InputStream in;
            OutputStream out;
            byte[] buff = new byte[1024];
            int read = 0;

            if (!audio1.exists()) {
                in = getResources().openRawResource(R.raw.audio1);
                out = new FileOutputStream(audio1);

                while((read = in.read(buff)) > 0){
                    out.write(buff , 0 ,read);
                }

                in.close();
                out.close();
            }
            if (!audio2.exists()) {
                in = getResources().openRawResource(R.raw.audio2);
                out = new FileOutputStream(audio2);

                while((read = in.read(buff)) > 0){
                    out.write(buff , 0 ,read);
                }

                in.close();
                out.close();
            }
        }catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    //Graba un audio del dispositivo y lo almacena en la SD
    private void recordAudio() {

        if (player.isPlaying() && player != null) player.stop();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setMaxDuration(10000);
        recorder.setAudioEncodingBitRate(32768);
        recorder.setAudioSamplingRate(8000);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            dialogRecord().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Crea el dialogo para grabar los audios
    private Dialog dialogRecord() throws IOException {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        File outputFile = new File(PATH_AUDIOS + "audio.3gp");
        recorder.setOutputFile(outputFile.getPath());
        recorder.prepare();
        recorder.start();
        builder.setTitle(R.string.dialogRecordTitle)
                .setIcon(android.R.drawable.ic_btn_speak_now)
                .setPositiveButton(R.string.stopRecord, (dialogInterface, i) -> {
                    recorder.stop();
                    readSounds();
                });

        return builder.create();
    }

    //Metodo que saca una foto
    private void takePick() {
        if (player.isPlaying() && player != null) player.stop();
        File fotoFile = new File(PATH_FOTO);
        if(!fotoFile.exists()) fotoFile.mkdirs();
        Intent intento = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intento, 1);

    }


}