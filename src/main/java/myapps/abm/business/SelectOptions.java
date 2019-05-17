package myapps.abm.business;

import myapps.abm.model.*;
import myapps.user.business.UsuarioBL;
import myapps.user.model.MuUsuario;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SelectOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(SelectOptions.class);
    @Inject
    private ProveedorBL proveedorBL;
    @Inject
    private DepartamentoBL departamentoBL;
    @Inject
    private BancoBL bancoBL;
    @Inject
    private MonedaBL monedaBL;
    @Inject
    private TipoServicioBL tipoServicioBL;
    @Inject
    private UOBL uobl;
    @Inject
    private ServicioBL servicioBL;
    @Inject
    private MunicipioBL municipioBL;
    @Inject
    private ProvinciaBL provinciaBL;
    @Inject
    private TipoInmuebleBL tipoInmuebleBL;
    @Inject
    private InmuebleBL inmuebleBL;
    @Inject
    private PropiedadBL propiedadBL;
    @Inject
    private TipoTorreBL tipoTorreBL;
    @Inject
    private ConstructorBL constructorBL;
    @Inject
    private LocalidadBL localidadBL;
    @Inject
    private RadioBaseBL radioBaseBL;
    @Inject
    private MedidorBL medidorBL;
    @Inject
    private UsuarioBL usuarioBL;
    @Inject
    private HistPagoMasivoBL histPagoMasivoBL;
    @Inject
    private CategoriaBL categoriaBL;
    @Inject
    private ContratosBL contratosBL;

    public List<ContratosEntity> cbxPropieatarioContratos() {
        return contratosBL.listAll();
    }

    public List<ProveedorEntity> cbxProveedores() {
        return proveedorBL.listAll();
    }

    public List<DepartamentoEntity> cbxDepartamento() {
        return departamentoBL.listAll();
    }

    public List<BancoEntity> cbxBancos() {
        return bancoBL.listAll();
    }

    public List<MonedaEntity> cbxMoneda() {
        return monedaBL.listAll();
    }

    public List<TipoServicioEntity> cbxTipoServicio() {
        return tipoServicioBL.listAll();
    }

    public List<UnidadOperativaEntity> cbxUO() {
        return uobl.listAll();
    }

    public List<ServicioEntity> cbxServicio() {
        return servicioBL.listAll();
    }
    public List<ServicioEntity> cbxOnlyServicio() {
        return servicioBL.listServicios();
    }

    public List<ServicioEntity> cbxServicionotMedidor() {
        return servicioBL.listAllnotMedidor();
    }

    public List<MunicipioEntity> cbxMunicipioIdProvincia(long idProvincia) {
        return municipioBL.listAllIdProvincia(idProvincia);
    }

    public List<ProvinciaEntity> cbxProvinciaDpto(long idDpto) {

        return provinciaBL.listAllIdDpto(idDpto);
    }

    public List<ProvinciaEntity> cbxProvincia() {

        return provinciaBL.listAll();
    }

    public List<MedidorEntity> cbxMedidor() {

        return medidorBL.listAll();
    }

    public List<TipoInmuebleEntity> cbxTipoInmueble() {
        return tipoInmuebleBL.listAll();
    }

    public List<TipoInmuebleEntity> cbxTipoInmFindAll() {
        return tipoInmuebleBL.findAll();
    }

    public List<InmuebleEntity> cbxImnueble() {
        return inmuebleBL.listAll();
    }


    public List<InmuebleEntity> cbxImnuebleByTipoInmueble(long tipo) {
        return inmuebleBL.listAllByTipoInmueble(tipo);
    }

    public List<RadioBaseEntity> cbxRadioBases() {
        return radioBaseBL.listAll();
    }

    public List<PropiedadEntity> cbxPropiedad(boolean allPropiedad) {
        return propiedadBL.listAll(allPropiedad);
    }

    public List<TipoTorreEntity> cbxTipoTorre() {
        return tipoTorreBL.listAll();
    }

    public List<ConstructorEntity> cbxConstructor() {
        return constructorBL.listAll();
    }

    public List<LocalidadEntity> cbxLocalidad() {
        return localidadBL.listAll();
    }

    public List<CategoriaEntity> cbxCategoria() {
        return categoriaBL.listAll();
    }


    public List<LocalidadEntity> cbxLocalidadIdMunicipio(long idMunicipio) {
        return localidadBL.listAllIdMunicipio(idMunicipio);
    }


    public List<MuUsuario> cbxUsuarios() {
        List<MuUsuario> muUsuarios = new ArrayList<>();
        try {
            muUsuarios = usuarioBL.getUsers();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return muUsuarios;
    }

    public List<HistorialPagosMasivosEntity> cbxListDocCargadosByUser() {
        return histPagoMasivoBL.listAll(null, null, null);
    }
}
