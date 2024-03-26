package es.taw.proyectotaw.dao;

import es.taw.proyectotaw.Entity.EmpresaEntity;
import es.taw.proyectotaw.Entity.UsuarioEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    @Query("select u.nombre, c.saldo from UsuarioEntity u, CuentabancoEntity c  where u.cuentabancoByCuentaBancoIdCuentaBanco= c")
    List<UsuarioEntity> todosLosUsuarios();

    @Query("select u from UsuarioEntity u where u.nif = :nif and u.contrasena = :contrasena")
    public UsuarioEntity usuarioByNIFyContrasena(@Param("nif") String nif, @Param("contrasena") String contrasena);

    @Query("select u from UsuarioEntity u where u.nif = :nif")
    public UsuarioEntity usuarioByNIF(@Param("nif") String nif);

    @Query("select u from UsuarioEntity u where u.nif = :nif and u.contrasena = :contrasena")
    public UsuarioEntity autenticarUsuarioEmpresa(@Param("nif") String nif, @Param("contrasena") String contrasena);

    @Query("select u from UsuarioEntity u where  u.empresaByEmpresaIdEmpresa.idEmpresa = :idEmpresa")
    public List<UsuarioEntity> buscarUsuariosMismaEmpresa(@Param("idEmpresa") Integer idEmpresa);

    List<UsuarioEntity> findAllByEmpresaByEmpresaIdEmpresa(EmpresaEntity orElse);

    @Query("select u from UsuarioEntity u inner join PeticionEntity p on u.idUsuario = p.usuarioByUsuarioIdUsuario.idUsuario where (p.estadoPeticion = 'noprocesada' and p.tipoPeticion = :tipo)")
    public List<UsuarioEntity> buscarUsuariosConSolicitudDeTipo(Sort by, String tipo);

    @Query("SELECT u FROM UsuarioEntity u WHERE u NOT IN (SELECT DISTINCT  u FROM UsuarioEntity u JOIN u.cuentabancoByCuentaBancoIdCuentaBanco c JOIN c.transaccionsByIdCuentaBanco t WHERE t.fechaInstruccion >= :fecha)")
    public List<UsuarioEntity> buscarUsuariosConInactividadDe30Dias(Sort by, Date fecha);
}
