<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %><%@ include file="fragments/header.jspf" %>
<section class="panel"><h2>Resumen</h2><table><tr><th>Módulo</th><th>Total</th></tr><% Map<String,Integer> m=(Map<String,Integer>)request.getAttribute("indicadores"); for(var e:m.entrySet()){ %><tr><td><%=e.getKey()%></td><td><%=e.getValue()%></td></tr><% } %></table><p>El sistema trabaja directamente con Servlets, JSP y MySQL. No usa APIs REST.</p></section>
<%@ include file="fragments/footer.jspf" %>
