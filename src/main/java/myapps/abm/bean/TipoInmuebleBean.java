package myapps.abm.bean;

import myapps.abm.business.TipoInmuebleBL;
import myapps.abm.model.TipoInmuebleEntity;
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
public class TipoInmuebleBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(TipoInmuebleBean.class);

    @Inject
    private TipoInmuebleBL tiBL;
    @Inject
    private ControlerBitacora controlerBitacora;
    @EJB
    private IApplicationContext beanApplicationContext;
    private List<TipoInmuebleEntity> tipoInmuebleEntityList = new ArrayList<>();
    private TipoInmuebleEntity tipoInmuebleEntityObj;

    private boolean edit;
    private Map<String, String> validateParameter;
    private Map<String, String> misc;
    private FileUploadEvent fileUploadEvent;

    @PostConstruct
    void init() {
        try {
            tipoInmuebleEntityObj = new TipoInmuebleEntity();
            validateParameter = beanApplicationContext.getMapValidateParameters();
            misc = beanApplicationContext.getMapMiscellaneousParameters();
            loadTIs();

        } catch (Exception e) {
            logger.error("[init] Fallo en el init.", e);
        }

    }

    public void newTI() {
        edit = false;
        tipoInmuebleEntityObj = new TipoInmuebleEntity();
    }

    public void actionTI() {
        try {
            if (tipoInmuebleEntityObj.getImagen() == null) {
                tipoInmuebleEntityObj.setImagen(UtilFile.loadFile(misc.get(EnumParametros.ICONO_PREDETERMINADO_INMUEBLE.toString())));
            }
            if (isEdit() && tipoInmuebleEntityObj.getIdTipoInmueble() > 0) {
                tiBL.update(tipoInmuebleEntityObj);
                controlerBitacora.update(DescriptorBitacora.TIPO_INMUEBLE, String.valueOf
                        (getTipoInmuebleEntityObj().getIdTipoInmueble()), getTipoInmuebleEntityObj()
                        .getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Actualización satisfactoria", "");
            } else {
                tiBL.save(tipoInmuebleEntityObj);
                controlerBitacora.insert(DescriptorBitacora.TIPO_INMUEBLE, String.valueOf(getTipoInmuebleEntityObj()
                        .getIdTipoInmueble()), getTipoInmuebleEntityObj().getNombre());
                SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Registro satisfactorio", "");
            }

            loadTIs();
            newTI();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al guardar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    private void loadTIs() {
        tipoInmuebleEntityList = tiBL.listAll();
    }

    public void editTI(TipoInmuebleEntity tipoInmuebleEntity) {
        setTipoInmuebleEntityObj(tipoInmuebleEntity);
        setEdit(true);

    }

    public void deleteTI(TipoInmuebleEntity tipoInmuebleEntity) {
        try {
            setTipoInmuebleEntityObj(tipoInmuebleEntity);
            tiBL.delete(getTipoInmuebleEntityObj());
            controlerBitacora.delete(DescriptorBitacora.TIPO_INMUEBLE, String.valueOf
                    (getTipoInmuebleEntityObj()
                            .getIdTipoInmueble()), getTipoInmuebleEntityObj().getNombre());
            SysMessage.info(validateParameter.get(EnumParametros.SUMMARY_INFO.toString()), "Se elimino el registro", "");
            newTI();
            loadTIs();
        } catch (Exception e) {
            SysMessage.error(validateParameter.get(EnumParametros.SUMMARY_ERROR.toString()), "Error al eliminar el registro: " + e.getMessage(), "");
            logger.log(Level.ERROR, e.getMessage());
        }
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
            tipoInmuebleEntityObj.setImagen(fileUploadEvent.getFile().getContents());
            SysMessage.handleFileUpload(event);
        } catch (Exception e) {
            log.log(Level.ERROR, e.getMessage());
        }
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

    public List<TipoInmuebleEntity> getTipoInmuebleEntityList() {
        return tipoInmuebleEntityList;
    }

    public void setTipoInmuebleEntityList(List<TipoInmuebleEntity> tipoInmuebleEntityList) {
        this.tipoInmuebleEntityList = tipoInmuebleEntityList;
    }


    public TipoInmuebleEntity getTipoInmuebleEntityObj() {
        return tipoInmuebleEntityObj;
    }

    public void setTipoInmuebleEntityObj(TipoInmuebleEntity tipoInmuebleEntityObj) {
        this.tipoInmuebleEntityObj = tipoInmuebleEntityObj;
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

    public FileUploadEvent getFileUploadEvent() {
        return fileUploadEvent;
    }

    public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
        this.fileUploadEvent = fileUploadEvent;
    }

}
