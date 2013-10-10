package thuctap.thudo.adapter;

/**
 * Created by vietanha34 on 9/5/13.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thuctap.thudo.models.FilesModels;
import thuctap.thudo.serverfileexplorer.R;

public class FileListAdapter extends BaseAdapter {

    public static class ViewHolder
    {
        public TextView resName;
        public ImageView resIcon;
        public TextView resMeta;
    }

    private static final String TAG = FileListAdapter.class.getName();
    private Context mContext;
    private List<FilesModels> files;
    private LayoutInflater mInflater;
    private boolean setFirst =  false;

    public FileListAdapter(Context context, List<FilesModels> files) {
        super();
        mContext = context;
        this.files = files;

    }


    @Override
    public int getCount() {
        Log.i("getcount" ,  String.valueOf(files.size()));
        if(files == null)
        {
            return 0;
        }
        else
        {
            return files.size();
        }
    }

    @Override
    public Object getItem(int arg0) {
        if(files == null)
            return null;
        else
            return files.get(arg0);
    }

    public List<FilesModels> getItems()
    {
        return files;
    }

    @Override
    public long getItemId(int position) {

        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        mInflater  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.fileitem, parent, false);
            holder = new ViewHolder();
            holder.resName = (TextView)convertView.findViewById(R.id.explorer_resName);
            holder.resMeta = (TextView)convertView.findViewById(R.id.explorer_resMeta);
            holder.resIcon = (ImageView)convertView.findViewById(R.id.explorer_resIcon);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        final FilesModels currentFile = files.get(position);
        holder.resName.setText(currentFile.getName());
        Drawable iconDir =  mContext.getResources().getDrawable(R.drawable.filetype_dir);
        Drawable iconFile = mContext.getResources().getDrawable(R.drawable.filetype_generic);
        if (this.files.get(position).getIsDir() == true){
            Log.i(String.valueOf(position) + " . " + this.files.get(position).getName() , String.valueOf(this.files.get(position).getIsDir()));
            holder.resIcon.setImageDrawable(iconDir);
        }else {
            holder.resIcon.setImageDrawable(iconFile);
        }

        // String meta = Util.prepareMeta(currentFile, mContext);
        holder.resMeta.setText("Size : " + String.valueOf(currentFile.getSize()/1024) + " KB" );
        if(position  == 0 || setFirst == false){
            convertView.setBackgroundResource(R.drawable.v5_preference_item_first_bg);
            setFirst = true;
        }else if (position == (files.size()-1)){
            convertView.setBackgroundResource(R.drawable.v5_preference_item_last_bg);
        }else{
            convertView.setBackgroundResource(R.drawable.v5_preference_item_middle_bg);

        }

        return convertView;
    }
}