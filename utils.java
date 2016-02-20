/**
 * Copyright 2016 Factom Foundation
 * Use of this source code is governed by the MIT
 * license that can be found in the LICENSE file.
 */

package FactomProject.simpleGui;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;


public class utils {


	// EXECUTE POST
		public static String executePost(String targetURL, String urlParameters) {
			  HttpURLConnection connection = null;  
			  try {
			    //Create connection
				
			    URL url = new URL(targetURL);
			    connection = (HttpURLConnection)url.openConnection();
			    connection.setRequestMethod("POST");
			    connection.setRequestProperty("Content-Type", "application/json");

			    connection.setRequestProperty("Content-Length", 
			        Integer.toString(urlParameters.getBytes().length));
			    connection.setRequestProperty("Content-Language", "en-US");  

			    connection.setUseCaches(false);
			    connection.setDoOutput(true);

			    //Send request
			    DataOutputStream wr = new DataOutputStream (
			        connection.getOutputStream());
			    wr.writeBytes(urlParameters);
			    wr.close();

			    //Get Response  
			    InputStream is = connection.getInputStream();
			    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
			    String line;
			    while((line = rd.readLine()) != null) {
			      response.append(line);
			      response.append('\r');
			    }
			    rd.close();
			   
	
			    if (response.length() == 0 ){
			    	// if nothing is coming back, at least send the response code.
			    	// a correct commit only returns 200-ok
			    	response.append(connection.getResponseCode());
			    }
				    return response.toString();
			  } catch (Exception e) {
			    e.printStackTrace();
			    return null;
			  } finally {
			    if(connection != null) {
			      connection.disconnect(); 
			    }
			  }
			} // execute post
		// EXECUTE POST
		public static String executePostBytes(String targetURL, byte[] urlParameters) {
			  HttpURLConnection connection = null;  
			  try {
			    //Create connection
			    URL url = new URL(targetURL);
			    connection = (HttpURLConnection)url.openConnection();
			    connection.setRequestMethod("POST");
			    connection.setRequestProperty("Content-Type", "application/json");

			    connection.setRequestProperty("Content-Length", 
			        Integer.toString(urlParameters.length));
			    connection.setRequestProperty("Content-Language", "en-US");  

			    connection.setUseCaches(false);
			    connection.setDoOutput(true);

			    //Send request
			    DataOutputStream wr = new DataOutputStream (
			        connection.getOutputStream());
			    wr.write(urlParameters);;
			    wr.close();

			    //Get Response  
			    InputStream is = connection.getInputStream();
			    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
			    String line;
			    while((line = rd.readLine()) != null) {
			      response.append(line);
			      response.append('\r');
			    }
			    rd.close();
			    
			    if (response.length() == 0 ){
			    	// if nothing is coming back, at least send the response code.
			    	// a correct commit only returns 200-ok
			    	response.append(connection.getResponseCode());
			    }
			    return response.toString();
			  } catch (Exception e) {
			    e.printStackTrace();
			    return null;
			  } finally {
			    if(connection != null) {
			      connection.disconnect(); 
			    }
			  }
			} // execute post
		
