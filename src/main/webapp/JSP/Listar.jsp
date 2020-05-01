<%-- 
    Document   : Listar
    Created on : 1/05/2020, 12:15:02 PM
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
        <title>LSITAR</title>
    </head>
    <body>
        <div class="container">
            <h3> LSITADO DE MENSAJES </h3>

            <table class="table">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>NOMBRE</th>
                        <th>EMAIL</th>
                        <th>WEBSITE</th>
                        <th>MENSAJE</th>
                        <th>USUARIO</th>
                        <th>ACCION</th>
                    </tr>
                </thead>


                <jsp:useBean id="mensajeDAO" class="DAO.MensajeJpaController" scope="request"></jsp:useBean>
                <c:forEach var="m" items="${mensajeDAO.findMensajeEntities()}">
                    <tr>
                        <td><c:out value="${m.getId()}"/></td>
                        <td><c:out value="${m.getNombre()}"/></td>
                        <td><c:out value="${m.getEmail()}"/></td>
                        <td><c:out value="${m.getWebsite()}"/></td>
                        <td><c:out value="${m.getMessage()}"/></td>
                        <td><c:out value="${m.getUsuario()}"/></td>
                        <td>
                            <form action="MensajeController" method="POST">
                                <input hidden="true" name="id" value="<c:out value="${m.getId()}"/>"></input>
                                <div class="form-group row">
                                    <div class="col-sm-12">

                                        <button type="submit" name="accion" value="editar"
                                                class="btn btn-dark">Editar</button>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-sm-12">
                                        <button type="submit" name="accion" value="eliminar"
                                                class="btn btn-dark">Eliminar</button>
                                    </div>
                                </div>
                            </form>
                        </td>
                    </tr>
               
           </c:forEach>
                     </table>
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
    </body>
</html>
