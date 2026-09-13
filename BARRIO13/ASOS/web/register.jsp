<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Crear cuenta | BARRIO 13</title>
<style>
*{box-sizing:border-box}
body{
  font-family:Arial,Helvetica,sans-serif;
  min-height:100vh;margin:0;display:grid;place-items:center;padding:24px;color:#f5f5f5;
  background:linear-gradient(rgba(0,0,0,.74),rgba(0,0,0,.91)),repeating-linear-gradient(135deg,#171717 0,#171717 14px,#0e0e0e 14px,#0e0e0e 28px)
}
.box{width:min(420px,100%);background:#111214;padding:34px;border:1px solid #34363b;border-top:5px solid #f2c94c;border-radius:7px;box-shadow:0 20px 60px rgba(0,0,0,.55)}
.tag{color:#f2c94c;font-size:13px;font-weight:900;letter-spacing:2px;text-transform:uppercase;margin-bottom:8px}
h1{margin:0 0 22px;text-transform:uppercase;letter-spacing:2px}
label{display:block;font-weight:800;margin-top:12px;margin-bottom:6px}
input{width:100%;padding:12px;background:#08090a;color:#fff;border:1px solid #3c3f45;border-radius:4px;outline:none}
input:focus{border-color:#f2c94c;box-shadow:0 0 0 2px rgba(242,201,76,.12)}
button{width:100%;padding:12px;margin-top:18px;background:#f2c94c;color:#080808;border:0;border-radius:4px;font-weight:900;text-transform:uppercase;letter-spacing:1px;cursor:pointer}
button:hover{background:#ffd85e}
.error{background:#351717;color:#ffb7b7;border:1px solid #7e2b2b;padding:10px;border-radius:4px}
a{color:#f2c94c;font-weight:700;text-decoration:none}a:hover{text-decoration:underline}
</style>
</head>
<body>
<div class="box">
  <div class="tag">BARRIO 13 // Sistema Escolar</div>
  <h1>Crear cuenta</h1>
  <% if(request.getAttribute("error")!=null){%><p class="error"><%=request.getAttribute("error")%></p><%}%>
  <form method="post" action="${pageContext.request.contextPath}/register">
    <label>Usuario</label><input name="usuario" required>
    <label>Correo</label><input type="email" name="correo" required>
    <label>Contraseña</label><input type="password" name="clave" required>
    <button type="submit">Registrar</button>
  </form>
  <p><a href="${pageContext.request.contextPath}/login.jsp">Volver al inicio de sesión</a></p>
</div>
</body>
</html>
