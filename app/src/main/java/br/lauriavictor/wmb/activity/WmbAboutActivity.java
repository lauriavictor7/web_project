package br.lauriavictor.wmb.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.lauriavictor.wmb.R;
import mehdi.sakout.aboutpage.AboutPage;

public class WmbAboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_wmb_about);

        String description = "O Where's My Beer, foi feito para você que é cervejeiro ficar por dentro de tudo que rola no universo dessa preciosa bebida que todos nós amamos, entre para a comunidade, receba e  contribua com opiniões sobre lugares, cervejas, eventos e muito mais! \n\n" +
                "Nosso trabalho é facilitar o seu trabalho, aproveite todos os nossos recursos e partiu tomar uma!";
        View about = new AboutPage(this)
                .setImage(R.drawable.wmb_logo)
                .setDescription(description)
                .addGroup("Fale conosco")
                .addEmail("wmb@wheresmybeer.com", "Enviar e-mail")
                .addWebsite("http://www.google.com/", "Acesse nosso site")
                .addGroup("Redes sociais")
                .addFacebook("google", "Curte a gente no Facebook")
                .addInstagram("google", "Nos siga no Instagram")
                .addTwitter("google", "Nos siga no Twitter")
                .addGitHub("google", "Conheça nossa comunidade no GitHub")
                .addPlayStore("com.google.android.apps.plus", "Google Play Store")
                .create();

        setContentView(about);
    }
}
