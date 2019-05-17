package myapps.abm.bean;


import myapps.abm.business.AlarmaBL;
import myapps.abm.business.EnvioBL;
import myapps.abm.model.AlarmaEntity;
import myapps.abm.model.EnvioEntity;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.ActiveDirectory;
import myapps.user.ldap.DescriptorBitacora;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;


@ManagedBean
@ViewScoped
public class NotificacionBean implements Serializable {
    private static final long serialVersionUID = 11L;

    private static final Logger log = LogManager.getLogger(NotificacionBean.class);

    @Inject
    private AlarmaBL alarmaBL;
    @Inject
    private EnvioBL envioBL;
    @Inject
    private ActiveDirectory activeDirectory;
    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;


    private Map<String, String> validateParameter;

    private transient List<AlarmaEntity> alarmaEntityList = new ArrayList<>();
    private transient List<EnvioEntity> envioEntityList = new ArrayList<>();
    private transient AlarmaEntity alarmaEntityObj;

    private Map<String, String> emailParameter;


    private transient AlarmaEntity selectalarmaEntityObj;

    private boolean edit;
    private String email;
    private int porcentaje;

    private String tipoalarma;
    private String falso = "false";
    private static final String ALARMA = "NOTIFICACION";

    @PostConstruct
    void init() {
        try {
            alarmaEntityObj = new AlarmaEntity();
            alarmaEntityObj.setTipo(ALARMA);
            validateParameter = beanApplicationContext.getMapValidateParameters();
            emailParameter = beanApplicationContext.getMapEmailParameters();
            loadAlarmas();
        } catch (Exception e) {
            log.error("[init] Fallo en el init.", e);
        }
    }

    public void newAlarma() {
        edit = false;
        alarmaEntityObj = new AlarmaEntity();
        alarmaEntityObj.setTipo(ALARMA);
        alarmaEntityObj.setListadestinos(new ArrayList<>());
        alarmaEntityObj.setListaCC(new ArrayList<>());
        alarmaEntityObj.setListaCO(new ArrayList<>());
        envioEntityList = new ArrayList<>();
        tipoalarma = "-1";
    }

