/**
 * Copyright 2016 Factom Foundation
 * Use of this source code is governed by the MIT
 * license that can be found in the LICENSE file.
 * 
 * This project uses json.org for son handling.
 * The license includes this restriction: 
 * "The software shall be used for good, not evil." If your conscience cannot live with that, then choose a different package.
 * Thank you json.org developers
 */

package FactomProject.simpleGui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Main {
// if you have your own entry credit address, you can chainge it here or put it into a local storage.
	// This is an example application, not a wallet.  It does not address securing private keys.
		public static String  ECPrivateAddress="Es3Ac4MGa1BpETEAq5eU6Rrws87LytWxHZCiZtuzLnvgzqLou31P";
		
		// if you use the commented out code below, you can create a different chain.  This is hardcoded to use the 
		// EC-Faucet chain set up for general use.  This is a chain created using the code below, not by EC-Faucet.
		public static String chainID="740B54866AA4BF84D861D813C52A55AB718DBC380EE95F53E53574AD2FCD8A44";
		
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		javaSigningAPI.factomdURL ="http://factomd-live.cloudapp.net:8088";
		javaSigningAPI.importPrivateKey(ECPrivateAddress);
		
		//  this makes the original chain that this test application is hardcoded to
		/* only gets run once.
		 * It is a make chain example if you are looking at the code
		// we generally want to put the signing key in the chain header.  future entries get signed by that key for origin verification
		// future entries by this user are signed with this key.  It is the public faucet pool key in this case.
	
		String[] extids=new String[5];
		extids[0]="HEX:" + utils.bytesToHex(javaSigningAPI.publicKeyBytes);	
		extids[1]="Factom";
		extids[2]="EC-Faucet";
		extids[3]="reddit";
		extids[4]="simplegui";
		String data="This is the Root chain for the Simple GUI set up for the Entry Credit Pool set up by the reddit user EC-Faucet.  Thank you EC-Faucet!\n\n  Simple GUI only adds entries at this time so required a chain.";
		
		System.out.println(javaSigningAPI.ComposeChainCommit("EC2uxPxmxom73T5h8s2YqV2EC3BdtGnTrUwvXiUzqpjc5HYmxUZD", extids, data));
		 */
		createAndShowGUI();
	}



	 private static void createAndShowGUI()  {
		 Dimension d=new Dimension();
		 Dimension textboxes=new Dimension();
		 
		 d.setSize(400,400);
	        JFrame frame1 = new JFrame("Factom EC_Faucet Example");
	        frame1.setSize(d);
	        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        JPanel panel=new JPanel();
	        frame1.add(panel, BorderLayout.CENTER);
	        
	        JTextField external1=new JTextField();
	        JTextField external2=new JTextField();
	        JTextField external3=new JTextField();
	        
	        JLabel eLabel1=new JLabel("External ID 1                 ");
	        JLabel eLabel2=new JLabel("External ID 2              ");
	        JLabel eLabel3=new JLabel("External ID 3      ");
	        JLabel contentLabel=new JLabel("Content");

		        
	        JTextArea tArea=new JTextArea();
	        
	        external1.setSize(textboxes);
	        external2.setSize(textboxes);
	        external3.setSize(textboxes);
	       tArea.setSize(400,400); 
	       tArea.setWrapStyleWord(true);
	   
		        JButton button = new JButton("Add Entry");
	        //Add action listener to button
	        button.addActionListener(new ActionListener() {
	 
	            public void actionPerformed(ActionEvent e)
	            {
	                //Execute when button is pressed
	                executeButton(external1.getText(),external2.getText(),external3.getText(),tArea.getText());
	            }
	        });      


	 GroupLayout layout = new GroupLayout(panel);
	 panel.setLayout(layout);
	 layout.setAutoCreateGaps(true);
	 layout.setAutoCreateContainerGaps(true);
	 
	 layout.setHorizontalGroup(
			   layout.createParallelGroup()
			      .addGroup(layout.createSequentialGroup()
				           .addComponent(eLabel1)
				           .addComponent(eLabel2)
				           .addComponent(eLabel3)
			    		  )
			      .addGroup(layout.createSequentialGroup()
				           .addComponent(external1)
				           .addComponent(external2)
				           .addComponent(external3)
			    		  )

			      
			      .addComponent(button)
		          .addComponent(contentLabel)
				  .addComponent(tArea)			      
			);
			layout.setVerticalGroup(
			   layout.createSequentialGroup()
			      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(eLabel1)
				           .addComponent(eLabel2)
				           .addComponent(eLabel3))
			      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(external1)
				           .addComponent(external2)
				           .addComponent(external3))
			      .addComponent(button)
			       .addComponent(contentLabel)
						      .addComponent(tArea)
			      
			); 
			
	        frame1.setVisible(true);

	        panel.setVisible(true);
	        external1.setVisible(true);
	        external2.setVisible(true);
	        external3.setVisible(true);
	        button.setVisible(true);
		    }
	 
	 private static void executeButton(String e1,String e2,String e3,String content){
		 try{
		 // how many external IDs were entered in the text box?
		 int exCount=2;
		 if (!e1.equals("")){
			 exCount++;
		 }
		 if (!e2.equals("")){
			 exCount++;
		 }
		 if (!e3.equals("")){
			 exCount++;
		 }
		 String[] externalIDs=new String[exCount];
		 // external ids wants an array of strings.  a signature is a byte array.
		 // the HEX: prefix is only to tell the local request builder that the data is 
		 // binary data encoded as HEX.  'HEX:' is not a Factom Centric requirement.
		 externalIDs[0]="HEX:" + utils.bytesToHex( javaSigningAPI.signData(content.getBytes("UTF-8")));
		 // You do not have to use time anywhere, this is just to differentiate your entries if you repeat
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		 String sdt = df.format(new Date(System.currentTimeMillis()));
		 externalIDs[1]=sdt;
		 
		 exCount=2;
		 if (!e1.equals("")){
			 externalIDs[exCount]=e1;
			 exCount++;
		 }
		 if (!e2.equals("")){
			 externalIDs[exCount]=e2;
			 exCount++;
		 }
		 if (!e3.equals("")){
			 externalIDs[exCount]=e3;
			 exCount++;
		 }		 
		 
		 String entryHash=javaSigningAPI.ComposeEntryCommit(javaSigningAPI.ecAddress, externalIDs, chainID, content);
		 callSearchGUI(entryHash);		 
		 } catch (Exception e){
			 e.printStackTrace();
		 }
		 

	 }

	 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 public static void callSearchGUI(String EntryHash){
		 Dimension d=new Dimension();
		 Dimension textboxSize=new Dimension();
		 Dimension textAreaSize=new Dimension();
		 d.setSize(400,400);
		 textAreaSize.setSize(380, 350);
		 
	        JFrame frame1 = new JFrame("Search Entry Hash");
	        frame1.setSize(d);
	        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        JPanel panel=new JPanel();
	        frame1.add(panel, BorderLayout.CENTER);
	        JTextArea tEntryHash=new JTextArea(1,20);
	        JTextArea tArea=new JTextArea(5,20);
	        tArea.setWrapStyleWord(true);
	        tArea.setSize(textAreaSize);
	        tEntryHash.setSize(textboxSize);

	        tEntryHash.setText(EntryHash);
	       		tArea.setSize(400,400); 
		        JButton bSearch = new JButton("Find Entry");
	        //Add action listener to button
		        bSearch.addActionListener(new ActionListener() {
	 
	            public void actionPerformed(ActionEvent e)
	            {
	                //Execute when button is pressed
	            	String entryResponse=handleEntryRequest(tEntryHash.getText());
	            	
	                tArea.setText(entryResponse);
	            }
	        });      


	 GroupLayout layout = new GroupLayout(panel);
	 panel.setLayout(layout);
	 layout.setAutoCreateGaps(true);
	 layout.setAutoCreateContainerGaps(true);
	 
	 layout.setHorizontalGroup(
			   layout.createParallelGroup()
			   	
			      .addComponent(tEntryHash)
			      .addComponent(bSearch)
			      .addComponent(tArea)

			);
			layout.setVerticalGroup(
			   layout.createSequentialGroup()
			      .addComponent(tEntryHash)
			      .addComponent(bSearch)
			      .addComponent(tArea)
			      
			); 
			
	        frame1.setVisible(true);

	        panel.setVisible(true);
	        tEntryHash.setVisible(true);
	        bSearch.setVisible(true);
	        tArea.setVisible(true);

		    }
	
	 private static String handleEntryRequest(String entryHash){
		 String resp="";
		 String ChainID="";
		 String content="";
		 resp=javaSigningAPI.getEntry(entryHash);
		 // not a perfect check universally, but response is usually in hex so it works
		 if (resp.indexOf("returned HTTP response code: 400 for URL") > 0 ){
			 return "Entries can take up to ten minutes to post to the blockchain.\nNew blocks are posted on the 10s.\nYour Entry is not yet available.";
		 }
		 
		 if (resp.toUpperCase().indexOf(chainID.toUpperCase()) > 0){
				JSONTokener jt=new JSONTokener(resp);
				JSONObject jo=new JSONObject();
				JSONArray ExtIDs=new JSONArray();
				String ext="";
				try {						
					 jo=(JSONObject) jt.nextValue();
					 ChainID=jo.getString("ChainID");

					 ExtIDs=jo.getJSONArray("ExtIDs");
					 for (int i=0;i<ExtIDs.length();i++) {
						 if (i==0) {
							 
							 ext+="Signature: " +	 ExtIDs.get(i).toString() + "\n";
						 }else if (i==1) {
							 
							 ext+="Entered: " + i + ": " + utils.hexToText(ExtIDs.get(i).toString()) + "\n";
						 }else {
						 ext+="External ID " + (i-1) + ": " + utils.hexToText(ExtIDs.get(i).toString()) + "\n";
						 }
						
						 
					 }		
						 content=utils.hexToText(jo.getString("Content"));				 
					 resp="Chain:  " + ChainID + "\n";
					 resp += ext;
					 resp +="Content: \n " + content + "\n";
					 
				} catch (Exception e) {
					
				}
		 }
		 
		 
		 return resp;
	 }
	 

	 
}
