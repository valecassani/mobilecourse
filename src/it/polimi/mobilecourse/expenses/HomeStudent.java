package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by valeriocassani on 18/03/15.
 */
public class HomeStudent extends AppCompatActivity {

    private static String TAG = "Home Student";

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
    private CircularImageView circImgView;
    private SessionManager sessionManager;

    private ArrayList<NavDrawerItem> mDrawerItems;
    private NavDrawerListAdapter mNavDrawerAdapter;
    private String userId;
    private boolean doubleBackToExitPressedOnce;
    private int itemSelected;
    public static Activity activity;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.student_home);
        Bundle data = getIntent().getExtras();

        if (data.getString("mail") != null) {
            Log.i(TAG, "username: " + data.getString("mail"));
            username = data.getString("mail");
        }

        if (data.getString("user_id") != null) {

            Log.i(TAG, "userid: " + data.getString("user_id"));

            userId = data.getString("user_id");
        } else {
            getUserIdFromMail();
            Log.i(TAG, "userid: " + userId);

        }

        if (data.getInt("position") != 0)
            positionRequired = data.getInt("position");
        mTitle = mDrawerTitle = getTitle();
        mDrawerOptions = getResources().getStringArray(R.array.student_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.student_drawer_list);
        mDrawerFragment = (RelativeLayout) findViewById(R.id.left_drawer_student);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.createLoginSession(userId, "0");


        loadUserInfos();
        registerGCM();


        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Home");
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(25);




        // set a custom shadow that overlays the main content when the drawer opens

        mDrawerItems = new ArrayList<NavDrawerItem>();


        //aggiunta icone al drawer


        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[0], R.drawable.home_icon));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[1], R.drawable.abc_ic_search_api_mtrl_alpha));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[2], R.drawable.user_icon));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[3], R.drawable.richieste_icon));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[4], R.drawable.prenot_icon));

        mDrawerItems.add(new NavDrawerItem(mDrawerOptions[5], R.drawable.icon_logout));


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
                getSupportActionBar().setTitle(mDrawerTitle);
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

    private void loadUserInfos() {
        circImgView = (CircularImageView) findViewById(R.id.drawer_image);
        if (Profile.getCurrentProfile() != null) {
            Uri pictureUri = Profile.getCurrentProfile().getProfilePictureUri(200, 200);

            Picasso.with(getApplicationContext()).load(pictureUri).into(circImgView);
        } else {
            checkImageOnDatabase();

        }

        circImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Profile.getCurrentProfile() == null) {
                    Intent intent = new Intent(getApplicationContext(), UpdateImage.class);//rimettere updateImage
                    Bundle bundle = new Bundle();
                    bundle.putString("tipo", "0");
                    bundle.putString("id", userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }


            }
        });


        String url = null;
        if (username != null) {
            Log.i(TAG, "url for username");
            url = "http://www.unishare.it/tutored/student_by_id.php?mail=" + username;
        } else {

            if (userId != null) {
                Log.d(TAG, "Used id query");
                url = "http://www.unishare.it/tutored/student_by_id.php?id=" + userId;
            }
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public boolean onResponse(JSONArray response) {
                        try {
                            TextView nome = (TextView) findViewById(R.id.drawer_nome);
                            JSONObject obj = response.getJSONObject(0);
                            Log.d(TAG, response.toString());
                            nome.setText(obj.get("nome").toString() + " " + obj.get("cognome").toString());
                            nome.setTextColor(Color.WHITE);
                            TextView mail = (TextView) findViewById(R.id.drawer_mail);
                            SpannableString content = new SpannableString(obj.get("username").toString());
                            content.setSpan(new UnderlineSpan(), 0, obj.get("username").toString().length(), 0);
                            mail.setText(content);
                            mail.setTextColor(Color.WHITE);
                            mail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), UpdatePassword.class);//rimettere updateImage
                                    Bundle bundle = new Bundle();
                                    bundle.putString("tipo", "0");
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

    private void registerGCM () {

        Intent intent = new Intent(getApplicationContext(), MyInstanceIDListenerService.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("tipo", "0");
        startService(intent);

    }

    private void checkImageOnDatabase() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://www.unishare.it/tutored/getImage.php?type_user=0&id=" + userId;
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

    private void getUserIdFromMail() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://www.unishare.it/tutored/student_by_id.php?mail=" + username;
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


    public void selectItem(int position) {
        // update the main content by replacing fragments
        final Fragment[] fragment = {null};

        switch (position) {
            case 0:
                fragment[0] = new HomeStudentFragment();
                break;
            case 1:

                fragment[0] = new SearchFragment();
                getSupportActionBar().setTitle("Ricerca per materie");







                break;
            case 2:
                Bundle bundle = new Bundle();
                bundle.putString("tipo","0");
                bundle.putString("id",userId);
                Intent intent = new Intent(this,UpdateInfo.class);
                intent.putExtras(bundle);
                startActivity(intent);
                fragment[0] = null;
                break;
            case 3:
                fragment[0] = new RichiesteFragment();
                bundle = new Bundle();
                bundle.putString("student_id", userId);
                fragment[0].setArguments(bundle);
                break;
            case 4:
                bundle = new Bundle();
                bundle.putString("student_id", userId);

                fragment[0] = new PrenotazioniFragment();
                fragment[0].setArguments(bundle);
                break;
            case 5:
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setMessage("Vuoi effetturare il logout?").setTitle("Attenzione");

                builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LoginManager.getInstance().logOut();
                        sessionManager.logoutUser();
                        HomeStudent.this.finish();


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();


                dialog.show();

                break;


        }

        if (fragment[0] != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack("back").replace(R.id.student_fragment, fragment[0]).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerFragment);

        }


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


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);

        }

    }


    ///////
    public String getUserId() {
        return userId;
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (getFragmentManager().findFragmentById(R.id.student_fragment) instanceof HomeStudentFragment) {


            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setMessage("Vuoi effetturare il logout?").setTitle("Attenzione");

            builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    LoginManager.getInstance().logOut();
                    sessionManager.logoutUser();
                    HomeStudent.this.finish();


                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();


            dialog.show();

            //super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

    }

    public void onResume() {
        super.onResume();
        loadUserInfos();
    }






}
