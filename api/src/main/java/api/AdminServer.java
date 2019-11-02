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
import java.util.concurrent.ConcurrentLinkedQueue ;
import java.util.Collection ;

public class AdminServer extends Application {

    private static AdminServer _adminServer ;

   // Node Sync Queues
    private static ConcurrentLinkedQueue<SyncRequest> node1_sync_queue ; 
    private static ConcurrentLinkedQueue<SyncRequest> node2_sync_queue ; 
    private static ConcurrentLinkedQueue<SyncRequest> node3_sync_queue ; 
    private static ConcurrentLinkedQueue<SyncRequest> node4_sync_queue ;
    private static ConcurrentLinkedQueue<SyncRequest> node5_sync_queue ; 

    // Instance Variables
    private ConcurrentHashMap<String,Node> nodes = new ConcurrentHashMap<String,Node>() ;
    private ConcurrentHashMap<String,ClientResource> clients = new ConcurrentHashMap<String,ClientResource>() ;
    private String  my_ip = "" ;
    private String  my_host = "" ;


    public synchronized static AdminServer getInstance()
    {
        if ( _adminServer == null ) {
            _adminServer = new AdminServer();
            _adminServer.initConfig() ;
        }
        return _adminServer;
    }

    public static void startup() {
        try {

            Component server = new Component() ;
            server.getServers().add(Protocol.HTTP, 8888) ;
            server.getDefaultHost().attach(AdminServer.getInstance()) ;
            server.start() ;

            // start Ping Checks Thread to monitor cluster status
            PingChecks pings = new PingChecks() ;
            new Thread(pings).start();              

            node1_sync_queue = new ConcurrentLinkedQueue<SyncRequest>() ; 
            node2_sync_queue = new ConcurrentLinkedQueue<SyncRequest>() ; 
            node3_sync_queue = new ConcurrentLinkedQueue<SyncRequest>() ; 
            node4_sync_queue = new ConcurrentLinkedQueue<SyncRequest>() ; 
            node5_sync_queue = new ConcurrentLinkedQueue<SyncRequest>() ; 

            // start Sync Threads to Sync Changes in cluster
            Sync sync1 = new Sync( "api_node_1", node1_sync_queue ) ;
            new Thread(sync1).start() ;             
            Sync sync2 = new Sync( "api_node_2", node2_sync_queue ) ;
            new Thread(sync2).start() ;              
            Sync sync3 = new Sync( "api_node_3", node3_sync_queue ) ;
            new Thread(sync3).start() ;              
            Sync sync4 = new Sync( "api_node_4", node4_sync_queue ) ;
            new Thread(sync4).start() ;              
            Sync sync5 = new Sync( "api_node_5", node5_sync_queue ) ;
            new Thread(sync5).start() ;  

        } catch ( Exception e ) {
            System.out.println( e ) ;
        }
    }

    public static void syncDocument( String key, String command ) {

        try {
            AdminServer server = AdminServer.getInstance() ;
            SyncRequest syncObject = API.get_sync_request( key ) ;
            syncObject.command = command ;
            int my_index = server.nodeIndex( server.getMyHostname() ) ;
            System.out.println(     "Sync Document: Key = " + key + " Command = " + command + " "
                                    + "[host:" + server.getMyHostname() 
                                    + " index:" + Integer.toString(my_index) + "]" 
                                     ) ;
            switch ( my_index ) {
                case 1:
                    //node1_sync_queue.add( syncObject ) ;
                    node2_sync_queue.add( syncObject ) ;
                    node3_sync_queue.add( syncObject ) ;
                    node4_sync_queue.add( syncObject ) ;
                    node5_sync_queue.add( syncObject ) ;
                    break ;
                case 2:
                    node1_sync_queue.add( syncObject ) ;
                    //node2_sync_queue.add( syncObject ) ;
                    node3_sync_queue.add( syncObject ) ;
                    node4_sync_queue.add( syncObject ) ;
                    node5_sync_queue.add( syncObject ) ;
                    break ;
                case 3:
                    node1_sync_queue.add( syncObject ) ;
                    node2_sync_queue.add( syncObject ) ;
                    //node3_sync_queue.add( syncObject ) ;
                    node4_sync_queue.add( syncObject ) ;
                    node5_sync_queue.add( syncObject ) ;
                    break ;
                case 4:
                    node1_sync_queue.add( syncObject ) ;
                    node2_sync_queue.add( syncObject ) ;
                    node3_sync_queue.add( syncObject ) ;
                    //node4_sync_queue.add( syncObject ) ;
                    node5_sync_queue.add( syncObject ) ;
                    break ;
                case 5:
                    node1_sync_queue.add( syncObject ) ;
                    node2_sync_queue.add( syncObject ) ;
                    node3_sync_queue.add( syncObject ) ;
                    node4_sync_queue.add( syncObject ) ;
                    //node5_sync_queue.add( syncObject ) ;
                    break ;
            }

        } catch ( Exception e ) {
            System.out.println( e ) ;
        }

    }

    public static ClientResource getSyncClient( String node ) {
        String URL = "http://"+node+":8888/sync" ;
        //System.out.println( URL ) ;
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


    /* Instance Methods */

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
            Node node = new Node() ;
            node.id = my_host ;
            node.name = "localhost" ;
            nodes.put( my_host, node ) ; 

        } catch (Exception e) {
            System.out.println( e ) ;
        }
    }

    public String getMyHostname() {
        return this.my_host ;
    }

    public int nodeIndex( String id ) {
        
        Node n = nodes.get( id ) ;
        String name = n.name ;
        int index = 0 ;
        switch ( name ) {
            case "api_node_1": index = 1 ; break ;
            case "api_node_2": index = 2 ; break ;
            case "api_node_3": index = 3 ; break ;
            case "api_node_4": index = 4 ; break ;
            case "api_node_5": index = 5 ; break ;
            default: index = 0 ;
        }

        return index ;

    }

    public void registerNode( String id, String name, String admin_port, String api_port ) {
        // register node name
        Node node = new Node() ;
        node.id = id ;
        node.name = name ;
        node.admin_port = admin_port ;
        node.api_port = api_port ;
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

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext()) ;      
        router.attach( "/", PingResource.class ) ;
        router.attach( "/node", NodeResource.class ) ;   
        router.attach( "/sync", SyncResource.class ) ;
        router.attach( "/sync/{key}", SyncResource.class ) ;         
        return router;
    }


}

