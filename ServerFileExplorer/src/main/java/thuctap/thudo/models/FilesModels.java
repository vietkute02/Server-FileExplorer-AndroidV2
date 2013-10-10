package thuctap.thudo.models;

/**
 * Created by vietanha34 on 9/6/13.
 */
public class FilesModels {

    private String path;
    private String name;
    private long size;
    private  boolean isDir;
    private String links;





    public FilesModels() {
        // TODO Auto-generated constructor stub
    }

    public FilesModels(String name, long size){
        this.name = name;
        this.size = size;

    }

    public FilesModels(String name , long size , String path){
        this.name = name;
        this.size = size;
        this.path = path;
    }

    public FilesModels(String name, long size, boolean isDir){
        this.name = name;
        this.size = size;
        this.isDir = isDir;
    }


    public String getLinks(){
        return  this.links;

    }

    public void setLinks(String links){
        this.links = links;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public long getSize() {
        return size;
    }


    public void setSize(long size) {
        this.size = size;
    }

    public void setIsDir(boolean isDir){
        this.isDir = isDir;

    }

    public  boolean getIsDir(){

        return isDir;
    }



}


