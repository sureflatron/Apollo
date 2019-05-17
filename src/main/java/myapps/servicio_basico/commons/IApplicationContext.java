package myapps.servicio_basico.commons;

import java.util.HashMap;

public interface IApplicationContext {

    public HashMap<String,String> getMapLdapParameters();       
    public HashMap<String, String> getFormParameters();
    public HashMap<String,String> getMapValidateParameters();
    public HashMap<String,String> getMapMiscellaneousParameters();   
    public HashMap<String,String> getMapEmailParameters();
    
    public void setFormParameters(HashMap<String, String> formParameters);        
    public void setMapLdapParameters(HashMap<String, String> mapLdapParameters);
    public void setMapValidateParameters(HashMap<String, String> mapValidateParameter);    
    public void setMapMiscellaneousParameters(HashMap<String, String> mapMiscellaneousParameter);    
    public void setMapEmailParameters(HashMap<String, String> mapEmailParameter);

    public void  restarParameters(TypeParameter type);
	
    
}
