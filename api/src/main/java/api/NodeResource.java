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


public class NodeResource extends ServerResource {


    @Post
    public Representation post_action (Representation rep) throws IOException {
        AdminServer admin = AdminServer.getInstance() ;
        JacksonRepresentation<Node> nodeRep = new JacksonRepresentation<Node> ( rep, Node.class ) ;
        Node node = nodeRep.getObject() ;
        try { 
                admin.registerNode( node.id, node.name, node.admin_port, node.api_port ) ;
                return new JacksonRepresentation<Node>(node) ;
        }
        catch ( Exception e ) {
                setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
                Status status = new Status() ;
                status.status = "error" ;
                status.message = "Server Error, Try Again Later." ;
                return new JacksonRepresentation<Status>(status) ;
        }        
    }

    @Get
    public Representation get_action (Representation rep) throws IOException {
        AdminServer admin = AdminServer.getInstance() ;
        Collection<Node> nodes = admin.getNodes() ;
        return new JacksonRepresentation<Collection<Node>>(nodes) ;      
    }



}

