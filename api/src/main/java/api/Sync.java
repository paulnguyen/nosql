package api ;

import org.json.* ;
import nojava.* ;
import java.io.* ;
import java.util.* ;

import org.restlet.resource.*;
import org.restlet.representation.* ;
import org.restlet.ext.json.* ;
import org.restlet.data.* ;
import org.restlet.* ;


public class Sync implements Runnable {

	private AdminServer server = AdminServer.getInstance() ;

    // Background Thread
	@Override
	public void run() {
		while (true) {
			try {
				// sleep for 5 seconds
				try { Thread.sleep( 5000 ) ; } catch ( Exception e ) {}  
				// sync nodes
				System.out.println ( server.getMyHostname() + ": Sync Nodes..." ) ;
			} catch (Exception e) {
				System.out.println( e ) ;
			}			
		}
	}    

}


