package myapps.workflow.entity;


import myapps.abm.model.ServicioEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "SOLICITUDES", schema = "SERVICIOS_BASICOS", catalog = "")
public class SolicitudesEntity {
	private long idSolicitud;
	private String unidad;
	private String nombreCooperativa;
	private Date fechaSolicitud;
	private String ciudad;
	private String tipoSolicitud;
	private String propietario;
	private String direccion;
	private BigDecimal monto;
	private Date fechaPago;
	private String formaPago;
	private String motivoAtraso;
	private String detalleDeuda;
	private ServicioEntity servicioByIdServicio;
	private Date fechaRegistro;
	private boolean estado;
	private Long idProceso;

	@Id
	@Column(name = "ID_SOLICITUD")
	@SequenceGenerator(name = "SEQ_SOLICITUDES_ID_GENERATOR", sequenceName = "SEQ_SOLICITUDES", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SOLICITUDES_ID_GENERATOR")
	public long getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	@Basic
	@Column(name = "UNIDAD")
	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	@Basic
	@Column(name = "NOMBRE_COOPERATIVA")
	public String getNombreCooperativa() {
		return nombreCooperativa;
	}

	public void setNombreCooperativa(String nombreCooperativa) {
		this.nombreCooperativa = nombreCooperativa;
	}

	@Basic
	@Column(name = "FECHA_SOLICITUD")
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	@Basic
	@Column(name = "CIUDAD")
	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	@Basic
	@Column(name = "TIPO_SOLICITUD")
	public String getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	@Basic
	@Column(name = "PROPIETARIO")
	public String getPropietario() {
		return propietario;
	}

	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}

	@Basic
	@Column(name = "DIRECCION")
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Basic
	@Column(name = "MONTO")
	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Basic
	@Column(name = "FECHA_PAGO")
	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	@Basic
	@Column(name = "FORMA_PAGO")
	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	@Basic
	@Column(name = "MOTIVO_ATRASO")
	public String getMotivoAtraso() {
		return motivoAtraso;
	}

	public void setMotivoAtraso(String motivoAtraso) {
		this.motivoAtraso = motivoAtraso;
	}

	@Basic
	@Column(name = "DETALLE_DEUDA")
	public String getDetalleDeuda() {
		return detalleDeuda;
	}

	public void setDetalleDeuda(String detalleDeuda) {
		this.detalleDeuda = detalleDeuda;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SolicitudesEntity that = (SolicitudesEntity) o;
		return idSolicitud == that.idSolicitud && Objects.equals(unidad, that.unidad)
				&& Objects.equals(nombreCooperativa, that.nombreCooperativa)
				&& Objects.equals(fechaSolicitud, that.fechaSolicitud) && Objects.equals(ciudad, that.ciudad)
				&& Objects.equals(tipoSolicitud, that.tipoSolicitud) && Objects.equals(propietario, that.propietario)
				&& Objects.equals(direccion, that.direccion) && Objects.equals(monto, that.monto)
				&& Objects.equals(fechaPago, that.fechaPago) && Objects.equals(formaPago, that.formaPago)
				&& Objects.equals(motivoAtraso, that.motivoAtraso) && Objects.equals(detalleDeuda, that.detalleDeuda);
	}

	@Override
	public int hashCode() {

		return Objects.hash(idSolicitud, unidad, nombreCooperativa, fechaSolicitud, ciudad, tipoSolicitud, propietario,
				direccion, monto, fechaPago, formaPago, motivoAtraso, detalleDeuda);
	}

	@ManyToOne
	@JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO")
	public ServicioEntity getServicioByIdServicio() {
		return servicioByIdServicio;
	}

	public void setServicioByIdServicio(ServicioEntity servicioByIdServicio) {
		this.servicioByIdServicio = servicioByIdServicio;
	}

	@Basic
	@Column(name = "FECHA_REGISTRO")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Basic
	@Column(name = "ESTADO")
	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	@Basic
	@Column(name = "ID_PROCESO")
	public Long getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(Long idProceso) {
		this.idProceso = idProceso;
	}
}
