package cz.xsendl00.synccontact.utils;

public class Utils {
  private static Long last = System.currentTimeMillis();
  private static Long now  = last;
  private static Integer i = 0;
  
  public static String getTime() {
    now = System.currentTimeMillis();
    String str = i.toString() + String.valueOf(now - last);
    i++;
    last = now;
    
    return str;
  }
}
