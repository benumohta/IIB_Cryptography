package com.ibm.wmb.crypto.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

import com.ibm.wmb.crypto.exceptions.KeyGenerationException;


public class GenerateEncrptionKeys
{
	public static void main(String[] agrs)
	{
		
			try {
				GenerateEncrptionKeys genKey = new GenerateEncrptionKeys();	
				genKey.generatePGPRSAKeys("RSA",
										  "C:\\BenuWorkspace\\Crypto\\PGPRSAKey\\PGPPublicKey.asc",
								          "C:\\BenuWorkspace\\Crypto\\PGPRSAKey\\PGPSecretKey.asc",
								          "benmohta@in.ibm.com",
								          "password",
								          false,
								          1024);
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| NoSuchProviderException | SignatureException
					| IOException | PGPException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		
		
	}
	
	private  KeyPairGenerator generator;
	private  KeyPair keyPair;
	private  PrivateKey privateKey;
	private  PublicKey  publicKey;	
	private static String RANDOM_TEXT1 = "SHA1PRNG";
	private static String RANDOM_TEXT2 = "IBMJCE";
	private static String SECURITY_PROVIDER = "BC";
	
  
	public String[] generateBasicRSAKeys (int keySize) throws KeyGenerationException 
	{
		try
		{
			//using Bouncy Castle as provider
			String[] keyArray = new String[2];
        	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
 
            // Create the public and private keys
        	//generator = KeyPairGenerator.getInstance(ConfigProperties.ALGORITHM_TEXT, ConfigProperties.SECURITY_PROVIDER);
        	generator = KeyPairGenerator.getInstance("RSA", "BC");
            
            //Generate Keys with Randomness	
            //SecureRandom random = SecureRandom.getInstance(ConfigProperties.RANDOM_TEXT1, ConfigProperties.RANDOM_TEXT2);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "IBMJCE");
            
            generator.initialize(keySize, random);
 
            keyPair = generator.generateKeyPair();           
            
            KeysUtilities keyUtils = new KeysUtilities();
            
            //Extracting and Saving Private Key            
            privateKey = keyPair.getPrivate();             
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());	
            keyArray[0] = keyUtils.encode(privateKeySpec.getEncoded());            
            //Extracting and Saving Public Key            
            publicKey =  keyPair.getPublic();
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            keyArray[1] =keyUtils.encode(publicKeySpec.getEncoded());            
            return keyArray;
            
		}
        catch (NoSuchAlgorithmException nsae)
        {
        	throw new KeyGenerationException(nsae.getMessage());
        }
        catch(NoSuchProviderException nspe)
        {
        	throw new KeyGenerationException(nspe.getMessage());
        } 
        
    }
 
	@SuppressWarnings("deprecation")
	public  void generatePGPRSAKeys (String algorithmText,
									 String publicKeyFileNameWithPath,
									 String secretKeyFileNameWithPath,
									 String secretKeyIndentity,
									 String secretKeyPassphrase,
									 boolean isSecretKeyBase64Encoded,
									 int keySize									 
									 ) throws NoSuchAlgorithmException, NoSuchProviderException,IOException, InvalidKeyException,SignatureException, PGPException
	{
		//using Bouncy Castle as provider
    	String  passphrase = null;
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    	
    	if (isSecretKeyBase64Encoded) 
    	{
    		KeysUtilities keyUtils 	= new KeysUtilities();
    		passphrase		= new String(keyUtils.decode(secretKeyPassphrase));
    	}
    	else
    		passphrase = secretKeyPassphrase;
    	
        // Create the public and private keys
    	generator = KeyPairGenerator.getInstance(algorithmText, SECURITY_PROVIDER); 
    	
        //Generate Keys with Randomness	
        SecureRandom random = SecureRandom.getInstance(RANDOM_TEXT1, RANDOM_TEXT2);
        generator.initialize(keySize, random);

        keyPair = generator.generateKeyPair();        
        publicKey 	= keyPair.getPublic();
        privateKey	= keyPair.getPrivate();
        
        FileOutputStream fosPublicKey 	= new FileOutputStream(publicKeyFileNameWithPath);
        OutputStream 	 fosPrivateKey 	= new FileOutputStream(secretKeyFileNameWithPath);
        
        fosPrivateKey = (OutputStream)new ArmoredOutputStream(fosPrivateKey);
        
        PGPDigestCalculator sha1Calc 	= new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1);
        PGPKeyPair          keyPair 	= new PGPKeyPair(PGPPublicKey.RSA_GENERAL, publicKey, privateKey, new Date());
        PGPSecretKey        secretKey 	= new PGPSecretKey(PGPSignature.DEFAULT_CERTIFICATION, 
        												   keyPair,
        												   secretKeyIndentity,
        												   sha1Calc,
        												   null,
        												   null,
        												   new JcaPGPContentSignerBuilder(keyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1), 
        												   new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.CAST5, sha1Calc).setProvider("BC").build(passphrase.toCharArray()));
        
        secretKey.encode(fosPrivateKey);
        fosPrivateKey.close();        
        PGPPublicKey  rsaPublicKey = secretKey.getPublicKey();        
        rsaPublicKey.encode(fosPublicKey);        
        fosPublicKey.close();        
        
	}	
}
   