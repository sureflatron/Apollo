package myapps.abm.business;

import myapps.abm.dao.BancoDAO;
import myapps.abm.model.BancoEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BancoBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(BancoBL.class);

    @Inject
    private BancoDAO bancoDAO;

    public void save(BancoEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            bancoDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(BancoEntity entity) {
        try {
            bancoDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(BancoEntity entity) {
        try {
            entity.setEstado(false);
            bancoDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<BancoEntity> listAll() {
        List<BancoEntity> list = new ArrayList<>();
        try {
            list = bancoDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public BancoEntity getById(long id) {
        BancoEntity bancoEntity = new BancoEntity();
        try {
            bancoEntity = bancoDAO.getById(id);
            return bancoEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return bancoEntity;
    }

    public BancoEntity getBancoByNombre(String nombre) {
        BancoEntity bancoEntity = new BancoEntity();
        try {
            if (!nombre.equals("")) {
                bancoEntity = bancoDAO.getBancoByNombre(nombre.toUpperCase());
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "buscarBanco - BL" + "|" + e.getMessage(), e);
        }

        return bancoEntity;
    }

}
