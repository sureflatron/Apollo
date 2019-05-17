package myapps.abm.business;

import myapps.abm.dao.DocumentosDAO;
import myapps.abm.model.DocumentosEntity;
import myapps.servicio_basico.util.UtilFile;
import myapps.user.bean.ControlerBitacora;
import myapps.user.ldap.DescriptorBitacora;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DocumentosBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(DocumentosBL.class);

    @Inject
    private DocumentosDAO documentosDAO;
    @Inject
    private ControlerBitacora controlerBitacora;

    public void save(DocumentosEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            documentosDAO.save(entity);

            UtilFile.writeBytesToFile(entity.getDocAdjunto(), entity.getUbicacion(),
                    entity.getPrefijo() + "-" + entity.getNombre(), "");

            String nameBitacora = entity.getUbicacion() + entity.getPrefijo() + "-" + entity.getNombre();
            controlerBitacora.insert(DescriptorBitacora.DOCUMENTOS, String.valueOf
                    (entity.getIdDocumento()), nameBitacora + " Pertenece:" + entity
                    .getIdPertenece());
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(DocumentosEntity entity) {
        try {
            documentosDAO.update(entity);
            controlerBitacora.update(DescriptorBitacora.DOCUMENTOS, String.valueOf
                    (entity.getIdDocumento()), entity.getUbicacion() + entity.getNombre() + " Pertenece:" + entity
                    .getIdPertenece());
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(DocumentosEntity entity) {
        try {
            entity.setEstado(false);
            documentosDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public void remove(DocumentosEntity entity) {
        try {
            documentosDAO.remove(entity);
            UtilFile.deleteFile(entity.getUbicacion() + entity.getPrefijo() + "-" + entity.getNombre
                    ());
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<DocumentosEntity> listAll(long idLic) {
        List<DocumentosEntity> list = new ArrayList<>();
        try {
            list = documentosDAO.listAll(idLic);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public DocumentosEntity getById(long id) {
        DocumentosEntity documentosEntity = new DocumentosEntity();
        try {
            documentosEntity = documentosDAO.getById(id);
            return documentosEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return documentosEntity;
    }

    public DocumentosEntity getByIdPertenece(long idPertenece, String textIdPertenece) {
        DocumentosEntity documentosEntity = new DocumentosEntity();
        try {
            documentosEntity = documentosDAO.getDocByIdPertenece(idPertenece, textIdPertenece);
            return documentosEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return documentosEntity;
    }

    public DocumentosEntity donwloadDoc(DocumentosEntity documentosEntity) {
        DocumentosEntity docByte = new DocumentosEntity();
        try {
            String pathDoc = documentosEntity.getUbicacion() + documentosEntity.getPrefijo() + "-" +
                    documentosEntity.getNombre();
            docByte.setDocAdjunto(UtilFile.loadFile(pathDoc));
            docByte.setNombre(documentosEntity.getNombre());
            docByte.setExtension(UtilFile.getExtensionFile(documentosEntity.getNombre()));
            return docByte;
        } catch (IOException e) {
            logger.log(Level.ERROR, "DOWNLOAD_DOC" + "|" + e.getMessage(), e);
        }
        return docByte;
    }

    public String contador(List<DocumentosEntity> lista) {
        int numero = 0;
        for (DocumentosEntity documento : lista) {
            if (!documento.getEliminar()) {
                numero++;
            }
        }
        return String.valueOf(numero);
    }

    public void actualizarLista(List<DocumentosEntity> documentosEntityList, long ide) {
        if (!documentosEntityList.isEmpty()) {
            for (DocumentosEntity documentosEntity : documentosEntityList) {
                documentosEntity.setIdPertenece(ide);
                documentosEntity.setEstado(actualizarEstado(documentosEntity));
                if (documentosEntity.getDocAdjunto() != null && !documentosEntity.getEliminar() && documentosEntity.getIdDocumento() == 0) {
                    save(documentosEntity);
                } else if (documentosEntity.getIdDocumento() > 0) {
                    update(documentosEntity);
                }
            }
        }
    }

    public void guardarLista(List<DocumentosEntity> documentosEntityList, long ide) {
        if (!documentosEntityList.isEmpty()) {
            for (DocumentosEntity documentosEntity : documentosEntityList) {
                documentosEntity.setIdPertenece(ide);
                if (!documentosEntity.getEliminar() && documentosEntity.getIdDocumento() == 0) {
                    save(documentosEntity);
                }
            }
        }
    }


    public boolean actualizarEstado(DocumentosEntity documentosEntity) {
        boolean valor = false;
        if (!documentosEntity.getEliminar()) {
            valor = true;
        }
        return valor;
    }
}
