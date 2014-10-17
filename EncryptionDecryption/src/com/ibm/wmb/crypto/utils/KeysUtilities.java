package com.ibm.wmb.crypto.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class KeysUtilities {

	/**
	 * @param args
	 */	
	private String key;
	private BASE64Encoder base64Encoder;
	private BASE64Decoder base64Decoder;	
	
	public KeysUtilities()
	{
		super();		 
	}
	
	//basic base 64 encode
	public String encode(byte[] byteToEncode)
	{
		base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(byteToEncode);
	}
	
	//basic base 64 decode
	public byte[] decode(String stringToDecode) throws IOException
	{
		base64Decoder = new BASE64Decoder();
		return base64Decoder.decodeBuffer(stringToDecode);
	}
	
	
	//This function reads key from a File
	public String getKey(String path) throws IOException 
	{
		// Read Key.		
		File filePrivateKey = new File(path);
		FileInputStream fis = new FileInputStream(path);
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();		
		key = new String(encodedPrivateKey);		
		return key;
		
	}
	
	public String getHexString(byte[] b) throws Exception
	{
       String result = "";
       for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
	}
	
	public byte[] hexStringToByteArray(String s)
	{
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4+ Character.digit(s.charAt(i+1), 16)));
		}
		
		return data;
		
	}
	
	public byte[] compressFile(String fileName, int algorithm) throws IOException
    {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(algorithm);
        PGPUtil.writeFileToLiteralData(comData.open(bOut), PGPLiteralData.BINARY,
            new File(fileName));
        comData.close();
        return bOut.toByteArray();
    }
 
}
