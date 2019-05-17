package myapps.user.bean;

import myapps.servicio_basico.appContext.IPcpParametroBusiness;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.commons.TypeParameter;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.ldap.DescriptorBitacora;
import myapps.user.model.Parametro;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class ConfigParameters implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(ConfigParameters.class);
	
	@EJB
    private IPcpParametroBusiness pcpParametroBusiness;
	
	@Inject
	private ControlerBitacora controlerBitacora;
	
    @EJB
    private IApplicationContext applicationContext;
        
    private List<Parametro>  parametros;  
    private TypeParameter typeParam;
    private Integer countParams;
    private String parameter;
    
    private HashMap<String,String> validateParameter;
    
    @PostConstruct
    public void init(){        
        try {
        	validateParameter =  applicationContext.getMapValidateParameters();
        	parameter=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type");
            typeParam=TypeParameter.valueOf(parameter);
            if(Optional.ofNullable(typeParam).isPresent()){
        		parametros = new ArrayList<>();
                loadParameters();
                if (!parametros.isEmpty()){
                    countParams=parametros.size();
                }else {
                    countParams=0;
                }
            }
        }catch (IllegalArgumentException e){
            typeParam=null;
            log.error(e.getMessage());            
        }catch (Exception e) {
            log.error(e.getMessage());
		}
    }

    public void updateParams(){
        try {
            pcpParametroBusiness.update(parametros);
            controlerBitacora.accion(DescriptorBitacora.PARAMETROS, "Se actualizaron parametros");                        
            loadParameters();            
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()),"Actualización satisfactoria", "");            
            applicationContext.restarParameters(TypeParameter.valueOf(typeParam.toString()));
        }catch (Exception e) {
            log.log(Level.ERROR,"|"+e.getMessage(),e);
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al actualizar los parametros: " + e.getMessage(), "");
		}

    }
    public void loadParameters() {
    	try {
			parametros=pcpParametroBusiness.list(parameter);
		} catch (Exception e) { 
			log.error(e.getMessage());
		}
    }
    
    public void invalidateSession(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public List<Parametro> getParametros() {
        return parametros;
    }

    public void setParametros(List<Parametro> parametros) {
        this.parametros = parametros;
    }

    public TypeParameter getTypeParam() {
        return typeParam;
    }

    public void setTypeParam(TypeParameter typeParam) {
        this.typeParam = typeParam;
    }

    public Integer getCountParams() {
        return countParams;
    }

    public void setCountParams(Integer countParams) {
        this.countParams = countParams;
    }
    
    public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
