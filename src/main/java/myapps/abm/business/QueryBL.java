package myapps.abm.business;

import myapps.abm.dao.QueryDAO;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;

@Stateless
public class QueryBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(QueryBL.class);

    @Inject
    private QueryDAO queryDAO;

    public String getQueryJson(String nombre) {
        String sqlJson = "";
        try {
            sqlJson = queryDAO.getQueryJson(nombre).getQuery();
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT_QUERY" + "|" + e.getMessage(), e);
        }
        return sqlJson;
    }
}
