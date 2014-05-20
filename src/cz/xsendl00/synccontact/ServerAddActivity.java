package cz.xsendl00.synccontact;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Add or edit server connection.
 */
@EActivity(R.layout.activity_server_add)
public class ServerAddActivity extends AccountAuthenticatorActivity {

  private static final String TAG = "ServerAddActivity";

  public static final String HOST = "192.168.0.102";
  public static final String DN = "dc=synccontact,dc=xsendl00,dc=cz";
  public static final String USER = "cn=admin," + DN;
  public static final String PASS = "synccontact";

  private final Handler handler = new Handler();
  private ProgressDialog progressBar;
  private Thread authThread;
  private AccountManager accountManager;

  @ViewById(R.id.add_account_encryption_value)
  protected Spinner encryptionSpinner;
  @ViewById(R.id.add_account_list_value)
  protected LinearLayout list;
  private LinearLayout linearLayout;
  private TextView portTextView;
  private EditText portEditText;
  private EditText nameEditText;
  private EditText passEditText;
  @ViewById(R.id.add_account_host_value)
  protected EditText hostEditText;
  @ViewById(R.id.add_account_dn_value)
  protected EditText dnEditText;

  private String authToken;
  private String authTokenType;
  private boolean first;

  private ContactManager contactManager;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    accountManager = AccountManager.get(this);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    contactManager = ContactManager.getInstance(ServerAddActivity.this);
  }

  @Override
  public void onResume() {
    super.onResume();
    init();
  }

  /**
   * Initialize activity.
   */
  @AfterViews
  protected void init() {

    first = getIntent().getBooleanExtra(Constants.INTENT_FIRST, false);
    Thread updateContactIdGroupThread = new Thread(new Runnable() {
      @Override
      public void run() {
        if (contactManager.getAccountData() == null) {
          contactManager.initAccountData();
        }
      }
    });
    updateContactIdGroupThread.start();


    createLoginText();
    createPortText();

    try {
      updateContactIdGroupThread.join(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (contactManager.getAccountData() == null) {
      contactManager.setAccountData(AccountData.initDefault());
    }
    setControls();
    setEncryptionSpinner();
    // TODO:delete after complete
    if (contactManager.getAccountData().isNewAccount()) {
      hostEditText.setText(HOST);
      dnEditText.setText(DN);
      nameEditText.setText(USER);
      passEditText.setText(PASS);
    } else {
      hostEditText.setText(contactManager.getAccountData().getHost());
      dnEditText.setText(contactManager.getAccountData().getBaseDn());
      nameEditText.setText(contactManager.getAccountData().getName());
      passEditText.setText(contactManager.getAccountData().getPassword());
    }
  }

  private void createLoginText() {
    nameEditText = new EditText(getApplicationContext());
    nameEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT));
    nameEditText.setHint(getResources().getString(R.string.add_account_name));
    nameEditText.setGravity(Gravity.CENTER_VERTICAL);

    passEditText = new EditText(getApplicationContext());
    passEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT));
    passEditText.setHint(getResources().getString(R.string.add_account_password));
    passEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    passEditText.setGravity(Gravity.CENTER_VERTICAL);
    passEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
  }

  private void createPortText() {
    int padding = getResources().getDimensionPixelOffset(R.dimen.margin_8);
    linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT));
    linearLayout.setPadding(0, padding, 0, 0);

    portTextView = new TextView(getApplicationContext());
    portTextView.setText(getResources().getString(R.string.add_account_port));
    portTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT));

    portEditText = new EditText(getApplicationContext());
    portEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT));
    portEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
    portEditText.setGravity(Gravity.CENTER_VERTICAL);

    linearLayout.addView(portTextView);
    linearLayout.addView(portEditText);
  }

  private void setEncryptionSpinner() {
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.encryption_methods, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    encryptionSpinner.setAdapter(adapter);

    encryptionSpinner.setSelection(contactManager.getAccountData().getEncryption());
    encryptionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
      }

      @Override
      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        list.removeAllViews();
        Log.i(TAG, "selecst = " + arg2);
        if (arg2 == 1) {
          list.addView(linearLayout);
          portEditText.setText(Constants.STARTTLS);
        } else if (arg2 == 2 | arg2 == 3) {
          list.addView(linearLayout);
          list.addView(nameEditText);
          list.addView(passEditText);
          if (arg2 == 2) {
            portEditText.setText(Constants.SSL_TLS);
            contactManager.getAccountData().setEncryption(Constants.SSL_TLS_INT);
          } else {
            portEditText.setText(Constants.STARTTLS);
            contactManager.getAccountData().setEncryption(Constants.STARTTLS_INT);
          }
        }
      }
    });
  }

  private void setControls() {
    Log.i(TAG, "setControls : " + contactManager.getAccountData().toString());

    if (!contactManager.getAccountData().isNewAccount()) {
      TextView info = (TextView) findViewById(R.id.add_info);
      info.setText(this.getResources().getString(R.string.edit_account_main));
      hostEditText.setText(contactManager.getAccountData().getHost());
      portEditText.setText(Integer.toString(contactManager.getAccountData().getPort()));
      nameEditText.setText(contactManager.getAccountData().getName());
      passEditText.setText(contactManager.getAccountData().getPassword());
    }
  }

  /**
   * Call this by click on next in Add activity. This function try connect to server and get base
   * info from them.
   *
   * @param view view.
   */
  public void createConnection(View view) {
    AccountData accountData = new AccountData();
    accountData.setName(nameEditText.getText().toString());
    try {
      accountData.setPort(Integer.parseInt(portEditText.getText().toString()));
    } catch (NumberFormatException nfe) {
      accountData.setPort(Integer.parseInt(Constants.STARTTLS));
    }
    accountData.setHost(hostEditText.getText().toString());
    accountData.setBaseDn(dnEditText.getText().toString());
    accountData.setPassword(passEditText.getText().toString());
    accountData.setEncryption(encryptionSpinner.getSelectedItemPosition());

    // show progressBar
    showProgressBar(view);

    authThread = ServerUtilities.attemptAuth(new ServerInstance(accountData),
        handler, ServerAddActivity.this, false);
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

      @Override
      public void onClick(DialogInterface dialog, int id) {
        // User clicked OK button
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  /**
   * Call after check server connection.
   *
   * @param baseDNs base DN LDAP.
   * @param result true/false - connect successfully
   * @param message message about error
   */
  public void onAuthenticationResult(String[] baseDNs, boolean result, String message) {
    Log.i(TAG, "onAuthenticationResult(" + result + ")");
    if (progressBar != null) {
      progressBar.dismiss();
    }

    if (result) {
      if (baseDNs != null) {
        contactManager.getAccountData().setBaseDNs(baseDNs);
        contactManager.getAccountData().setName(nameEditText.getText().toString());
        try {
          contactManager.getAccountData().setPort(Integer.parseInt(portEditText.getText().toString()));
        } catch (NumberFormatException nfe) {
          contactManager.getAccountData().setPort(Integer.parseInt(Constants.STARTTLS));
        }
        contactManager.getAccountData().setHost(hostEditText.getText().toString());
        contactManager.getAccountData().setBaseDn(dnEditText.getText().toString());
        contactManager.getAccountData().setPassword(passEditText.getText().toString());
        contactManager.getAccountData().setEncryption(encryptionSpinner.getSelectedItemPosition());
        // now save account
        saveAccount();
      }
    } else {
      showDialog(message);
      Log.e(TAG, "onAuthenticationResult: failed to authenticate");
    }
  }

  private void saveAccount() {
    // final Account account = new Account(accountData.getHost(), Constants.ACCOUNT_TYPE);
    final Account account = new Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE);

    // if new account
    if (contactManager.getAccountData().isNewAccount()) {
      Log.i(TAG, "saveAccount() - new");
      Bundle userData = AccountData.toBundle(contactManager.getAccountData());
      // create new account for contact in table accounts
      accountManager.addAccountExplicitly(account, contactManager.getAccountData().getPassword(),
          userData);
      // Set contacts sync for this account.
      ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
      ContactManager.makeGroupVisible(account.name, getContentResolver());
    } else {
      Log.i(TAG, "saveAccount() - update");
      accountManager.setPassword(account, contactManager.getAccountData().getPassword());
      accountManager.setUserData(account, Constants.PAR_USERNAME, contactManager.getAccountData()
          .getName());
      accountManager.setUserData(account, Constants.PAR_PORT, contactManager.getAccountData()
          .getPort() + "");
      accountManager.setUserData(account, Constants.PAR_HOST, contactManager.getAccountData()
          .getHost());
      accountManager.setUserData(account, Constants.PAR_ENCRYPTION, contactManager.getAccountData()
          .getEncryption() + "");
      accountManager.setUserData(account, Constants.PAR_SEARCHFILTER,
          contactManager.getAccountData().getSearchFilter());
      accountManager.setUserData(account, Constants.PAR_BASEDN, contactManager.getAccountData()
          .getBaseDn());
    }
    // ServerUtilities.updateContacts(new ServerInstance(accountData), accountData, handler,
    // ServerAddActivity.this);

    final Intent intent = new Intent();
    authToken = contactManager.getAccountData().getPassword();
    intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
    if (authTokenType != null && authTokenType.equals(Constants.AUTHTOKEN_TYPE)) {
      intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
    }
    setAccountAuthenticatorResult(intent.getExtras());
    setResult(RESULT_OK, intent);

    if (contactManager.getAccountData().isNewAccount() || first) {
      Intent intent1 = new Intent(this, InfoSyncActivity_.class);
      startActivity(intent1);
    } else {
      Intent databackIntent = new Intent();
      setResult(Activity.RESULT_OK, databackIntent);
      databackIntent.putExtra("EDITED", true);
      finish();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    if (contactManager.getAccountData() == null || contactManager.getAccountData().isNewAccount()
        || first) {
      inflater.inflate(R.menu.settings_menu, menu);
    } else {
      inflater.inflate(R.menu.sync_menu, menu);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
      case R.id.action_help:
        intent = new Intent(this, HelpActivity_.class);
        if (first) {
          intent.putExtra(Constants.INTENT_FIRST, true);
        }
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
