<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ page import="java.util.List" %>
        <%@ page import="es.taw.proyectotaw.Entity.UsuarioEntity" %>
            <%@ page import="es.taw.proyectotaw.Entity.EmpresaEntity" %>
                <%@ page import="es.taw.proyectotaw.Entity.CambiodivisaEntity" %>
                    <%-- Created by IntelliJ IDEA. User: x Date: 30/04/2023 Time: 2:55 To change this template use File
                        | Settings | File Templates. --%>
                        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
                            <html>
                            <% UsuarioEntity socio=(UsuarioEntity) request.getAttribute("socio"); EmpresaEntity
                                empresa=(EmpresaEntity) request.getAttribute("empresa"); List<CambiodivisaEntity>
                                cambioDivisa = (List<CambiodivisaEntity>) request.getAttribute("cambioDivisa");
                                    %>
                                    <html>

                                    <head>
                                        <title>Title</title>
                                        <style>
                                            body {
                                                font-family: Arial, sans-serif;
                                                margin: 0;
                                                padding: 0;
                                            }

                                            #container {
                                                max-width: 800px;
                                                margin: 0 auto;
                                                padding: 20px;
                                            }

                                            h1 {
                                                text-align: center;
                                            }

                                            form {
                                                display: flex;
                                                flex-direction: column;
                                                align-items: center;
                                                margin-top: 30px;
                                            }

                                            label {
                                                margin-top: 10px;
                                                font-size: 18px;
                                            }

                                            select {
                                                margin-top: 10px;
                                                padding: 5px;
                                                font-size: 16px;
                                            }

                                            button {
                                                margin-top: 20px;
                                                padding: 10px 20px;
                                                font-size: 18px;
                                                background-color: #4CAF50;
                                                color: #fff;
                                                border: none;
                                                border-radius: 5px;
                                                cursor: pointer;
                                            }

                                            button:hover {
                                                background-color: #4CAF50;
                                            }
                                        </style>
                                    </head>

                                    <body>
                                        <div id="container">
                                            <h1>Transferencia bancaria</h1>
                                            <div>La cuenta tiene un saldo
                                                de <%=empresa.getCuentabancoByCuentaEmpresaIdCuentaBanco().getSaldo()%>
                                                    <%=empresa.getCuentabancoByCuentaEmpresaIdCuentaBanco().getTipoMoneda()%>
                                            </div>
                                            <form method="post" action="/verificarTransferenciaEmpresa">
                                                <input type="hidden" name="id" value="<%=socio.getIdUsuario()%>">
                                                Cantidad:<input type="number" name="cantidad"
                                                    max="<%=empresa.getCuentabancoByCuentaEmpresaIdCuentaBanco().getSaldo()%>">
                                                IBAN Beneficiario:<input type="text" name="iban">
                                                <button>Realizar transferencia</button>
                                            </form>
                                            <button> <a
                                                    href="/goPrincipalEmpresa?id=<%=socio.getIdUsuario()%>">SALIR</a></button>

                                        </div>
                                    </body>

                                    </html>