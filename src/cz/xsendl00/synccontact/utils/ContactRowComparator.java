package cz.xsendl00.synccontact.utils;

import java.util.Comparator;

/**
 * Comparator for ContactRow. Compare by name.
 * @author portilo
 *
 */
public class ContactRowComparator implements Comparator<ContactRow> {
  
  public int compare(ContactRow left, ContactRow right) {
    return left.getName().compareTo(right.getName());
  }
}