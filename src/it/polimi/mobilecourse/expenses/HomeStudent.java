package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;


/**
 * Created by valeriocassani on 18/03/15.
 */
public class HomeStudent extends Activity{

    private String TAG = "STUDENT";


    private CharSequence mTitle;
    private String username;
    private CharSequence mDrawerTitle;
    private String[] mDrawerOptions;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerFragment;
    private Intent intent;

    private ArrayList<NavDrawerItem> mDrawerItems;
    private NavDrawerListAdapter mNavDrawerAdapter;
    private int userId;

    public HomeStudent() {


    }

    public HomeStudent(int userId) {
        this.userId = userId;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);
        intent = getIntent();
        username = intent.getStringExtra("username");
        Log.d(TAG, "This is the username from Facebook App "  + username );

        mTitle = mDrawerTitle = getTitle();
        mDrawerOptions = getResources().getStringArray(R.array.student_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.student_drawer_list);
        mDrawerFragment = (LinearLayout) findViewById(R.id.left_drawer_student);


        // set a custom shadow that overlays the main content when the drawer opens

        mDrawerItems = new ArrayList<NavDrawerItem>();


        //aggiunta icone al drawer


        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[0],R.drawable.com_facebook_button_like_icon));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[1],R.drawable.com_facebook_button_like_icon_selected));


        // setting the nav drawer list adapter
        mNavDrawerAdapter = new NavDrawerListAdapter(getApplicationContext(),
                mDrawerItems);
        mDrawerList.setAdapter(mNavDrawerAdapter);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
       boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerFragment);
       menu.findItem(R.id.action_logout).setVisible(!drawerOpen);

       return super.onPrepareOptionsMenu(menu);
    }


    //metodo per far aprire e chiudere il drawer col bottone
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new StudentDataFragment();
                Bundle bundle = new Bundle();
                username = "valerio.cassani@gmail.com";
                bundle.putString("username", username);
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new MidFragmentHome();
                break;
            case 2:
                fragment = new BottomFragmentHome();
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("back").replace(R.id.student_fragment, fragment).commit();






        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerOptions[position]);
        mDrawerLayout.closeDrawer(mDrawerFragment);
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private  class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);

        }

    }
}
