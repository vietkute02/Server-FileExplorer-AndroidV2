package thuctap.thudo.callback;

import android.content.Intent;
import android.net.Uri;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.ShareActionProvider;
import android.widget.ShareActionProvider.OnShareTargetSelectedListener;
import android.widget.Toast;


import com.actionbarsherlock.view.ActionMode.Callback;

import thuctap.thudo.models.FilesModels;
import thuctap.thudo.serverfileexplorer.FileExplorerMain;
import thuctap.thudo.serverfileexplorer.R;

public abstract class FileActionsCallback implements Callback {

	private FileExplorerMain activity;
	private FilesModels file;
	static int[] allOptions = {R.id.menu_copy,R.id.menu_cut, R.id.menu_delete, R.id.menu_props, R.id.menu_share, R.id.menu_rename, R.id.menu_zip, R.id.menu_unzip};
	
	public FileActionsCallback(FileExplorerMain activity,
                               FilesModels fileListEntry) {

		this.activity = activity;
		this.file = fileListEntry;

	}

    /**
     * Called to refresh an action mode's action menu whenever it is invalidated.
     *
     * @param mode ActionMode being prepared
     * @param menu Menu used to populate action buttons
     * @return true if the menu or action mode was updated, false otherwise.
     */
    @Override
    public boolean onPrepareActionMode(com.actionbarsherlock.view.ActionMode mode, com.actionbarsherlock.view.Menu menu) {
        return false;
    }


    /**
     * Called when action mode is first created. The menu supplied will be used to
     * generate action buttons for the action mode.
     *
     * @param mode ActionMode being created
     * @param menu Menu used to populate action buttons
     * @return true if the action mode should be created, false if entering this
     * mode should be aborted.
     */
    @Override
    public boolean onCreateActionMode(com.actionbarsherlock.view.ActionMode mode, com.actionbarsherlock.view.Menu menu) {
        menu.add("Copy").setIcon(R.drawable.v5_bottom_bar_copy_icon_dark);
        return  true;
    }


    /**
     * Called to report a user click on an action button.
     *
     * @param mode The current ActionMode
     * @param item The item that was clicked
     * @return true if this callback handled the event, false if the standard MenuItem
     * invocation should continue.
     */
    @Override
    public boolean onActionItemClicked(com.actionbarsherlock.view.ActionMode mode, com.actionbarsherlock.view.MenuItem item) {
        Toast.makeText(activity.getApplicationContext() , "copy" , Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * Called when an action mode is about to be exited and destroyed.
     *
     * @param mode The current ActionMode being destroyed
     */
    @Override
    public void onDestroyActionMode(com.actionbarsherlock.view.ActionMode mode) {

    }
}
