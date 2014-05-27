/*
 * Copyright (C) 2014 by xsendl00.*/
package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Show contacts of group.
 *
 * @author portilo
 */
@EActivity
public class ContactsDetailAddActivity extends ListActivity {

  private static final String TAG = "ContactsDetailAddActivity";
  private ContactManager contactManager;
  private Integer groupId;
  private List<Integer> selectedIds;
  private List<ContactRow> contactRows;
  private boolean isSync;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    selectedIds = intent.getIntegerArrayListExtra(Constants.INTENT_SELECTED);
    isSync = intent.getBooleanExtra(Constants.INTENT_SYNC, true);
    if (selectedIds == null) {
      selectedIds = new ArrayList<Integer>();
    }
    groupId = intent.getIntExtra(Constants.INTENT_ID, 0);
    contactManager = ContactManager.getInstance(ContactsDetailAddActivity.this);
    loadData();
  }


  /**
   * Initialize data for showing list of names.
   */
  @UiThread
  public void init() {
    this.contactRows = contactManager.getLocalContacts();
    if (!contactRows.isEmpty()) {
      Log.i(TAG, "is not empty");
      String[] values = new String[contactRows.size()];
      int i = 0;

      for (ContactRow contactRow : contactRows) {
        values[i++] = contactRow.getName();
      }
      ArrayAdapter<ContactRow> adapter = new ArrayAdapter<ContactRow>(this,
          android.R.layout.simple_list_item_multiple_choice, contactRows);
      setListAdapter(adapter);

    } else {
      Log.i(TAG, "is empty");
      ArrayAdapter<ContactRow> adapter = new ArrayAdapter<ContactRow>(
          this, android.R.layout.simple_list_item_multiple_choice);
      setListAdapter(adapter);

    }

    setFinishOnTouchOutside(false);
    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    int pos = 0;
    Log.i(TAG, selectedIds.toString());
    for (ContactRow contactRow : contactRows) {
      if (selectedIds.contains(contactRow.getId())) {
        //Log.i(TAG, contactRow.getName());
        getListView().setItemChecked(pos, true);
      }
      pos++;
    }

  }

  /**
   * Load data into {@link ContactManager}.
   * @return thread
   */
  public Thread loadData() {
    final ProgressDialog progressDialog = ProgressDialog.show(ContactsDetailAddActivity.this, "",
        getText(R.string.progress_loading), true);
    progressDialog.setCanceledOnTouchOutside(false);
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        contactManager.getLocalContacts();
        if (progressDialog != null) {
          progressDialog.dismiss();
        }
        init();
      }
    };
    return Utils.performOnBackgroundThread(runnable);
  }

  /**
   * Save change to database.
   * @param progressDialog
   * @return thread
   */
  private Thread save(final ProgressDialog progressDialog) {
    contactManager.setLocalGroupsContactsInit(false);
    contactManager.setLocalGroupsInit(false);
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
     // SAVE TO db

        final List<ContactRow> selected = new ArrayList<ContactRow>();
        SparseBooleanArray checked = getListView().getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
          if (checked.valueAt(i)) {
            ContactRow contactRow = (ContactRow) getListView().getItemAtPosition(checked.keyAt(i));
            selected.add(contactRow);
            Log.i("xxxx", i + " " + contactRow.toString());
          }
        }

        // add to group
        //selected
        Thread addThread = new Thread(new Runnable() {

          @Override
          public void run() {
            List<ContactRow> updateList = new ArrayList<ContactRow>();
            for (ContactRow contactRow : selected) {
              if (!selectedIds.contains(contactRow.getId())) {
                updateList.add(contactRow);
              }
            }
            new AndroidDB().addContact2Group(ContactsDetailAddActivity.this, updateList, groupId);
            // set contacts to sync
            if (isSync) {
              new AndroidDB().updateContactsSync(ContactsDetailAddActivity.this, updateList, true);
            }
          }
        });

        addThread.start();
        // remove to gtoup

        Thread removeThread = new Thread(new Runnable() {

          @Override
          public void run() {
            List<Integer> toDelete = new ArrayList<Integer>();
            List<ContactRow> toNoSync = new ArrayList<ContactRow>();
            for (Integer id : selectedIds) {
              boolean found = false;
              for (ContactRow contactRow : selected) {
                if (contactRow.getId() == id) {
                  found = true;
                  break;
                }
              }
              if (!found) {
                toDelete.add(id);
                ContactRow setContact = new ContactRow();
                setContact.setId(id);
                toNoSync.add(setContact);
              }
            }
            if (isSync) {
              new AndroidDB().updateContactsSync(ContactsDetailAddActivity.this, toNoSync, false);
            }
            new AndroidDB().removeContactFromGroup(ContactsDetailAddActivity.this, toDelete, groupId);
          }
        });
        removeThread.start();

        try {
          removeThread.join();
          addThread.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        new AndroidDB().setGroupModify(ContactsDetailAddActivity.this, groupId);
//        new Thread(new Runnable() {
//
//          @Override
//          public void run() {
            contactManager.setLocalContactsInit(false);
            contactManager.getLocalContacts();
            contactManager.setLocalGroupsContactsInit(false);
            contactManager.getLocalGroupsContacts();
            contactManager.setLocalGroupsInit(false);
            contactManager.getLocalGroups();
//          }
//        }).start();

        if (progressDialog != null) {
          progressDialog.dismiss();
        }
        go2Back();
      }
    };
    return Utils.performOnBackgroundThread(runnable);
  }

  @UiThread
  public void go2Back() {
    Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_saved), Toast.LENGTH_SHORT);
    toast.show();
    finish();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.contacts_menu_detail_add, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
      case R.id.action_save:
        Log.i(TAG, "save");
        final ProgressDialog progressDialog = ProgressDialog.show(ContactsDetailAddActivity.this, "",
            getText(R.string.progress_saving), true);
        progressDialog.setCanceledOnTouchOutside(false);
        save(progressDialog);
        break;
      case R.id.action_help:
        intent = new Intent(this, HelpActivity_.class);
        startActivity(intent);
        break;
      case R.id.action_settings:
        intent = new Intent(this, SettingsActivity_.class);
        startActivity(intent);
        break;
      case android.R.id.home:
        finish();
        break;
      default:
        break;
    }
    return true;
  }
}
