package br.lauriavictor.wmb.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.fragment.CallFragment;
import br.lauriavictor.wmb.fragment.ListPlaceFragment;
import br.lauriavictor.wmb.fragment.RemoPlaceFragment;
import br.lauriavictor.wmb.fragment.SugestionPlacesFragment;
import br.lauriavictor.wmb.model.ViewPagerAdapter;

public class WmbListPlaceActivity extends AppCompatActivity {

    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmb_list_place);


        tableLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.AddFragment(new ListPlaceFragment(), "Seus lugares");
        viewPagerAdapter.AddFragment(new RemoPlaceFragment(), "Remover lugares");
        viewPagerAdapter.AddFragment(new SugestionPlacesFragment(), "Sugestões para você");
        viewPagerAdapter.AddFragment(new CallFragment(), "Atualize seus dados");


        viewPager.setAdapter(viewPagerAdapter);
        tableLayout.setupWithViewPager(viewPager);
    }
}
