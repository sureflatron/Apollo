package myapps.abm.business;

import myapps.abm.dao.ServiciooDAO;
import myapps.abm.model.*;
import myapps.cargainicial.CargaInicialServiciosDto;
import myapps.servicio_basico.util.ImportXlsx;
import myapps.servicio_basico.util.ResumeLoadExcel;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static myapps.servicio_basico.util.ValidarUtil.isEmptyValue;

@Stateless
public class ServicioBL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ServicioBL.class);

    @Inject
    private ServiciooDAO serviciooDAO;
    @Inject
    private InmServBL inmServBL;
    @Inject
    private InmuebleBL inmuebleBL;
    @Inject
    private RdbsServBL rdbsServBL;
    @Inject
    private RadioBaseBL radioBaseBL;
    @Inject
    private DocumentosBL documentosBL;
    @Inject
    private TipoServicioBL tipoServicioBL;
    @Inject
    private ProveedorBL proveedorBL;
    @Inject
    private BancoBL bancoBL;
    @Inject
    private MonedaBL monedaBL;
    @Inject
    private UOBL uobl;


    private int numValidFields = 0;
    private int numNoValidFields = 0;
    private boolean isEmptyAll = true;

    private CargaInicialServiciosDto dtoIni;
    private ServicioEntity datosValidos;
    private CargaInicialServiciosDto datosNoValidos;

    public void save(ServicioEntity entity) {
        logger.log(Level.INFO, "SAVE");
        try {
            entity.setEstado(true);
            serviciooDAO.save(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "SAVE" + "|" + e.getMessage(), e);
        }
    }

    public void update(ServicioEntity entity) {
        try {
            serviciooDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "UPDATE" + "|" + e.getMessage(), e);
        }
    }

    public void delete(ServicioEntity entity) {
        try {
            entity.setEstado(false);
            serviciooDAO.update(entity);
        } catch (Exception e) {
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
    }

    public List<ServicioEntity> listServicios(){
        List<ServicioEntity> list = new ArrayList<>();
        try {
            list = serviciooDAO.listAll();
        }catch (Exception e){
            logger.log(Level.ERROR, "DELETE" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<ServicioEntity> listAll() {
        List<ServicioEntity> list = new ArrayList<>();
        try {
            list = serviciooDAO.listAll();
            int cont = 0;
            for (ServicioEntity servicioEntity : list) {

                InmServEntity inmServEntityOjb = inmServBL.getByIdServicio(servicioEntity
                        .getIdServicio());
                if (inmServEntityOjb.getIdInmServ() > 0) {
                    InmuebleEntity inmuebleEntity = inmuebleBL.getById(inmServEntityOjb.getInmuebleByIdInmueble().getIdInmueble());
                    servicioEntity.setNombreTipoInmueble(inmuebleEntity.getTipoInmuebleByIdTipoInmueble().getNombre());
                    servicioEntity.setLatitud(inmuebleEntity.getLatitud());
                    servicioEntity.setLonguitud(inmuebleEntity.getLongitud());
                    servicioEntity.setImagen(inmuebleEntity.getTipoInmuebleByIdTipoInmueble().getImagen());
                    servicioEntity.setInmuebleEntity(inmuebleEntity);
                    servicioEntity.setPertenece("INMUEBLE");
                    list.set(cont, servicioEntity);
                } else {
                    RdbServEntity rdbServEntityObj = rdbsServBL.getByIdServicio(servicioEntity
                            .getIdServicio());
                    RadioBaseEntity radioBaseEntity = radioBaseBL.getById(rdbServEntityObj.getRadioBaseByIdRadioBase().getIdRadioBase());
                    servicioEntity.setNombreTipoInmueble(radioBaseEntity.getTipoTorreByIdTipoTorre().getTorre());
                    servicioEntity.setLatitud(radioBaseEntity.getLatitud());
                    servicioEntity.setLonguitud(radioBaseEntity.getLongitud());
                    servicioEntity.setImagen(radioBaseEntity.getTipoTorreByIdTipoTorre().getImagen());
                    servicioEntity.setRadioBaseEntity(radioBaseEntity);
                    servicioEntity.setPertenece("RADIO BASE");
                    list.set(cont, servicioEntity);
                }
                cont++;
            }
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECTALL" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<ServicioEntity> listAllnotMedidor() {
        List<ServicioEntity> list = new ArrayList<>();
        try {
            list = serviciooDAO.listAllnotMedidor();
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECTAllNOMEDIDOR" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public List<ServicioEntity> listAllFilter(Map<String, Object> parameter, int selectQuery) {
        List<ServicioEntity> list = new ArrayList<>();
        try {
            list = serviciooDAO.listAllFilter(parameter, selectQuery);
            return list;
        } catch (Exception e) {
            logger.log(Level.ERROR, "SELECTALLFILTER" + "|" + e.getMessage(), e);
        }
        return list;
    }

    public ServicioEntity getById(long id) {
        ServicioEntity servicioEntity = new ServicioEntity();
        try {
            servicioEntity = serviciooDAO.getById(id);
            return servicioEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "GET_BY_ID" + "|" + e.getMessage(), e);
        }
        return servicioEntity;
    }

    public ServicioEntity buscar(String asociado) {
        ServicioEntity servicioEntity = new ServicioEntity();
        try {
            servicioEntity = serviciooDAO.buscar(asociado);
            return servicioEntity;
        } catch (Exception e) {
            logger.log(Level.ERROR, "Error al buscar" + "|" + e.getMessage(), e);
        }
        return servicioEntity;
    }

    @SuppressWarnings("rawtypes")
	public List<ArrayList> readFile(byte[] doc) {
        List<ArrayList> dataReaded = new ArrayList<>();
        numValidFields = 0;
        numNoValidFields = 0;
        try {
            List<CargaInicialServiciosDto> dtoList = new ArrayList<>();
            List<ResumeLoadExcel> resumeLoadList = new ArrayList<>();
            ResumeLoadExcel resumeLoadExcel;
            ImportXlsx importXlsx = new ImportXlsx(doc);
            List<String[]> fileReaded = importXlsx.getExcelReaded();
            resumeLoadExcel = importXlsx.getResumeLoadExcel();
            for (String[] datosArray : fileReaded) {

                CargaInicialServiciosDto dto = new CargaInicialServiciosDto();
                dto.setNombreRdbInm(isEmptyValue(datosArray[0]));
                dto.setCodRdbInm(isEmptyValue(datosArray[1]));
                dto.setTipoServicio(isEmptyValue(datosArray[2]));
                dto.setIdSite(isEmptyValue(datosArray[3]));
                dto.setCodigoServicio(isEmptyValue(datosArray[4]));
                dto.setNombreServicio(isEmptyValue(datosArray[5]));
                dto.setProveedor(isEmptyValue(datosArray[6]));
                dto.setCodServMed(isEmptyValue(datosArray[7]));
                dto.setCodFijo(isEmptyValue(datosArray[8]));
                dto.setFormaPago(isEmptyValue(datosArray[9]));
                dto.setBanco(isEmptyValue(datosArray[10]));
                dto.setMoneda(isEmptyValue(datosArray[11]));
                dto.setNroCuenta(isEmptyValue(datosArray[12]));
                dto.setObs(isEmptyValue(datosArray[13]));
                dto.setUnidadOperativa(isEmptyValue(datosArray[14]));
                validos(dto);
                if (!isEmptyAll) {
                    dtoList.add(dto);
                }

            }
            resumeLoadExcel.setValidFields(this.numValidFields);
            resumeLoadExcel.setNoValidFields(this.numNoValidFields);
            resumeLoadList.add(resumeLoadExcel);

            dataReaded.add((ArrayList<CargaInicialServiciosDto>) dtoList);
            dataReaded.add((ArrayList<ResumeLoadExcel>) resumeLoadList);
        } catch (Exception e) {
            logger.log(Level.ERROR, "readFile" + "|" + e.getMessage(), e);
        }
        return dataReaded;
    }
   
    private void validos(CargaInicialServiciosDto dto) {

        isEmptyAll = dto.getNombreRdbInm().equals("")
                && dto.getCodRdbInm().equals("")
                && dto.getTipoServicio().equals("")
                && dto.getIdSite().equals("")
                && dto.getCodigoServicio().equals("")
                && dto.getNombreServicio().equals("")
                && dto.getProveedor().equals("")
                && dto.getCodServMed().equals("")
                && dto.getFormaPago().equals("")
                && dto.getBanco().equals("")
                && dto.getMoneda().equals("")
                && dto.getNroCuenta().equals("")
                && dto.getObs().equals("")
                && dto.getUnidadOperativa().equals("");

        if (validarObligatorios(dto) && !isEmptyAll) {
            numNoValidFields++;
        }
        if (!validarObligatorios(dto) && !isEmptyAll) {
            numValidFields++;
        }


    }

    public boolean validarObligatorios(CargaInicialServiciosDto dto) {
        return dto.getNombreRdbInm().equals("")
                || dto.getCodRdbInm().equals("")
                || dto.getTipoServicio().equals("")
                || dto.getIdSite().equals("")
                || dto.getCodigoServicio().equals("")
                || dto.getNombreServicio().equals("")
                || dto.getProveedor().equals("")
                || dto.getCodServMed().equals("")
                || dto.getFormaPago().equals("")
                || dto.getBanco().equals("")
                || dto.getMoneda().equals("")
                || dto.getNroCuenta().equals("")
                || dto.getUnidadOperativa().equals("");
    }

    @SuppressWarnings("rawtypes")
	public List<ArrayList> saveServBasicosIni(List<CargaInicialServiciosDto> dtoList,
                                              DocumentosEntity documentEntity) {
        List<ArrayList> resultSave = new ArrayList<>();
        List<ServicioEntity> servValidos = new ArrayList<>();        
        List<CargaInicialServiciosDto> resultServBasicErrors = new ArrayList<>();

        try {

            for (CargaInicialServiciosDto dto : dtoList) {
                ServicioEntity servicioEntity = new ServicioEntity();
                dtoIni = new CargaInicialServiciosDto();
                dtoIni = dto;
                validarDatos();
                if ((datosValidos != null) && datosNoValidos.getError().equals("")) {
                    servicioEntity.setTipoServicioByIdTipoServicio(datosValidos.getTipoServicioByIdTipoServicio());
                    servicioEntity.setProveedorByIdProveedor(datosValidos.getProveedorByIdProveedor());
                    servicioEntity.setBancoByIdBanco(datosValidos.getBancoByIdBanco());
                    servicioEntity.setMonedaByIdMoneda(datosValidos.getMonedaByIdMoneda());
                    servicioEntity.setUnidadOperativaByIdUnidadOperativa(datosValidos.getUnidadOperativaByIdUnidadOperativa());

                    servicioEntity.setIdSite(dto.getIdSite());
                    servicioEntity.setCodigo(dto.getCodigoServicio());
                    servicioEntity.setNombreSite(dto.getNombreServicio());
                    servicioEntity.setCodServMed(dto.getCodServMed());
                    servicioEntity.setCodFijo(dto.getCodFijo());
                    servicioEntity.setFormaPago(dto.getFormaPago());
                    servicioEntity.setNroCuenta(dto.getNroCuenta());
                    servicioEntity.setObservaciones(dto.getObs());

                    saveServicios(servicioEntity, documentEntity);
                    servValidos.add(servicioEntity);
                } else {
                    dto.setError(datosNoValidos.getError());
                    resultServBasicErrors.add(dto);
                }

            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "saveServBasicosIni" + "|" + e.getMessage(), e);
        }
        resultSave.add((ArrayList<CargaInicialServiciosDto>) resultServBasicErrors);
        resultSave.add((ArrayList<ServicioEntity>) servValidos);
        return resultSave;
    }

    private InmServEntity inmServEntity;
    private RdbServEntity rdbServEntity;

    private void validarDatos() {
        inmServEntity = new InmServEntity();
        rdbServEntity = new RdbServEntity();
        datosValidos = new ServicioEntity();
        datosNoValidos = new CargaInicialServiciosDto();
        StringBuilder noExiste = new StringBuilder();

        if (!validarObligatorios(dtoIni)) {

            InmuebleEntity inmuebleEntity = inmuebleBL.getByCodInmueble(this.dtoIni.getCodRdbInm());
            RadioBaseEntity radioBaseEntity = radioBaseBL.getByCodRadioBase(this.dtoIni.getCodRdbInm());

            if (inmuebleEntity.getIdInmueble() > 0) {
                inmServEntity.setInmuebleByIdInmueble(inmuebleEntity);
                inmServEntity.setTipoInm(String.valueOf(inmuebleEntity.getTipoInmuebleByIdTipoInmueble()
                        .getIdTipoInmueble()));
            } else if (radioBaseEntity.getIdRadioBase() > 0) {
                rdbServEntity.setRadioBaseByIdRadioBase(radioBaseEntity);
            } else {
                noExiste.append(this.dtoIni.getCodRdbInm()).append(',');
            }

            TipoServicioEntity tipoServicioEntity = tipoServicioBL.getByNombre(this.dtoIni
                    .getTipoServicio());
            if (tipoServicioEntity.getIdTipoServicio() > 0) {
                datosValidos.setTipoServicioByIdTipoServicio(tipoServicioEntity);
            } else {
                noExiste.append(this.dtoIni.getTipoServicio()).append(',');
            }

            ProveedorEntity proveedorEntity = proveedorBL.getProveedorByNombre(this.dtoIni.getProveedor());
            if (proveedorEntity.getIdProveedor() > 0) {
                datosValidos.setProveedorByIdProveedor(proveedorEntity);
            } else {
                noExiste.append(this.dtoIni.getProveedor()).append(',');
            }

            noExiste.append(validarBancoMoneda());

            UnidadOperativaEntity unidadOperativaEntity = uobl.getUOperativaByNombre(this.dtoIni
                    .getUnidadOperativa());
            if (unidadOperativaEntity.getIdUnidadOperativa() > 0) {
                datosValidos.setUnidadOperativaByIdUnidadOperativa(unidadOperativaEntity);
            } else {
                noExiste.append(this.dtoIni.getUnidadOperativa());
            }
            datosNoValidos.setError(noExiste.toString());
        } else {
            datosValidos = null;
        }
    }

    private StringBuilder validarBancoMoneda(){
        StringBuilder noExiste = new StringBuilder("");
        try{
            BancoEntity bancoEntity = bancoBL.getBancoByNombre(this.dtoIni.getBanco());
            if (bancoEntity.getIdBanco() > 0) {
                datosValidos.setBancoByIdBanco(bancoEntity);
            } else {
                noExiste.append(this.dtoIni.getBanco()).append(',');
            }

            MonedaEntity monedaEntity = monedaBL.getMonedaByNombre(this.dtoIni.getMoneda());
            if (monedaEntity.getIdMoneda() > 0) {
                datosValidos.setMonedaByIdMoneda(monedaEntity);
            } else {
                noExiste.append(this.dtoIni.getMoneda()).append(',');
            }
        }catch (Exception e){
            logger.log(Level.ERROR, "validarLong" + "|" + e.getMessage(), e);
        }
        return noExiste;
    }
    int contSave = 0;

    private void saveServicios(ServicioEntity servicioEntity, DocumentosEntity documentosEntity) {
        try {

            servicioEntity.setcInicial(Long.valueOf(documentosEntity.getPrefijo()));
            this.save(servicioEntity);

            if ((inmServEntity.getIdInmServ() == 0) && (inmServEntity.getInmuebleByIdInmueble()
                    != null)) {
                inmServEntity.setServicioByIdServicio(servicioEntity);
                inmServBL.save(inmServEntity);
            }
            if ((rdbServEntity.getIdRdbsServ() == 0) && (rdbServEntity.getRadioBaseByIdRadioBase()
                    != null)) {
                rdbServEntity.setServicioByIdServicio(servicioEntity);
                rdbsServBL.save(rdbServEntity);
            }

            if (servicioEntity.getIdServicio() > 0) {
                contSave++;
            }
            if (contSave == 1) {
                documentosBL.save(documentosEntity);
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "saveServicios" + "|" + e.getMessage(), e);
        }

    }
}
