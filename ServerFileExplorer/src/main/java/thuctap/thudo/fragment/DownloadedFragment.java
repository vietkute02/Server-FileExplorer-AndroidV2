package thuctap.thudo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import thuctap.thudo.serverfileexplorer.R;

/**
 * Created by vietanha34 on 9/11/13.
 */
public class DownloadedFragment  extends SherlockFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.donwloadfragment , container , false);
    }
}
