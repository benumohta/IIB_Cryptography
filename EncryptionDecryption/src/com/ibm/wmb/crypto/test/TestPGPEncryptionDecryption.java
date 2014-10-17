package com.ibm.wmb.crypto.test;

import com.ibm.wmb.crypto.PGPRSAEncryptionDecryptionData;
import com.ibm.wmb.crypto.utils.KeysUtilities;
@SuppressWarnings("unused")
public class TestPGPEncryptionDecryption {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			KeysUtilities ku = new KeysUtilities();
			
			String encrytpText = ku.getKey("C:\\Project\\EBAI_MarketLive_trunk\\EncryptionDecryption\\TestFile\\Test.xml");
			PGPRSAEncryptionDecryptionData encryption = new PGPRSAEncryptionDecryptionData();
			PGPRSAEncryptionDecryptionData decryption = new PGPRSAEncryptionDecryptionData();
			
			String encryptedText = null;
			String decryptedText = null;
			try {
				//encryptedText =  encryption.encryptMessage(encrytpText.getBytes());
				System.out.println(encryptedText);
				//decryptedText = new String(decryption.decryptMessage(encryptedText));
				System.out.println(decryptedText);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
			}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

}
