package es.taw.proyectotaw.controller;

import es.taw.proyectotaw.Entity.*;
import es.taw.proyectotaw.dao.*;
import es.taw.proyectotaw.ui.FiltroTransaccion;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//import javax.validation.Valid;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.lang.System.*;

@Controller
public class ClienteController {

    @Autowired
    protected UsuarioRepository usuarioRepository;

    @Autowired
    protected PeticionRepository peticionRepository;

    @Autowired
    protected DireccionRepository direccionRepository;

    @Autowired
    protected CambiodivisaRepository cambiodivisaRepository;

    @Autowired
    protected CuentabancoRepository cuentabancoRepository;

    @Autowired
    protected TransaccionRepository transaccionRepository;

    @Autowired
    protected PagoRepository pagoRepository;

    @GetMapping("/Cliente/crearNuevoCliente")
    public String crearNuevoCliente() {
        return "/Cliente/crearNuevoCliente";
    }

    @GetMapping("/Cliente/loginCliente")
    public String iniciarSesionCliente(Model model) {
        List<UsuarioEntity> lista = this.usuarioRepository.findAll();
        model.addAttribute("listaUsuarios", lista);
        return "Cliente/loginCliente";
    }

    @PostMapping("/loginCliente")
    public String doComprobarCredenciales(@RequestParam("nif") String nif,
            @RequestParam("contrasena") String contrasena,
            Model model, HttpSession session) {
        String urlTo = "Cliente/loginCliente";
        UsuarioEntity usuario = this.usuarioRepository.usuarioByNIFyContrasena(nif, contrasena);

        if (usuario == null || !usuario.getTipoUsuario().equals("cliente")) {
            model.addAttribute("error", "Credenciales incorrectas");
        } else if (usuario.getEstadoUsuario().equals("bloqueado")) {
            model.addAttribute("cliente", usuario);
            urlTo = "Cliente/esperarDesbloqueo";
        } else if (usuario.getEstadoUsuario().equals("pendiente")) {
            model.addAttribute("cliente", usuario);
            urlTo = "Cliente/esperarVerificado";
        } else if (usuario.getEstadoUsuario().equals("inactivo")) {
            model.addAttribute("cliente", usuario);
            urlTo = "Cliente/esperarActivacion";
        } else {
            model.addAttribute("cliente", usuario);
            session.setAttribute("usuario", usuario);
            urlTo = "Cliente/indexCliente";
        }
        return urlTo;
    }

    @GetMapping("/editarUsuario")
    public String editarCliente(Integer id, Model model, HttpSession session) {
        UsuarioEntity cliente = this.usuarioRepository.getReferenceById(id);
        model.addAttribute("cliente", cliente);
        return "Cliente/editarDatosCliente";
    }

    @GetMapping("/historialOperaciones")
    public String mostrarHistorial(Integer id, Model model, HttpSession session) {
        UsuarioEntity cliente = this.usuarioRepository.getReferenceById(id);
        model.addAttribute("usuario", cliente);
        if (model.containsAttribute("filtroTransacciones") && model.getAttribute("filtroTransacciones") != null) {
            return procesarFiltradoTransacciones(id, model,
                    (FiltroTransaccion) model.getAttribute("filtroTransacciones"));
        } else {
            return procesarFiltradoTransacciones(id, model, null);
        }
    }

    @PostMapping("/Cliente/filtrarTransacciones")
    public String filtrarTransaccionesCliente(Model model,
            @ModelAttribute("filtroTransacciones") FiltroTransaccion filtroTransaccion) {
        return procesarFiltradoTransacciones(filtroTransaccion.getId_usuario(), model, filtroTransaccion);
    }

