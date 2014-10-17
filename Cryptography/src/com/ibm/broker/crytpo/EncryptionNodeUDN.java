package com.ibm.broker.crytpo;

import com.ibm.broker.config.appdev.InputTerminal;
import com.ibm.broker.config.appdev.Node;
import com.ibm.broker.config.appdev.NodeProperty;
import com.ibm.broker.config.appdev.OutputTerminal;

/*** 
 * <p>  <I>EncryptionNodeUDN</I> instance</p>
 * <p></p>
 */
public class EncryptionNodeUDN extends Node {

	private static final long serialVersionUID = 1L;

	// Node constants
	protected final static String NODE_TYPE_NAME = "com/ibm/broker/crytpo/EncryptionNode";
	protected final static String NODE_GRAPHIC_16 = "platform:/plugin/Cryptography/icons/full/obj16/com/ibm/broker/crytpo/Encryption.gif";
	protected final static String NODE_GRAPHIC_32 = "platform:/plugin/Cryptography/icons/full/obj30/com/ibm/broker/crytpo/Encryption.gif";

	protected final static String PROPERTY_ALGORITHM = "algorithm";
	protected final static String PROPERTY_PUBLICKEY = "publicKey";


	/**
	 * <I>ENUM_ENCRYPTION_ALGORITHM</I>
	 * <pre>
	 * ENUM_ENCRYPTION_ALGORITHM.RSA = RSA
	 * ENUM_ENCRYPTION_ALGORITHM.PEB = PEB
	 * </pre>
	 */
	public static class ENUM_ENCRYPTION_ALGORITHM {
		private String value;

		public static final ENUM_ENCRYPTION_ALGORITHM RSA = new ENUM_ENCRYPTION_ALGORITHM("RSA");
		public static final ENUM_ENCRYPTION_ALGORITHM PEB = new ENUM_ENCRYPTION_ALGORITHM("PEB");

		protected ENUM_ENCRYPTION_ALGORITHM(String value) {
			this.value = value;
		}
		public String toString() {
			return value;
		}

		protected static ENUM_ENCRYPTION_ALGORITHM getEnumFromString(String enumValue) {
			ENUM_ENCRYPTION_ALGORITHM enumConst = ENUM_ENCRYPTION_ALGORITHM.RSA;
			if (ENUM_ENCRYPTION_ALGORITHM.PEB.value.equals(enumValue)) enumConst = ENUM_ENCRYPTION_ALGORITHM.PEB;
			return enumConst;
		}

		public static String[] values = new String[]{ "RSA", "PEB" };

	}
	protected NodeProperty[] getNodeProperties() {
		return new NodeProperty[] {
			new NodeProperty(EncryptionNodeUDN.PROPERTY_ALGORITHM,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.ENUMERATION, "RSA", ENUM_ENCRYPTION_ALGORITHM.class,"","",	"com/ibm/broker/crytpo/Encryption",	"Cryptography"),
			new NodeProperty(EncryptionNodeUDN.PROPERTY_PUBLICKEY,		NodeProperty.Usage.OPTIONAL,	false,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/crytpo/Encryption",	"Cryptography")
		};
	}

	public EncryptionNodeUDN() {
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
	 * Set the <I>EncryptionNodeUDN</I> "<I>algorithm</I>" property
	 * 
	 * @param value ENUM_ENCRYPTION_ALGORITHM ; the value to set the property "<I>algorithm</I>"
	 */
	public EncryptionNodeUDN setAlgorithm(ENUM_ENCRYPTION_ALGORITHM value) {
		setProperty(EncryptionNodeUDN.PROPERTY_ALGORITHM, value.toString());
		return this;
	}

	/**
	 * Get the <I>EncryptionNodeUDN</I> "<I>algorithm</I>" property
	 * 
	 * @return ENUM_ENCRYPTION_ALGORITHM; the value of the property "<I>algorithm</I>"
	 */
	public ENUM_ENCRYPTION_ALGORITHM getAlgorithm() {
		ENUM_ENCRYPTION_ALGORITHM value = ENUM_ENCRYPTION_ALGORITHM.getEnumFromString((String)getPropertyValue(EncryptionNodeUDN.PROPERTY_ALGORITHM));
		return value;
	}

	/**
	 * Set the <I>EncryptionNodeUDN</I> "<I>publicKey</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>publicKey</I>"
	 */
	public EncryptionNodeUDN setPublicKey(String value) {
		setProperty(EncryptionNodeUDN.PROPERTY_PUBLICKEY, value);
		return this;
	}

	/**
	 * Get the <I>EncryptionNodeUDN</I> "<I>publicKey</I>" property
	 * 
	 * @return String; the value of the property "<I>publicKey</I>"
	 */
	public String getPublicKey() {
		return (String)getPropertyValue(EncryptionNodeUDN.PROPERTY_PUBLICKEY);
	}

	public String getNodeName() {
		String retVal = super.getNodeName();
		if ((retVal==null) || retVal.equals(""))
			retVal = "Encryption";
		return retVal;
	};
}
