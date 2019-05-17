package myapps.abm.business;

import myapps.abm.dao.MunicipioDAO;
import myapps.abm.model.MunicipioEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class MunicipioBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(MunicipioBL.class);

    @Inject
    private MunicipioDAO municipioDAO;

    public List<MunicipioEntity> listAll() {
        List<MunicipioEntity> list = new ArrayList<>();
        try {
            list = municipioDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<MunicipioEntity> listAllIdProvincia(long idProvincia) {
        List<MunicipioEntity> list = new ArrayList<>();
        try {
            list = municipioDAO.listAllIdProvincia(idProvincia);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

}
