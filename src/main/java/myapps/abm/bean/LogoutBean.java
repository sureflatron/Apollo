package myapps.abm.bean;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;


@ManagedBean
@ViewScoped
public class LogoutBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String variable = "hola";
    private static final Logger log = LogManager.getLogger(LocalidadBean.class);

    @PostConstruct
    void init() {
    }


    public String getVariable() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
            String address = request.getRemoteAddr();
            request.getSession().invalidate();
           //request.logout();
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");

        } catch (Exception e) {
            System.out.println("LOCALIDAD " + " LOGOUT" + e.getMessage());
        }
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}
