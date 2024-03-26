package es.taw.proyectotaw.Entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "direccion", schema = "mydb", catalog = "")
public class DireccionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_direccion", nullable = false)
    private Integer idDireccion;
    @Basic
    @Column(name = "calle", nullable = false, length = 100)
    private String calle;
    @Basic
    @Column(name = "numero", nullable = false, length = 45)
    private String numero;
    @Basic
    @Column(name = "puerta", nullable = false, length = 45)
    private String puerta;
    @Basic
    @Column(name = "ciudad", nullable = false, length = 45)
    private String ciudad;
    @Basic
    @Column(name = "pais", nullable = false, length = 45)
    private String pais;
    @Basic
    @Column(name = "cp", nullable = false, length = 45)
    private String cp;
    @Basic
    @Column(name = "region", nullable = true, length = 45)
    private String region;
    @Basic
    @Column(name = "valida", nullable = false)
    private Byte valida;
    @OneToMany(mappedBy = "direccionByDireccionIdDireccion")
    private Collection<EmpresaEntity> empresasByIdDireccion;
    @OneToMany(mappedBy = "direccionByDireccionIdDireccion")
    private Collection<UsuarioEntity> usuariosByIdDireccion;

    public Integer getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(Integer idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Byte getValida() {
        return valida;
    }

    public void setValida(Byte valida) {
        this.valida = valida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DireccionEntity direccion = (DireccionEntity) o;
        return Objects.equals(idDireccion, direccion.idDireccion) && Objects.equals(calle, direccion.calle) && Objects.equals(numero, direccion.numero) && Objects.equals(puerta, direccion.puerta) && Objects.equals(ciudad, direccion.ciudad) && Objects.equals(pais, direccion.pais) && Objects.equals(cp, direccion.cp) && Objects.equals(region, direccion.region) && Objects.equals(valida, direccion.valida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDireccion, calle, numero, puerta, ciudad, pais, cp, region, valida);
    }

    public Collection<EmpresaEntity> getEmpresasByIdDireccion() {
        return empresasByIdDireccion;
    }

    public void setEmpresasByIdDireccion(Collection<EmpresaEntity> empresasByIdDireccion) {
        this.empresasByIdDireccion = empresasByIdDireccion;
    }

    public Collection<UsuarioEntity> getUsuariosByIdDireccion() {
        return usuariosByIdDireccion;
    }

    public void setUsuariosByIdDireccion(Collection<UsuarioEntity> usuariosByIdDireccion) {
        this.usuariosByIdDireccion = usuariosByIdDireccion;
    }
}
