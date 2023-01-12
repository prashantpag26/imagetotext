package com.pahelview.imagetotext;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceToTextActivity extends AppCompatActivity {

    private EditText mResultEt;
    private static final int WRITE_EXTERNAL_STORAGE_CODE=1;
    String mstring;
    private Button button_save;
    private CardView cvText;
    private LinearLayout llImageToText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_to_text);
        mResultEt = (EditText) findViewById(R.id.txvResult);
        cvText=(CardView)findViewById(R.id.cvText);
        llImageToText=(LinearLayout) findViewById(R.id.llImageToText);
        findViewById(R.id.btnCopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", mResultEt.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(VoiceToTextActivity.this, "Text copied! ☻", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, mResultEt.getText().toString());
                startActivity(Intent.createChooser(intent,"Share"));
            }
        });
    }

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mResultEt.setText(result.get(0));
                    cvText.setVisibility(View.GONE);
                    llImageToText.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
                    saveToText(mstring);
                }
                else{
                    Toast.makeText(VoiceToTextActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveToText(String mstring) {
        String timestamp=new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(System.currentTimeMillis());
        try{

            File path= Environment.getExternalStorageDirectory();
            File dir=new File(path+"/Image To Text/");
            dir.mkdir();
            String filename="VoiceFile_ "+timestamp+".txt";
            File file=new File(dir,filename);
            FileWriter fw=new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf=new BufferedWriter(fw);
            bf.write(mstring);
            bf.close();
            Toast.makeText(VoiceToTextActivity.this, filename+" is saved to\n "+dir, Toast.LENGTH_LONG).show();

        }
        catch (Exception e){
            Toast.makeText(VoiceToTextActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }
}

