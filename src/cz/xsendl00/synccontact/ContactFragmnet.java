package cz.xsendl00.synccontact;

import com.xsendl00.synccontact.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactFragmnet extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {

      View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
       
      return rootView;
  }

}
