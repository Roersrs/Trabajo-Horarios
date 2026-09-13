<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>BARRIO 13 - Login</title>
<style>
*{box-sizing:border-box}
body{
  font-family:Arial,Helvetica,sans-serif;
  min-height:100vh;
  margin:0;
  display:grid;
  place-items:center;
  padding:24px;
  color:#f5f5f5;
  background:
    linear-gradient(rgba(0,0,0,.72),rgba(0,0,0,.9)),
    repeating-linear-gradient(45deg,#171717 0,#171717 14px,#0f0f0f 14px,#0f0f0f 28px);
}
.box{
  width:min(420px,100%);
  background:#111214;
  padding:34px;
  border:1px solid #34363b;
  border-top:5px solid #f2c94c;
  border-radius:7px;
  box-shadow:0 20px 60px rgba(0,0,0,.55);
}
.tag{color:#f2c94c;font-size:13px;font-weight:900;letter-spacing:2px;text-transform:uppercase;margin-bottom:8px}
h1{font-size:38px;letter-spacing:4px;margin:0;text-transform:uppercase}
.subtitle{color:#b7bbc2;margin:8px 0 24px}
label{display:block;font-weight:800;margin-top:12px;margin-bottom:6px}
input{
  width:100%;padding:12px;background:#08090a;color:#fff;border:1px solid #3c3f45;border-radius:4px;outline:none
}
input:focus{border-color:#f2c94c;box-shadow:0 0 0 2px rgba(242,201,76,.12)}
button{
  width:100%;padding:12px;margin-top:18px;background:#f2c94c;color:#080808;border:0;border-radius:4px;
  font-weight:900;text-transform:uppercase;letter-spacing:1px;cursor:pointer
}
button:hover{background:#ffd85e}
.error{background:#351717;color:#ffb7b7;border:1px solid #7e2b2b;padding:10px;border-radius:4px}
.demo{font-size:13px;color:#9fa3aa;background:#0a0a0b;padding:10px;border-left:3px solid #f2c94c;margin-top:18px}
a{color:#f2c94c;font-weight:700;text-decoration:none}
a:hover{text-decoration:underline}
</style>
</head>
<body>
<div class="box">
  <div class="tag">Sistema de organización escolar</div>
  <h1>BARRIO 13</h1>
  <p class="subtitle">Gestión de horarios con estilo urbano.</p>
  <% if(request.getAttribute("error")!=null){%><p class="error"><%=request.getAttribute("error")%></p><%}%>
  <form method="post" action="${pageContext.request.contextPath}/login">
    <label>Usuario o correo</label>
    <input name="usuario" required autocomplete="username">
    <label>Contraseña</label>
    <input type="password" name="clave" required autocomplete="current-password">
    <button type="submit">Entrar</button>
  </form>
  <p class="demo">Usuario de prueba: <b>admin</b> / <b>admin123</b></p>
  <a href="${pageContext.request.contextPath}/register.jsp">Crear usuario</a>
</div>
</body>
</html>
