package com.hswie.educaremobile.carer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.dialog.AddMessageDialog;
import com.hswie.educaremobile.dialog.AddTaskDialog;

import java.util.List;

public class CarerPanel extends AppCompatActivity{

    public static final String TAG = "CarerPanel";

    private Context context;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private boolean isLoading = false;
    private FloatingActionButton fab;
    private AddMessageDialog addMessageDialog;



    public static int currentPage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_panel);
        context = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                currentPage = position;

                if (position == 0 || position == 2)
                    fab.setVisibility(View.VISIBLE);
                else fab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (currentPage) {

                    case 0:  Intent myIntent = new Intent(CarerPanel.this, AddResidentActivity.class);
                             CarerPanel.this.startActivity(myIntent);
                             break;

                    case 2:  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                             addMessageDialog = AddMessageDialog.newInstance();
                             addMessageDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.EduCareAppDialog);
                             addMessageDialog.callback = new AddMessageDialog.DismissCallback() {
                                 @Override
                                 public void dismissAddMessageDialog() {

                                     Log.d(TAG, "DISMISS");
                                 }
                             };
                             addMessageDialog.show(fragmentTransaction, "AddCarerMessageDialog");
                             break;
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nurse_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            for (Fragment fragment:fragmentList) {

                Log.d(TAG, "Fragment: " + fragment.getTag());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ResidentListFragment.newInstance(0, "Page # 1");
                case 1:
                    return CarerListFragment.newInstance(1,"Page # 2");
                case 2:
                    return MessagesFragment.newInstance(2, "Page # 2");
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.residents_list_tab);
                case 1:
                    return getString(R.string.carers_list_tab);
                case 2:
                    return getString(R.string.messages_tab);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_persons_list, container, false);

            return rootView;
        }
    }


}
