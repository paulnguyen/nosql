package api ;


import org.restlet.representation.* ;
import org.restlet.data.* ;
import org.restlet.ext.json.* ;
import org.restlet.resource.* ;
import org.restlet.ext.jackson.* ;

import org.json.* ;
import nojava.* ;
import java.io.IOException ;


public class APIResource extends ServerResource {


    @Post
    public Representation post_action (Representation rep) throws IOException {
    	String doc_key = getAttribute("key") ;
    	if ( doc_key == null || doc_key.equals("") ) {
            setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
            Status status = new Status() ;
            status.status = "Error!" ;
            status.message = "Missing Document Key." ;
            return new JacksonRepresentation<Status>(status) ;
        } else {
            try {
                String exists = API.get_document(doc_key) ;
                setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
                Status status = new Status() ;
                status.status = "Error!" ;
                status.message = "Document Exists." ;
                return new JacksonRepresentation<Status>(status) ;
            } catch ( Exception e ) { }
        	JsonRepresentation represent = new JsonRepresentation(rep);
            JSONObject jsonobject = represent.getJsonObject();
            String doc_json = jsonobject.toString();
            // HashMap Document
            Document doc = new Document() ;
            doc.key = doc_key ;
            doc.json = "" ; // don't store in cache
            doc.message = "Document Queued for Storage." ;
            // Store to DB
            try {
            	API.create_document( doc_key, doc_json ) ;
            	return new JacksonRepresentation<Document>(doc) ;
            } catch (Exception e) {
            	setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
            	Status status = new Status() ;
            	status.status = "Server Error!" ;
            	status.message = e.toString() ;
            	return new JacksonRepresentation<Status>(status) ;  
            }
		}
    }


    @Get
    public Representation get_action (Representation rep) throws IOException {
    	String doc_key = getAttribute("key") ;
    	if ( doc_key == null || doc_key.equals("") ) {
    		return new JacksonRepresentation<Document[]>(API.get_hashmap()) ;  
        } else {
        	try {
        		String doc = API.get_document( doc_key ) ;	
        		return new StringRepresentation(doc, MediaType.APPLICATION_JSON);
        	} catch ( Exception e ) {
	            setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
	            Status status = new Status() ;
	            status.status = "Error!" ;
	            status.message = e.toString() ;      	
	            return new JacksonRepresentation<Status>(status) ;
        	}
		}
    }


   @Put
    public Representation update_action (Representation rep) throws IOException {
    	String doc_key = getAttribute("key") ;
    	if ( doc_key == null || doc_key.equals("") ) {
            setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
            Status status = new Status() ;
            status.status = "Error!" ;
            status.message = "Missing Document Key." ;
            return new JacksonRepresentation<Status>(status) ;
        } else {
        	JsonRepresentation represent = new JsonRepresentation(rep);
            JSONObject jsonobject = represent.getJsonObject();
            String doc_json = jsonobject.toString();
            // Update in DB
            try {
            	API.update_document( doc_key, doc_json ) ;
            	Status status = new Status() ;
            	status.status = "Ok!" ;
            	status.message = "Document Updated: " + doc_key ;
            	return new JacksonRepresentation<Status>(status) ;  
            } catch (Exception e) {
            	setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
            	Status status = new Status() ;
            	status.status = "Server Error!" ;
            	status.message = e.toString() ;
            	return new JacksonRepresentation<Status>(status) ;  
            }
		}
    }

    @Delete
    public Representation delete_action (Representation rep) throws IOException {
    	String doc_key = getAttribute("key") ;
    	if ( doc_key == null || doc_key.equals("") ) {
            setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
            Status status = new Status() ;
            status.status = "Error!" ;
            status.message = "Missing Document Key." ;
            return new JacksonRepresentation<Status>(status) ;
        } else {
        	try {
        		API.delete_document( doc_key ) ;	
            	Status status = new Status() ;
            	status.status = "Ok!" ;
            	status.message = "Document Deleted: " + doc_key ;
            	return new JacksonRepresentation<Status>(status) ;  
        	} catch ( Exception e ) {
	            setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
	            Status status = new Status() ;
	            status.status = "Error!" ;
	            status.message = e.toString() ;      	
	            return new JacksonRepresentation<Status>(status) ;
        	}
		}
    }



}

