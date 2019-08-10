package api ;

import org.json.* ;
import org.restlet.representation.* ;
import org.restlet.ext.json.* ;
import org.restlet.resource.* ;
import org.restlet.ext.jackson.* ;

import java.io.IOException ;

public class PingResource extends ServerResource {

    @Get
    public Representation get_action (Representation rep) throws IOException {

        api.Status api = new api.Status() ;
        api.status = "OK" ;
        api.message = "Simple Java NoSQL Database" ;
        api.version = "1.0" ;
        return new JacksonRepresentation<api.Status>(api) ;

    }


}


