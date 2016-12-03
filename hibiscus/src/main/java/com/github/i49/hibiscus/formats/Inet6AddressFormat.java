package com.github.i49.hibiscus.formats;

import javax.json.JsonString;

import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * String format which represents IPv6 addresses.
 */
public class Inet6AddressFormat extends AbstractFormat<JsonString> implements StringFormat {

	/**
	 * The one and only instance of this format.
	 */
	private static final Inet6AddressFormat INSTANCE = new Inet6AddressFormat();

	/**
	 * Returns the Singleton instance of this format.
	 * @return the instance of this class.
	 */
	public static Inet6AddressFormat getInstance() {
		return INSTANCE;
	}

	private Inet6AddressFormat() {
	}

	@Override
	public String getName() {
		return "ipv6";
	}

	@Override
	public boolean matches(JsonString value) {
		return InetAddressValidator.getInstance().isValidInet6Address(value.getString());
	}
}
