package api ;

import org.restlet.*;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import org.restlet.resource.*;
import org.restlet.representation.* ;
import org.restlet.ext.json.* ;
import org.restlet.data.* ;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue ;
import java.util.concurrent.LinkedBlockingQueue ;
import java.util.concurrent.ConcurrentHashMap ;
import java.util.Collection ;

public class AdminServer extends Application {

    private static AdminServer _adminServer ;

    // Instance Variables
    private ConcurrentHashMap<String,Node> nodes = new ConcurrentHashMap<String,Node>() ;
    private ConcurrentHashMap<String,ClientResource> clients = new ConcurrentHashMap<String,ClientResource>() ;
    private String my_ip = "" ;
    private String my_host = "" ;

    public synchronized static AdminServer getInstance()
    {
        if ( _adminServer == null ) {
            _adminServer = new AdminServer();
            _adminServer.initConfig() ;
        }
        return _adminServer;
    }

    public void initConfig() {
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("IP address : " + ip);
            System.out.println("Hostname : " + hostname);
            my_ip = ip.toString() ;
            my_host = hostname.toString() ;
        } catch (Exception e) {
            System.out.println( e ) ;
        }
    }

    public static void startup() {
        try {
            Component server = new Component() ;
            server.getServers().add(Protocol.HTTP, 8888) ;
            server.getDefaultHost().attach(AdminServer.getInstance()) ;
            server.start() ;
        } catch ( Exception e ) {
            System.out.println( e ) ;
        }
    }

    public String getMyHostname() {
        return this.my_host ;
    }

    public int nodeIndex( String id ) {
        Node n = nodes.get( id ) ;
        String name = n.name ;
        if ( name.equals("api_node_1") ) { return 1 ; }
        else if ( name.equals("api_node_2") ) { return 2 ; }
        else if ( name.equals("api_node_3") ) { return 3 ; }
        else if ( name.equals("api_node_4") ) { return 4 ; }
        else if ( name.equals("api_node_5") ) { return 5 ; }
        else return 0 ;
    }

    public void registerNode( String id, String name ) {
        // register node name
        Node node = new Node() ;
        node.id = id ;
        node.name = name ;
        nodes.put( id, node ) ;     
        System.out.println( "Register Node: " + id + " as: " + name ) ;     
    }

    public Collection<Node> getNodes() {
        return nodes.values() ;
    }

    public void nodeUp( String id ) {
        Node node = nodes.get( id ) ;
        node.status = "up" ;
    }

    public void nodeDown( String id ) {
        Node node = nodes.get( id ) ;
        node.status = "down" ;
    }

    public void nodeSelf( String id ) {
        Node node = nodes.get( id ) ;
        node.status = "self" ;
    }

    public ClientResource getPingResource( String id ) {
        Node node = nodes.get( id ) ;
        String name = node.name ;
        String URL = "http://"+name+":8888" ;
        ClientResource resource = new ClientResource( URL ) ;
        /* Create a Client with the socketTimout parameter for HttpClient and "attach"
           it to the ClientResource. */
        Context context = new Context() ;
        context.getParameters().add("readTimeout", "1000") ;
        context.getParameters().add("idleTimeout", "1000");
        context.getParameters().add("socketTimeout", "1000");
        context.getParameters().add("socketConnectTimeoutMs", "1000") ;
        resource.setNext( new Client(context, Protocol.HTTP) ) ;
        // Set the client to not retry on error. Default is true with 2 attempts.
        resource.setRetryOnError(false) ;
        return resource ;
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext()) ;      
        router.attach( "/", PingResource.class ) ;
        router.attach( "/node", NodeResource.class ) ;    
        return router;
    }


}