		// EXECUTE POST
		public static String executePostWithCredentials(String targetURL, String urlParameters,String userName,String password) {
			  HttpURLConnection connection = null;  
			  try {
			    //Create connection
				  String userPassword = userName + ":" + password;
				  String encoded = Base64.getEncoder().encodeToString(userPassword.getBytes("UTF-8"));

			    URL url = new URL(targetURL);
			    connection = (HttpURLConnection)url.openConnection();
			    connection.setRequestMethod("POST");
			    connection.setRequestProperty("Content-Type", "application/json");
			    connection.setRequestProperty("Authorization", "Basic " + encoded);	
			    connection.setRequestProperty("Content-Length", 
			        Integer.toString(urlParameters.getBytes().length));
			    connection.setRequestProperty("Content-Language", "en-US");  

			    connection.setUseCaches(false);
			    connection.setDoOutput(true);

			    //Send request
			    DataOutputStream wr = new DataOutputStream (
			        connection.getOutputStream());
			    wr.writeBytes(urlParameters);
			    wr.close();

			    //Get Response  
			    InputStream is = connection.getInputStream();
			    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
			    String line;
			    while((line = rd.readLine()) != null) {
			      response.append(line);
			      response.append('\r');
			    }
			    rd.close();
			   
	
			    if (response.length() == 0 ){
			    	// if nothing is coming back, at least send the response code.
			    	// a correct commit only returns 200-ok
			    	response.append(connection.getResponseCode());
			    }
				    return response.toString();
			  } catch (Exception e) {
			    e.printStackTrace();
			    return null;
			  } finally {
			    if(connection != null) {
			      connection.disconnect(); 
			    }
			  }
			} // execute post
		// EXECUTE POST
		
		
	// EXECUTE GET
	public static String executeGet(String targetURL) {	
		  HttpURLConnection connection = null;  
		  try {
		    //Create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Content-Type", "application/json");
		    connection.setRequestProperty("Content-Language", "en-US");  

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);


		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
		    String line;
		    while((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		  } catch (Exception e) {
		    return e.getMessage();
		  } finally {
		    if(connection != null) {
		      connection.disconnect(); 
		    }
		  }
		} // execute post
	

	
	
// EXECUTE GET
public static String executeGetWithCredentials(String targetURL,String userName,String password) {	
	  HttpURLConnection connection = null;  
	  try {
	    //Create connection
		  
		  String userPassword = userName + ":" + password;
		  String encoded = Base64.getEncoder().encodeToString(userPassword.getBytes("UTF-8"));
	    URL url = new URL(targetURL);
	    connection = (HttpURLConnection)url.openConnection();
	    connection.setRequestMethod("GET");
	    connection.setRequestProperty("Content-Type", "application/json");
	    connection.setRequestProperty("Content-Language", "en-US");  
connection.setRequestProperty("Authorization", "Basic " + encoded);
	    connection.setUseCaches(false);
	    connection.setDoOutput(true);


	    //Get Response  
	    InputStream is = connection.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
	    String line;
	    while((line = rd.readLine()) != null) {
	      response.append(line);
	      response.append('\r');
	    }
	    rd.close();
	    return response.toString();
	  } catch (Exception e) {
		  e.printStackTrace();
	    return e.getMessage();
	  } finally {
	    if(connection != null) {
	      connection.disconnect(); 
	    }
	  }
	} // execute post


	
	public static byte[] MilliTime(){
		byte[] holder;
		long ml=System.currentTimeMillis();
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(ml);
		holder= buffer.array();				
	

		//This gives you 8 bytes.  we want 6
		byte[] resp=new byte[6];
		resp[0]=holder[2];
		resp[1]=holder[3];
		resp[2]=holder[4];
		resp[3]=holder[5];
		resp[4]=holder[6];
		resp[5]=holder[7];
		
		return resp;
	}
	
		public static String randomString( int len ){
			String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			Random rnd = new Random();	
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
		}
	

		
		public static String readfile(String path){
			  String resp="";
			  File f;
			  FileInputStream inp;
			  try {
			          f = new File(path);
			          inp = new FileInputStream(f);
			          byte[] bf = new byte[(int)f.length()];
			          inp.read(bf);
			          resp = new String(bf, "UTF-8");
			            inp.close();
			      } catch (FileNotFoundException e) {
			          e.printStackTrace();
			      } catch (IOException e) {
			          e.printStackTrace();
			      } 
			  return resp;
			}
			
			
			
	
		public static byte[] sha256Bytes(byte[] base) {
		    try{
		        MessageDigest digest = MessageDigest.getInstance("SHA-256");
		        byte[] hash = digest.digest(base);

		        return hash;
		    } catch(Exception ex){
		       throw new RuntimeException(ex);
		    }
		}
		
		public static byte[] sha256String(String base) {
		    try{
		        MessageDigest digest = MessageDigest.getInstance("SHA-256");
		        byte[] hash = digest.digest(base.getBytes("UTF-8"));

		        return hash;
		    } catch(Exception ex){
		       throw new RuntimeException(ex);
		    }
		}
		
		public static byte[] sha512Bytes(byte[] base) {
		    try{
		        MessageDigest digest = MessageDigest.getInstance("SHA-512");
		        byte[] hash = digest.digest(base);

		        return hash;
		    } catch(Exception ex){
		       throw new RuntimeException(ex);
		    }
		}
		
		public static byte[] sha512String(String base) {
		    try{
		        MessageDigest digest = MessageDigest.getInstance("SHA-512");
		        byte[] enc=base.getBytes("UTF-8");
		        byte[] hash = digest.digest(enc);


		        return hash;
		    } catch(Exception ex){
		       throw new RuntimeException(ex);
		    }
		}
		
		public static byte[] appendByteArrays(byte[] first,byte[] second){
			byte[] temp=new byte[first.length + second.length ];
			System.arraycopy(first, 0, temp, 0, first.length);
			System.arraycopy(second, 0, temp, first.length, second.length);	
			return temp;
		}
		public static byte[] appendByteToArray(byte[] ary,byte bt){
			byte[] temp=new byte[ary.length + 1];
			System.arraycopy(ary, 0, temp, 0, ary.length);
			temp[ary.length]=bt;
			return temp;
		}		
		
		public static String[] appendStringToArray(String[] ary,String bt){
			String[] temp=new String[ary.length + 1];
			System.arraycopy(ary, 0, temp, 0, ary.length);
			temp[ary.length]=bt;
			return temp;
		}	
		
		public static String bytesToHex( byte [] raw ) {
			String HexCharacters = "0123456789ABCDEF";
		    if ( raw == null ) {
		        return null;
		    }
		    final StringBuilder hex = new StringBuilder( 2 * raw.length );
		    for ( final byte b : raw ) {
		        hex.append(HexCharacters.charAt((b & 0xF0) >> 4))
		            .append(HexCharacters.charAt((b & 0x0F)));
		    }
		    return hex.toString();
		}	
		
		public static byte[] hexToBytes(String s) {
		    int len = s.length();
		    byte[] data = new byte[len / 2];
		    for (int i = 0; i < len; i += 2) {
		        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
		                             + Character.digit(s.charAt(i+1), 16));
		    }
		    return data;
		}
		
		
		public static byte[] IntToByteArray(int i){
			short s=(short)i;
			ByteBuffer dbuf = ByteBuffer.allocate(2);
			dbuf.putShort(s);
			byte[] bytes = dbuf.array(); // { 0, 1 }
			
			return bytes;
		}
		
