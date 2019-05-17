package myapps.abm.business;


import myapps.abm.dao.InmuebleDAO;
import myapps.abm.model.*;
import myapps.cargainicial.ExcelUtil;
import myapps.servicio_basico.util.ImportXlsx;
import myapps.servicio_basico.util.Importar;
import myapps.servicio_basico.util.ResumenCarga;
import myapps.servicio_basico.util.UtilNumber;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class InmuebleBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(InmuebleBL.class);

    @Inject
    private InmuebleDAO inmuebleDAO;
    @Inject
    private LocalidadBL localidadBL;
    @Inject
    private TipoInmuebleBL tipoInmuebleBL;
    @Inject
    private PropiedadBL propiedadBL;
    @Inject
    private DocumentosBL documentosBL;

    private int nroColumn = 0;
    private int nroIncorrecto = 0;
    private int nroCorrecto = 0;

    public void save(InmuebleEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            inmuebleDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(InmuebleEntity entity) {
        try {
            inmuebleDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(InmuebleEntity entity) {
        try {
            entity.setEstado(false);
            inmuebleDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<InmuebleEntity> listAll() {
        List<InmuebleEntity> list = new ArrayList<>();
        try {
            list = inmuebleDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "listAll" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<InmuebleEntity> listAllByTipoInmueble(long tipo) {
        List<InmuebleEntity> list = new ArrayList<>();
        try {
            list = inmuebleDAO.listAllByIdTipoInmueble(tipo);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "listAllByTipoInmueble" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public InmuebleEntity getById(long id) {
        InmuebleEntity inmuebleEntity = new InmuebleEntity();
        try {
            inmuebleEntity = inmuebleDAO.getById(id);
            return inmuebleEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return inmuebleEntity;
    }

    public InmuebleEntity getByNombre(String nombre) {
        InmuebleEntity inmuebleEntity = new InmuebleEntity();
        try {
            inmuebleEntity = inmuebleDAO.getByNombre(nombre.toUpperCase());
            return inmuebleEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_NAME" + "|" + e.getMessage(), e);
        }
        return inmuebleEntity;
    }
    public InmuebleEntity getByCodInmueble(String codigo){
        InmuebleEntity inmuebleEntity = new InmuebleEntity();
        try {
            inmuebleEntity = inmuebleDAO.getByCodInmueble(codigo);
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_NAME" + "|" + e.getMessage(), e);
        }
        return inmuebleEntity;
    }

    public List<InmuebleEntity> listAllFilter(Map<String, Object> parameter) {
        List<InmuebleEntity> list = new ArrayList<>();
        try {
            list = inmuebleDAO.listAllFilter(parameter);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    @SuppressWarnings("rawtypes")
	public List<ArrayList> readFile(String ext, byte[] doc) {
        List<ArrayList> arrayLists = new ArrayList<>();

        List<ResumenCarga> resumenCargaList = new ArrayList<>();
        List<InmuebleEntity> inmuebleEntityList = new ArrayList<>();
        ResumenCarga resumenCarga = new ResumenCarga();
        nroCorrecto = 0;
        nroIncorrecto = 0;
        try {
            List<String[]> fileLoaded = new ArrayList<>();
            ExcelUtil excelUtilObj = new ExcelUtil();
            if (ext.equals(".xlsx")) {
                fileLoaded = ImportXlsx.leerrXlsx(doc);
                excelUtilObj = ImportXlsx.obtenerDatos(doc);
                nroColumn = excelUtilObj.getColumnas();

            }
            if (ext.equals(".xls")) {
                fileLoaded = Importar.leerExcel(doc);
                excelUtilObj = Importar.obtenerDatos(doc);
                nroColumn = excelUtilObj.getColumnas();
            }
            resumenCarga.setNroColumn(String.valueOf(excelUtilObj.getColumnas()));
            resumenCarga.setNroFilas(String.valueOf(excelUtilObj.getRows() - 1));
            int datosCont = 0;
            for (String[] datosArray : fileLoaded) {
                if (datosArray.length > 1) {
                    InmuebleEntity inmuebleEntity = null;
                    if (datosCont > 0) {
                        String[] datos = validacionDatos(datosArray,ext);
                        if (datos.length > 0) {
                            contadorDatosNoValidos(datos);
                            inmuebleEntity = new InmuebleEntity();

                            LocalidadEntity localidad = new LocalidadEntity();
                            localidad.setNombre(datos[0]);
                            inmuebleEntity.setLocalidadByIdLocalidad(localidad);

                            inmuebleEntity.setSitioid(datos[1]);
                            inmuebleEntity.setNombreRbs(datos[2] != null ? datos[2] : "");

                            TipoInmuebleEntity tipoInmuebleEntity = new TipoInmuebleEntity();
                            tipoInmuebleEntity.setNombre(datos[3]);
                            inmuebleEntity.setTipoInmuebleByIdTipoInmueble(tipoInmuebleEntity);
                            PropiedadEntity propiedadEntity = new PropiedadEntity();
                            propiedadEntity.setNombre(datos[4]);
                            inmuebleEntity.setPropiedadByIdPropiedad(propiedadEntity);

                            inmuebleEntity.setDireccion(datos[5]);
                            inmuebleEntity.setCodInmueble(datos[6]);
                            inmuebleEntity.setCodCve(datos[7]);
                            inmuebleEntity.setCostoInstall(datos[8].replace(',','.'));
                            inmuebleEntity.setEstadoInmueble(datos[9]);
                            inmuebleEntity.setLatitud(datos[10].replace(',', '.'));
                            inmuebleEntity.setLongitud(datos[11].replace(',', '.'));
                            inmuebleEntity.setObservaciones(datos[12]);
                            inmuebleEntity.setSector(datos[13]);
                        }

                    }
                    if (inmuebleEntity != null) {
                        inmuebleEntityList.add(inmuebleEntity);
                    }
                }
                datosCont++;
            }

            resumenCarga.setNroRegistrosValidos(String.valueOf(nroCorrecto));
            resumenCarga.setNroRegistrosNoValidos(String.valueOf(nroIncorrecto));
            resumenCargaList.add(resumenCarga);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.log(Level.ERROR, "readFile - Fuera de rango" + "|" + e.getMessage(), e);
        }

        arrayLists.add((ArrayList<InmuebleEntity>) inmuebleEntityList);
        arrayLists.add((ArrayList<ResumenCarga>) resumenCargaList);
        
        return arrayLists;
    }

    private String[] validacionDatos(String[] datos, String ext) {
        String[] datoValidated = new String[nroColumn];
        int contEmptyXLSX = 1;
        int contEmptyXLS = 0;
        if (ext.equals(".xls")) {
            for (String datosString : datos) {
                if (datosString.equals("")) {
                    contEmptyXLS++;
                }
            }
        }

        if (contEmptyXLS == nroColumn) {
            return new String[0];
        } else {
            for (int i = 0; i < datoValidated.length; i++) {

                if (datos[i] != null) {
                    datoValidated[i] = datos[i];
                } else {
                    datoValidated[i] = "";
                    contEmptyXLSX++;
                }
            }
            if (contEmptyXLSX == nroColumn) {
                return new String[0];
            }
        }

        return datoValidated;
    }

    private void contadorDatosNoValidos(String[] datos) {
        boolean isValid = true;
        for (String dato : datos) {
            isValid = !dato.equals("") && isValid;
        }
        if (isValid) {
            nroCorrecto++;
        } else {
            nroIncorrecto++;
        }
    }

    @SuppressWarnings("rawtypes")
	public List<ArrayList> saveInmIni(List<InmuebleEntity> inmuebleEntityList,
                                      DocumentosEntity documentEntity) {
        List<InmuebleEntity> entityListError = new ArrayList<>();
        List<ArrayList> arrayLists = new ArrayList<>();
        List<String> obs = new ArrayList<>();
        try {
            int contSaveInm = 0;
            for (InmuebleEntity inmuebleEntity : inmuebleEntityList) {
                StringBuilder erro = new StringBuilder();
                InmuebleEntity inmueble = this.getByNombre(inmuebleEntity.getNombreRbs());
                boolean inmuebleExist = false;
                if ((inmueble.getIdInmueble() > 0) || validateFieldEmpty(inmuebleEntity)) {
                    inmuebleExist = true;
                    erro.append(inmueble.getNombreRbs()).append(',');
                }

                boolean localidad = false;
                LocalidadEntity localidadEntity = localidadBL.getByNombre(inmuebleEntity
                        .getLocalidadByIdLocalidad().getNombre());
                if (localidadEntity.getIdLocalidad() > 0) {
                    inmuebleEntity.setLocalidadByIdLocalidad(localidadEntity);
                } else {
                    localidad = true;
                    erro.append(inmuebleEntity
                            .getLocalidadByIdLocalidad().getNombre()).append(',');
                }
                boolean tipoInmueble = false;
                TipoInmuebleEntity tipoInmuebleEntity = tipoInmuebleBL.getByNombre(inmuebleEntity
                        .getTipoInmuebleByIdTipoInmueble().getNombre());
                if (tipoInmuebleEntity.getIdTipoInmueble() > 0) {
                    inmuebleEntity.setTipoInmuebleByIdTipoInmueble(tipoInmuebleEntity);
                } else {
                    tipoInmueble = true;
                    erro.append(inmuebleEntity
                            .getTipoInmuebleByIdTipoInmueble().getNombre()).append(',');
                }
                boolean propiedad = false;
                PropiedadEntity propiedadEntity = propiedadBL.getByNombre(inmuebleEntity
                        .getPropiedadByIdPropiedad().getNombre());
                if (propiedadEntity.getIdPropiedad() > 0) {
                    inmuebleEntity.setPropiedadByIdPropiedad(propiedadEntity);
                } else {
                    propiedad = true;
                    erro.append(inmuebleEntity
                            .getPropiedadByIdPropiedad().getNombre());
                }
                boolean isNotNumeric = false;
                if (!UtilNumber.validateMonto(inmuebleEntity.getCostoInstall())) {
                    isNotNumeric = true;
                    erro.append(inmuebleEntity.getCostoInstall());
                }
                if (inmuebleExist || localidad || tipoInmueble || propiedad || isNotNumeric) {
                    inmuebleEntity.setObsErrors(erro.toString());
                    entityListError.add(inmuebleEntity);
                    continue;
                }
                inmuebleEntity.setcInicial(Long.valueOf(documentEntity.getPrefijo()));

                this.save(inmuebleEntity);
                contSaveInm++;

                if (contSaveInm == 1) {
                    documentosBL.save(documentEntity);
                }
            }
            obs.add(String.valueOf(contSaveInm));
            arrayLists.add((ArrayList<InmuebleEntity>) entityListError);
            arrayLists.add((ArrayList<String>) obs);
        } catch (Exception e) {
            logger.log(Level.ERROR, "saveInmIni" + "|" + e.getMessage(), e);
        }
        return arrayLists;
    }
    private boolean validateFieldEmpty(InmuebleEntity inmuebleEntity) {
        return inmuebleEntity.getSitioid().equals("") || inmuebleEntity.getNombreRbs().equals("")
                || inmuebleEntity.getCodInmueble().equals("") || inmuebleEntity.getCodCve().equals("") || inmuebleEntity.getCostoInstall().equals("");

    }
}
