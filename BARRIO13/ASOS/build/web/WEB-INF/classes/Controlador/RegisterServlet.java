package Controlador;
import Modelo.RepositorioJdbc;import jakarta.servlet.*;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.*;import java.sql.*;
@WebServlet("/register") public class RegisterServlet extends HttpServlet{
 private final RepositorioJdbc repo=new RepositorioJdbc();
 protected void doPost(HttpServletRequest q,HttpServletResponse r)throws IOException,ServletException{q.setCharacterEncoding("UTF-8");try{repo.registrar(q.getParameter("usuario"),q.getParameter("correo"),q.getParameter("clave"));r.sendRedirect(q.getContextPath()+"/login.jsp");}catch(SQLException e){q.setAttribute("error","No se pudo registrar: "+e.getMessage());q.getRequestDispatcher("/register.jsp").forward(q,r);}}
}
