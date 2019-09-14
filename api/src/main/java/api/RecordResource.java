package api ;


import org.restlet.representation.* ;
import org.restlet.data.* ;
import org.restlet.ext.json.* ;
import org.restlet.resource.* ;
import org.restlet.ext.jackson.* ;

import org.json.* ;
import nojava.* ;
import java.io.IOException ;


public class RecordResource extends ServerResource {

    @Get
    public Representation get_action() throws JSONException {
        SM db = SMFactory.getInstance() ;
        SM.OID record_id ;
        String record_key = getAttribute("key") ;
        SM.Record found ;
        String jsonText = "" ;        
        if ( record_key == null || record_key.equals("") ) {
            setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
            Status status = new Status() ;
            status.status = "Error!" ;
            status.message = "Missing Record Key." ;
            return new JacksonRepresentation<Status>(status) ;
        } else {
            record_id = db.getOID( record_key.getBytes() ) ;
            try {
                found = db.fetch( record_id ) ;
                byte[] bytes = found.getBytes() ;
                jsonText = new String(bytes) ;
                return new StringRepresentation(jsonText, MediaType.APPLICATION_JSON);
            } catch (SM.NotFoundException nfe) {
                setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
                Status status = new Status() ;
                status.status = "Error!" ;
                status.message = "Record Not Found." ;
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


    @Post
    public Representation post_action (Representation rep) throws IOException {
        SM db = SMFactory.getInstance() ;
        SM.OID record_id ;
        SM.Record record ;
        String record_key ;
        String jsonText = "" ;
        try {
            JsonRepresentation represent = new JsonRepresentation(rep);
            JSONObject jsonobject = represent.getJsonObject();
            jsonText = jsonobject.toString();
            int size = jsonText.getBytes().length ;
            record = new SM.Record( size ) ;
            record.setBytes( jsonText.getBytes() ) ;
            record_id = db.store( record ) ;
            record_key = new String(record_id.toBytes()) ;
            Key key = new Key() ;
            key.key = record_key ;
            key.message = "Record Created!" ;
            return new JacksonRepresentation<Key>(key) ;
        } catch (Exception e) {
            setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
            Status status = new Status() ;
            status.status = "Server Error!" ;
            status.message = e.toString() ;
            return new JacksonRepresentation<Status>(status) ;                  
        }
    }


   @Put
    public Representation put_action (Representation rep) throws IOException {
        SM db = SMFactory.getInstance() ;
        SM.Record record ;
        SM.OID record_id ;
        SM.OID update_id ;
        String jsonText = "" ;
        String record_key = getAttribute("key") ;       
        if ( record_key == null || record_key.equals("") ) {
            setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
            Status status = new Status() ;
            status.status = "Error!" ;
            status.message = "Missing Record Key." ;
            return new JacksonRepresentation<Status>(status) ;
        } else {
            record_id = db.getOID( record_key.getBytes() ) ;
            JsonRepresentation represent = new JsonRepresentation(rep);
            JSONObject jsonobject = represent.getJsonObject();
            jsonText = jsonobject.toString();
            int size = jsonText.getBytes().length ;
            try {
                record = new SM.Record( size ) ;
                record.setBytes( jsonText.getBytes() ) ;
                update_id = db.update( record_id, record ) ;
                record_key = new String(update_id.toBytes()) ;
                Key key = new Key() ;
                key.key = record_key ;
                key.message = "Record Updated!" ;
                return new JacksonRepresentation<Key>(key) ;                
            } catch (SM.NotFoundException nfe) {
                setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
                Status status = new Status() ;
                status.status = "Error!" ;
                status.message = "Record Not Found." ;
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
       SM db = SMFactory.getInstance() ;
        SM.OID record_id ;
        String record_key = getAttribute("key") ;       
        if ( record_key == null || record_key.equals("") ) {
            setStatus( org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST ) ;
            Status status = new Status() ;
            status.status = "Error!" ;
            status.message = "Missing Record Key." ;
            return new JacksonRepresentation<Status>(status) ;
        } else {
            record_id = db.getOID( record_key.getBytes() ) ;
            try {
                db.delete( record_id ) ;
                Status status = new Status() ;
                status.status = "OK!" ;
                status.message = "Record Deleted." ;
                return new JacksonRepresentation<Status>(status) ;  
            } catch (SM.NotFoundException nfe) {
                setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
                Status status = new Status() ;
                status.status = "Error!" ;
                status.message = "Record Not Found." ;
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

}



