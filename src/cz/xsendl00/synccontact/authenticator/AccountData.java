package cz.xsendl00.synccontact.authenticator;

public class AccountData {
  private String name;
  private String password;
  private Integer port;
  private String host;
  private String baseDn;
  private Integer encryption;
  private boolean newAccount = true;
  private String searchFilter = "(objectClass=googleContact)";
  
  public AccountData() {
  }
  
  public AccountData(String name, String password, Integer port, String host, String baseDn, Integer encryption, boolean newAccount) {
    this.name = name;
    this.port = port;
    this.host = host;
    this.baseDn = baseDn;
    this.password = password;
    this.encryption = encryption;
    this.newAccount = newAccount;
  }
  
  public AccountData(AccountData aData) {
    this(aData.getName(), aData.getPassword(), aData.getPort(), aData.getHost(), aData.getBaseDn(), aData.getEncryption(), aData.isNewAccount());
  }
  
  public void setHost(String host) {
    this.host = host;
  }
  
  public void setPassword(String pass) {
    this.password = pass;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setEncryption(Integer enc) {
    this.encryption = enc;
  }
  
  public void setPort(Integer port) {
    this.port = port;
  }
  
  public String getHost() {
    return host;
  }
  
  public void setBaseDNs(String[] baseDNs) {
    this.baseDn = "";
    for(String str : baseDNs) {
      this.baseDn += str; 
    }
  }
  
  public String getBaseDn() {
    return baseDn;
  }
  
  public void setBaseDn(String base) {
    this.baseDn = base;
  }
  
  public Integer getPort() {
    return port;
  }
  
  public String getPassword() {
    return password;
  }
  
  public String getName() {
    return name;
  }
  
  public Integer getEncryption() {
    return encryption;
  }

  public boolean isNewAccount() {
    return newAccount;
  }

  public void setNewAccount(boolean newAccount) {
    this.newAccount = newAccount;
  }

  public String getSearchFilter() {
    return searchFilter;
  }

  public void setSearchFilter(String searchFilter) {
    this.searchFilter = searchFilter;
  }
}
