package myapps.servicio_basico.appContext;

import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.commons.TypeParameter;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.Serializable;
import java.util.HashMap;

@Singleton
@Remote(IApplicationContext.class)
@Startup
public class ApplicationContextSession implements IApplicationContext,Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(ApplicationContextSession.class);
    private static String reset ="reset";
    
    @EJB
    private IPcpParametroBusiness pcpParametroBusiness;
        
    private HashMap<String,String>  mapLdapParameters;
    private HashMap<String, String> formParameters;
    private HashMap<String, String> validateParameters;
    private HashMap<String, String> miscellaneousParameter;
    private HashMap<String, String> emailParameter;

    @PostConstruct
    public void postConstruct() {
        logger.log(Level.INFO, "=========The Application is starting up!============");
        mapLdapParameters=pcpParametroBusiness.listMap(TypeParameter.LDAP.toString());
        formParameters= pcpParametroBusiness.listMap(TypeParameter.FORM.toString());
        validateParameters = pcpParametroBusiness.listMap(TypeParameter.VALIDATE.toString());
        miscellaneousParameter = pcpParametroBusiness.listMap(TypeParameter.MISC.toString());
        emailParameter = pcpParametroBusiness.listMap(TypeParameter.EMAIL.toString());
        logger.log(Level.INFO,"========The Application is start up!============");
    }
    @PreDestroy
    public void preDestroy(){
        mapLdapParameters=null;

    }
    
    public synchronized void restarParameters(TypeParameter type){
        switch (type){
            case LDAP:
                mapLdapParameters=pcpParametroBusiness.listMap(TypeParameter.LDAP.toString());
                logger.log(Level.INFO, reset +"|"+type+"|"+mapLdapParameters);
                break;
            case FORM:
            	formParameters=pcpParametroBusiness.listMap(TypeParameter.FORM.toString());
                logger.log(Level.INFO, reset +"|"+type+"|"+formParameters);
                break;
            case VALIDATE:
            	validateParameters=pcpParametroBusiness.listMap(TypeParameter.VALIDATE.toString());
                logger.log(Level.INFO, reset +"|"+type+"|"+validateParameters);
                break;
            case MISC:
                miscellaneousParameter=pcpParametroBusiness.listMap(TypeParameter.MISC.toString());
                logger.log(Level.INFO, reset +"|"+type+"|"+miscellaneousParameter);
                break;
            case EMAIL:
                emailParameter=pcpParametroBusiness.listMap(TypeParameter.EMAIL.toString());
                logger.log(Level.INFO, reset +"|"+type+"|"+emailParameter);
                break;     
             default:
                logger.log(Level.ERROR,"Tipo de parametro no es valido |type="+type);
        }
    }

    @Override
    public HashMap<String, String> getMapLdapParameters() {
        return this.mapLdapParameters;
    }

    @Override
    public void setMapLdapParameters(HashMap<String, String> mapLdapParameters) {
        this.mapLdapParameters=mapLdapParameters;
    }
   	
	@Override
	public HashMap<String, String> getMapValidateParameters() {		
		return validateParameters;
	}

    @Override
	public void setMapValidateParameters(HashMap<String, String> mapValidateParameter) {
		this.validateParameters = mapValidateParameter;		
	}
	
	@Override
	public HashMap<String, String> getMapMiscellaneousParameters() {		
		return miscellaneousParameter;
	}
	@Override
	public HashMap<String, String> getMapEmailParameters() {		
		return emailParameter;
	}
	@Override
	public void setMapMiscellaneousParameters(HashMap<String, String> mapMiscellaneousParameter) {
		this.miscellaneousParameter = mapMiscellaneousParameter;		
	}
	@Override
	public void setMapEmailParameters(HashMap<String, String> mapEmailParameter) {
		this.emailParameter = mapEmailParameter;		
	}
	@Override
	public HashMap<String, String> getFormParameters() {		
		return formParameters;
	}
	@Override
	public void setFormParameters(HashMap<String, String> formParameters) {		
		this.formParameters = formParameters;
	}

    

}
