package com.ibm.wmb;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;
import com.ibm.wmb.crypto.exceptions.KeyGenerationException;
import com.ibm.wmb.crypto.utils.GenerateEncrptionKeys;;

public class GenerateBasicRSAKeys_JavaCompute extends MbJavaComputeNode {
	
	private static String RSA_PUBLIC_KEY_FILEPATH; 	
	private static String RSA_PRIVATE_KEY_FILEPATH;	
	private static String RSA_PUBLIC_KEY_FILENAME;
	private static String RSA_PRIVATE_KEY_FILENAME;	
	private static Integer KEY_SIZE;
	
	public void onInitialize() throws MbException
	{
		RSA_PUBLIC_KEY_FILEPATH = (String)getUserDefinedAttribute("RSA_PUBLIC_KEY_FILEPATH");
		RSA_PRIVATE_KEY_FILEPATH = (String)getUserDefinedAttribute("RSA_PRIVATE_KEY_FILEPATH");
		RSA_PUBLIC_KEY_FILENAME = (String)getUserDefinedAttribute("RSA_PUBLIC_KEY_FILENAME");
		RSA_PRIVATE_KEY_FILENAME = (String)getUserDefinedAttribute("RSA_PRIVATE_KEY_FILENAME");
		KEY_SIZE = (Integer)getUserDefinedAttribute("KEY_SIZE");
	}
	
	@SuppressWarnings("unused")
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();

		// create new message
		MbMessage outMessage = new MbMessage();
		MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly,
				outMessage);

		try {
			// 
			//Generating Keys
			GenerateEncrptionKeys genKey = new GenerateEncrptionKeys();
			String[] keys = genKey.generateBasicRSAKeys(KEY_SIZE.intValue());
			
			//Writing public key to file
			copyMessageHeaders(inMessage, outMessage);
			MbElement outRootParser = outMessage.getRootElement().createElementAsLastChild(MbBLOB.PARSER_NAME);			
			MbElement outBody 		= outRootParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"BLOB",keys[1].getBytes());
			
			MbMessage localEnv  	= outAssembly.getLocalEnvironment();
			MbElement destination   = localEnv.getRootElement().createElementAsFirstChild(MbElement.TYPE_NAME,"Destination", null);
			MbElement fileTree	    = destination.createElementAsFirstChild(MbElement.TYPE_NAME,"File",null);
			MbElement filepath 		= fileTree.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"Directory",RSA_PUBLIC_KEY_FILEPATH);
			MbElement filename 		= fileTree.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"Name",RSA_PUBLIC_KEY_FILENAME);			
			out.propagate(outAssembly);
			
			//updating the message and writing private key
			outBody.setValue(keys[0].getBytes());
			filepath.setValue(RSA_PRIVATE_KEY_FILEPATH);
			filename.setValue(RSA_PRIVATE_KEY_FILENAME);
			out.propagate(outAssembly);		
			

		} catch (KeyGenerationException e) {
			// TODO Auto-generated catch block
			throw new MbUserException(this.getClass().getName(),
											"evaluate",
											inMessage.getRootElement().toString(),
											"message key","Could Not generate keys "+ e.getMessage() + " CRITICAL",
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

}
