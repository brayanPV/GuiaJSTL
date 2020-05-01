<%-- 
    Document   : Registrar
    Created on : 1/05/2020, 11:46:47 AM
    Author     : stive
--%>

<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="DTO.Mensaje"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet"
              href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
              integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
              crossorigin="anonymous">
        <title>Registrar</title>
    </head>
    <body>
        <div class="container">
            <div>
                <h3>REGISTRAR MENSAJES</h3>
                <form action="MensajeController" method="POST">
                    <div class="form-group row">
                        <br> <label for="inputEmail3" style="text-align: left;"
                                    class="col-sm-3 col-form-label">Nombre</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" 
                                   name="nombre" placeholder="nombre" required="Ingrese su nombre">
                        </div>
                    </div>
                    <div class="form-group row">
                        <br> <label for="inputEmail3" style="text-align: left;"
                                    class="col-sm-3 col-form-label">Email</label>
                        <div class="col-sm-9">
                            <input type="email" class="form-control" 
                                   name="email" placeholder="email" required="Ingrese su email">
                        </div>
                    </div>
                    <div class="form-group row">
                        <br> <label for="inputEmail3" style="text-align: left;"
                                    class="col-sm-3 col-form-label">website</label>
                        <div class="col-sm-9">
                            <input type="url" class="form-control" 
                                   name="website" placeholder="website" required="Ingrese su website">
                        </div>
                    </div>
                    <div class="form-group row">
                        <br> <label for="inputEmail3" style="text-align: left;"
                                    class="col-sm-3 col-form-label">Mensaje</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" 
                                   name="message" placeholder="mensaje" required="Ingrese su mensaje">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputPassword3" style="text-align: left;"
                               class="col-sm-3 col-form-label">Usuario</label>
                        <div class="col-sm-9">

                            <jsp:useBean id="usuarioDAO" class="DAO.UsuarioJpaController" scope="request"></jsp:useBean>

                                <select name="usuario">
                                    <option>Elije a un usuario</option>
                                <c:forEach var="u" items="${usuarioDAO.findUsuarioEntities()}">

                                    <option value="<c:out value="${u.getUsuario()}"/>"><c:out value="${u.getUsuario()}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group row">
                            <div class="col-sm-12">
                                <button type="submit" name="accion" value="crear"
                                        class="btn btn-dark">Registrar Mensaje</button>
                            </div>
                        </div>
                </form>
                <form action="MensajeController" method="POST">
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <button type="submit" name="accion" value="atras"
                                    class="btn btn-dark">Regresar</button>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </body>
</html>
