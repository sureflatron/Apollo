package myapps.abm.business;

import myapps.abm.dao.RadioBaseDAO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class RadioBaseBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(RadioBaseBL.class);

    @Inject
    private RadioBaseDAO inmuebleDAO;
    @Inject
    private LocalidadBL localidadBL;
    @Inject
    private PropiedadBL propiedadBL;
    @Inject
    private TipoTorreBL tipoTorreBL;
    @Inject
    private ConstructorBL constructorBL;
    @Inject
    private DocumentosBL documentosBL;

    private int nroColumn = 0;
    private int nroIncorrecto = 0;
    private int nroCorrecto = 0;

    public void save(RadioBaseEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado((long) 1);
            inmuebleDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(RadioBaseEntity entity) {
        try {
            inmuebleDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(RadioBaseEntity entity) {
        try {
            entity.setEstado((long) 0);
            inmuebleDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<RadioBaseEntity> listAll() {
        List<RadioBaseEntity> list = new ArrayList<>();
        try {
            list = inmuebleDAO.listAll();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }
    public List<RadioBaseEntity> listAllByTipoTorre(long idTipoTorre) {
        List<RadioBaseEntity> list = new ArrayList<>();
        try {
            list = inmuebleDAO.listAllByTipoTorre(idTipoTorre);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<RadioBaseEntity> listAllFilter(RadioBaseEntity radioBaseEntityObj) {
        Map<String, Object> parameter = new HashMap<>();
        if (radioBaseEntityObj != null) {
            if (!radioBaseEntityObj.getSitioid().equals("")) {
                parameter.put("sitioId", radioBaseEntityObj
                        .getSitioid());
            }
            if (!radioBaseEntityObj.getNombreRbs().equals("")) {
                parameter.put("nombreRbs", radioBaseEntityObj.getNombreRbs());
            }
            if (radioBaseEntityObj.getPropiedadByIdPropiedad().getIdPropiedad() > 0) {
                parameter.put("idProp", radioBaseEntityObj.getPropiedadByIdPropiedad()
                        .getIdPropiedad());
            }
            if (!radioBaseEntityObj.getCodInmueble().equals("")) {
                parameter.put("idIn", radioBaseEntityObj.getCodInmueble());
            }
            if (radioBaseEntityObj.getTipoTorreByIdTipoTorre().getIdTipoTorre() > 0) {
                parameter.put("idTTo", radioBaseEntityObj.getTipoTorreByIdTipoTorre()
                        .getIdTipoTorre());
            }
            if (radioBaseEntityObj.getLocalidadByIdLocalidad().getIdLocalidad() > 0) {
                parameter.put("idLoc", radioBaseEntityObj.getLocalidadByIdLocalidad().getIdLocalidad
                        ());
            }
            MunicipioEntity municipioEntity = radioBaseEntityObj.getLocalidadByIdLocalidad()
                    .getMunicipioByIdMunicipio();
            ProvinciaEntity provinciaEntity = municipioEntity.getProvinciaByIdProvincia();
            DepartamentoEntity departamentoEntity = provinciaEntity
                    .getDepartamentoByIdDepartamento();

            if (departamentoEntity.getIdDepartamento() > 0) {
                parameter.put("idDep", departamentoEntity.getIdDepartamento());
            }
            if (provinciaEntity.getIdeProvincia() > 0) {
                parameter.put("idProv", provinciaEntity.getIdeProvincia());
            }
            if (municipioEntity.getIdMunicipio() > 0) {
                parameter.put("idMun", municipioEntity.getIdMunicipio());
            }
        }

        List<RadioBaseEntity> list = new ArrayList<>();
        try {
            list = inmuebleDAO.listAllFilter(parameter);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECT" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public RadioBaseEntity getById(long id) {
        RadioBaseEntity radioBaseEntity = new RadioBaseEntity();
        try {
            radioBaseEntity = inmuebleDAO.getById(id);
            return radioBaseEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return radioBaseEntity;
    }

    public RadioBaseEntity getByNombre(String nombre) {
        RadioBaseEntity radioBaseEntity = new RadioBaseEntity();
        try {
            radioBaseEntity = inmuebleDAO.getByNombre(nombre.toUpperCase());
            return radioBaseEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_NAME" + "|" + e.getMessage(), e);
        }
        return radioBaseEntity;
    }
    public RadioBaseEntity getByCodRadioBase(String codigo) {
        RadioBaseEntity radioBaseEntity = new RadioBaseEntity();
        try {
            radioBaseEntity = inmuebleDAO.getByCodRadioBase(codigo);
        } catch (Exception e) {
            logger.log(Level.ERROR, "getByCodRadioBase - BL" + "|" + e.getMessage(), e);
        }
        return radioBaseEntity;
    }

    @SuppressWarnings("rawtypes")
	public List<ArrayList> readFile(String ext, byte[] path) {
        List<ArrayList> arrayLists = new ArrayList<>();

        List<ResumenCarga> resumenCargaList = new ArrayList<>();
        List<RadioBaseEntity> radioBaseEntityList = new ArrayList<>();
        ResumenCarga resumenCarga = new ResumenCarga();
        nroCorrecto = 0;
        nroIncorrecto = 0;
        try {
           List<String[]> fileLoaded = new ArrayList<>();
            ExcelUtil excelUtilObj = new ExcelUtil();
            if (ext.equals(".xlsx")) {
                fileLoaded = ImportXlsx.leerrXlsx(path);
                excelUtilObj = ImportXlsx.obtenerDatos(path);
                nroColumn = excelUtilObj.getColumnas();
            }
            if (ext.equals(".xls")) {
                fileLoaded = Importar.leerExcel(path);
                excelUtilObj = Importar.obtenerDatos(path);
                nroColumn = excelUtilObj.getColumnas();
            }
            resumenCarga.setNroColumn(String.valueOf(excelUtilObj.getColumnas()));
            resumenCarga.setNroFilas(String.valueOf(excelUtilObj.getRows() - 1));
            int datosCont = 0;
            for (String[] datosArray : fileLoaded) {
                if (datosArray.length > 1) {
                    RadioBaseEntity radioBaseEntity = null;
                    if (datosCont > 0) {
                        String[] datos = validacionDatos(datosArray, ext);
                        if (datos.length > 0) {
                            contadorDatosNoValidos(datos);
                            radioBaseEntity = new RadioBaseEntity();

                            LocalidadEntity localidad = new LocalidadEntity();
                            localidad.setNombre(datos[0]);
                            radioBaseEntity.setLocalidadByIdLocalidad(localidad);

                            radioBaseEntity.setSitioid(datos[1]);
                            radioBaseEntity.setNombreRbs(datos[2]);

                            PropiedadEntity propiedadEntity = new PropiedadEntity();
                            propiedadEntity.setNombre(datos[3]);
                            radioBaseEntity.setPropiedadByIdPropiedad(propiedadEntity);

                            radioBaseEntity.setDireccion(datos[4]);
                            radioBaseEntity.setCodInmueble(datos[5]);

                            TipoTorreEntity tipoTorreEntity = new TipoTorreEntity();
                            tipoTorreEntity.setTorre(datos[6]);
                            radioBaseEntity.setTipoTorreByIdTipoTorre(tipoTorreEntity);

                            radioBaseEntity.setAltura(datos[7]);
                            radioBaseEntity.setCodCve(datos[8]);
                            radioBaseEntity.setCostoInstall(datos[9].replace(',','.'));
                            radioBaseEntity.setAreax1(datos[10]);
                            radioBaseEntity.setAreax2(datos[11]);
                            radioBaseEntity.setDcattt((datos[12].equals("TRUE")) ? "SI" : "NO");
                            radioBaseEntity.setEstadoInmueble(datos[13]);

                            ConstructorEntity constructorEntity = new ConstructorEntity();
                            constructorEntity.setNombre(datos[14]);
                            radioBaseEntity.setConstructorByIdConstructor(constructorEntity);

                            radioBaseEntity.setLatitud(datos[15].replace(',', '.'));
                            radioBaseEntity.setLongitud(datos[16].replace(',', '.'));
                            radioBaseEntity.setObservaciones(datos[17]);
                            radioBaseEntity.setAreay1(datos[18]);
                            radioBaseEntity.setAreay2(datos[19]);
                        }
                    }
                    if (radioBaseEntity != null) {
                        radioBaseEntityList.add(radioBaseEntity);
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
        arrayLists.add((ArrayList<RadioBaseEntity>) radioBaseEntityList);
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
	public List<ArrayList> saveRdbIni(List<RadioBaseEntity> radioBaseEntityList, DocumentosEntity documentEntity) {
        List<RadioBaseEntity> entityListError = new ArrayList<>();
        List<ArrayList> arrayLists = new ArrayList<>();
        List<String> obs = new ArrayList<>();
        try {
            int contSaveInm = 0;
            for (RadioBaseEntity radioBaseEntity : radioBaseEntityList) {
                StringBuilder erro = new StringBuilder();
                RadioBaseEntity rdbs = this.getByNombre(radioBaseEntity.getNombreRbs());
                boolean rdbsExist = false;
                if ((rdbs.getIdRadioBase() > 0) || validateFieldEmpty(radioBaseEntity)){

                    rdbsExist = true;
                    erro.append(rdbs.getNombreRbs()).append(',');
                }

                boolean localidad = false;
                LocalidadEntity localidadEntity = localidadBL.getByNombre(radioBaseEntity
                        .getLocalidadByIdLocalidad().getNombre());
                if (localidadEntity.getIdLocalidad() > 0) {
                    radioBaseEntity.setLocalidadByIdLocalidad(localidadEntity);
                } else {
                    localidad = true;
                    erro.append(radioBaseEntity.getLocalidadByIdLocalidad().getNombre()).append(',');
                }
                boolean tipoTorre = false;
                TipoTorreEntity tipoTorreEntity = tipoTorreBL.getByNombre(radioBaseEntity
                        .getTipoTorreByIdTipoTorre().getTorre());
                if (tipoTorreEntity.getIdTipoTorre() > 0) {
                    radioBaseEntity.setTipoTorreByIdTipoTorre(tipoTorreEntity);
                } else {
                    tipoTorre = true;
                    erro.append(radioBaseEntity.getTipoTorreByIdTipoTorre().getTorre()).append(',');
                }
                boolean propiedad = false;
                PropiedadEntity propiedadEntity = propiedadBL.getByNombre(radioBaseEntity
                        .getPropiedadByIdPropiedad().getNombre());
                if (propiedadEntity.getIdPropiedad() > 0) {
                    radioBaseEntity.setPropiedadByIdPropiedad(propiedadEntity);
                } else {
                    propiedad = true;
                    erro.append(radioBaseEntity.getPropiedadByIdPropiedad().getNombre());
                }
                boolean constructor = false;
                ConstructorEntity constructorEntity = constructorBL.getByNombre(radioBaseEntity
                        .getConstructorByIdConstructor().getNombre());
                if (constructorEntity.getIdConstructor() > 0) {
                    radioBaseEntity.setConstructorByIdConstructor(constructorEntity);
                } else {
                    constructor = true;
                    erro.append(radioBaseEntity.getConstructorByIdConstructor().getNombre());
                }
                boolean isNotNumeric = false;
                if (!UtilNumber.validateMonto(radioBaseEntity.getCostoInstall())) {
                    isNotNumeric = true;
                    erro.append(radioBaseEntity.getCostoInstall());
                }
                if (rdbsExist || localidad || tipoTorre || propiedad || constructor || isNotNumeric) {
                    radioBaseEntity.setObsErrors(erro.toString());
                    entityListError.add(radioBaseEntity);
                    continue;
                }
                radioBaseEntity.setcInicial(Long.valueOf(documentEntity.getPrefijo()));
                radioBaseEntity.setDcattt((radioBaseEntity.getDcattt().equals("SI"))
                        ? "true" : "false");
                this.save(radioBaseEntity);
                contSaveInm++;

                if (contSaveInm == 1) {
                    documentosBL.save(documentEntity);
                }
            }
            obs.add(String.valueOf(contSaveInm));
            arrayLists.add((ArrayList<RadioBaseEntity>) entityListError);
            arrayLists.add((ArrayList<String>) obs);
        } catch (Exception e) {
            logger.log(Level.ERROR, "saveInmIni" + "|" + e.getMessage(), e);
        }
        return arrayLists;
    }

    private boolean validateFieldEmpty(RadioBaseEntity radioBaseEntity) {
        return radioBaseEntity.getSitioid().equals("") || radioBaseEntity.getNombreRbs().equals("")
                || radioBaseEntity.getCodInmueble().equals("") || radioBaseEntity.getCodCve().equals("") || radioBaseEntity.getCostoInstall().equals("");

    }

}
