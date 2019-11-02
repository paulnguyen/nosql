package api ;

import nojava.* ;

import java.util.* ;
import java.io.* ;

import java.util.concurrent.BlockingQueue ;
import java.util.concurrent.LinkedBlockingQueue ;
import java.util.concurrent.ConcurrentHashMap ;
import java.util.Collection ;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.json.* ;
import org.restlet.resource.*;
import org.restlet.representation.* ;
import org.restlet.ext.json.* ;
import org.restlet.data.* ;


public class API implements Runnable {

	// queue of new documents
    private static BlockingQueue<Document> CREATE_QUEUE = new LinkedBlockingQueue<Document>() ;						

    // key to record map
    private static ConcurrentHashMap<String,Document> KEYMAP_CACHE = new ConcurrentHashMap<String,Document>() ; 	


    // Background Thread
	@Override
	public void run() {
		while (true) {
			try {
				// sleep for 5 seconds
				try { Thread.sleep( 5000 ) ; } catch ( Exception e ) {}  

				// process any new additions to database
				Document doc = CREATE_QUEUE.take();
  				SM db = SMFactory.getInstance() ;
        		SM.OID record_id  ;
        		String record_key ;
        		SM.Record record  ;
        		String jsonText = doc.json ;
            	int size = jsonText.getBytes().length ;
            	record = new SM.Record( size ) ;
            	record.setBytes( jsonText.getBytes() ) ;
            	record_id = db.store( record ) ;
            	record_key = new String(record_id.toBytes()) ;
            	doc.record = record_key ;
            	doc.json = "" ;
            	KEYMAP_CACHE.put( doc.key, doc ) ;    
                System.out.println( "Created Document: " + doc.key ) ;
                
                // sync nodes
                AdminServer.syncDocument( doc.key, "create" ) ; 

			} catch (InterruptedException ie) {
				ie.printStackTrace() ;
			} catch (Exception e) {
				System.out.println( e ) ;
			}			
		}
	}    


    public static Document[] get_hashmap() {
    	return (Document[]) KEYMAP_CACHE.values().toArray(new Document[0]) ;
    }


    public static void save_hashmap() {
		try {
		  FileOutputStream fos = new FileOutputStream("index.db");
		  ObjectOutputStream oos = new ObjectOutputStream(fos);
		  oos.writeObject(KEYMAP_CACHE);
		  oos.close();
		  fos.close();
		} catch(IOException ioe) {
		  ioe.printStackTrace();
		}
    }


    public static void load_hashmap() {
		 try {
		     FileInputStream fis = new FileInputStream("index.db") ;
		     ObjectInputStream ois = new ObjectInputStream(fis) ;
		     KEYMAP_CACHE = (ConcurrentHashMap) ois.readObject();
		     ois.close() ;
		     fis.close() ;
		  } catch(IOException ioe) {
		     ioe.printStackTrace() ;
		  } catch(ClassNotFoundException c) {
		     System.out.println("Class not found");
		     c.printStackTrace() ;
		  }
    }


   public static void sync_document(SyncRequest sync) throws DocumentException {

        String key = sync.key ;
        String value = sync.json ;
        String[] vclock = sync.vclock ;
        String command = sync.command ;

        try {

            AdminServer server = AdminServer.getInstance() ;
            String my_host = server.getMyHostname() ;
            String my_version = my_host + ":" + Integer.toString(1) ;
            int my_index = server.nodeIndex( my_host ) ;

            switch ( command ) {
                case "create":
                    Document doc = new Document() ;
                    doc.vclock[0] = my_host ;
                    doc.vclock[1] = vclock[1] ;
                    doc.vclock[2] = vclock[2] ;
                    doc.vclock[3] = vclock[3] ;
                    doc.vclock[4] = vclock[4] ;
                    doc.vclock[5] = vclock[5] ;
                    doc.vclock[my_index] = my_host + ":" + Integer.toString(1) ;
                    SM db = SMFactory.getInstance() ;
                    SM.OID record_id  ;
                    SM.Record record  ;
                    String jsonText = value ;
                    int size = jsonText.getBytes().length ;
                    record = new SM.Record( size ) ;
                    record.setBytes( jsonText.getBytes() ) ;
                    record_id = db.store( record ) ;
                    String record_key = new String(record_id.toBytes()) ;
                    doc.record = record_key ;
                    doc.json = "" ;
                    doc.key = key ;
                    KEYMAP_CACHE.put( key, doc ) ;
                    System.out.println( "SYNC: Created Document Key: " + key 
                                        + " Record: " + record_key 
                                        + " vClock: " + Arrays.toString(doc.vclock) 
                                    ) ;
                    break ;
                case "update":
                    break ;
                case "delete":
                    break ;
            }   

        } catch (Exception e) {
            throw new DocumentException( e.toString() ) ;
        }

    }


    public static void create_document(String key, String value) throws DocumentException {
    	try {
	    	System.out.println( "Create Document: Key = " + key + " Value = " + value ) ;
	    	Document doc = new Document() ;
	    	doc.key = key ;
            AdminServer server = AdminServer.getInstance() ;
            String my_host = server.getMyHostname() ;
            System.out.println( "My Host Name: " + my_host ) ;
            doc.vclock[0] = my_host ;
            String my_version = my_host + ":" + Integer.toString(1) ;
            int my_index = server.nodeIndex( my_host ) ;
            System.out.println( "Node Index: " + my_index ) ;
            doc.vclock[my_index] = my_version ;
	    	KEYMAP_CACHE.put( key, doc ) ;
	    	doc.json = value ;
	        CREATE_QUEUE.put( doc ) ; 
	    	System.out.println( "New Document Queued: " + key ) ;    		
	    } catch (Exception e) {
	    	throw new DocumentException( e.toString() ) ;
	    }

    }