    private String procesarFiltradoTransacciones(Integer id, Model model, FiltroTransaccion filtroTransaccion) {
        UsuarioEntity usuario = this.usuarioRepository.findById(id).orElse(null);
        model.addAttribute("usuario", usuario);
        List<TransaccionEntity> listaTransacciones = null;

        if (filtroTransaccion == null) {
            filtroTransaccion = new FiltroTransaccion();
            listaTransacciones = this.transaccionRepository.findAllByCuentabancoByCuentaBancoIdCuentaBanco(
                    Sort.by(filtroTransaccion.getOrden()), usuario.getCuentabancoByCuentaBancoIdCuentaBanco());
        }
        if (filtroTransaccion.getPropiedad().equals("")) {
            listaTransacciones = this.transaccionRepository.findAllByCuentabancoByCuentaBancoIdCuentaBanco(
                    Sort.by(filtroTransaccion.getOrden()), usuario.getCuentabancoByCuentaBancoIdCuentaBanco());
        }
        if (filtroTransaccion.getPropiedad().equals("Pago")) {
            listaTransacciones = this.transaccionRepository
                    .findAllByCuentabancoByCuentaBancoIdCuentaBancoEqualsAndPagoByPagoIdPagoNotNull(
                            usuario.getCuentabancoByCuentaBancoIdCuentaBanco(), Sort.by(filtroTransaccion.getOrden()));
        }
        if (filtroTransaccion.getPropiedad().equals("Cambio de divisa")) {
            listaTransacciones = this.transaccionRepository
                    .findAllByCuentabancoByCuentaBancoIdCuentaBancoEqualsAndCambiodivisaByCambioDivisaIdCambioDivisaNotNull(
                            usuario.getCuentabancoByCuentaBancoIdCuentaBanco(), Sort.by(filtroTransaccion.getOrden()));
        }

        model.addAttribute("listaTransacciones", listaTransacciones);
        model.addAttribute("filtroTransaccion", filtroTransaccion);

        return "Cliente/historialOperaciones";
    }

    @GetMapping("/volverIndex")
    public String volverIndexCliente(Integer id, Model model, HttpSession session) {
        UsuarioEntity cliente = this.usuarioRepository.getReferenceById(id);
        model.addAttribute("cliente", cliente);
        return "Cliente/indexCliente";
    }

    @GetMapping("/nuevaPeticionAlta")
    public String pedirAlta(Integer id, Model model, HttpSession session) {
        UsuarioEntity cliente = this.usuarioRepository.getReferenceById(id);
        model.addAttribute("cliente", cliente);
        PeticionEntity peticion = new PeticionEntity();
        peticion.setTipoPeticion("alta");
        peticion.setEstadoPeticion("noprocesada");
        peticion.setFechaPeticion(new Timestamp(currentTimeMillis()));
        peticion.setUsuarioByUsuarioIdUsuario(cliente);
        this.peticionRepository.save(peticion);
        return "Cliente/PeticionEnviada";
    }

    @GetMapping("/nuevaPeticionBloqueado")
    public String pedirDesbloqueo(@RequestParam("idUsuario") Integer idUsuario, Model model, HttpSession session) {
        UsuarioEntity cliente = this.usuarioRepository.getReferenceById(idUsuario);
        PeticionEntity peticion = new PeticionEntity();
        peticion.setTipoPeticion("desbloqueo");
        peticion.setEstadoPeticion("noprocesada");
        peticion.setFechaPeticion(new Timestamp(currentTimeMillis()));
        peticion.setUsuarioByUsuarioIdUsuario(cliente);
        this.peticionRepository.save(peticion);
        return "Cliente/PeticionEnviada";
    }

    @GetMapping("/nuevaPeticionActivacion")
    public String pedirActivacion(@RequestParam("idUsuario") Integer idUsuario, Model model, HttpSession session) {
        UsuarioEntity cliente = this.usuarioRepository.getReferenceById(idUsuario);
        PeticionEntity peticion = new PeticionEntity();
        peticion.setTipoPeticion("activar");
        peticion.setEstadoPeticion("noprocesada");
        peticion.setFechaPeticion(new Timestamp(currentTimeMillis()));
        peticion.setUsuarioByUsuarioIdUsuario(cliente);
        this.peticionRepository.save(peticion);
        return "Cliente/PeticionEnviada";
    }

    @PostMapping("/guardarCliente")
    public String doGuardar(@ModelAttribute("cliente") UsuarioEntity cliente) {
        this.usuarioRepository.save(cliente);
        return "Cliente/indexCliente";
    }

