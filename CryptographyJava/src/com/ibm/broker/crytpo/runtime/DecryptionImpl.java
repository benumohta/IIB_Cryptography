package com.ibm.broker.crytpo.runtime;

import java.io.IOException;
import java.security.NoSuchProviderException;

import org.bouncycastle.openpgp.PGPException;

import com.ibm.broker.plugin.MbBLOB;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbInputTerminal;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbNode;
import com.ibm.broker.plugin.MbNodeInterface;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import com.ibm.wmb.crypto.PGPRSAEncryptionDecryptionData;
import com.ibm.wmb.crypto.exceptions.CipherException;
import com.ibm.wmb.crypto.exceptions.KeyNotFoundException;

public class DecryptionImpl extends MbNode implements MbNodeInterface {
	
	private static String LOCAL_ENV_CRYPTO_TREE_NAME = "Cryptography";
	private static String LOCAL_ENV_DECRYPTION_TREE_NAME = "Decryption";
	private PGPRSAEncryptionDecryptionData decryptData = null;
	
	String keyIdentity;
	String keyPassphrase;
	boolean isPassprahseEncoded = false;
	String secretKeyFile;
	String algorithm = "RSA";
	
	private String encryptedText;
	private byte[] decryptedData;
	
	
	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getKeyIdentity() {
		return keyIdentity;
	}

	public void setKeyIdentity(String keyIdentity) {
		this.keyIdentity = keyIdentity;
	}

	public String getKeyPassphrase() {
		return keyPassphrase;
	}

	public void setKeyPassphrase(String keyPassphrase) {
		this.keyPassphrase = keyPassphrase;
	}

	public String isPassprahseEncoded() {
		return Boolean.toString(isPassprahseEncoded);
	}

	public void setPassprahseEncoded(String isPassprahseEncoded) {
		this.isPassprahseEncoded = Boolean.parseBoolean(isPassprahseEncoded);
	}

	public String getSecretKeyFile() {
		return secretKeyFile;
	}

	public void setSecretKeyFile(String secretKeyFile) {
		this.secretKeyFile = secretKeyFile;
	}

	public DecryptionImpl() throws MbException {
		// TODO Auto-generated constructor stub
		createInputTerminal ("in");
		createOutputTerminal ("out");
		createOutputTerminal ("failure");
	}

	@SuppressWarnings("unused")
	@Override
	public void evaluate(MbMessageAssembly inAssembly, MbInputTerminal arg1)
			throws MbException {
		
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();

		// create new message
		boolean isLocalEnvPopulated = false;
		MbMessage outMessage = new MbMessage();
		MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly,outMessage);
		
		MbMessage inputLocalEnv 		= inAssembly.getLocalEnvironment();
		MbMessage outputLocalEnv 		= outAssembly.getLocalEnvironment();
		outputLocalEnv					= inputLocalEnv;
		
		try {
				MbElement inputEncryptionTree 	= inputLocalEnv.getRootElement().getFirstElementByPath(LOCAL_ENV_CRYPTO_TREE_NAME)
					.getFirstElementByPath(LOCAL_ENV_DECRYPTION_TREE_NAME);
				isLocalEnvPopulated = true;
			
		} catch (Exception e) {
			// TODO: handle exception
			isLocalEnvPopulated = false;
		}

		try {
			
			if (isLocalEnvPopulated)
			{
				copyEntireMessage(inMessage, outMessage);
				outputLocalEnv= inputLocalEnv;
				
				MbElement outputDecryptionTree = outputLocalEnv.getRootElement().getFirstElementByPath(LOCAL_ENV_CRYPTO_TREE_NAME)
						.getFirstElementByPath(LOCAL_ENV_DECRYPTION_TREE_NAME).getFirstChild();
				
				
				while (outputDecryptionTree != null) 
				{
					encryptedText = outputDecryptionTree.getValueAsString();
					decryptData = new PGPRSAEncryptionDecryptionData();					
					decryptedData = decryptData.decryptMessage(encryptedText,this.secretKeyFile,this.keyPassphrase,this.isPassprahseEncoded);
					outputDecryptionTree.setValue(new String(decryptedData));
					outputDecryptionTree = outputDecryptionTree.getNextSibling();
					decryptData = null;
					
				}
				
				out.propagate(outAssembly);
				
			}
			else
			{	
				//copy message headers
				copyMessageHeaders(inMessage, outMessage);
				// get parser
				MbElement inputRoot = inMessage.getRootElement();
				MbElement inputParser = inputRoot.getLastChild();
				
				//get encrypted Text
				encryptedText = new String(inputParser.toBitstream(null, null, null, 0, 0, 0));
				
				//decrypting now
				decryptData = new PGPRSAEncryptionDecryptionData();			
				decryptedData = decryptData.decryptMessage(encryptedText,this.secretKeyFile,this.keyPassphrase,this.isPassprahseEncoded);
				decryptData =  null;
				//creating output parser
				MbElement outRootParser = outMessage.getRootElement().createElementAsLastChild(MbBLOB.PARSER_NAME);			
				MbElement outBody 		= outRootParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"BLOB",decryptedData);			
				out.propagate(outAssembly);
			}

		} catch (CipherException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (NoSuchProviderException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (KeyNotFoundException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (IOException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (PGPException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} finally {

			// clear the outMessage even if there's an exception
			outMessage.clearMessage();
		}

	}
	
	public void copyMessageHeaders(MbMessage inMessage, MbMessage outMessage)
			throws MbException {
		MbElement outRoot = outMessage.getRootElement();

		// iterate though the headers starting with the first child of the root
		// element
		MbElement header = inMessage.getRootElement().getFirstChild();
		while (header != null && header.getNextSibling() != null) // stop before
		// the last
		// child
		// (body)
		{
			// copy the header and add it to the out message
			outRoot.addAsLastChild(header.copy());
			// move along to next header
			header = header.getNextSibling();
		}
	}
	
	public void copyEntireMessage(MbMessage inMessage, MbMessage outMessage)
			throws MbException {
		MbElement outRoot = outMessage.getRootElement();

		// iterate though the headers starting with the first child of the root
		// element
		MbElement header = inMessage.getRootElement().getFirstChild();
		while (header != null) // stop before
		// the last
		// child
		// (body)
		{
			// copy the header and add it to the out message
			outRoot.addAsLastChild(header.copy());
			// move along to next header
			header = header.getNextSibling();
		}
	}
	
	public static String getNodeName()
	{
	   return "DecryptionNode";
	}

}
