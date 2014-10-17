package com.ibm.broker.crytpo;

import com.ibm.broker.config.appdev.InputTerminal;
import com.ibm.broker.config.appdev.Node;
import com.ibm.broker.config.appdev.NodeProperty;
import com.ibm.broker.config.appdev.OutputTerminal;

/*** 
 * <p>  <I>DecryptionNodeUDN</I> instance</p>
 * <p></p>
 */
public class DecryptionNodeUDN extends Node {

	private static final long serialVersionUID = 1L;

	// Node constants
	protected final static String NODE_TYPE_NAME = "com/ibm/broker/crytpo/DecryptionNode";
	protected final static String NODE_GRAPHIC_16 = "platform:/plugin/Cryptography/icons/full/obj16/com/ibm/broker/crytpo/Decryption.gif";
	protected final static String NODE_GRAPHIC_32 = "platform:/plugin/Cryptography/icons/full/obj30/com/ibm/broker/crytpo/Decryption.gif";

	protected final static String PROPERTY_KEYIDENTITY = "keyIdentity";
	protected final static String PROPERTY_KEYPASSPHRASE = "keyPassphrase";
	protected final static String PROPERTY_SECRETKEYFILE = "secretKeyFile";
	protected final static String PROPERTY_ALGORITHM = "algorithm";
	protected final static String PROPERTY_ISPASSPRAHSEENCODED = "isPassprahseEncoded";


	/**
	 * <I>ENUM_DECRYPTION_ALGORITHM</I>
	 * <pre>
	 * ENUM_DECRYPTION_ALGORITHM.RSA = RSA
	 * ENUM_DECRYPTION_ALGORITHM.AES = AES
	 * </pre>
	 */
	public static class ENUM_DECRYPTION_ALGORITHM {
		private String value;

		public static final ENUM_DECRYPTION_ALGORITHM RSA = new ENUM_DECRYPTION_ALGORITHM("RSA");
		public static final ENUM_DECRYPTION_ALGORITHM AES = new ENUM_DECRYPTION_ALGORITHM("AES");

		protected ENUM_DECRYPTION_ALGORITHM(String value) {
			this.value = value;
		}
		public String toString() {
			return value;
		}

		protected static ENUM_DECRYPTION_ALGORITHM getEnumFromString(String enumValue) {
			ENUM_DECRYPTION_ALGORITHM enumConst = ENUM_DECRYPTION_ALGORITHM.RSA;
			if (ENUM_DECRYPTION_ALGORITHM.AES.value.equals(enumValue)) enumConst = ENUM_DECRYPTION_ALGORITHM.AES;
			return enumConst;
		}

