# FactomSimpleGUI
Simple Java Application to add Entries to the Factom Blockchain using an existing funded Entry Credit address


This application is hard coded to the Entry Credit Pool set up by Github user EC-Faucet mentioned in the post:

https://np.reddit.com/r/factom/comments/46b8bn/free_entry_credit_faucet_now_live/


It takes up to 3 external IDs and content text to be added to the Factom Blockchain.  It does not require running factom components.  It operates with http posts to a publicly facing Factomd server.


This is not a wallet.  It does not keep track of your transactions.  You need to write down your entry hashes to find them at a later time.    It is an example of how to use java to program agianst factom.