    public boolean validarCombos() {
        boolean validar = false;
        List<String> mensaje = new ArrayList<>();
        if (!alarmaEntityObj.getNombre().isEmpty()) {
            for (AlarmaEntity alarma : alarmaEntityList) {
                if (alarmaEntityObj.getNombre().equals(alarma.getNombre()) && alarmaEntityObj.getIdAlarma() != alarma.getIdAlarma()) {
                    mensaje.add("Este nombre ya está registrado");
                }
            }
        }
        if (envioEntityList == null || envioEntityList.isEmpty()) {
            mensaje.add("No se definieron los envios");
        }
        if (!mensaje.isEmpty()) {
            validar = true;
            for (String message : mensaje) {
                SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), message, null);
            }
        }

        return validar;
    }

    public void actionAlarma() {
        if (validarCombos()) {
            return;
        }
        try {
            if (isEdit() && alarmaEntityObj.getIdAlarma() > 0) {
                alarmaEntityObj.setFrecuencia("Diario");
                alarmaEntityObj.setTipoAlarma(tipoalarma);
                alarmaEntityObj.setTipo(ALARMA);

                alarmaBL.update(alarmaEntityObj);
                for (EnvioEntity envioEntity : envioEntityList) {
                    envioEntity.setAlarmaByIdAlarma(alarmaEntityObj);
                    envioEntity.setEstado(enviarEstado(envioEntity));
                    envioBL.update(envioEntity);
                }
                controlerBitacora.update(DescriptorBitacora.ALARMAS, String.valueOf
                        (getAlarmaEntityObj().getIdAlarma()), getAlarmaEntityObj()
                        .getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                alarmaEntityObj.setTipoAlarma(tipoalarma);
                alarmaEntityObj.setTipo(ALARMA);
                alarmaEntityObj.setFrecuencia("Diario");
                alarmaEntityObj.setFechaCreacion(new Date());
                alarmaBL.save(alarmaEntityObj);
                for (EnvioEntity envioEntity : validarLista(envioEntityList)) {
                    envioEntity.setAlarmaByIdAlarma(alarmaEntityObj);
                    envioBL.save(envioEntity);
                }
                controlerBitacora.insert(DescriptorBitacora.ALARMAS, String.valueOf(getAlarmaEntityObj()
                        .getIdAlarma()), getAlarmaEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
            loadAlarmas();
            newAlarma();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }


    private void loadAlarmas() {
        List<AlarmaEntity> lista = alarmaBL.listAll(ALARMA);
        for (AlarmaEntity alarma : lista) {
            alarma.setListadestinos(armarLista(alarma.getPara()));
            alarma.setListaCC(armarLista(alarma.getCc()));
            alarma.setListaCO(armarLista(alarma.getCo()));
            alarma.setFrecuenciaNotificacion(convertirStringEnvios(envioBL.getByIdAlarma(alarma.getIdAlarma())));
        }
        alarmaEntityList = lista;

    }

    public List<String[]> armarLista(String cadenafinal) {
        List<String[]> listaAlarma = new ArrayList<>();
        if (cadenafinal != null) {
            String[] parts = cadenafinal.split(";");
            for (String a : parts) {
                String[] alarma = new String[2];
                alarma[0] = falso;
                alarma[1] = a;
                listaAlarma.add(alarma);
            }
        }
        return listaAlarma;
    }

    public void editAlarma(AlarmaEntity objeto) {
        alarmaEntityObj = objeto;
        tipoalarma = objeto.getTipoAlarma();
        envioEntityList = envioBL.getByIdAlarma(objeto.getIdAlarma());
        convertirStringEnvios();
        setEdit(true);
    }

    public void deleteAlarma(AlarmaEntity objeto) {
        try {
            alarmaBL.delete(objeto);

            for (EnvioEntity envioEntity : objeto.getEnviosByIdAlarma()) {
                envioBL.delete(envioEntity);
            }
            controlerBitacora.delete(DescriptorBitacora.ALARMAS, String.valueOf
                    (getAlarmaEntityObj()
                            .getIdAlarma()), getAlarmaEntityObj().getNombre());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newAlarma();
            loadAlarmas();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public void obtener(int valor) {
        switch (valor) {
            case 1:
                alarmaEntityObj.setListadestinos(buscarUsuario(email, alarmaEntityObj.getListadestinos()));
                break;
            case 2:
                alarmaEntityObj.setListaCC(buscarUsuario(email, alarmaEntityObj.getListaCC()));
                break;
            case 3:
                alarmaEntityObj.setListaCO(buscarUsuario(email, alarmaEntityObj.getListaCO()));
                break;
            default:
                log.error("Valor Indefinido");
                break;
        }
        email = "";
    }

    public List<String[]> buscarUsuario(String usuario, List<String[]> lista) {
        String[] correo = new String[2];
        correo[0] = falso;
        correo[1] = activeDirectory.getCorreo(usuario);
        if (buscarrepetido(correo[1], lista)) {
            SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Usuario Repetido", null);
        } else {
            if (lista == null) {
                lista = new ArrayList<>();
            }
            if (!correo[1].isEmpty()) {
                if (lista.size() < 30) {
                    lista.add(correo);
                } else {
                    SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Limite Maximo alcanzado", null);
                }
            } else {
                SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Usuario no se encuentra en el Active Directory", null);
            }
        }
        return lista;
    }

    public boolean buscarrepetido(String usuario, List<String[]> lista) {
        boolean valor = false;
        if (lista != null) {
            for (String[] alarma : lista) {
                for (String mail : alarma) {
                    if (usuario.equals(mail)) {
                        valor = true;
                    }
                }
            }
        }
        return valor;
    }


    public void cargar(int valor) {
        if (valor == 1 && alarmaEntityObj.getListadestinos() != null) {
            alarmaEntityObj.setPara(convertirString(alarmaEntityObj.getListadestinos()));
        }
        if (valor == 2 && alarmaEntityObj.getListaCC() != null) {
            alarmaEntityObj.setCc(convertirString(alarmaEntityObj.getListaCC()));
        }
        if (valor == 3 && alarmaEntityObj.getListaCO() != null) {
            alarmaEntityObj.setCo(convertirString(alarmaEntityObj.getListaCO()));
        }
    }

    public String convertirString(List<String[]> lista) {
        Iterator<String[]> iterator = lista.iterator();
        while (iterator.hasNext()) {
            if (iterator.next()[0].equals("true")) {
                iterator.remove();
            }
        }
        StringBuilder cadena = new StringBuilder();
        int contador = 0;
        for (String[] valor : lista) {
            contador++;
            cadena = cadena.append(valor[1]);
            if (contador < lista.size()) {
                cadena = cadena.append(";");
            }
        }
        return cadena.toString();
    }

    public void guardarPorcentaje() {
        EnvioEntity nuevoEnvio = new EnvioEntity();
        if (porcentaje <= 100) {
            nuevoEnvio.setPorcentaje(Long.valueOf(porcentaje));
        } else {
            SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Porcentaje no permitido", null);
            porcentaje = 0;
            return;
        }
        if (envioEntityList.size() <= 9) {
            boolean repetido = false;
            for (EnvioEntity envio : envioEntityList) {
                if (nuevoEnvio.getPorcentaje() == envio.getPorcentaje()) {
                    repetido = true;
                    break;
                }
            }
            if (!repetido) {
                envioEntityList.add(nuevoEnvio);
                Collections.sort(envioEntityList, (p1, p2) -> Integer.valueOf(p1.getPorcentaje().intValue()).compareTo((p2.getPorcentaje().intValue())));
            } else {
                SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Este porcentaje ya está definido", null);
            }
        } else {
            SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Limite máximo alcanzado", null);
        }
        porcentaje = 0;
    }


    public String convertirStringEnvios() {
        StringBuilder view = new StringBuilder();
        if (envioEntityList != null && !envioEntityList.isEmpty()) {
            int contador = 0;
            List<EnvioEntity> lista2 = validarLista(envioEntityList);
            for (EnvioEntity entity : lista2) {
                contador++;
                view = view.append(entity.getPorcentaje());
                view = view.append("%");
                if (contador < lista2.size()) {
                    view = view.append("; ");
                }
            }
            alarmaEntityObj.setFrecuenciaNotificacion(view.toString());
        }
        return view.toString();
    }

    public String convertirStringEnvios(List<EnvioEntity> lista) {
        StringBuilder view = new StringBuilder();
        if (lista != null && !lista.isEmpty()) {
            int contador = 0;
            for (EnvioEntity entity : lista) {
                contador++;
                view = view.append(entity.getPorcentaje());
                view = view.append("%");
                if (contador < lista.size()) {
                    view = view.append("; ");
                }
            }
        }
        return view.toString();
    }


    public List<EnvioEntity> validarLista(List<EnvioEntity> lista) {
        List<EnvioEntity> envios = new ArrayList<>();
        for (EnvioEntity envio : lista) {
            if (!envio.isEliminar()) {
                envios.add(envio);
            }
        }
        return envios;
    }

    public boolean enviarEstado(EnvioEntity envio) {
        boolean valor = false;
        if (!envio.isEliminar()) {
            valor = true;
        }
        return valor;
    }

    public AlarmaEntity getAlarmaEntityObj() {
        return alarmaEntityObj;
    }

    public void setAlarmaEntityObj(AlarmaEntity alarmaEntityObj) {
        this.alarmaEntityObj = alarmaEntityObj;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AlarmaEntity getSelectalarmaEntityObj() {
        return selectalarmaEntityObj;
    }

    public void setSelectalarmaEntityObj(AlarmaEntity selectalarmaEntityObj) {
        this.selectalarmaEntityObj = selectalarmaEntityObj;
    }

    public String getTipoalarma() {
        return tipoalarma;
    }

    public void setTipoalarma(String tipoalarma) {
        this.tipoalarma = tipoalarma;
    }

    public List<AlarmaEntity> getAlarmaEntityList() {
        return alarmaEntityList;
    }

    public void setAlarmaEntityList(List<AlarmaEntity> alarmaEntityList) {
        this.alarmaEntityList = alarmaEntityList;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public List<EnvioEntity> getEnvioEntityList() {
        return envioEntityList;
    }

    public void setEnvioEntityList(List<EnvioEntity> envioEntityList) {
        this.envioEntityList = envioEntityList;
    }

    public Map<String, String> getEmailParameter() {
        return emailParameter;
    }

    public void setEmailParameter(Map<String, String> emailParameter) {
        this.emailParameter = emailParameter;
    }


}
