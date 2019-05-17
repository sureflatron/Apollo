package myapps.servicio_basico.appContext;



import myapps.user.model.Parametro;

import java.util.HashMap;
import java.util.List;

public interface IPcpParametroBusiness {
     void update(List<Parametro> pcpParametros) throws Exception;
     List<Parametro> list(String typeParam)throws Exception;
     HashMap<String,String> listMap(String typeParam);
}
