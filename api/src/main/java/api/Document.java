package api ;

import java.io.Serializable ;

class Document implements Serializable {

    public String key ;					   // API Key (i.e. Key in K/V Pair)
    public String record ;  			   // DB Record ID 
    public String json ;				   // API JSON Document (i.e. Value in K/V Pair)
    public String[] vclock = new String[6] ; // Vector Clock -- Max Five Nodes (Index 0 is Self ID)
                                           // Format:  Node ID:Count (i.e. c28c278cb4fb:2)
    public String message ; 			   // API Message Response 


    public String getKey() { return this.key ; }
    public String getRecord() { return this.record ; }
    public String getJson() { return this.json ; }
    public String[] getVclock() { return this.vclock ; }
    public String getMessage() { return this.message ; }

    public void setKey( String key ) { this.key = key ; }
    public void setRecord( String record ) { this.record = record ; }
    public void setJson( String json ) { this.json = json ; }
    public void setVclock( String[] vclock ) { this.vclock = vclock ; }
    public void setMessage( String message ) { this.message = message ; }

}