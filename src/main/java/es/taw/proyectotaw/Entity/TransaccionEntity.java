package es.taw.proyectotaw.Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "transaccion", schema = "mydb", catalog = "")
public class TransaccionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_transaccion", nullable = false)
    private Integer idTransaccion;
    @Basic
    @Column(name = "fecha_instruccion", nullable = false)
    private Date fechaInstruccion;
    @Basic
    @Column(name = "fecha_ejecucion", nullable = true)
    private Date fechaEjecucion;
    @Basic
    @Column(name = "id_usuario_actor", nullable = true)
    private Integer idUsuarioActor;
    @ManyToOne
    @JoinColumn(name = "cuenta_banco_id_cuenta_banco", referencedColumnName = "id_cuenta_banco", nullable = false)
    private CuentabancoEntity cuentabancoByCuentaBancoIdCuentaBanco;
    @ManyToOne
    @JoinColumn(name = "cambio_divisa_id_cambio_divisa", referencedColumnName = "id_cambio_divisa")
    private CambiodivisaEntity cambiodivisaByCambioDivisaIdCambioDivisa;
    @ManyToOne
    @JoinColumn(name = "Pago_id_pago", referencedColumnName = "id_pago")
    private PagoEntity pagoByPagoIdPago;

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Date getFechaInstruccion() {
        return fechaInstruccion;
    }

    public void setFechaInstruccion(Date fechaInstruccion) {
        this.fechaInstruccion = fechaInstruccion;
    }

    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public Integer getIdUsuarioActor() {
        return idUsuarioActor;
    }

    public void setIdUsuarioActor(Integer idUsuarioActor) {
        this.idUsuarioActor = idUsuarioActor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransaccionEntity that = (TransaccionEntity) o;
        return Objects.equals(idTransaccion, that.idTransaccion) && Objects.equals(fechaInstruccion, that.fechaInstruccion) && Objects.equals(fechaEjecucion, that.fechaEjecucion) && Objects.equals(idUsuarioActor, that.idUsuarioActor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransaccion, fechaInstruccion, fechaEjecucion, idUsuarioActor);
    }

    public CuentabancoEntity getCuentabancoByCuentaBancoIdCuentaBanco() {
        return cuentabancoByCuentaBancoIdCuentaBanco;
    }

    public void setCuentabancoByCuentaBancoIdCuentaBanco(CuentabancoEntity cuentabancoByCuentaBancoIdCuentaBanco) {
        this.cuentabancoByCuentaBancoIdCuentaBanco = cuentabancoByCuentaBancoIdCuentaBanco;
    }

    public CambiodivisaEntity getCambiodivisaByCambioDivisaIdCambioDivisa() {
        return cambiodivisaByCambioDivisaIdCambioDivisa;
    }

    public void setCambiodivisaByCambioDivisaIdCambioDivisa(CambiodivisaEntity cambiodivisaByCambioDivisaIdCambioDivisa) {
        this.cambiodivisaByCambioDivisaIdCambioDivisa = cambiodivisaByCambioDivisaIdCambioDivisa;
    }

    public PagoEntity getPagoByPagoIdPago() {
        return pagoByPagoIdPago;
    }

    public void setPagoByPagoIdPago(PagoEntity pagoByPagoIdPago) {
        this.pagoByPagoIdPago = pagoByPagoIdPago;
    }
}
