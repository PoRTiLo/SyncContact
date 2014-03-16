package cz.xsendl00.synccontact.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncAuthenticatorService extends Service {

  private SyncAuthenticator syncAuthenticator;
  
  @Override
  public void onCreate() {
    syncAuthenticator = new SyncAuthenticator(this);
  }

  @Override
  public void onDestroy() {
  }

  @Override
  public IBinder onBind(Intent intent) {
    return syncAuthenticator.getIBinder();
  }

}
