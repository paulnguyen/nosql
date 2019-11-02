package api ;


import org.restlet.representation.* ;
import org.restlet.data.* ;
import org.restlet.ext.json.* ;
import org.restlet.resource.* ;
import org.restlet.ext.jackson.* ;

import org.json.* ;
import nojava.* ;
import java.io.IOException ;
import java.util.Collection ;


public class SyncResource extends ServerResource {


    @Post
    public Representation post_action (Representation rep) throws IOException {

        AdminServer admin = AdminServer.getInstance() ;

        JacksonRepresentation<SyncRequest> syncRep = 
            new JacksonRepresentation<SyncRequest> ( rep, SyncRequest.class ) ;

        // System.out.println( "Sync Message: " + rep.getText() ) ;

        try { 

            SyncRequest syncObject = syncRep.getObject() ;

            if ( syncObject == null ) {
                System.out.println( "*** Sync Message Null ***" ) ;
                setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
                Status status = new Status() ;
                status.status = "error" ;
                status.message = "Server Error, Try Again Later." ;
                return new JacksonRepresentation<Status>(status) ;                
            } else {
                System.out.println( "Sync Object: " + syncObject ) ;

                switch( syncObject.command ) {
                    case "create":
                        syncObject.message = "Document Created Successfully." ;
                        break ;
                    case "update":
                        syncObject.message = "Document Updated Successfully." ;
                        break ;
                    case "delete":
                        syncObject.message = "Document Deleted Successfully." ;
                        break ;
                    default:
                        setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
                        syncObject.message = "Sync Command Not Recognized."  ;
                }

                API.sync_document( syncObject ) ;
                System.out.println( syncObject.message ) ;
                return new JacksonRepresentation<SyncRequest>(syncObject) ;    
            }

        } catch ( Exception e ) {
            e.printStackTrace(); 
            setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
            Status status = new Status() ;
            status.status = "error" ;
            status.message = "Server Error, Try Again Later." ;
            return new JacksonRepresentation<Status>(status) ;
        }        
    }

    @Get
    public Representation get_action (Representation rep) throws IOException {
        try {
            String doc_key = getAttribute("key") ;
            SyncRequest syncObject = API.get_sync_request( doc_key ) ;
            return new JacksonRepresentation<SyncRequest>(syncObject) ;                 
        } catch ( Exception e ) {
            setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
            Status status = new Status() ;
            status.status = "error" ;
            status.message = "Server Error, Try Again Later." ;
            return new JacksonRepresentation<Status>(status) ;
        }
    }


}

