package cz.xsendl00.synccontact;

import android.os.Parcel;
import android.os.Parcelable;

public class PairIdName implements Parcelable {
  private String name;
  private String id;
  
  @Override
  public int describeContents() {
    // TODO Auto-generated method stub
    return 0;
  }
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    // TODO Auto-generated method stub
    
  }
}
