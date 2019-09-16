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


public class PingChecks implements Runnable {

	private AdminServer server = AdminServer.getInstance() ;

    // Background Thread
	@Override
	public void run() {
		while (true) {
			try {
				// sleep for 1 second
				try { Thread.sleep( 1000 ) ; } catch ( Exception e ) {}  
				// ping & sync nodes
				Collection<Node> nodes = server.getNodes() ;
    			for (Iterator<Node> iterator = nodes.iterator(); iterator.hasNext();) {
    				Node n = iterator.next() ;
    				String my_host = server.getMyHostname() ;
    				if ( !n.id.equals(my_host) ) {
						System.out.println( "Ping Node [id:" + n.id + "  name:" + n.name + "]" ) ;
	    				try {
							ClientResource resource = server.getPingResource( n.id ) ;	
							Representation result  = resource.get() ; 
	  						JSONObject json = new JSONObject( result.getText() ) ;
	  						System.out.println( "[id:" + n.id + "  name:" + n.name + "] " + json.toString() ) ;  
	  						server.nodeUp( n.id ) ;      	
	  					} catch (Exception e) {
	  						server.nodeDown( n.id ) ; 
	  						System.out.println( e ) ;
						}   		    					
    				} else {
    					server.nodeSelf( n.id ) ;
    				}
    				
  				} 
			} catch (Exception e) {
				System.out.println( e ) ;
			}			
		}
	}    


}


