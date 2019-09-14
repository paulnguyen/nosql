package api ;

class Node {
    public String id ;			        // Docker Node ID
    public String name ;  		        // Docker Node Name
    public String status = "unknown" ;  // "up", "down", "unknown", "self"
}