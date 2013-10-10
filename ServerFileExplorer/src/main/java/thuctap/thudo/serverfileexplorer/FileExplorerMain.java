package thuctap.thudo.serverfileexplorer;


import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TabPageIndicator;

import thuctap.thudo.fragment.DownloadedFragment;
import thuctap.thudo.fragment.DownloadingFragment;
import thuctap.thudo.fragment.FileListFragment;
import thuctap.thudo.fragment.TestFragment;


public class FileExplorerMain extends SherlockFragmentActivity {
    private static final String[] CONTENT = new String[] { "File Explorer", "Downloading", "Downloaded"};
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    private CharSequence[] locations;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initLocations();

        FragmentPagerAdapter adapter = new ServerFileExplorerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.paper);
        pager.setAdapter(adapter);
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        prepareActionBar();


    }

    private void initLocations(){
        locations = getResources().getStringArray(R.array.goto_locations);
    }

    private void prepareActionBar(){
        ActionBar actionBar =  getSupportActionBar();
       actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        mSpinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item,locations);
       actionBar.setListNavigationCallbacks(mSpinnerAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                return true;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.file_explorer_main , menu);
        return  true;
    }

    public class ServerFileExplorerAdapter extends FragmentPagerAdapter{



        public ServerFileExplorerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Log.i("tao moi 1 the hien" , String.valueOf(position));
            switch (position){
                case 0:
                    FileListFragment fileListFragment = FileListFragment.newInstance();
                    return fileListFragment;
                case 1:
                    DownloadingFragment downloadingFragment = new DownloadingFragment();
                    return  downloadingFragment;
                case 2:
                    DownloadedFragment downloadedFragment =  new DownloadedFragment();
                    return downloadedFragment;
                case 3:
                    DownloadedFragment downloaded2Fragment =  new DownloadedFragment();
                    return downloaded2Fragment;
                default:
                    break;
            }
            return  null;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            Log.i("delete" ,  String.valueOf(position));
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

    }



    
}
