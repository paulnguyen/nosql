package api ;


class SyncRequest {

    public String key ;					        // API Key (i.e. Key in K/V Pair)
    public String json ;				        // API JSON Document (i.e. Value in K/V Pair)
    public String[] vclock = new String[6] ;    // Vector Clock -- Max Five Nodes (Index 0 is Self ID)
                                                // Format:  Node ID:Count (i.e. c28c278cb4fb:2)
    public String command ;                     // create, update, delete
    public String message ; 			   		// API Message Response 

}