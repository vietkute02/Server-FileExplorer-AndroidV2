package thuctap.thudo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;



import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedOutputStream;
import java.nio.ByteBuffer;

import thuctap.thudo.adapter.DownloadListAdapter;
import thuctap.thudo.adapter.FileListAdapter;
import thuctap.thudo.callback.FileActionsCallback;
import thuctap.thudo.models.FilesModels;
import thuctap.thudo.serverfileexplorer.FileExplorerMain;
import thuctap.thudo.serverfileexplorer.R;
import thuctap.thudo.utils.JsonHandle;


/**
 * Created by vietanha34 on 9/11/13.
 */
public class FileListFragment extends SherlockFragment {
    private ListView explorerListView = null;
    private List<FilesModels> filemodels  = new ArrayList<FilesModels>();
    private FileListAdapter adapter;
    private Socket clientSocket = null;
    public static final int SERVERPORT = 12345;
    public static final String SERVER_IP = "10.0.2.2";
    private Thread myConnThread = null;
    private BufferedReader in = null;
    private BufferedOutputStream out = null;
    private Context mContext  ;
    private String path = ".";
    protected Object mCurrentActionMode;


    public static FileListFragment newInstance(){
        FileListFragment fragment = new FileListFragment();

        return fragment;
    }

    private final Handler myHandler  = new Handler(){

        public void handleMessage(Message msg) {
            initFileList();
        }
    };
    private  final  Handler startDownloadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (explorerListView == null){
                initFileList();
            }else{

            }
        }
    };


    public void initFileList(){

        this.explorerListView = (ListView) getView().findViewById(R.id.listfileserver);
        explorerListView.getLayoutParams();
        adapter  = new FileListAdapter(getActivity().getApplicationContext(), filemodels);
        explorerListView.setAdapter(adapter);
        explorerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (explorerListView.isClickable()){
                    FilesModels file = (FilesModels) explorerListView.getAdapter().getItem(i);
                    if (file.getIsDir()){
                        path = path + "/" + file.getName();
                        myConnThread = new Thread(new ConnThread(clientSocket , in , out , path));
                        myConnThread.start();
                    }else{
                        String pathDownload =  path + "/" + file.getName();
                        sendBroadcast(file, pathDownload);




                    }
                }
            }
        });

        explorerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        explorerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!explorerListView.isLongClickable())
                    return true;
                view.setSelected(true);
                final FilesModels file = (FilesModels) adapter
                        .getItem(i);
                if (mCurrentActionMode != null) {
                    return false;
                }
                explorerListView.setEnabled(false);
                mCurrentActionMode = getSherlockActivity().startActionMode(new FileActionsCallback((FileExplorerMain)getActivity(),file) {

                    /**
                     * Called when an action mode is about to be exited and destroyed.
                     *
                     * @param mode The current ActionMode being destroyed
                     */
                    @Override
                    public void onDestroyActionMode(ActionMode actionMode) {
                        explorerListView.setEnabled(true);
                    }

                });

                return  true;
            }
        });
        registerForContextMenu(explorerListView);
    }



    private void updateData(List<FilesModels> list){
        this.filemodels = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        getActivity().setProgressBarVisibility(true);
        this.myConnThread = new Thread(new ConnThread(this.clientSocket , in ,out , "" ));
        this.myConnThread.start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filelistlayout , container , false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStop() {
        Log.i("onStop" , "onStop");
        super.onStop();
    }

    public void sendBroadcast(FilesModels file , String path){
        Intent intent = new Intent("ItemOnClick");
        intent.putExtra("name", file.getName());
        intent.putExtra("size" , file.getSize());
        intent.putExtra("path" , path);

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);

    }


    class ConnThread implements  Runnable {
        Socket mainClientSocket = null;
        BufferedReader inThread =  null;
        BufferedOutputStream outThread = null;
        JsonHandle handle;
        List<FilesModels> list;
        String getStr =  "get|";



        public ConnThread(Socket socket , BufferedReader in , BufferedOutputStream out  , String path){
            this.mainClientSocket = socket;
            this.inThread = in;
            this.outThread = out;
            this.getStr  = this.getStr + path;
        }

        public void run(){
            try{
                if (this.mainClientSocket == null){
                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                    this.mainClientSocket =  new Socket(serverAddr , SERVERPORT);
                    this.inThread = new BufferedReader(new InputStreamReader(this.mainClientSocket.getInputStream()));
                    this.outThread = new BufferedOutputStream(mainClientSocket.getOutputStream());
                }

                int lenght = this.getStr.getBytes().length;
                byte[] len = ByteBuffer.allocate(4).putInt(lenght).array();
                byte[] str = this.getStr.getBytes();
                Log.i("getStr" , getStr);

                outThread.write(len , 0 , len.length);
                outThread.flush();
                outThread.write(str , 0 , str.length);
                outThread.flush();

                String inputStr = inThread.readLine();
                this.handle = new JsonHandle(inputStr);
                this.list = this.handle.jsonHandle();
                //outThread.close();
                //inThread.close();
                //this.mainClientSocket.close();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateData(list);
                        clientSocket = mainClientSocket;
                        in = inThread;
                        out = outThread;
                       // getActivity().setProgressBarVisibility(false);

                    }
                });

                Message msg = myHandler.obtainMessage();
                myHandler.sendMessage(msg);


            }catch (UnknownHostException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }




}
