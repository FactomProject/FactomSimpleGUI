/**
 * Copyright 2016 Factom Foundation
 * Use of this source code is governed by the MIT
 * license that can be found in the LICENSE file.
 * 
 * A special thank you to str4d on github for the creative commons licensing on his 25519 encryption
 * https://github.com/str4d/ed25519-java
 * 
 * 
 * 
 */

package FactomProject.simpleGui;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Signature;

import net.i2p.crypto.eddsa.*;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

public class javaSigningAPI {
	
	public javaSigningAPI(){
		// constructor
	};

	public static String factomdURL="http://localhost:8088";
	public static byte[] privateKeyBytes;
	public static byte[] publicKeyBytes;
	public static String ecAddress="";
	public static EdDSAPublicKey pubKey;
	public static EdDSAPrivateKey priKey;
	
	/**ComposeChainCommit  - 
	 *This is not base functionality.  It builds the first entry of a chain
	* This does the transaction the hard way.  if your version of fctwallet has compose functionality, use that
	* It does leave signing to the fctwallet
	* 
	* @param name paying ec address
	* @param extids external ids as array of strings
	* @param data - data to be to blockchain entry
	* @return
	* JSON document containing chainid.  Formatted for terminal<p>
	* {"Response":"11914c19a28a98c57d12f3cce6c32b7944784f4b4781a706c24eb1dc284e2856","Success":true}
	* or error message as Response
	* <p>
	* Example:<p>
	* String [] extids={"123","456"}
	* 
	* responseText=ComposeChainCommit("EC2RYZzZxJvu2xT6JdKrVLjCMjXX5pmYyNMJG4tLoSyihvTemwyX",extids,"here is the body of your entry"); 
	**/		
	
