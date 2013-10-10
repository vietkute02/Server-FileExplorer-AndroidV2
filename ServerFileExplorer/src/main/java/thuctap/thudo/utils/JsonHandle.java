package thuctap.thudo.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thuctap.thudo.models.FilesModels;

/**
 * Created by vietanha34 on 9/6/13.
 */
public class JsonHandle{

    private String jsonStr;
    private List<FilesModels> filesList =  new ArrayList<FilesModels>();

    public JsonHandle(String jsonStr){
        this.jsonStr = jsonStr;
        this.filesList.add(new FilesModels(".",0 , true));
        this.filesList.add(new FilesModels(".." , 0 , true));

    }

    public JsonHandle(){}


    public void setJsonStr(String jsonStr){
        this.jsonStr = jsonStr;
    }

    public String getJsonStr(){
        return  jsonStr;
    }

    public List<FilesModels> jsonHandle(){
        try{

            JSONArray arrayList = new JSONArray(this.jsonStr);


            JSONArray dirList = arrayList.getJSONArray(0);

            // xu ly du lieu tu json dir
            for (int i = 0 ; i < dirList.length()  ; i ++){
                JSONObject dir = (JSONObject) dirList.get(i);
                FilesModels dirData = new FilesModels();
                dirData.setIsDir(true);
                dirData.setLinks((String) dir.get("link"));
                dirData.setName((String) dir.get("dirname"));
                dirData.setSize(0);

                filesList.add(dirData);


            }
            // xu ly du lieu tu json list files
            JSONArray fileList = arrayList.getJSONArray(1);

            for (int i = 0 ; i < fileList.length() ; i++){
                JSONObject file = (JSONObject) fileList.get(i);
                FilesModels fileData =  new FilesModels();
                fileData.setSize(Long.parseLong((String) file.get("size")));
                fileData.setName((String) file.get("filename"));
                fileData.setIsDir(false);
                fileData.setLinks("");

                filesList.add(fileData);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();


        }
        return filesList;

    }
}