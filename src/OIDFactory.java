
public class OIDFactory
{
	
	public static SM.OID getInstance(byte[] bytes)
	{
		SM sm = SMFactory.getInstance();
		return sm.getOID( bytes ) ;
	}

    protected OIDFactory()
    {

    }
} 