	public static String ComposeChainCommit(String name, String[] extids,String data) {
		String resp="";		
		Chain c=new Chain();	
		Entry e=new  Entry();	
		int datasize=0;
		//e.setChainID(chainID);
		e.setExtIDs(extids);
		e.Content =data.getBytes();
		byte[] postData=new byte[0];
		// put external IDs in entry
		byte[] weld;
		byte[] double256Chain;
		//put content into entry		
		c.setFirstEntry(e);
		e.setEntryHash();
		
		datasize=getEntrySize(e);
		datasize=(int) datasize / 1024 + 11;
		
		double256Chain=utils.sha256Bytes(utils.sha256Bytes(c.ChainID));
		weld=utils.sha256Bytes(utils.sha256Bytes(utils.appendByteArrays(e.entryHash , c.ChainID)));

		postData=utils.appendByteToArray(postData, (byte)0);						//version
		postData=utils.appendByteArrays(postData,utils. MilliTime());		//millitime
		postData=utils.appendByteArrays(postData,double256Chain );//chainid hash hash twice
		postData=utils.appendByteArrays(postData, weld);	//commit weld
		postData=utils.appendByteArrays(postData,e.entryHash );		//hash of first entry
		postData=utils.appendByteToArray(postData, (byte)datasize);						//cost
		byte[] Sig=signData(postData);
		verifyData(postData,Sig);
		postData=utils.appendByteArrays(postData, publicKeyBytes);
		postData=utils.appendByteArrays(postData,Sig);
		
		//rI could use a json marshal here, but I want the message format to be obvious.
		String jsonPost="{\"CommitChainMsg\":\"" + utils.bytesToHex(postData).toLowerCase() + "\"}";
		
		
		resp=utils.executePost(factomdURL + "/v1/commit-chain/",jsonPost);

		if ( resp.equals("200")){
			System.out.println("waiting 10 seconds before calling reveal.");
			System.out.println("Milestone 2 will have receipts making this redundant.");
			try{
			//Thread.sleep(10000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			resp=RevealChainOrEntry(e,"Chain");
		} else {
			
		}
		if (resp.equals("200")) {
			return utils.bytesToHex(e.ChainID) ;
		} else {	
			return resp;		
		}

	}
	

	
	
	/**ComposeEntryCommit  - 
	 *This is not base functionality.  It adds an entry to a known chain
	* This does the transaction the hard way.  if your version of fctwallet has compose functionality, use that
	* It does leave signing to the fctwallet
	* 
	* @param name paying ec address
	* @param extids external ids as array of strings
	* @param chainID - chain id
	* @param data - data to be to blockchain entry
	* @return
	* JSON document containing entry hash.  Formatted for terminal<p>
	* {"Response":"75668b338e44cb45998594c9cf5f36b52929d85bb616a4052b043da30319956d","Success":true}
	* or error message as Response
	* <p>
	* Example:<p>
	* String [] extids={"123","456"}
	* 
	* responseText=ComposeEntryCommit("EC2RYZzZxJvu2xT6JdKrVLjCMjXX5pmYyNMJG4tLoSyihvTemwyX",extids,"11914c19a28a98c57d12f3cce6c32b7944784f4b4781a706c24eb1dc284e2856","here is the body of your entry"); 
	**/	
  public static String ComposeEntryCommit(String name,String[] extids,String chainID,String data)  {
	  	String resp="";

		byte[] postData=new byte[0];

		Entry e=new Entry();
		
		// change this.  it is client.java specific and looking for -c and -e
		e.setChainID(chainID);
		e.setExtIDs(extids);
		e.Content = data.getBytes();
		
		//now that everything is is, hash it
		e.setEntryHash();
		int datasize=getEntrySize(e);
		datasize=(int) datasize / 1024 + 1;
		// build commit
		postData=utils.appendByteToArray(postData, (byte)0);			//version
		postData=utils.appendByteArrays(postData, utils.MilliTime());	//millitime
		if (e.entryHash == null) {
			e.setEntryHash();
		}
		postData=utils.appendByteArrays(postData, e.entryHash);	//hash of entry
		postData=utils.appendByteToArray(postData, (byte)datasize);	//entry cost (entry content divided by 1k
		byte[] Sig=signData(postData);
		verifyData(postData,Sig);
		postData=utils.appendByteArrays(postData, publicKeyBytes);
		postData=utils.appendByteArrays(postData,Sig);
		//   send this to fctwallet for signing

		String temp="{\"CommitEntryMsg\":\"" + utils.bytesToHex(postData) + "\"}";
		//rI could use a json marshal here, but I want the message format to be obvious.

		resp=utils.executePost(factomdURL + "/v1/commit-entry/" ,temp);

		if ( resp.equals("200")){
		//	System.out.println("waiting 10 seconds before calling reveal");
		//	System.out.println("Milestone 2 will have receipts making this redundant.");
			try{
				//Thread.sleep(10000);
			} catch (Exception ex) {
			// this should never happen
				ex.printStackTrace();
				
			}
			resp=RevealChainOrEntry(e,"Entry");
		} else {
			
		}		
		return resp;    
	   
  }
  
  // called by compose
	/**RevealChainOrEntry  - 
	 * Adding chains and entries is a two step process.  first commit.  This is Factom committing to post your entry
	 * Then you reveal what that entry is
	* 
	* @param  {@link Factom.Entry} 
	* @param RevealType - String - 'Chain' or 'Entry'
	* @return
	* JSON document containing entry hash.  Formatted for terminal<p>
	* {"Response":"75668b338e44cb45998594c9cf5f36b52929d85bb616a4052b043da30319956d","Success":true}
	* or error message as Response
	* <p>
	* Example:<p>

	* responseText=RevealChainOrEntry(e,"Chain"); 
	**/	
  private static String RevealChainOrEntry(Entry e,String RevealType) {
		String resp="";
		int i;
		int extidlength=0;
		String temp="";
		byte[] postData=new byte[0];
		// put external IDs in entry


		postData=utils.appendByteToArray(postData, (byte)0);						//version
		postData=utils.appendByteArrays(postData, e.ChainID);						//ChainID

		for (i=0;i<e.ExtIDs .length ;i++){
			extidlength=extidlength + e.ExtIDs[i].length + 2;
		}
		
		postData=utils.appendByteArrays(postData,  utils.IntToByteArray(extidlength ));	//extid size (2 bytes) length of all extid data + item counts
		
		for ( i=0;i< e.ExtIDs .length ;i++){  //add each ext id
				postData=utils.appendByteArrays(postData, utils.IntToByteArray(e.ExtIDs[i].length ) );				//extid size (2 bytes)
				postData=utils.appendByteArrays(postData, e.ExtIDs [i]);
		}
		postData=utils.appendByteArrays(postData, e.Content);	//extid size (2 bytes)
		
		temp="{\"Entry\":\"" + utils.bytesToHex(postData) + "\"}";
		
		//rI could use a json marshal here, but I want the message format to be obvious.		
		postData=utils.appendByteArrays("{\"Entry\":\"".getBytes(), postData);
		postData=utils.appendByteArrays( postData,"\"}".getBytes());

		if (RevealType=="Chain"){
			resp=utils.executePost(factomdURL + "/v1/reveal-chain/",temp);
			if (resp.equals("200")) {
				resp=utils.bytesToHex(e.ChainID);
			} 
			    System.out.println("Chain ID: " + utils.bytesToHex(e.ChainID));
		} else if (RevealType=="Entry"){
			resp=utils.executePost(factomdURL + "/v1/reveal-entry/",temp);
			if (resp.equals("200")) {
				resp=utils.bytesToHex(e.entryHash);
			} 
				System.out.println("Entry Hash: " + utils.bytesToHex(e.entryHash));
		}

		
		
		return resp;    
	   
  }

  
	/**getEntryCreditBalance  - 
	 * hit Factomd for the balance on this address
	* 
	* @param Address ec address

	* @return
	* JSON document containing balance.  Formatted for terminal<p>
	* 
	* responseText=getEntryCreditBalance("EC2RYZzZxJvu2xT6JdKrVLjCMjXX5pmYyNMJG4tLoSyihvTemwyX"); 
	**/		
	
	public static String getEntryCreditBalance(String Address) {

		
		try {
		String resp=utils.executeGet(factomdURL + "/v1/entry-credit-balance/" + Address);

		return resp;		
		} catch (Exception e) {
			return e.getMessage();
		}

	}
	
	/**getEntry  - 
	 * hit Factomd for the entry fir ths hash
	* 
	* @param Address entry Hash

	* @return
	* JSON document containing entry.  Formatted for terminal<p>
	* 
	* responseText=getEntryCreditBalance("EC2RYZzZxJvu2xT6JdKrVLjCMjXX5pmYyNMJG4tLoSyihvTemwyX"); 
	**/		
	
	public static String getEntry(String hash) {

		
		try {
		String resp=utils.executeGet(factomdURL + "/v1/entry-by-hash/" + hash);

		return resp;		
		} catch (Exception e) {
			return e.getMessage();
		}

	}
  
  
private static int getEntrySize(Entry e){
	int resp=0;
	
	for (int i=0;i<e.ExtIDs .length ;i++){
		
	resp=resp + e.ExtIDs [i].length ;
	}
	resp=resp + e.Content .length ;
	
	return resp;
}



  
public static void setAddressFromSeed(byte[] seed){
	
	  try{

		  EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512"); 
		  EdDSAPrivateKeySpec privateKeySpec = new EdDSAPrivateKeySpec(seed, spec); 
		  EdDSAPublicKeySpec pubKeySpec = new EdDSAPublicKeySpec(privateKeySpec.getA(), spec); 
		  EdDSAPublicKey publicKey = new EdDSAPublicKey(pubKeySpec); 
		  EdDSAPrivateKey privateKey = new EdDSAPrivateKey(privateKeySpec); 
		  pubKey=publicKey;
		  priKey=privateKey;
		  /*
		  net.i2p.crypto.eddsa.KeyPairGenerator kp=new net.i2p.crypto.eddsa.KeyPairGenerator();
		  SecureRandom sr=new SecureRandom();
		  sr.setSeed(toss);
		  kp.initialize(256, sr);
		 KeyPair pp= kp.generateKeyPair();
		 EdDSAPrivateKey privateKey=(EdDSAPrivateKey)pp.getPrivate();
		 EdDSAPublicKey publicKey=(EdDSAPublicKey)pp.getPublic();
		 */		 
		 privateKeyBytes=seed;
		 publicKeyBytes=publicKey.getAbyte();

		 ecAddress=getECAddressFromPublicKey(publicKeyBytes);
	
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  
	  
	  

}

  public static byte[] signData(byte[] data){
	  byte[] resp=new byte[0];
	  try{
		/*  EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512"); 
		  EdDSAPrivateKeySpec privateKeySpec = new EdDSAPrivateKeySpec(signingKey, spec);
	      PrivateKey pk=new EdDSAPrivateKey(privateKeySpec);
	      */	      
	      Signature sgr = new EdDSAEngine(MessageDigest.getInstance("SHA-512"));

		  sgr.initSign(priKey);
		  sgr.update(data);
		return sgr.sign();
	  } catch (Exception e) {
	  return resp;		  
	  }
	  
  }
  
  

  
  
  private static Boolean verifyData(byte[] data,byte[] sig){
	  
	  try{
	/*	  EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512"); 
		  EdDSAPublicKeySpec publicKeySpec = new EdDSAPublicKeySpec(signingKey, spec);
	  PublicKey pk=new EdDSAPublicKey(publicKeySpec);
	  */
	  Signature sgr = new EdDSAEngine(MessageDigest.getInstance("SHA-512"));
		sgr.initVerify(pubKey);
		sgr.update(data);
		if (sgr.verify(sig)) {
			return true;
		} else {
			return false;
		}
		
	  } catch (Exception e) {
	  return false;		  
	  }
	  
  }
  
  public static void importPrivateKey(String key){
	  if (key.toUpperCase().substring(0, 2).equals("ES")) {
		  byte[] pk=Encode58addressto256(key);
		  byte[] seed=new byte[32];
		  for (int i=0;i<32;i++){
			  seed[i]=pk[i+2];
		  }
		  setAddressFromSeed(seed);
		
	  }else if (key.toUpperCase().substring(0, 2).equals("FS")) {
		  // not yet implemented
	  } else {
		  System.out.println("invalid key type");
	  }
  }
 	  
	  private static String getECAddressFromPublicKey(byte[] key){
		  String address = "";
          byte[] appendPrefix = new byte[34];
          byte[] appendSuffix = new byte[38];
          byte[] firstHash;
          appendPrefix[0] =0x59;
          appendPrefix[1] =0x2a;
          System.arraycopy(key, 0, appendPrefix, 2, 32);
          firstHash = utils.sha256Bytes(utils.sha256Bytes(appendPrefix));
          System.arraycopy(appendPrefix, 0, appendSuffix, 0, 34);
          appendSuffix[34] = firstHash[0];
          appendSuffix[35] = firstHash[1];
          appendSuffix[36] = firstHash[2];
          appendSuffix[37] = firstHash[3];
          address = Encode256to58(appendSuffix);
          ecAddress=address;
          return address;		  
		
	  } 
	  
	  
	  
      private static String Encode256to58(byte[] data)
      {
    	  // you are doing signed integer math here.  it may be unsigned in the rest of the world.  watch it
          String code_string = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
          String strResponse = "";
          int unsig=0;
          byte[] response=new byte[52];
          // move bytestring to integer
          BigInteger intData = BigInteger.valueOf(0);
          for (int i = 0; i < data.length; i++)
          {
              intData = intData .multiply(BigInteger.valueOf(256));
              unsig=data[i] & 0xff;
              intData= intData.add((BigInteger.valueOf(unsig))) ;
          }

          // Encode BigInteger to Base58 string

          int j = 0;
          while (intData.compareTo(BigInteger.valueOf(0)) > 0)
          {
              byte remainder = (intData.mod(BigInteger.valueOf(58))).byteValue();
              intData = intData.divide(BigInteger.valueOf(58));
              strResponse =  code_string.substring(remainder, remainder+1) + strResponse ;
              response[j] = remainder ;
              j = j + 1;
          }	 
          return strResponse;
      }
      
      private static byte[] Encode58addressto256(String data)
      {
    	  // you are doing signed integer math here.  it may be unsigned in the rest of the world.  watch it
          String code_string = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
          int pos=0;
          byte[] response=new byte[39];
          // move bytestring to integer
          BigInteger intData = BigInteger.valueOf(0);
          for (int i = 0; i < data.length(); i++)
          {
        	  pos=code_string.indexOf(data.substring(i,i+1));
              intData = intData .multiply(BigInteger.valueOf(58));
         	  intData=intData.add(BigInteger.valueOf(pos));             
             // unsig=data[i] ;
          }

          // Encode BigInteger to Base256 


          byte[] tmp=new byte[1];
          while (intData.compareTo(BigInteger.valueOf(0)) > 0)
          {
              int remainder = intData.mod(BigInteger.valueOf(256)).intValueExact();
              intData = intData.subtract(BigInteger.valueOf(remainder));
              intData = intData.divide(BigInteger.valueOf(256));
              tmp[0]=(byte)remainder;
              response=utils.appendByteArrays( tmp,response);
              //response[j] = remainder ;
          }	 
          return response;
      }
      
	
}
