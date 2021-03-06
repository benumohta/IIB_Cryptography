package com.ibm.wmb;

import java.io.IOException;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;
import com.ibm.wmb.crypto.BasicRSAEncryptDecryptData;
import com.ibm.wmb.crypto.exceptions.CipherException;
import com.ibm.wmb.crypto.exceptions.KeyNotFoundException;

@SuppressWarnings("unused")
public class BasicRSAEncryption_Encrypt extends MbJavaComputeNode {
	
	private String encryptedMessage;
	private byte[] dataToBeEncrypted;
	private static String RSA_PUBLIC_KEY_FILE;

	public void onInitialize() throws MbException
	{
		RSA_PUBLIC_KEY_FILE = (String)getUserDefinedAttribute("RSA_PUBLIC_KEY_FILE");		
	}
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();

		// create new message
		MbMessage outMessage = new MbMessage();
		MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly,
				outMessage);

		try {
			// optionally copy message headers
			copyMessageHeaders(inMessage, outMessage);

			// ----------------------------------------------------------
			// Add user code below
			MbElement inputRoot = inMessage.getRootElement();			
			dataToBeEncrypted = (byte[])(inputRoot.getFirstElementByPath("/BLOB/BLOB").getValue());			
						
			BasicRSAEncryptDecryptData encyrptData = new BasicRSAEncryptDecryptData();
			encryptedMessage = encyrptData.encryptMessage(dataToBeEncrypted,RSA_PUBLIC_KEY_FILE);
			
			MbElement outRootParser = outMessage.getRootElement().createElementAsLastChild(MbBLOB.PARSER_NAME);
			MbElement outBody 		= outRootParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"BLOB",encryptedMessage.getBytes());
			
			// End of user code
			// ----------------------------------------------------------

			// The following should only be changed
			// if not propagating message to the 'out' terminal
			out.propagate(outAssembly);

		} catch (CipherException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Encrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (KeyNotFoundException e) {
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
