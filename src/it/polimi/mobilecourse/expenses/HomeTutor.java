package it.polimi.mobilecourse.expenses;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.mobilecourse.expenses.R.drawable.ic_action_lock_closed;


public class HomeTutor extends AppCompatActivity {

    private static String TAG ="Home Tutor";

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private String[] mDrawerOptions;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerFragment;
    private String username;
    private Toolbar toolbar;
    private int positionRequired;
    private SessionManager sessionManager;
    private CircularImageView circImgView;

    private ArrayList<NavDrawerItem> mDrawerItems;
    private NavDrawerListAdapter mNavDrawerAdapter;
    private String userId;
    private boolean doubleBackToExitPressedOnce;
    private int itemSelected;
    public static Activity activity;
    private Service instanceIdService;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activity = this;


        setContentView(R.layout.tutor_home);
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
        if (data.getInt("position") != 0)
            positionRequired = data.getInt("position");
        mTitle = mDrawerTitle = getTitle();
        mDrawerOptions = getResources().getStringArray(R.array.tutor_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.tutor_drawer_list);
        mDrawerFragment = (RelativeLayout) findViewById(R.id.left_drawer_tutor);


        loadUserInfos();
        registerGCM();
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.createLoginSession(userId,"1");



        toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);

        }

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(25);



        // set a custom shadow that overlays the main content when the drawer opens

        mDrawerItems = new ArrayList<NavDrawerItem>();


        //aggiunta icone al drawer



        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[0],R.drawable.home_icon));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[1], R.drawable.user_icon));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[2], R.drawable.icon_materie));


        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[3], R.drawable.prenot_icon));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[4], R.drawable.icon_logout));



        // setting the nav drawer list adapter
        mNavDrawerAdapter = new NavDrawerListAdapter(getApplicationContext(),
                mDrawerItems);
        mDrawerList.setAdapter(mNavDrawerAdapter);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());



        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,
                toolbar,        /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
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

        if (savedInstanceState == null) {
            selectItem(0);
        }
        selectItem(positionRequired);



    }

    private void registerGCM() {

        Intent intent = new Intent (getApplicationContext(),MyInstanceIDListenerService.class);
        intent.putExtra("user_id",userId);
        intent.putExtra("tipo","1");
        startService(intent);

    }

    private void loadUserInfos() {
        circImgView = (CircularImageView)findViewById(R.id.drawer_image);
        if (Profile.getCurrentProfile() != null) {
            Uri pictureUri = Profile.getCurrentProfile().getProfilePictureUri(200, 200);

            Picasso.with(getApplicationContext()).load(pictureUri).into(circImgView);
        } else {
            checkImageOnDatabase();

        }

        circImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UpdateInfo.class);//rimettere updateImage
                Bundle bundle = new Bundle();
                bundle.putString("tipo","1");
                bundle.putString("id",userId);
                intent.putExtras(bundle);
                startActivity(intent);



            }
        });



        String url = null;
        if (username != null) {
            Log.i(TAG,"url for username");
            url = "http://www.unishare.it/tutored/tutor_by_id.php?mail=" + username;
        }    else {

            if (userId != null) {
                Log.d(TAG,"Used id query");
                url = "http://www.unishare.it/tutored/tutor_by_id.php?id="+userId;
            }
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            TextView nome = (TextView)findViewById(R.id.drawer_nome);
                            JSONObject obj = response.getJSONObject(0);
                            Log.d(TAG, response.toString());
                            nome.setText(obj.get("nome").toString() + " " + obj.get("cognome").toString());
                            nome.setTextColor(Color.WHITE);
                            TextView mail = (TextView)findViewById(R.id.drawer_mail);
                            SpannableString content=new SpannableString(obj.get("username").toString());
                            content.setSpan(new UnderlineSpan(),0,obj.get("username").toString().length(),0);
                            mail.setText(content);
                            mail.setTextColor(Color.WHITE);
                            mail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), UpdatePassword.class);//rimettere updateImage
                                    Bundle bundle = new Bundle();
                                    bundle.putString("tipo", "1");
                                    bundle.putString("id", userId);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return false;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        queue.add(jsonObjReq);
    }

    private void getUserIdFromMail() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://www.unishare.it/tutored/tutor_by_id.php?mail=" + username;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            Log.d(TAG, obj.toString());
                            userId = obj.getString("id");
                            Log.d(TAG, "Assignment done of " + userId);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return false;
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
        switch (item.getItemId()) {
            case R.id.action_search:
                selectItem(1);
                return true;
            case R.id.action_logout:
                LoginManager.getInstance().logOut();
                sessionManager.logoutUser();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }




    public void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeTutorFragment();

                break;
            case 1:
                //profilo
                /*fragment = new StudentDataFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mail", username);
                bundle.putString("id", userId);
                fragment.setArguments(bundle);*/
                break;

            case 2:

                //materie

                fragment=new MaterieTutorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("idt",userId);

                fragment.setArguments(bundle);
                break;

            case 3:
                //prenotazioni
                //bundle = new Bundle();
                //bundle.putString("student_id",userId);

                //fragment = new PrenotazioniFragment();
                //fragment.setArguments(bundle);
                break;

            case 4:
                //logout


        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack("back").replace(R.id.tutor_fragment, fragment).commit();

        }







        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerFragment);
    }

    private void checkImageOnDatabase() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://www.unishare.it/tutored/getImage.php?type_user=1&id="+userId;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            Log.d(TAG, obj.toString());
                            String urlPhoto = obj.getString("url");
                            if (urlPhoto.equals("NO")) {
                                circImgView.setImageResource(R.drawable.dummy_profpic);

                            } else {
                                Picasso.with(getApplicationContext()).load("http://www.unishare.it/tutored/" + urlPhoto).into(circImgView);

                            }


                            Log.d(TAG, "Assignment done of " + userId);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return false;
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

    ///////
    public String getUserId(){
        return userId;
    }


    @Override
    public void onBackPressed(){

        int count=getFragmentManager().getBackStackEntryCount();

        if(getFragmentManager().findFragmentById(R.id.tutor_fragment) instanceof HomeTutorFragment){


            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setMessage("Vuoi effetturare il logout?").setTitle("Attenzione");

            builder.setPositiveButton("Sì",null);
            builder.setNegativeButton("No",null);

            AlertDialog dialog = builder.create();


            dialog.show();

            //super.onBackPressed();
        }
        else{
            getFragmentManager().popBackStack();
        }

    }

}
