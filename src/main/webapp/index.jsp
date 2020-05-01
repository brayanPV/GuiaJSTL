<%-- 
    Document   : index
    Created on : 1/05/2020, 11:45:02 AM
    Author     : stive
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<html>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">


<head>
<meta charset="ISO-8859-1">
<title>Mensajeria</title>
</head>
<body>
    <div class="container">
	<h1>Mensajeria</h1>
	<form action="MensajeController" method="POST">
            
		<div class="form-group row">
			<div class="col-sm-12">
				<button type="submit" name="accion" value="registrar"
					class="btn btn-dark">Registrar Mensaje</button>

			</div>
		</div>
		<div class="form-group row">
			<div class="col-sm-12">
				<button type="submit" name="accion" value="listar"
					class="btn btn-dark">Listar Mensaje</button>

			</div>
		</div>

	</form>
</div>
</body>
</html>