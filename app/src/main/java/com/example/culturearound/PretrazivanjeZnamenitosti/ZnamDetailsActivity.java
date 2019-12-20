package com.example.culturearound.PretrazivanjeZnamenitosti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.DataLoader;
import com.example.culturearound.MainActivity;
import com.example.culturearound.R;
import com.example.database.Entities.Korisnik;
import com.example.database.Entities.Lokacija;
import com.example.database.Entities.Znamenitost;
import com.example.database.MyDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import loaders.DbDataLoader;

public class ZnamDetailsActivity extends AppCompatActivity {
    @BindView(R.id.textNaziv)
    TextView textNaziv;
    @BindView(R.id.imageZnamenitost)
    ImageView imageZnamenitost;
    @BindView(R.id.textAdresa)
    TextView textAdresa;
    @BindView(R.id.textOpisZnamenitosti)
    TextView textOpisZnamenitosti;

    public static MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_znam_details);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Intent intent = getIntent();
        int znamId = intent.getIntExtra("id_znamenitost", -1);

        if(znamId != -1){
            Znamenitost znamenitost =
                    MainActivity.database.getDAO().loadZnamenitostiById(znamId);
            Toast.makeText(this, znamenitost.getNaziv(), Toast.LENGTH_LONG);
        }
    }
}
