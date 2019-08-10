package api ;

import org.restlet.*;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class AdminServer extends Application {

    public static void startup() {
        try {
            Component server = new Component() ;
            server.getServers().add(Protocol.HTTP, 8888) ;
            server.getDefaultHost().attach(new AdminServer()) ;
            server.start() ;
        } catch ( Exception e ) {
            System.out.println( e ) ;
        }
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext()) ;      
        router.attach( "/", PingResource.class ) ;        
        return router;
    }


}



        