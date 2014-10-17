package com.ibm.wmb.crypto.utils;

import java.io.InputStream;
import java.util.Properties;


public class ConfigProperties {

	// Configuration Property File Name
	private static final String CONFIG_PROPERTY_FILE = "/properties/Crypto.properties";
	private static final String ERR_CONFIG_READ_ERROR = "Error occured reading configurations";
	public static  final String ERR_NO_KEY_READ = "No Keys Read from Key File Specificed";

	// Configuration Properties Name for LDAP
	private static final String RSA_PUBLIC_KEY_FILEPATH_PROP 		= "RSA_PUBLIC_KEY_FILEPATH";
	private static final String RSA_PRIVATE_KEY_FILEPATH_PROP  		= "RSA_PRIVATE_KEY_FILEPATH";
	private static final String ALGORITHM_TEXT_PROP		 			= "ALGORITHM_TEXT_PROP";
	private static final String SECURITY_PROVIDER_PROP				= "SECURITY_PROVIDER_PROP"; 
	private static final String KEY_SIZE_PROP		 				= "KEY_SIZE_PROP";
	private static final String RSA_IDENTITY_PROP					= "RSA_IDENTITY";
	private static final String RSA_PASSPHRASE_PROP					= "RSA_PASSPHRASE";
	private static final String RANDOM_TEXT1_PROP					= "RANDOM_TEXT1";
	private static final String RANDOM_TEXT2_PROP					= "RANDOM_TEXT2";
	
	// Declare Static Variables for KeyPath
	public static String RSA_PUBLIC_KEY_FILEPATH;
	public static String RSA_PRIVATE_KEY_FILEPATH;
	public static String RSA_PASSWORD;
	public static String ALGORITHM_TEXT;
	public static int KEY_SIZE;
	public static String RSA_IDENTITY;
	public static String SECURITY_PROVIDER;
	public static String RANDOM_TEXT1;
	public static String RANDOM_TEXT2;
	
	
	// static initializer block
	static { 
		try {
			loadProperties();
			} catch (Exception e) {			
			throw new RuntimeException(ERR_CONFIG_READ_ERROR);
		}
	}

	private static void loadProperties() throws Exception {

		
		Properties properties = new Properties();
		InputStream propStream = ConfigProperties.class.getResourceAsStream(CONFIG_PROPERTY_FILE);
		properties.load(propStream);
		propStream.close();

		// Read the properties from properties file and initialize variables
		RSA_PUBLIC_KEY_FILEPATH 		= properties.getProperty(RSA_PUBLIC_KEY_FILEPATH_PROP);
		RSA_PRIVATE_KEY_FILEPATH 		= properties.getProperty(RSA_PRIVATE_KEY_FILEPATH_PROP);
		ALGORITHM_TEXT					= properties.getProperty(ALGORITHM_TEXT_PROP);
		KEY_SIZE						= Integer.parseInt(properties.getProperty(KEY_SIZE_PROP));
		RSA_PASSWORD					= properties.getProperty(RSA_PASSPHRASE_PROP);
		RSA_IDENTITY					= properties.getProperty(RSA_IDENTITY_PROP);
		SECURITY_PROVIDER				= properties.getProperty(SECURITY_PROVIDER_PROP);
		RANDOM_TEXT1					= properties.getProperty(RANDOM_TEXT1_PROP);
		RANDOM_TEXT2					= properties.getProperty(RANDOM_TEXT2_PROP);
		
		
	}
}