    @GetMapping("/pagoCliente")
    public String pagoCliente(Integer id, Model model, HttpSession session) {
        UsuarioEntity cliente = this.usuarioRepository.getReferenceById(id);
        model.addAttribute("cliente", cliente);
        List<CambiodivisaEntity> cambioDivisa = this.cambiodivisaRepository
                .listaCambioDivisa(cliente.getCuentabancoByCuentaBancoIdCuentaBanco().getTipoMoneda());
        model.addAttribute("cambioDivisa", cambioDivisa);

        return "Cliente/pagoCliente";
    }

    @PostMapping("/verificarTransferencia")
    public String doVerificarPago(@RequestParam("id") Integer id, @RequestParam("cantidad") Integer cantidad,
            @RequestParam("iban") String iban, Model model) {
        String UrlTo = "Cliente/verificarPagoCliente";
        UsuarioEntity cliente = this.usuarioRepository.findById(id).orElse(null);
        CuentabancoEntity cb = this.cuentabancoRepository.cuentaDestinatario(iban);
        PagoEntity pago = new PagoEntity();
        pago.setCantidad(cantidad);
        pago.setMoneda(cliente.getCuentabancoByCuentaBancoIdCuentaBanco().getTipoMoneda());
        pago.setIbanBeneficiario(iban);
        if (cb != null) {//si la cuenta es de nuestro banco le sumamos el dinero si posee el mismo tipo de moneda
            if (cb.getTipoMoneda().equals(cliente.getCuentabancoByCuentaBancoIdCuentaBanco().getTipoMoneda())) {
                cb.setSaldo(cb.getSaldo() + cantidad);
                cliente.getCuentabancoByCuentaBancoIdCuentaBanco()
                        .setSaldo(cliente.getCuentabancoByCuentaBancoIdCuentaBanco().getSaldo() - cantidad);
                this.usuarioRepository.save(cliente);
                TransaccionEntity transaccion = new TransaccionEntity();
                transaccion.setFechaInstruccion(Date.valueOf(LocalDate.now()));
                transaccion
                        .setCuentabancoByCuentaBancoIdCuentaBanco(cliente.getCuentabancoByCuentaBancoIdCuentaBanco());
                this.pagoRepository.save(pago);
                transaccion.setPagoByPagoIdPago(pago);
                this.transaccionRepository.save(transaccion);
            } else {
                UrlTo = "Cliente/errorMonedaDistinta";
            }
        } else {

            cliente.getCuentabancoByCuentaBancoIdCuentaBanco()
                    .setSaldo(cliente.getCuentabancoByCuentaBancoIdCuentaBanco().getSaldo() - cantidad);
            this.usuarioRepository.save(cliente);
            TransaccionEntity transaccion = new TransaccionEntity();
            transaccion.setFechaInstruccion(Date.valueOf(LocalDate.now()));
            transaccion.setCuentabancoByCuentaBancoIdCuentaBanco(cliente.getCuentabancoByCuentaBancoIdCuentaBanco());
            this.pagoRepository.save(pago);
            transaccion.setPagoByPagoIdPago(pago);
            this.transaccionRepository.save(transaccion);
        }
        model.addAttribute("cliente", cliente);

        return UrlTo;
    }

    @GetMapping("/cambioDeDivisaCliente")
    public String cambioDivisa(Integer id, Model model, HttpSession session) {
        UsuarioEntity cliente = this.usuarioRepository.getReferenceById(id);
        model.addAttribute("cliente", cliente);
        List<CambiodivisaEntity> cambioDivisa = this.cambiodivisaRepository
                .listaCambioDivisa(cliente.getCuentabancoByCuentaBancoIdCuentaBanco().getTipoMoneda());
        model.addAttribute("cambioDivisa", cambioDivisa);

        return "Cliente/cambioDivisaCliente";
    }