		public static String[] values = new String[]{ "RSA", "AES" };

	}
	protected NodeProperty[] getNodeProperties() {
		return new NodeProperty[] {
			new NodeProperty(DecryptionNodeUDN.PROPERTY_KEYIDENTITY,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/crytpo/Decryption",	"Cryptography"),
			new NodeProperty(DecryptionNodeUDN.PROPERTY_KEYPASSPHRASE,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/crytpo/Decryption",	"Cryptography"),
			new NodeProperty(DecryptionNodeUDN.PROPERTY_SECRETKEYFILE,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/crytpo/Decryption",	"Cryptography"),
			new NodeProperty(DecryptionNodeUDN.PROPERTY_ALGORITHM,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.ENUMERATION, "RSA", ENUM_DECRYPTION_ALGORITHM.class,"","",	"com/ibm/broker/crytpo/Decryption",	"Cryptography"),
			new NodeProperty(DecryptionNodeUDN.PROPERTY_ISPASSPRAHSEENCODED,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.BOOLEAN, "false","","",	"com/ibm/broker/crytpo/Decryption",	"Cryptography")
		};
	}

	public DecryptionNodeUDN() {
	}

	public final InputTerminal INPUT_TERMINAL_IN = new InputTerminal(this,"InTerminal.in");
	@Override
	public InputTerminal[] getInputTerminals() {
		return new InputTerminal[] {
			INPUT_TERMINAL_IN
	};
	}

	public final OutputTerminal OUTPUT_TERMINAL_OUT = new OutputTerminal(this,"OutTerminal.out");
	@Override
	public OutputTerminal[] getOutputTerminals() {
		return new OutputTerminal[] {
			OUTPUT_TERMINAL_OUT
		};
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
	 * Set the <I>DecryptionNodeUDN</I> "<I>keyIdentity</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>keyIdentity</I>"
	 */
	public DecryptionNodeUDN setKeyIdentity(String value) {
		setProperty(DecryptionNodeUDN.PROPERTY_KEYIDENTITY, value);
		return this;
	}

	/**
	 * Get the <I>DecryptionNodeUDN</I> "<I>keyIdentity</I>" property
	 * 
	 * @return String; the value of the property "<I>keyIdentity</I>"
	 */
	public String getKeyIdentity() {
		return (String)getPropertyValue(DecryptionNodeUDN.PROPERTY_KEYIDENTITY);
	}

	/**
	 * Set the <I>DecryptionNodeUDN</I> "<I>keyPassphrase</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>keyPassphrase</I>"
	 */
	public DecryptionNodeUDN setKeyPassphrase(String value) {
		setProperty(DecryptionNodeUDN.PROPERTY_KEYPASSPHRASE, value);
		return this;
	}

	/**
	 * Get the <I>DecryptionNodeUDN</I> "<I>keyPassphrase</I>" property
	 * 
	 * @return String; the value of the property "<I>keyPassphrase</I>"
	 */
	public String getKeyPassphrase() {
		return (String)getPropertyValue(DecryptionNodeUDN.PROPERTY_KEYPASSPHRASE);
	}

	/**
	 * Set the <I>DecryptionNodeUDN</I> "<I>secretKeyFile</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>secretKeyFile</I>"
	 */
	public DecryptionNodeUDN setSecretKeyFile(String value) {
		setProperty(DecryptionNodeUDN.PROPERTY_SECRETKEYFILE, value);
		return this;
	}

	/**
	 * Get the <I>DecryptionNodeUDN</I> "<I>secretKeyFile</I>" property
	 * 
	 * @return String; the value of the property "<I>secretKeyFile</I>"
	 */
	public String getSecretKeyFile() {
		return (String)getPropertyValue(DecryptionNodeUDN.PROPERTY_SECRETKEYFILE);
	}

	/**
	 * Set the <I>DecryptionNodeUDN</I> "<I>algorithm</I>" property
	 * 
	 * @param value ENUM_DECRYPTION_ALGORITHM ; the value to set the property "<I>algorithm</I>"
	 */
	public DecryptionNodeUDN setAlgorithm(ENUM_DECRYPTION_ALGORITHM value) {
		setProperty(DecryptionNodeUDN.PROPERTY_ALGORITHM, value.toString());
		return this;
	}

	/**
	 * Get the <I>DecryptionNodeUDN</I> "<I>algorithm</I>" property
	 * 
	 * @return ENUM_DECRYPTION_ALGORITHM; the value of the property "<I>algorithm</I>"
	 */
	public ENUM_DECRYPTION_ALGORITHM getAlgorithm() {
		ENUM_DECRYPTION_ALGORITHM value = ENUM_DECRYPTION_ALGORITHM.getEnumFromString((String)getPropertyValue(DecryptionNodeUDN.PROPERTY_ALGORITHM));
		return value;
	}

	/**
	 * Set the <I>DecryptionNodeUDN</I> "<I>isPassprahseEncoded</I>" property
	 * 
	 * @param value boolean ; the value to set the property "<I>isPassprahseEncoded</I>"
	 */
	public DecryptionNodeUDN setIsPassprahseEncoded(boolean value) {
		setProperty(DecryptionNodeUDN.PROPERTY_ISPASSPRAHSEENCODED, String.valueOf(value));
		return this;
	}

	/**
	 * Get the <I>DecryptionNodeUDN</I> "<I>isPassprahseEncoded</I>" property
	 * 
	 * @return boolean; the value of the property "<I>isPassprahseEncoded</I>"
	 */
	public boolean getIsPassprahseEncoded(){
	if (getPropertyValue(DecryptionNodeUDN.PROPERTY_ISPASSPRAHSEENCODED).equals("true")){
		return true;
	} else {
		return false;
		}
	}

	public String getNodeName() {
		String retVal = super.getNodeName();
		if ((retVal==null) || retVal.equals(""))
			retVal = "Decryption";
		return retVal;
	};
}
