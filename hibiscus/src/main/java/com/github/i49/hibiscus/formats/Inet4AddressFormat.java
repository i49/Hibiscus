package com.github.i49.hibiscus.formats;

import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * <strong>ipv4</strong> format which represents Internet Protocol Version 4 address.
 * <p>
 * An instance of this format can be obtained by {@link Formats#ipv4()} method.
 * </p>
 */
public class Inet4AddressFormat extends StringFormat {

	/**
	 * The Singleton instance of this format.
	 */
	public static final Inet4AddressFormat INSTANCE = new Inet4AddressFormat();
	
	private Inet4AddressFormat() {
	}

	@Override
	public String getName() {
		return "ipv4";
	}

	@Override
	public boolean test(String value) {
		return InetAddressValidator.getInstance().isValidInet4Address(value);
	}
}
