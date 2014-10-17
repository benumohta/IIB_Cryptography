package com.ibm.wmb.crypto.test;

import java.io.*;

import com.ibm.wmb.crypto.BasicRSAEncryptDecryptData;
import com.ibm.wmb.crypto.utils.KeysUtilities;

public class TestEncryptionDecryption {
@SuppressWarnings("unused")
public static void main(String[] args){
		
		// TODO Auto-generated method stub
	try {
		KeysUtilities ku = new KeysUtilities();
		
		String encyrptedData="";
		
		String dataToEncrypt = ku.getKey("C:\\Project\\EBAI_MarketLive\\EncryptionDecryption\\TestFile\\Test.xml");
		
		BasicRSAEncryptDecryptData ed = new BasicRSAEncryptDecryptData();		
		//encyrptedData=ed.encryptMessage(dataToEncrypt.getBytes());		
		System.out.println(encyrptedData);		
		FileOutputStream fos = new FileOutputStream("C:\\Project\\EBAI_MarketLive\\EncryptionDecryption\\TestFile\\EncryptedFile");
		fos.write(encyrptedData.getBytes());
		fos.flush();
		fos.close();
		
		
		String decryptedText = null;		
		try {
			//decryptedText = new String(ed.decryptMessage(encyrptedData));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(decryptedText);
		
		}
	catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
	}
		
	}
	

}
