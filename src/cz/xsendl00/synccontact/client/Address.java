package cz.xsendl00.synccontact.client;

import android.text.TextUtils;


public class Address {

  private AddressType type;
	private String street = "";
	private String city = "";
	private String pobox = "";
	private String region;
	private String state = "";
	private String zip = "";
	private String country = "";
	private String extendedAddress;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Address) {
			Address t = (Address) o;
			if (street == null || city == null || state == null || zip == null || country == null) {
				return false;
			}
			return (street.equals(t.getStreet()) && city.equals(t.getCity()) && state.equals(t.getState()) && zip.equals(t.getZip()) && country.equals(t
					.getCountry()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 4;
		hash = 17 * hash + (null == street ? 0 : street.hashCode());
		hash = 17 * hash + (null == city ? 0 : city.hashCode());
		hash = 17 * hash + (null == state ? 0 : state.hashCode());
		hash = 17 * hash + (null == zip ? 0 : zip.hashCode());
		hash = 17 * hash + (null == country ? 0 : country.hashCode());

		return hash;
	}

	/**
	 * Quick check for an empty address.
	 * 
	 * @return <code>true</code> if all fields are empty.
	 */
	public boolean isEmpty() {
		if (TextUtils.isEmpty(street) && TextUtils.isEmpty(city) && TextUtils.isEmpty(state) && TextUtils.isEmpty(zip) && TextUtils.isEmpty(country)) {
			return true;
		}
		return false;
	}

  public AddressType getType() {
    return type;
  }

  public void setType(AddressType type) {
    this.type = type;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getExtendedAddress() {
    return extendedAddress;
  }

  public void setExtendedAddress(String extendedAddress) {
    this.extendedAddress = extendedAddress;
  }

  public String getPobox() {
    return pobox;
  }

  public void setPobox(String pobox) {
    this.pobox = pobox;
  }

}
