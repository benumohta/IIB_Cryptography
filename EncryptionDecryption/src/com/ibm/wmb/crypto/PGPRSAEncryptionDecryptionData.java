package com.ibm.wmb.crypto;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.util.io.Streams;

import com.ibm.wmb.crypto.exceptions.CipherException;
import com.ibm.wmb.crypto.exceptions.KeyNotFoundException;
import com.ibm.wmb.crypto.utils.ConfigProperties;
import com.ibm.wmb.crypto.utils.KeysUtilities;
import com.ibm.wmb.crypto.utils.ReadRSAKeys;

public class PGPRSAEncryptionDecryptionData implements PGPRSAEncrptionDecryptionIntf{
	
	private PGPObjectFactory 			pgpObjFactory,pgpObjFactoryDec,pgpObjFactoryCompressData;
	private PGPEncryptedDataList		pgpEncDataList;
	private PGPPublicKeyEncryptedData 	pgpEncData;
	private Object 						obj,encrptedMessage;	
	private InputStream					byteInputStream,clear,privateKeyFIS,publicKeyFIS;	
	private PGPSecretKeyRingCollection  pgpSecretKeyRingCollection;
	private PGPPublicKeyRingCollection  pgpPublickKeyRingCollection;
	private	PGPPrivateKey 				rsaPriavteKey;
	private PGPCompressedData			pgpCompressedData;
	private PGPLiteralData				pgpLiteralData;
	private byte[] 						decryptedData;
	private String 						password;
	private KeysUtilities				keyUtils;
	private PGPPublicKey				rsaPublickKey;
	
		
	@Override
	public byte[] decryptMessage(String encryptedText,
								 String secretKeyFilePath,
								 String secretKeyPassphrase,
								 boolean isPassphraseEncoded)
			throws KeyNotFoundException, IOException, CipherException, PGPException, NoSuchProviderException 
	{
		//read and decode the encrypted Byte Array
		byteInputStream = new ByteArrayInputStream(encryptedText.getBytes());
		byteInputStream = PGPUtil.getDecoderStream(byteInputStream);	
		
		//Create a PGP Object to get Encrypted Data objects
		pgpObjFactory 				= new PGPObjectFactory(byteInputStream);		
		obj 						= pgpObjFactory.nextObject();
		
		// the first object might be a PGP marker packet.
        if (obj instanceof PGPEncryptedDataList)
        {
        	pgpEncDataList = (PGPEncryptedDataList)obj;
        }
        else
        {
        	pgpEncDataList = (PGPEncryptedDataList)pgpObjFactory.nextObject();
        }
        
        
        
        //get private Key from the Config File;
        privateKeyFIS = new BufferedInputStream(new FileInputStream(secretKeyFilePath));
        Iterator<?> itr = pgpEncDataList.getEncryptedDataObjects();
        pgpSecretKeyRingCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(privateKeyFIS));
        
        //matching is the key correct to decrypt the data.
        while (rsaPriavteKey == null && itr.hasNext())
        {
        	pgpEncData = (PGPPublicKeyEncryptedData)itr.next();
            
        	if(isPassphraseEncoded)
        	{
        		keyUtils = new  KeysUtilities();
        		password = new String(keyUtils.decode(secretKeyPassphrase));
        	}
        	else
        		password=secretKeyPassphrase;
        	
        	rsaPriavteKey = ReadRSAKeys.findSecretKey(pgpSecretKeyRingCollection, pgpEncData.getKeyID(), password.toCharArray());
        }
        
        if (rsaPriavteKey == null)
        {
            throw new KeyNotFoundException("secret key for message not found.");
        }
        
        clear 				= pgpEncData.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(rsaPriavteKey));       
        pgpObjFactoryDec 	= new PGPObjectFactory(clear);
        encrptedMessage 	= pgpObjFactoryDec.nextObject();
        
        if (encrptedMessage instanceof PGPCompressedData)
        {
        	pgpCompressedData 			= (PGPCompressedData)encrptedMessage;
        	pgpObjFactoryCompressData   = new PGPObjectFactory(pgpCompressedData.getDataStream());            
        	encrptedMessage = pgpObjFactoryCompressData.nextObject();
        }
        if (encrptedMessage instanceof PGPLiteralData)
        {
        	pgpLiteralData = (PGPLiteralData)encrptedMessage;
        	decryptedData = Streams.readAll(pgpLiteralData.getInputStream());
        }
        else if (encrptedMessage instanceof PGPOnePassSignatureList)
        {
            throw new PGPException("encrypted message contains a signed message - not literal data.");
        }
        else
        {
            throw new PGPException("message is not a simple encrypted file - type unknown.");
        }
        
        
		return decryptedData;
		
	}

	@Override	
	public String encryptMessage(byte[] dataToEncrypt, String pgpPublicKey) throws KeyNotFoundException, IOException,CipherException, Exception
	{
		// TODO Auto-generated method stub
		Security.addProvider(new BouncyCastleProvider());
		
		//get private Key from the Config File;
        publicKeyFIS = new BufferedInputStream(new FileInputStream(pgpPublicKey));        
        pgpPublickKeyRingCollection = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(publicKeyFIS));
        Iterator<?> rIt = pgpPublickKeyRingCollection.getKeyRings();
        boolean loop = true;
        while (rIt.hasNext() && loop) 
        {
            PGPPublicKeyRing kRing = (PGPPublicKeyRing) rIt.next();
            Iterator<?> kIt = kRing.getPublicKeys();

            while (kIt.hasNext())
            {
            	rsaPublickKey = (PGPPublicKey) kIt.next();

                if (rsaPublickKey.isEncryptionKey())
                {   
                    loop = false;
                    break;
                }
            }
        
        }
        
        if(loop) 
        {
        	throw new IllegalArgumentException("Can't find encryption key in key ring.");
        }
            
       
        //Compressing data before encryption.
        ByteArrayOutputStream bOutCompress = new ByteArrayOutputStream();
        PGPCompressedDataGenerator compressDataGen = new PGPCompressedDataGenerator(PGPEncryptedDataGenerator.CAST5);
        OutputStream osCompress = compressDataGen.open(bOutCompress); // open it with the final destination
        PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
        // we want to generate compressed data. This might be a user option later,
        // in which case we would pass in bOut.
        OutputStream  pOut = lData.open(osCompress, // the compressed output stream
                                        PGPLiteralData.BINARY,
                                        PGPLiteralData.CONSOLE,  // "filename" to store
                                        dataToEncrypt.length, // length of clear data
                                        new Date()  // current time
                                      );

        pOut.write(dataToEncrypt);
        pOut.close();
        compressDataGen.close();
        
        
        //encrypting data
        byte[] compressedData = bOutCompress.toByteArray();
        ByteArrayOutputStream bosEncryptedData = new ByteArrayOutputStream();
        OutputStream osEncryptedData = bosEncryptedData;
        PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(new JcePGPDataEncryptorBuilder(PGPEncryptedDataGenerator.CAST5).setSecureRandom(new SecureRandom()).setProvider(ConfigProperties.SECURITY_PROVIDER));
        encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(rsaPublickKey).setProvider(ConfigProperties.SECURITY_PROVIDER));
        OutputStream encOut = encGen.open(osEncryptedData, compressedData.length);
        encOut.write(compressedData);
        encOut.close();
        osEncryptedData.close();
        keyUtils = new KeysUtilities();
       
		return  keyUtils.encode(bosEncryptedData.toByteArray());
        
	}
}

