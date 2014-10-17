package com.ibm.broker.crytpo.runtime;

import java.io.IOException;

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
import com.ibm.wmb.crypto.exceptions.KeyNotFoundException;

public class EncryptionImpl extends MbNode implements MbNodeInterface {
	
	private byte[] dataToBeEncrypted;
	private String encryptedMessage;
	PGPRSAEncryptionDecryptionData encyrptData = null;
	
	
	private static String LOCAL_ENV_CRYPTO_TREE_NAME = "Cryptography";
	private static String LOCAL_ENV_ENCRYPTION_TREE_NAME = "Encryption";
	String publicKey;
	String algorithm = "RSA";	
	
	 

	public byte[] getDataToBeEncrypted() {
		return dataToBeEncrypted;
	}

	public void setDataToBeEncrypted(byte[] dataToBeEncrypted) {
		this.dataToBeEncrypted = dataToBeEncrypted;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getPublicKey() {
		return this.publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public EncryptionImpl() throws MbException {
		// TODO Auto-generated constructor stub
		// create terminals here
		createInputTerminal ("in");
		createOutputTerminal ("out");
		createOutputTerminal ("failure");
	}

	@SuppressWarnings("unused")
	@Override
	public void evaluate(MbMessageAssembly inAssembly, MbInputTerminal arg1)
			throws MbException {
		
		MbMessage inMessage = inAssembly.getMessage();
		
		if (!this.algorithm.equalsIgnoreCase("RSA"))
		{
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"Only RSA Alogrithm is support as of now",
					"CRITICAL", null );
			
		}
		
		// create new message
		boolean isLocalEnvPopulated	= false;
		MbMessage outMessage 			= new MbMessage();
		MbMessageAssembly outAssembly 	= new MbMessageAssembly(inAssembly, outMessage);
		
		MbMessage inputLocalEnv 		= inAssembly.getLocalEnvironment();
		MbMessage outputLocalEnv 		= outAssembly.getLocalEnvironment();
		outputLocalEnv					= inputLocalEnv;
		
		try {
				MbElement inputEncryptionTree 	= inputLocalEnv.getRootElement().getFirstElementByPath(LOCAL_ENV_CRYPTO_TREE_NAME)
					.getFirstElementByPath(LOCAL_ENV_ENCRYPTION_TREE_NAME);
				isLocalEnvPopulated = true;
			
		} catch (Exception e) {
			// TODO: handle exception
			isLocalEnvPopulated = false;
		}
		
		
		
		
		try {	
			//if local env has tree 
			if (isLocalEnvPopulated)
			{
				copyEntireMessage(inMessage, outMessage);
				outputLocalEnv= inputLocalEnv;
				
				MbElement outputEncryptionTree = outputLocalEnv.getRootElement().getFirstElementByPath(LOCAL_ENV_CRYPTO_TREE_NAME)
						.getFirstElementByPath(LOCAL_ENV_ENCRYPTION_TREE_NAME).getFirstChild();
				
				
				while (outputEncryptionTree != null) 
				{
					dataToBeEncrypted = outputEncryptionTree.getValueAsString().getBytes();
					
					if (encyrptData == null)
							encyrptData = new PGPRSAEncryptionDecryptionData();
					
					encryptedMessage = encyrptData.encryptMessage(dataToBeEncrypted,getPublicKey());
					outputEncryptionTree.setValue(encryptedMessage);
					outputEncryptionTree = outputEncryptionTree.getNextSibling();
				}
				
				MbOutputTerminal out = getOutputTerminal("out");
				out.propagate(outAssembly);
				
			}
				
			else
			{
				// optionally copy message headers
				copyMessageHeaders(inMessage, outMessage);
				
				//getting the parser
				MbElement inputRoot = inMessage.getRootElement();
				MbElement inputParser = inputRoot.getLastChild();
				
				//extracting payload
				dataToBeEncrypted = inputParser.toBitstream(null, null, null, 0, 0, 0);	
				//encrypting
				encyrptData = new PGPRSAEncryptionDecryptionData();
				encryptedMessage = encyrptData.encryptMessage(dataToBeEncrypted,getPublicKey());
				
				//setting 
				MbElement outRootParser = outMessage.getRootElement().createElementAsLastChild(MbBLOB.PARSER_NAME);
				MbElement outBody 		= outRootParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"BLOB",encryptedMessage.getBytes());
			
	
				// The following should only be changed
				// if not propagating message to the 'out' terminal
				MbOutputTerminal out = getOutputTerminal("out");
				out.propagate(outAssembly);
			}
		

		}  catch (KeyNotFoundException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Encrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (IOException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Encrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (Exception e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Encrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		}
		finally {

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
	   return "EncryptionNode";
	}

}