    public static String get_document(String key) throws DocumentException {
    	System.out.println( "Get Document: " + key ) ;
    	Document doc = KEYMAP_CACHE.get( key ) ;
    	if ( doc == null || doc.record == null )
    		throw new DocumentException( "Document Not Found: " + key ) ;
    	String record_key = doc.record ;
    	SM db = SMFactory.getInstance() ;
    	SM.OID record_id ;
        SM.Record found ;
		record_id = db.getOID( record_key.getBytes() ) ;
        try {
            found = db.fetch( record_id ) ;
            byte[] bytes = found.getBytes() ;
            String jsonText = new String(bytes) ;
            System.out.println( "Document Found: " + key ) ;    
            return jsonText ;
        } catch (SM.NotFoundException nfe) {
        	System.out.println( "Document Found: " + key ) ;    
			throw new DocumentException( "Document Not Found: " + key ) ;   
		} catch (Exception e) {
			throw new DocumentException( e.toString() ) ;                 
        }   	
    }


    public static SyncRequest get_sync_request(String key) throws DocumentException {
        System.out.println( "Get Document: " + key ) ;
        Document doc = KEYMAP_CACHE.get( key ) ;
        if ( doc == null || doc.record == null )
            throw new DocumentException( "Document Not Found: " + key ) ;
        String record_key = doc.record ;
        SM db = SMFactory.getInstance() ;
        SM.OID record_id ;
        SM.Record found ;
        record_id = db.getOID( record_key.getBytes() ) ;
        try {
            found = db.fetch( record_id ) ;
            byte[] bytes = found.getBytes() ;
            String jsonText = new String(bytes) ;
            System.out.println( "Document Found: " + key ) ;    
            SyncRequest sync = new SyncRequest() ;
            sync.key = doc.key ;
            sync.json = jsonText ;
            sync.vclock = doc.vclock ;
            sync.command = "" ; // set by caller
            return sync ;
        } catch (SM.NotFoundException nfe) {
            System.out.println( "Document Found: " + key ) ;    
            throw new DocumentException( "Document Not Found: " + key ) ;   
        } catch (Exception e) {
            throw new DocumentException( e.toString() ) ;                 
        }       
    }


    public static void update_document( String key, String value ) throws DocumentException {
    	System.out.println( "Get Document: " + key ) ;
    	Document doc = KEYMAP_CACHE.get( key ) ;
    	if ( doc == null || doc.record == null )
    		throw new DocumentException( "Document Not Found: " + key ) ;
    	String record_key = doc.record ;
    	SM db = SMFactory.getInstance() ;
        SM.Record found ;
        SM.Record record ;
        SM.OID update_id ;        
		SM.OID record_id = db.getOID( record_key.getBytes() ) ;
		String jsonText = value ;
		int size = jsonText.getBytes().length ;
 		try {
            // store json to db
            record = new SM.Record( size ) ;
            record.setBytes( jsonText.getBytes() ) ;
            update_id = db.update( record_id, record ) ;
            System.out.println( "Document Updated: " + key ) ;
            // update vclock
            AdminServer server = AdminServer.getInstance() ;
            String my_host = server.getMyHostname() ;
            doc.vclock[0] = my_host ;
            int my_index = server.nodeIndex( my_host ) ;
            String old_version = doc.vclock[my_index] ;
            String[] splits = old_version.split(":") ;
            int version = Integer.parseInt(splits[1])+1 ;
            String new_version = my_host + ":" + Integer.toString(version) ;            
            doc.vclock[my_index] = new_version ;
            // sync nodes
            AdminServer.syncDocument( key, "update" ) ; 
			return ;             
        } catch (SM.NotFoundException nfe) {
			throw new DocumentException( "Document Not Found: " + key ) ;
       	} catch (Exception e) {
           	throw new DocumentException( e.toString() ) ;           
        }
    }


    public static void delete_document( String key ) throws DocumentException {
    	System.out.println( "Delete Document: " + key ) ;
    	Document doc = KEYMAP_CACHE.get( key ) ;
    	if ( doc == null || doc.record == null )
    		throw new DocumentException( "Document Not Found: " + key ) ;
    	String record_key = doc.record ;
    	SM db = SMFactory.getInstance() ;
        SM.Record found ;
        SM.Record record ;     
		SM.OID record_id = db.getOID( record_key.getBytes() ) ;
       	try {
            db.delete( record_id ) ;
            // sync nodes
            AdminServer.syncDocument( key, "delete" ) ; 
            // remove key map
            KEYMAP_CACHE.remove( key ) ;
			System.out.println( "Document Deleted: " + key ) ;
        } catch (SM.NotFoundException nfe) {
           throw new DocumentException( "Document Not Found: " + key ) ;
        } catch (Exception e) {
         	throw new DocumentException( e.toString() ) ;            
        }		
    }



}


