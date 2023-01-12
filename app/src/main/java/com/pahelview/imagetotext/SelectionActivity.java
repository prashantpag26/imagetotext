package com.pahelview.imagetotext;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView cvITT,cvVTT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        cvITT=(CardView)findViewById(R.id.cvITT);
        cvVTT=(CardView)findViewById(R.id.cvVTT);

        cvITT.setOnClickListener(this);
        cvVTT.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.cvVTT:
                Intent ii=new Intent(SelectionActivity.this, VoiceToTextActivity.class);
                startActivity(ii);
                break;



            case R.id.cvITT:
                Intent iz=new Intent(SelectionActivity.this, ImageToTextActivity.class);
                startActivity(iz);
                break;

        }

    }
}

