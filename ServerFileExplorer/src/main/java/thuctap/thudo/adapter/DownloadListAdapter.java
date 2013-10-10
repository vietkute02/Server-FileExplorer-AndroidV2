package thuctap.thudo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;

import thuctap.thudo.models.FilesModels;
import thuctap.thudo.serverfileexplorer.R;

/**
 * Created by vietanha34 on 9/9/13.
 */
public class DownloadListAdapter  extends BaseAdapter {

    private Context mcontext ;
    private List<FilesModels> downloadList;
    LayoutInflater mInflater ;

    public static class ViewHolder{
        public TextView resName;
        public TextView resSize;
        public ProgressBar resBar;

    }


    public DownloadListAdapter(Context mcontext , List<FilesModels> downloadList){
        super();
        this.mcontext = mcontext;
        this.downloadList = downloadList;
        mInflater = (LayoutInflater)this.mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        Log.i("getcount" , String.valueOf(downloadList.size()));
        if(downloadList == null)
        {
            return 0;
        }
        else
        {
            return downloadList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return downloadList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View currentView, ViewGroup viewGroup) {
        ViewHolder holder =  null;

        if (currentView == null){

            currentView = mInflater.inflate(R.layout.download_item, viewGroup, false);
            holder =  new ViewHolder();
            holder.resBar = (ProgressBar) currentView.findViewById(R.id.download_progressBar);
            holder.resName  = (TextView) currentView.findViewById(R.id.download_filename);
            holder.resSize = (TextView) currentView.findViewById(R.id.download_filesize);
            currentView.setTag(holder);
        }else{
            holder = (ViewHolder)currentView.getTag();
        }

        final FilesModels fileDownload = downloadList.get(i);
        holder.resName.setText(fileDownload.getName());
        holder.resSize.setText("Size : " + String.valueOf(fileDownload.getSize()/1024)  + " KB");
        return currentView;
    }
}
