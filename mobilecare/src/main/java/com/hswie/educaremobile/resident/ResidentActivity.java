package com.hswie.educaremobile.resident;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hswie.educaremobile.BuildConfig;
import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.dialog.AddFamilyDialog;
import com.hswie.educaremobile.dialog.AddMedicineDialog;
import com.hswie.educaremobile.dialog.AddTaskDialog;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.util.List;

public class ResidentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "ResidentActivity";
    private Resident resident;
    private ImageView headerAvatar;
    public static FloatingActionButton fab;
    private AddTaskDialog addTaskDialog;
    private AddMedicineDialog addMedicineDialog;
    private AddFamilyDialog addFamilyDialog;
    private Toolbar toolbar;

    public int currentPage;

    private static boolean isFamily;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFamily = BuildConfig.IS_FAMILY;
        setContentView(R.layout.activity_resident);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(addTaskDialog != null)
                    addTaskDialog.dismiss();
                if(addMedicineDialog != null)
                    addMedicineDialog.dismiss();
                if(addFamilyDialog != null)
                    addFamilyDialog.dismiss();


                headerAvatar = (ImageView) findViewById(R.id.imageViewHeaderAvatar);
                resident = ResidentsModel.get().getCurrentResident();
                Log.d(TAG, "CurrentResident: " + resident.getFirstName());
                Bitmap avatar = BitmapFactory.decodeFile(resident.getPhotoCache());
                headerAvatar.setImageBitmap(avatar);


            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, OverviewFragment.newInstance("0", "OverviewFragment")).commit();


        fab = (FloatingActionButton) findViewById(R.id.fabResidentActivity);
        if(isFamily)
            fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, TAG);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (currentPage) {

                    case 0:

                        addTaskDialog = AddTaskDialog.newInstance();
                        addTaskDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.EduCareAppDialog);
                        addTaskDialog.callback  = new AddTaskDialog.DismissCallback() {
                            @Override
                            public void dismissTaskDialog() {



                                Log.d(TAG, "DISMISS");
                                List<Fragment>  fragmentList= getSupportFragmentManager().getFragments();
                                for (Fragment f:fragmentList) {

                                    if (f instanceof OverviewFragment)
                                        ((OverviewFragment) f).refreshData();

                                }

                            }
                        };
                        addTaskDialog.show(fragmentTransaction, "addTaskDialog");
                        break;

                    case 1:

                        addMedicineDialog = AddMedicineDialog.newInstance();
                        addMedicineDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.EduCareAppDialog);
                        addMedicineDialog.callback  = new AddMedicineDialog.DismissCallback() {
                            @Override
                            public void dismissMedicineDialog() {

                                Log.d(TAG, "DISMISS");
                                Toast.makeText(getApplicationContext(), getString(R.string.adding_medicine_successful), Toast.LENGTH_LONG).show();
                                List<Fragment>  fragmentList= getSupportFragmentManager().getFragments();
                                for (Fragment f:fragmentList) {

                                    if (f instanceof PrescribedMedicines)
                                        ((PrescribedMedicines) f).refreshData();

                                }
                            }
                        };
                        addMedicineDialog.show(fragmentTransaction, "addMedicineDialog");
                        break;

                    case 2:

                        addFamilyDialog = AddFamilyDialog.newInstance();
                        addFamilyDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.EduCareAppDialog);
                        addFamilyDialog.callback  = new AddFamilyDialog.DismissCallback() {
                            @Override
                            public void dismissFamilyDialog() {

                                Log.d(TAG, "DISMISS");
                                Toast.makeText(getApplicationContext(), getString(R.string.adding_family_successful), Toast.LENGTH_LONG).show();
                                List<Fragment>  fragmentList= getSupportFragmentManager().getFragments();
                                for (Fragment f:fragmentList) {

                                    if (f instanceof FamilyListFragment)
                                        ((FamilyListFragment) f).refreshData();

                                }
                            }
                        };
                        addFamilyDialog.show(fragmentTransaction, "addFamilyDialog");
                        break;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.resident, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        Class fragmentClass = null;
        int id = item.getItemId();

        if (id == R.id.nav_overview_fragment) {
            fragmentClass  = OverviewFragment.newInstance("0", "Overview").getClass();
            toolbar.setTitle(R.string.drawer_label_overview);
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_event_white_48dp);
            currentPage = 0;
        } else if (id == R.id.nav_medicines_fragment) {
            fragmentClass = PrescribedMedicines.newInstance("1", "PrescribedMedicines").getClass();
            toolbar.setTitle(R.string.drawer_label_medicines);
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_local_hospital_white_48dp);
            currentPage = 1;
        } else if (id == R.id.nav_family_fragment) {
            fragmentClass = FamilyListFragment.newInstance("2", "FamilyList").getClass();
            toolbar.setTitle(R.string.drawer_label_family);
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_group_add_white_48dp);
            currentPage = 2;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(isFamily)
            fab.setVisibility(View.INVISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment, "currentFragment").commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
