package com.example.ushanfernando.sharetaxi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ushanfernando.sharetaxi.Model.Login;
import com.example.ushanfernando.sharetaxi.Model.STApiRequest;
import com.example.ushanfernando.sharetaxi.View.BlankFragment;
import com.example.ushanfernando.sharetaxi.View.BlankFragment1;
import com.example.ushanfernando.sharetaxi.View.BlankFragment2;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements STApiRequest.OnRequestComplete
        , Login.OnLoginRequestComplete{

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Log.d(TAG, "onCreate: starts!");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        final SessionManager sessionManager = new SessionManager(this);


        Log.i(TAG, "onCreate: " + sessionManager.getPassword() + sessionManager.getEmail());
        if (sessionManager.getEmail() == null || sessionManager.getPassword() == null) {
            Log.d(TAG, "onCreate: Session not exists");
            launchSignupActivity();
        } else {
            Log.d(TAG, "onCreate: session Exists");
            Login login = new Login(this, MainActivity.this);
            login.login(sessionManager.getEmail(), sessionManager.getPassword());

        }



        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_logout:
                                sessionManager.logout();
                                launchSignupActivity();
                                break;

                        }

                        // close drawer when item is tapped
//                        Toast.makeText(MainActivity.this,"Item clicked : "+menuItem.toString(),Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchSignupActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccessRequest(JSONObject result, String instanceName) {

    }

    @Override
    public void onLoginRequestComplete(int status, String msg) {
        Log.d(TAG, "onLoginRequestComplete on Main activity Starts : " + status + " : " + msg);

        switch (status) {
            case 0:
                Log.d(TAG, "onSuccessRequest: Login Success  !");
                break;
            case 1:
                Log.d(TAG, "onSuccessRequest: empty input");
                break;
            case 2:
                Log.d(TAG, "onSuccessRequest: Invalid pass");
                launchSignupActivity();
                break;
            case 3:

                Intent intent1 = new Intent(this, CompleteRegistration.class);
                startActivity(intent1);
                break;
            case 4:
                Log.e(TAG, "onLoginRequestComplete: Cant Login Api error!");

                break;
        }
    }



    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[]{"Find a Vehicle", "Find a Passenger", "Share a Taxi"};
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new BlankFragment();
                case 1:
                    return new BlankFragment1();
                case 2:
                    return new BlankFragment2();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout.cstom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;
        }


    }
}
