package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by valeriocassani on 18/03/15.
 */
public class HomeStudent2 extends AppCompatActivity {

    private static String TAG ="Home Student";

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private String[] mDrawerOptions;
    private LinearLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerFragment;
    private String username;
    private Toolbar toolbar;
    private int positionRequired;
    private AccountHeader headerResult;

    private ArrayList<NavDrawerItem> mDrawerItems;
    private NavDrawerListAdapter mNavDrawerAdapter;
    private String userId;
    private boolean doubleBackToExitPressedOnce;



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);
        Bundle data = getIntent().getExtras();

        if (data.getString("mail") != null) {
            Log.i(TAG, "username: " + data.getString("mail"));
            username = data.getString("mail");
            getUserIdFromMail();
            Log.i(TAG, "userid: " + userId);
        } else {
            if (data.getString("user_id") != null) {
                Log.i(TAG, "userid: " + data.getString("user_id"));

                userId = data.getString("user_id");
            } else {

            }
        }


        Drawer.Result result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Settings")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                    }
                })
                .build();

        if (data.getInt("position") != 0)
            positionRequired = data.getInt("position");
        mTitle = mDrawerTitle = getTitle();
        mDrawerOptions = getResources().getStringArray(R.array.student_drawer);
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.student_drawer_list);
        mDrawerFragment = (LinearLayout) findViewById(R.id.left_drawer_student);

        toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);




        // set a custom shadow that overlays the main content when the drawer opens

        mDrawerItems = new ArrayList<NavDrawerItem>();


        //aggiunta icone al drawer


        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[0],R.drawable.com_facebook_button_icon));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[1], R.drawable.com_facebook_button_like_icon_selected));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[2], R.drawable.ic_plusone_standard_off_client));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[3], R.drawable.abc_ic_search_api_mtrl_alpha));


        // setting the nav drawer list adapter
        mNavDrawerAdapter = new NavDrawerListAdapter(getApplicationContext(),
                mDrawerItems);
        mDrawerList.setAdapter(mNavDrawerAdapter);

        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());



        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        /*mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
*/
        if (savedInstanceState == null) {
            selectItem(0);
        }
        selectItem(positionRequired);



    }

    private void getUserIdFromMail() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://www.unishare.it/tutored/student_by_id.php?mail=" + username;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            Log.d(TAG, obj.toString());
                            userId = obj.getString("id");
                            Log.d(TAG, "Assignment done of " + userId);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });


// Adding request to request queue
        queue.add(jsonObjReq);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );
        if (null != searchView )
        {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            public boolean onQueryTextChange(String newText)
            {
                // this is your adapter that will be filtered

                return true;
            }

            public boolean onQueryTextSubmit(String query)
            {
                onSearchRequested();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }





    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
       /*boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerFragment);
       menu.findItem(R.id.action_logout).setVisible(!drawerOpen);*/


        return super.onPrepareOptionsMenu(menu);
    }


    //metodo per far aprire e chiudere il drawer col bottone
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                return true;
            case R.id.action_logout:
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(HomeStudent2.this,LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putString("hello", "world");
        startSearch(null, false, appData, false);
        return true;
    }




    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SearchFragment();
                getSupportActionBar().setTitle("Ricerca Tutor");
                break;
            case 1:
                fragment = new StudentDataFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mail", username);
                bundle.putString("id", userId);
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment = new RichiesteFragment();
                bundle = new Bundle();
                bundle.putString("student_id",userId);
                fragment.setArguments(bundle);
                break;
            case 3:
                bundle = new Bundle();
                bundle.putString("student_id",userId);

                fragment = new PrenotazioniFragment();
                fragment.setArguments(bundle);
                break;

        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("back").replace(R.id.student_fragment, fragment).commit();






        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerOptions[position]);
        //mDrawerLayout.closeDrawer(mDrawerFragment);
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
