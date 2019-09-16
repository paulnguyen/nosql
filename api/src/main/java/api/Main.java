package api ;

import nojava.* ;

public class Main {

	private static SM db ;

    public static void main(String[] args) throws Exception {

    	db = SMFactory.getInstance() ;          // startup DB Instance
        AppServer.startup() ;                   // startup App REST Service on port 9090
        AdminServer.startup() ;                 // startup Admin REST Service on port 8888
        API api = new API() ;
        new Thread(api).start();                // start API Thread to Create New Documents
        PingChecks pings = new PingChecks() ;
        new Thread(pings).start();              // start Ping Checks Thread to monitor cluster status
        Sync syncs = new Sync() ;
        new Thread(syncs).start() ;             // start Syncs Thread to Sync Changes in cluster

    }

}



        