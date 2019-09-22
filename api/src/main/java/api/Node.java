package api ;

class Node {
    public String id ;			        // Docker Node ID
    public String name ;  		        // Docker Node Name
    public String status = "unknown" ;  // "up", "down", "unknown", "self"
    public String admin_port ;			// External Admin Port (For Test Harnest)
    public String api_port;				// External API Port (For Test Harnest)
}