    @PostMapping("/verificarCambioDivisa")
    public String doVerificarCambioDivisa(@RequestParam("id") Integer id, @RequestParam("cambio") Integer cambio,
            Model model) {
        UsuarioEntity cliente = this.usuarioRepository.findById(id).orElse(null);
        CambiodivisaEntity cd = this.cambiodivisaRepository.findById(cambio).orElse(null);
        model.addAttribute("cambioDivisa", cd);
        model.addAttribute("cliente", cliente);
        float f = Float.valueOf(cd.getCantidadVenta()).floatValue() / Float.valueOf(cd.getCantidadCompra()).floatValue()
                * cliente.getCuentabancoByCuentaBancoIdCuentaBanco().getSaldo();
        int pasta = Float.valueOf(f).intValue();
        model.addAttribute("pasta", pasta);
        return "Cliente/verificarCambioDivisaCliente";
    }

    @GetMapping("/realizarCambio")
    public String realizarCambio(Integer id, Integer cambioDivisa, Integer pasta, Model model, HttpSession session) {
        UsuarioEntity cliente = this.usuarioRepository.findById(id).orElse(null);
        CambiodivisaEntity cd = this.cambiodivisaRepository.findById(cambioDivisa).orElse(null);
        cliente.getCuentabancoByCuentaBancoIdCuentaBanco().setSaldo(pasta);
        cliente.getCuentabancoByCuentaBancoIdCuentaBanco().setTipoMoneda(cd.getMonedaCompra());
        this.usuarioRepository.save(cliente);
        TransaccionEntity transaccion = new TransaccionEntity();
        transaccion.setFechaInstruccion(Date.valueOf(LocalDate.now()));
        transaccion.setCuentabancoByCuentaBancoIdCuentaBanco(cliente.getCuentabancoByCuentaBancoIdCuentaBanco());
        transaccion.setCambiodivisaByCambioDivisaIdCambioDivisa(cd);
        this.transaccionRepository.save(transaccion);
        model.addAttribute("cliente", cliente);
        return "Cliente/indexCliente";
    }

    @PostMapping("/registrarCliente")
    public String registrarCliente(@RequestParam String nif, @RequestParam String nombre,
            @RequestParam(required = false) String segundoNombre, @RequestParam String apellido1,
            @RequestParam(required = false) String apellido2, @RequestParam Date fechaNacimiento,
            @RequestParam String calle, @RequestParam String numeroVivienda, @RequestParam String planta,
            @RequestParam String ciudad, @RequestParam(required = false) String region,
            @RequestParam String pais, @RequestParam String cp, @RequestParam boolean valida,
            @RequestParam String contrasena, @RequestParam String repcontrasena, Model model) {
        UsuarioEntity us = this.usuarioRepository.usuarioByNIF(nif);
        String urlTo = "Cliente/usuarioExistente";
        if (us == null && contrasena.equals(repcontrasena)) {
            Byte val = 0;
            if (valida) {
                val = 1;
            }
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setTipoUsuario("cliente");
            usuario.setContrasena(contrasena);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setNif(nif);
            usuario.setNombre(nombre);
            usuario.setSegundoNombre(segundoNombre);
            usuario.setPrimerApellido(apellido1);
            usuario.setSegundoApellido(apellido2);
            usuario.setEstadoUsuario("pendiente");
            usuario.setFechaInicio(Date.valueOf(LocalDate.now()));
            DireccionEntity direccion = new DireccionEntity();
            direccion.setCalle(calle);
            direccion.setCiudad(ciudad);
            direccion.setNumero(numeroVivienda);
            direccion.setCp(cp);
            direccion.setPais(pais);
            direccion.setRegion(region);
            direccion.setPuerta(planta);
            direccion.setValida(val);
            this.direccionRepository.save(direccion);
            usuario.setDireccionByDireccionIdDireccion(direccion);
            this.usuarioRepository.save(usuario);
            model.addAttribute("cliente", usuario);
            PeticionEntity peticion = new PeticionEntity();
            peticion.setTipoPeticion("alta");
            peticion.setEstadoPeticion("noprocesada");
            peticion.setFechaPeticion(new Timestamp(currentTimeMillis()));
            peticion.setUsuarioByUsuarioIdUsuario(usuario);
            this.peticionRepository.save(peticion);
            urlTo = "Cliente/esperarVerificado";
        } else {
            urlTo = "/Cliente/crearNuevoCliente";
        }

        return urlTo;
    }

}
