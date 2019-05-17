package myapps.abm.bean;

import myapps.abm.business.TipoTorreBL;
import myapps.abm.model.TipoTorreEntity;
import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import myapps.servicio_basico.util.SysMessage;
import myapps.servicio_basico.util.UtilFile;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.DescriptorBitacora;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static myapps.user.controler.Login.log;

@ManagedBean
@ViewScoped
public class TipoTorreBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(TipoTorreBean.class);

    @Inject
    private TipoTorreBL tipoTorreBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB

    private IApplicationContext beanApplicationContext;

    private List<TipoTorreEntity> tipoTorreEntityList = new ArrayList<>();

    private TipoTorreEntity tipoTorreEntityObj;
    private Map<String, String> validateParameter;
    private Map<String, String> misc;
    private boolean editTorres;
    private FileUploadEvent fileUploadEvent;

    @PostConstruct
    void init() {
        try {
            tipoTorreEntityObj = new TipoTorreEntity();
            loadTipoTorres();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            misc = beanApplicationContext.getMapMiscellaneousParameters();
        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }
    }

    public void newTorres() {
        tipoTorreEntityObj = new TipoTorreEntity();
        setEditTorres(false);
    }

    public void actionTorres() {
        try {
            if (tipoTorreEntityObj.getImagen() == null) {
                tipoTorreEntityObj.setImagen(UtilFile.loadFile(misc.get(EnumParametros.ICONO_PREDETERMINADO_RADIO_BASE.toString())));
            }
            if (isEditTorres() && tipoTorreEntityObj.getIdTipoTorre() > 0) {
                tipoTorreBL.update(tipoTorreEntityObj);
                controlerBitacora.update(DescriptorBitacora.TIPO_TORRES, String.valueOf
                        (getTipoTorreEntityObj().getIdTipoTorre()), getTipoTorreEntityObj()
                        .getTorre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                tipoTorreBL.save(tipoTorreEntityObj);
                controlerBitacora.insert(DescriptorBitacora.TIPO_TORRES, String.valueOf
                        (getTipoTorreEntityObj()
                                .getIdTipoTorre()), getTipoTorreEntityObj().getTorre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }
            loadTipoTorres();
            newTorres();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadTipoTorres() {
        tipoTorreEntityList = tipoTorreBL.listAll();
    }

    public void editarTorres(TipoTorreEntity torreEntity) {
        setTipoTorreEntityObj(torreEntity);
        setEditTorres(true);
    }

    public void deleteTorres() {
        try {
            tipoTorreBL.delete(tipoTorreBL.getById((Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("idTipoTorre")))));
            controlerBitacora.delete(DescriptorBitacora.TIPO_TORRES, String.valueOf
                    (getTipoTorreEntityObj()
                            .getIdTipoTorre()), getTipoTorreEntityObj().getTorre());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newTorres();
            loadTipoTorres();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public List<TipoTorreEntity> getTipoTorreEntityList() {
        return tipoTorreEntityList;
    }

    public void setTipoTorreEntityList(List<TipoTorreEntity> tipoTorreEntityList) {
        this.tipoTorreEntityList = tipoTorreEntityList;
    }

    public String convertirImagen(byte[] imagen) {
        String imageString = "";
        try {
            imageString = UtilFile.mostrarImagen(imagen);
        } catch (Exception e) {
            log.info("No se pudo convetir la imagen", null);
        }
        return imageString;
    }


    public void handleFileUpload(FileUploadEvent event) {
        try {
            InputStream is = event.getFile().getInputstream();
            BufferedImage img = ImageIO.read(is);
            if (img.getWidth() > 50 || img.getHeight() > 50) {
                SysMessage.warn(validateParameter.get(EnumParametros.SUMMARY_MSG.toString()), "Por favor seleccione una imagen de 50 px ", null);
                is.close();
                return;
            }
            setFileUploadEvent(event);
            tipoTorreEntityObj.setImagen(fileUploadEvent.getFile().getContents());
            SysMessage.handleFileUpload(event);
        } catch (Exception e) {
            log.log(Level.ERROR, e.getMessage());
        }
    }

    public TipoTorreEntity getTipoTorreEntityObj() {
        return tipoTorreEntityObj;
    }

    public void setTipoTorreEntityObj(TipoTorreEntity tipoTorreEntityObj) {
        this.tipoTorreEntityObj = tipoTorreEntityObj;
    }

    public boolean isEditTorres() {
        return editTorres;
    }

    public void setEditTorres(boolean editarTorres) {
        this.editTorres = editarTorres;
    }


    public Map<String, String> getValidateParameter() {
        return validateParameter;
    }

    public void setValidateParameter(Map<String, String> validateParameter) {
        this.validateParameter = validateParameter;
    }

    public FileUploadEvent getFileUploadEvent() {
        return fileUploadEvent;
    }

    public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
        this.fileUploadEvent = fileUploadEvent;
    }


}
