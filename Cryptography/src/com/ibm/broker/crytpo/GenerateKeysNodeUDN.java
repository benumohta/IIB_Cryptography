package com.ibm.broker.crytpo;

import com.ibm.broker.config.appdev.InputTerminal;
import com.ibm.broker.config.appdev.Node;
import com.ibm.broker.config.appdev.NodeProperty;
import com.ibm.broker.config.appdev.OutputTerminal;

/*** 
 * <p>  <I>GenerateKeysNodeUDN</I> instance</p>
 * <p></p>
 */
public class GenerateKeysNodeUDN extends Node {

	private static final long serialVersionUID = 1L;

	// Node constants
	protected final static String NODE_TYPE_NAME = "com/ibm/broker/crytpo/GenerateKeysNode";
	protected final static String NODE_GRAPHIC_16 = "platform:/plugin/Cryptography/icons/full/obj16/com/ibm/broker/crytpo/GenerateKeys.gif";
	protected final static String NODE_GRAPHIC_32 = "platform:/plugin/Cryptography/icons/full/obj30/com/ibm/broker/crytpo/GenerateKeys.gif";

	protected final static String PROPERTY_ALGORITHM = "algorithm";
	protected final static String PROPERTY_PUBLICKEYPATH = "publicKeyPath";
	protected final static String PROPERTY_PRIVATEKEYPATH = "privateKeyPath";
	protected final static String PROPERTY_KEYIDENTITY = "keyIdentity";
	protected final static String PROPERTY_KEYPASSPHRASE = "keyPassphrase";
	protected final static String PROPERTY_ISPASSPRAHSEENCODED = "isPassprahseEncoded";
	protected final static String PROPERTY_KEYSIZE = "keySize";


	/**
	 * <I>ENUM_GENERATEKEYS_ALGORITHM</I>
	 * <pre>
	 * ENUM_GENERATEKEYS_ALGORITHM.RSA = RSA
	 * ENUM_GENERATEKEYS_ALGORITHM.AES = AES
	 * </pre>
	 */
	public static class ENUM_GENERATEKEYS_ALGORITHM {
		private String value;

		public static final ENUM_GENERATEKEYS_ALGORITHM RSA = new ENUM_GENERATEKEYS_ALGORITHM("RSA");
		public static final ENUM_GENERATEKEYS_ALGORITHM AES = new ENUM_GENERATEKEYS_ALGORITHM("AES");

		protected ENUM_GENERATEKEYS_ALGORITHM(String value) {
			this.value = value;
		}
		public String toString() {
			return value;
		}

		protected static ENUM_GENERATEKEYS_ALGORITHM getEnumFromString(String enumValue) {
			ENUM_GENERATEKEYS_ALGORITHM enumConst = ENUM_GENERATEKEYS_ALGORITHM.RSA;
			if (ENUM_GENERATEKEYS_ALGORITHM.AES.value.equals(enumValue)) enumConst = ENUM_GENERATEKEYS_ALGORITHM.AES;
			return enumConst;
		}

