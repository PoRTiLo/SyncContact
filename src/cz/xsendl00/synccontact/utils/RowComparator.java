/*
 NaturalOrderComparator.java -- Perform 'natural order' comparisons of strings in Java.
 Copyright (C) 2003 by Pierre-Luc Paour <natorder@paour.com>

 Based on the C version by Martin Pool, of which this is more or less a straight conversion.
 Copyright (C) 2000 by Martin Pool <mbp@humbug.org.au>

 This software is provided 'as-is', without any express or implied
 warranty.  In no event will the authors be held liable for any damages
 arising from the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely, subject to the following restrictions:

 1. The origin of this software must not be misrepresented; you must not
 claim that you wrote the original software. If you use this software
 in a product, an acknowledgment in the product documentation would be
 appreciated but is not required.
 2. Altered source versions must be plainly marked as such, and must not be
 misrepresented as being the original software.
 3. This notice may not be removed or altered from any source distribution.
 */

package cz.xsendl00.synccontact.utils;

import java.util.Comparator;
import java.util.Locale;

/**
 * FROM http://www.java2s.com/Code/Java/Collections-Data-Structure/NaturalOrderComparator.htm
 * Comparator for ContactRow. Compare by name.
 *
 * @author portilo
 *
 */
public class RowComparator implements Comparator<AbstractRow> {

  private int compareRight(String a, String b) {
    int bias = 0;
    int ia = 0;
    int ib = 0;

    // The longest run of digits wins. That aside, the greatest
    // value wins, but we can't know that it will until we've scanned
    // both numbers to know that they have the same magnitude, so we
    // remember it in BIAS.
    for (;; ia++, ib++) {
      char ca = charAt(a, ia);
      char cb = charAt(b, ib);

      if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
        return bias;
      } else if (!Character.isDigit(ca)) {
        return -1;
      } else if (!Character.isDigit(cb)) {
        return +1;
      } else if (ca < cb) {
        if (bias == 0) {
          bias = -1;
        }
      } else if (ca > cb) {
        if (bias == 0) {
          bias = +1;
        }
      } else if (ca == 0 && cb == 0) {
        return bias;
      }
    }
  }

  @Override
  public int compare(AbstractRow left, AbstractRow right) {
    if (left.getName() == null) {
      return -1;
    } else if (right.getName() == null) {
      return 1;
    }
    Locale locale  = new Locale("cs", "CZ");
    String leftContact = left.getName().toLowerCase(locale);
    String rightContact = right.getName().toLowerCase(locale);

    int ia = 0;
    int ib = 0;
    int nza = 0;
    int nzb = 0;
    char charLeft;
    char charRight;
    int result;

    while (true) {
      // only count the number of zeroes leading the last number compared
      nza = nzb = 0;

      charLeft = charAt(leftContact, ia);
      charRight = charAt(rightContact, ib);

      // skip over leading spaces or zeros
      while (Character.isSpaceChar(charLeft) || charLeft == '0') {
        if (charLeft == '0') {
          nza++;
        } else {
          // only count consecutive zeroes
          nza = 0;
        }

        charLeft = charAt(leftContact, ++ia);
      }

      while (Character.isSpaceChar(charRight) || charRight == '0') {
        if (charRight == '0') {
          nzb++;
        } else {
          // only count consecutive zeroes
          nzb = 0;
        }

        charRight = charAt(rightContact, ++ib);
      }

      // process run of digits
      if (Character.isDigit(charLeft) && Character.isDigit(charRight)) {
        result = compareRight(leftContact.substring(ia), rightContact.substring(ib));
        if (result != 0) {
          return result;
        }
      }
      if (charLeft == 0 && charRight == 0) {
        // The strings compare the same. Perhaps the caller
        // will want to call strcmp to break the tie.
        return nza - nzb;
      }

      if (charLeft < charRight) {
        return -1;
      } else if (charLeft > charRight) {
        return +1;
      }

      ++ia;
      ++ib;
    }
  }

  private static char charAt(String s, int i) {
    if (i >= s.length()) {
      return 0;
    } else {
      return s.charAt(i);
    }
  }
}