package thuctap.thudo.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import thuctap.thudo.adapter.DownloadListAdapter;
import thuctap.thudo.models.FilesModels;
import thuctap.thudo.serverfileexplorer.R;


/**
 * Created by vietanha34 on 9/11/13.
 */
public class DownloadingFragment extends SherlockFragment {

    private ListView downloadListView;
    private Context mContext;
    private List<FilesModels> filesDownload;
    public static final int SERVERPORT = 12345;
    public static final String SERVER_IP = "10.0.2.2";
    private DownloadListAdapter adapter ;
    private Socket socket =  null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.donwloadfragment , container , false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDownloadList(getView());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =  getActivity().getApplicationContext();
        filesDownload = new ArrayList<FilesModels>();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(broadcastReceiver , new IntentFilter("ItemOnClick"));
    }

    private void updateListView(){
        this.adapter.notifyDataSetChanged();
        this.downloadListView.invalidate();
    }



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FilesModels file = new FilesModels(intent.getStringExtra("name") , intent.getLongExtra("size" , 0) , intent.getStringExtra("path"));
            int position = filesDownload.size();
            filesDownload.add(file);
            updateListView();
            Thread downloadThread =  new Thread(new DownloadThread(file,intent.getStringExtra("path") , position));
            downloadThread.start();
        }
    };


    public void initDownloadList(View v){

        downloadListView = (ListView)v.findViewById(R.id.download_list);
        adapter = new DownloadListAdapter(getActivity().getApplicationContext(), filesDownload);
        downloadListView.setAdapter(adapter);

    }

    class DownloadThread implements Runnable{
        Socket socket ;
        InputStream in ;
        BufferedOutputStream out;
        String fileName;
        String filePath;
        int progressStatus;
        int position;
        long fileSize;
        ProgressBar progress = null;
        TextView status = null;
        File path;
        File fileEnv = Environment.getExternalStorageDirectory();


        public  DownloadThread(FilesModels file ,  String filePath , View v){
            this.fileName = file.getName();
            this.fileSize = file.getSize();
            this.filePath = filePath;
            DownloadListAdapter.ViewHolder holder = (DownloadListAdapter.ViewHolder)v.getTag();
            this.progress = holder.resBar;
            this.path = new File(fileEnv.getAbsolutePath() + "/" + fileName);
            int i = 1;

            while (this.path.exists()){
                this.path  = new File((fileEnv.getAbsolutePath() + "/" + fileName + "_" + String.valueOf(i)));
                i++;
            }
            Log.i("path" , path.getAbsolutePath());

        }





        public  DownloadThread(FilesModels file ,  String filePath , int position){
            this.fileName = file.getName();
            this.fileSize = file.getSize();
            this.filePath = filePath;
            this.position = position;
            this.path = new File(fileEnv.getAbsolutePath() + "/" + fileName);
            int i = 1;
            while (this.path.exists()){
                this.path  = new File((fileEnv.getAbsolutePath() + "/" + fileName + "_" + String.valueOf(i)));
                i++;
            }
            Log.i("path" , path.getAbsolutePath());

        }



        public void run(){
            try{



                Thread.sleep(1000);
                this.progress = (ProgressBar)downloadListView.getChildAt(position).findViewById(R.id.download_progressBar);
                this.status = (TextView)downloadListView.getChildAt(position).findViewById(R.id.download_status);
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                this.socket =  new Socket(serverAddr , SERVERPORT);
                this.in = socket.getInputStream();
                this.out = new BufferedOutputStream((socket.getOutputStream()) );
                FileOutputStream fos = new FileOutputStream(this.path);
                String ins = "download|" + filePath;
                byte[] len = ByteBuffer.allocate(4).putInt(ins.length()).array();
                byte[] str = ins.getBytes();

                out.write(len , 0 , len.length);
                out.flush();
                out.write(str , 0 , str.length);
                out.flush();

                long size = 0 ;
                int count = 0 ;
                //this.status.setText("den day roi ");

                byte[] buffer = new byte[4096];
                socket.setSoTimeout(2000);
                int currentStatus = 0;
                while ( size <= this.fileSize && (count = in.read(buffer)) >= 0  ){
                    fos.write(buffer , 0 , count);
                    size = size + count;
                     progressStatus = Integer.parseInt(String.valueOf(100*size/fileSize));

                    if (this.progress !=  null){
                        progress.setProgress(progressStatus);
                    }


                    if (currentStatus < progressStatus){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                status.setText(String.valueOf(progressStatus) + " %");
                            }
                        });
                        currentStatus = progressStatus;


                    }



                }
                this.in.close();
                this.out.close();
                this.socket.close();
                Log.i("download", "download complete");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "download file " + fileName + "complete", Toast.LENGTH_SHORT).show();
                        filesDownload.remove(position);
                        adapter.notifyDataSetChanged();
                        downloadListView.invalidate();
                    }
                });
            }catch (UnknownHostException e){
                e.printStackTrace();
            }catch (FileNotFoundException e){
                Toast.makeText(mContext,"khong the ghi file vao thu muc sdcard" ,  Toast.LENGTH_LONG).show();
            }catch (SocketTimeoutException e){
                Log.i("download" , "download complete");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        filesDownload.remove(position);
                        adapter.notifyDataSetChanged();
                        downloadListView.invalidate();
                        Toast.makeText(mContext, "download file " + fileName + " complete", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


}