	  public static boolean isAlphaNumeric(String check) {
	
		if (check.matches("[a-zA-Z0-9]")) {
			return true;
		} else {
			return false;
		}

	
	  }
	  
	  
	  public static String bytesToEntryCreditAddress(byte[] key){
		  String address="";
		  
		  
		  return address;
		  
	  }
	  public static byte[] bigToLittleEndian(byte[] bigendian) {
		    ByteBuffer buf = ByteBuffer.allocate(bigendian.length );
			    buf.put(bigendian);	  
		    buf.order(ByteOrder.BIG_ENDIAN);

		 
		    buf.order(ByteOrder.LITTLE_ENDIAN);
		    byte[] li=buf.array();
		    return buf.array();
		}
	  
	  
	  public static void checkpath(String directoryName){

		    File direc = new File(directoryName);
		   
		    // if the directory does not exist, create it
		    if (!direc.exists())
		    {
		      System.out.println("creating directory: " + directoryName);
		      direc.mkdir();
		    }
		  
	  }
	  
	   public static String hexToText(String hex)
	   {
	      StringBuilder output = new StringBuilder("");
	      for (int i = 0; i < hex.length(); i += 2)
	      {
	         String str = hex.substring(i, i + 2);
	         output.append((char) Integer.parseInt(str, 16));
	      }
	      return output.toString();
	   }
	  
}
