package cz.xsendl00.synccontact;

import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ServerInstance;
import cz.xsendl00.synccontact.client.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddServerActivity extends AccountAuthenticatorActivity {
  
  private static final String TAG = "AddServerActivity";
  
  public static final String ARG_ACCOUNT_TYPE = "accountType";
  public static final String ARG_AUTH_TYPE = "authtokenType";
  public static final String ARG_IS_ADDING_NEW_ACCOUNT= "newContact";
  public static final String ARG_USERNAME = "username";
  public static final String ARG_PASSWORD = "password";
  public static final String ARG_CONFIRMCREDENTIALS = "confirmCredentials";
  public static final String ARG_PORT = "port";
  public static final String ARG_HOST = "host";
  public static final String ARG_ENCRYPTION = "encryption";
  public static final String HOST = "192.168.0.103";
  public static final String DN = "cn=admin,dc=synccontact,dc=xsendl00,dc=cz";
  public static final String PASS = "synccontact";
  
  private final Handler handler = new Handler();
  private ProgressDialog progressBar;
  private Thread authThread;
  private AccountData accountData;
  private AccountManager accountManager;
  private Spinner encryptionSpinner;
  private LinearLayout list;
  private LinearLayout listH;
  private TextView portTextView;
  private EditText portEditText;
  private EditText nameEditText;
  private EditText passEditText;
  private EditText hostEditText;
  
  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    accountManager = AccountManager.get(this);
    setContentView(R.layout.activity_add_account);
    
    init();
  }

  private void init() {
    fillValueFromIntent();
    
    createLoginText();
    createPortText();
    setControls();
    setEncryptionSpinner();
    hostEditText.setText(HOST);
    nameEditText.setText(DN);
    passEditText.setText(PASS);
    
  }
  
  private void createLoginText() {
    //int padding = getResources().getDimensionPixelOffset(R.dimen.margin_20);
    
    nameEditText = new EditText(getApplicationContext());
    nameEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    //nameEditText.setPadding(0, padding, 0, 0);
    nameEditText.setHint(getResources().getString(R.string.add_account_name));
    nameEditText.setGravity(Gravity.CENTER_VERTICAL);
    
    passEditText = new EditText(getApplicationContext());
    passEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    //passEditText.setPadding(0, padding, 0, 0);
    passEditText.setHint(getResources().getString(R.string.add_account_password));
    passEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    passEditText.setGravity(Gravity.CENTER_VERTICAL);
  }
  
  private void createPortText() {
    int padding = getResources().getDimensionPixelOffset(R.dimen.margin_20);
    
    listH = new LinearLayout(this);
    listH.setOrientation(LinearLayout.HORIZONTAL);
    listH.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    listH.setPadding(0, padding, 0, 0);
    
    
    portTextView = new TextView(getApplicationContext());
    portTextView.setText(getResources().getString(R.string.add_account_port));
    portTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    
    portEditText = new EditText(getApplicationContext());
    portEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    portEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
    portEditText.setGravity(Gravity.CENTER_VERTICAL);
    
    listH.addView(portTextView);
    listH.addView(portEditText);
  }
  
  private void setEncryptionSpinner() {
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.encryption_methods, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    encryptionSpinner.setAdapter(adapter);
    
    encryptionSpinner.setSelection(accountData.getEncryption());
    list = (LinearLayout)findViewById(R.id.add_account_list_value);
    encryptionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
      }
      @Override
      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        list.removeAllViews();
        Log.i(TAG, "selecst = "+arg2);
        if (arg2 == 1) {
          list.addView(listH);
          portEditText.setText(Constants.STARTTLS);
        } else if (arg2 == 2 | arg2 == 3){
          list.addView(listH);
          list.addView(nameEditText);
          list.addView(passEditText);
          if (arg2 == 2) {
            portEditText.setText(Constants.SSL_TLS);
            accountData.setEncryption(Constants.SSL_TLS_INT);
          } else {
            portEditText.setText(Constants.STARTTLS);
            accountData.setEncryption(Constants.STARTTLS_INT);
          }
        }
      }
    });
  }
  
  private void setControls() {
    encryptionSpinner = (Spinner) findViewById(R.id.add_account_encryption_value);
    hostEditText = (EditText) findViewById(R.id.add_account_host_value);
    hostEditText.setText(accountData.getHost());
    portEditText.setText(Integer.toString(accountData.getPort()));
    nameEditText.setText(accountData.getName());
    passEditText.setText(accountData.getPassword());
  }
  
  private void fillValueFromIntent() {
    final Intent intent = getIntent();
    this.accountData = new AccountData();
    this.accountData.setPort(intent.getIntExtra((ARG_PORT), 389));
    this.accountData.setHost(intent.getStringExtra(ARG_HOST));
    this.accountData.setName(intent.getStringExtra(ARG_USERNAME));
    this.accountData.setPassword(intent.getStringExtra(ARG_PASSWORD));
    this.accountData.setEncryption(intent.getIntExtra(ARG_ENCRYPTION, 0));
    this.accountData.setNewAccount(this.accountData.getName() == null);
  }
  
  /**
   * Call this by click on next in Add activity. This function try connect to server and get base info from them.
   * @param view
   */
  public void getLDAPServerDetails(View view) {
    if(accountData.isNewAccount()) {
      accountData.setName(nameEditText.getText().toString());
    }
    try {
      accountData.setPort(Integer.parseInt(portEditText.getText().toString()));
    } catch (NumberFormatException nfe) {
      accountData.setPort(Integer.parseInt(Constants.STARTTLS));
    }
    accountData.setHost(hostEditText.getText().toString());
    accountData.setPassword(passEditText.getText().toString());
    
    // show progressBar
    showProgressBar(view);
    
    authThread = ServerUtilities.attemptAuth(new ServerInstance(accountData), handler, AddServerActivity.this);
  }
  
  private void showProgressBar(View view) {
    progressBar = new ProgressDialog(view.getContext());
    progressBar.setCancelable(true);
    progressBar.setMessage("Autentication...");
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.show();
  }
  
  private void showDialog(String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(message).setTitle("error");
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
          // User clicked OK button
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }
  
  /**
   * Call after
   * @param baseDNs
   * @param result
   * @param message
   */
  public void onAuthenticationResult(String[] baseDNs, boolean result, String message) {
    Log.i(TAG, "onAuthenticationResult(" + result + ")");
    if (progressBar != null) {
      progressBar.dismiss();
    }
    
    if (result) {
      if (baseDNs != null) {
        //ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, baseDNs);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // mBaseDNSpinner.setAdapter(adapter);
      }
    } else {
      showDialog(message);
      Log.e(TAG, "onAuthenticationResult: failed to authenticate");
    }
  } 
}
