package myapps.user.ldap;

public enum DescriptorBitacora {
    PARAMETROS("CONFIGURACIÓN DE PARAMETROS"),
    ROL("GESTIONAR ROL"),
    GRUPO("GESTIONAR GRUPO"),
    USUARIO("GESTIONAR USUARIO"),
    PROVEEDORES("GESTIONAR PROVEEDORES"),
    UNION_PROVEEDORES("UNIÓN PROVEEDORES"),
    BANCOS("GESTIONAR BANCOS"),
    TIPO_TORRES("GESTIONAR TIPO DE TORRES"),
    ALARMAS("GESTIONAR ALARMAS"),
    NOTIFICACIONES("GESTIONAR NOTIFICACIÓN"),
    UNIDAD_OPERATIVA("GESTIONAR UNIDAD OPERATIVA"),
    TIPO_INMUEBLE("GESTIONAR TIPO DE INMUEBLE"),
    LICENCIA_AMBIENTAL("GESTIONAR LICENCIA AMBIENTAL"),
    CARGA_MASIVA_X_BANCO("CARGA MASIVA POR BANCO"),
    LEGAL("LEGAL"),
    SERVICIOS_BASICOS("SERVICIOS BASICOS"),
    INMUEBLES("INMUEBLES"),
    RADIRO_BASES("RADIO BASES"),
    PROPIEDAD("PROPIEDAD"),
    LOCALIDAD("LOCALIDAD"),
    CONSTRUCTOR("CONSTRUCTOR"),
    SERVICIOS("SERVICIOS"),
    PAGO_SERVICIO("PAGO_SERVICIO"),
    PAGO_SERVICIO_MAS("PAGO SERVICIO MASIVO"),
    HISTORIAL_PAGO_SERVICIO_MAS("HISTORIAL PAGO SERVICIO MASIVO"),
    TIPO_SERVICIO("TIPO_SERVICIO"),
    COLUMNAS("COLUMNAS"),
    ORDEN_COMPRA("ORDEN DE COMPRA"),
    CONTRATOS("CONTRATOS"),
    MEDIDOR("MEDIDOR"),
    DETALLE_MEDIDOR("DETALLE DEL MEDIDOR"),
    CONFIGURACION_COLUMNA("CONFIGURACION COLUMNAS"),
    DOCUMENTOS("GESTIONAR DOCUMENTOS"),
    SOLICITUD("SOLICITUDES");

    private String formulario;

    DescriptorBitacora(String formulario) {
        this.formulario = formulario;
    }

    public String getFormulario() {
        return formulario;
    }
}
