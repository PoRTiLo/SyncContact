/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.xsendl00.synccontact;

import com.xsendl00.synccontact.R;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ContactsListActivity extends FragmentActivity implements ContactsListFragment.OnContactsInteractionListener {

  private static final String TAG = "ContactsListActivity";

  //private ContactDetailFragment mContactDetailFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact);
    
    //String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
    //ContactsListFragment mContactsListFragment = (ContactsListFragment)
    //        getSupportFragmentManager().findFragmentById(R.id.contact_list);

    //mContactsListFragment.setSearchQuery(searchQuery);

  }

  /**
   * This interface callback lets the main contacts list fragment notify
   * this activity that a contact has been selected.
   *
   * @param contactUri The contact Uri to the selected contact.
   */
  @Override
  public void onContactSelected(Uri contactUri) {
          //Intent intent = new Intent(this, ContactDetailActivity.class);
          //intent.setData(contactUri);
          //startActivity(intent);
  }

  /**
   * This interface callback lets the main contacts list fragment notify
   * this activity that a contact is no longer selected.
   */
  @Override
  public void onSelectionCleared() {
     // if (mContactDetailFragment != null) {
     //     mContactDetailFragment.setContact(null);
     // }
    }
}
