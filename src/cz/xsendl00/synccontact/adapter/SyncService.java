package cz.xsendl00.synccontact.adapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SyncService extends Service {
  private static final String TAG = "SyncService";

  private static final Object syncAdapterLock = new Object();
  private static SyncContactAdapter syncContactAdapter = null;

  @Override
  public void onCreate() {
    Log.v(TAG, "onCreate");
    synchronized (syncAdapterLock) {
      if (syncContactAdapter == null) {
        syncContactAdapter = new SyncContactAdapter(getApplicationContext(), true);
      }
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    Log.v(TAG, "onBind");
    return syncContactAdapter.getSyncAdapterBinder();
  }
}