		public static String[] values = new String[]{ "RSA", "AES" };

	}
	protected NodeProperty[] getNodeProperties() {
		return new NodeProperty[] {
			new NodeProperty(GenerateKeysNodeUDN.PROPERTY_ALGORITHM,		NodeProperty.Usage.OPTIONAL,	true,	NodeProperty.Type.ENUMERATION, "RSA", ENUM_GENERATEKEYS_ALGORITHM.class,"","",	"com/ibm/broker/crytpo/GenerateKeys",	"Cryptography"),
			new NodeProperty(GenerateKeysNodeUDN.PROPERTY_PUBLICKEYPATH,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/crytpo/GenerateKeys",	"Cryptography"),
			new NodeProperty(GenerateKeysNodeUDN.PROPERTY_PRIVATEKEYPATH,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/crytpo/GenerateKeys",	"Cryptography"),
			new NodeProperty(GenerateKeysNodeUDN.PROPERTY_KEYIDENTITY,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/crytpo/GenerateKeys",	"Cryptography"),
			new NodeProperty(GenerateKeysNodeUDN.PROPERTY_KEYPASSPHRASE,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/crytpo/GenerateKeys",	"Cryptography"),
			new NodeProperty(GenerateKeysNodeUDN.PROPERTY_ISPASSPRAHSEENCODED,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.BOOLEAN, "false","","",	"com/ibm/broker/crytpo/GenerateKeys",	"Cryptography"),
			new NodeProperty(GenerateKeysNodeUDN.PROPERTY_KEYSIZE,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.INTEGER, "0","","",	"com/ibm/broker/crytpo/GenerateKeys",	"Cryptography")
		};
	}

	public GenerateKeysNodeUDN() {
	}

	public final InputTerminal INPUT_TERMINAL_IN = new InputTerminal(this,"InTerminal.in");
	@Override
	public InputTerminal[] getInputTerminals() {
		return new InputTerminal[] {
			INPUT_TERMINAL_IN
	};
	}

	@Override
	public OutputTerminal[] getOutputTerminals() {
		return null;
	}

	@Override
	public String getTypeName() {
		return NODE_TYPE_NAME;
	}

	protected String getGraphic16() {
		return NODE_GRAPHIC_16;
	}

	protected String getGraphic32() {
		return NODE_GRAPHIC_32;
	}

	/**
	 * Set the <I>GenerateKeysNodeUDN</I> "<I>algorithm</I>" property
	 * 
	 * @param value ENUM_GENERATEKEYS_ALGORITHM ; the value to set the property "<I>algorithm</I>"
	 */
	public GenerateKeysNodeUDN setAlgorithm(ENUM_GENERATEKEYS_ALGORITHM value) {
		setProperty(GenerateKeysNodeUDN.PROPERTY_ALGORITHM, value.toString());
		return this;
	}

	/**
	 * Get the <I>GenerateKeysNodeUDN</I> "<I>algorithm</I>" property
	 * 
	 * @return ENUM_GENERATEKEYS_ALGORITHM; the value of the property "<I>algorithm</I>"
	 */
	public ENUM_GENERATEKEYS_ALGORITHM getAlgorithm() {
		ENUM_GENERATEKEYS_ALGORITHM value = ENUM_GENERATEKEYS_ALGORITHM.getEnumFromString((String)getPropertyValue(GenerateKeysNodeUDN.PROPERTY_ALGORITHM));
		return value;
	}

	/**
	 * Set the <I>GenerateKeysNodeUDN</I> "<I>publicKeyPath</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>publicKeyPath</I>"
	 */
	public GenerateKeysNodeUDN setPublicKeyPath(String value) {
		setProperty(GenerateKeysNodeUDN.PROPERTY_PUBLICKEYPATH, value);
		return this;
	}

	/**
	 * Get the <I>GenerateKeysNodeUDN</I> "<I>publicKeyPath</I>" property
	 * 
	 * @return String; the value of the property "<I>publicKeyPath</I>"
	 */
	public String getPublicKeyPath() {
		return (String)getPropertyValue(GenerateKeysNodeUDN.PROPERTY_PUBLICKEYPATH);
	}

	/**
	 * Set the <I>GenerateKeysNodeUDN</I> "<I>privateKeyPath</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>privateKeyPath</I>"
	 */
	public GenerateKeysNodeUDN setPrivateKeyPath(String value) {
		setProperty(GenerateKeysNodeUDN.PROPERTY_PRIVATEKEYPATH, value);
		return this;
	}

	/**
	 * Get the <I>GenerateKeysNodeUDN</I> "<I>privateKeyPath</I>" property
	 * 
	 * @return String; the value of the property "<I>privateKeyPath</I>"
	 */
	public String getPrivateKeyPath() {
		return (String)getPropertyValue(GenerateKeysNodeUDN.PROPERTY_PRIVATEKEYPATH);
	}

	/**
	 * Set the <I>GenerateKeysNodeUDN</I> "<I>keyIdentity</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>keyIdentity</I>"
	 */
	public GenerateKeysNodeUDN setKeyIdentity(String value) {
		setProperty(GenerateKeysNodeUDN.PROPERTY_KEYIDENTITY, value);
		return this;
	}

	/**
	 * Get the <I>GenerateKeysNodeUDN</I> "<I>keyIdentity</I>" property
	 * 
	 * @return String; the value of the property "<I>keyIdentity</I>"
	 */
	public String getKeyIdentity() {
		return (String)getPropertyValue(GenerateKeysNodeUDN.PROPERTY_KEYIDENTITY);
	}

	/**
	 * Set the <I>GenerateKeysNodeUDN</I> "<I>keyPassphrase</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>keyPassphrase</I>"
	 */
	public GenerateKeysNodeUDN setKeyPassphrase(String value) {
		setProperty(GenerateKeysNodeUDN.PROPERTY_KEYPASSPHRASE, value);
		return this;
	}

	/**
	 * Get the <I>GenerateKeysNodeUDN</I> "<I>keyPassphrase</I>" property
	 * 
	 * @return String; the value of the property "<I>keyPassphrase</I>"
	 */
	public String getKeyPassphrase() {
		return (String)getPropertyValue(GenerateKeysNodeUDN.PROPERTY_KEYPASSPHRASE);
	}

	/**
	 * Set the <I>GenerateKeysNodeUDN</I> "<I>isPassprahseEncoded</I>" property
	 * 
	 * @param value boolean ; the value to set the property "<I>isPassprahseEncoded</I>"
	 */
	public GenerateKeysNodeUDN setIsPassprahseEncoded(boolean value) {
		setProperty(GenerateKeysNodeUDN.PROPERTY_ISPASSPRAHSEENCODED, String.valueOf(value));
		return this;
	}

	/**
	 * Get the <I>GenerateKeysNodeUDN</I> "<I>isPassprahseEncoded</I>" property
	 * 
	 * @return boolean; the value of the property "<I>isPassprahseEncoded</I>"
	 */
	public boolean getIsPassprahseEncoded(){
	if (getPropertyValue(GenerateKeysNodeUDN.PROPERTY_ISPASSPRAHSEENCODED).equals("true")){
		return true;
	} else {
		return false;
		}
	}

	/**
	 * Set the <I>GenerateKeysNodeUDN</I> "<I>keySize</I>" property
	 * 
	 * @param value int ; the value to set the property "<I>keySize</I>"
	 */
	public GenerateKeysNodeUDN setKeySize(int value) {
		setProperty(GenerateKeysNodeUDN.PROPERTY_KEYSIZE, Integer.toString(value));
		return this;
	}

	/**
	 * Get the <I>GenerateKeysNodeUDN</I> <I>keySize</I> property
	 * 
	 * @return int; the value of the property "<I>keySize</I>"
	 */
	public int getKeySize() {
		String value = (String)getPropertyValue(GenerateKeysNodeUDN.PROPERTY_KEYSIZE);
		return Integer.valueOf(value).intValue();
	}

	public String getNodeName() {
		String retVal = super.getNodeName();
		if ((retVal==null) || retVal.equals(""))
			retVal = "GenerateKeys";
		return retVal;
	};
}
