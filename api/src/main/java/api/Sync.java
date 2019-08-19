package api ;

import org.json.* ;
import nojava.* ;
import java.io.* ;

import org.restlet.resource.*;
import org.restlet.representation.* ;
import org.restlet.ext.json.* ;
import org.restlet.data.* ;


public class Sync implements Runnable {

    // Background Thread
	@Override
	public void run() {
		while (true) {
			try {
				// sleep for 10 seconds
				try { Thread.sleep( 10000 ) ; } catch ( Exception e ) {}  

            	// process background node syncs
            	String URL = "https://cmpe281.getsandbox.com" ;
  				ClientResource client = new ClientResource( URL ) ;
  				try {
  					Representation result  = client.get() ; 
      				JSONObject json = new JSONObject( result.getText() ) ;
      				System.out.println( json.toString() ) ;
    			} catch (Exception e) {
      				System.out.println( e.getMessage() ) ;
    			}    
			} catch (Exception e) {
				System.out.println( e ) ;
			}			
		}
	}    


}


