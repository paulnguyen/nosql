package api ;

import nojava.* ;

public class Main {

	private static SM db ;

    public static void main(String[] args) throws Exception {
    	db = SMFactory.getInstance() ;
        AppServer.startup() ;
        AdminServer.startup() ;
    }

}



        