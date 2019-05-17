package myapps.workflow.business;


import myapps.workflow.dao.TareasDAO;
import myapps.workflow.entity.TareasEntity;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TareasBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(TareasBL.class);


    public List<TareasEntity> listAll() {
        List<TareasEntity> list = new ArrayList<>();
        try {
            TareasDAO a = new TareasDAO();
            list = a.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

}
