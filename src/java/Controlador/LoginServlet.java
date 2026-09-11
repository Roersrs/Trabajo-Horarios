package Controlador;
import Modelo.RepositorioJdbc;
import jakarta.servlet.*;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.*;import java.sql.*;
@WebServlet("/login") public class LoginServlet extends HttpServlet{
 private final RepositorioJdbc repo=new RepositorioJdbc();
 protected void doPost(HttpServletRequest q,HttpServletResponse r)throws IOException,ServletException{q.setCharacterEncoding("UTF-8");try{if(repo.autenticar(q.getParameter("usuario"),q.getParameter("clave"))){q.getSession(true).setAttribute("usuario",q.getParameter("usuario"));r.sendRedirect(q.getContextPath()+"/dashboard");}else{q.setAttribute("error","Usuario o contraseña incorrectos");q.getRequestDispatcher("/login.jsp").forward(q,r);}}catch(SQLException e){q.setAttribute("error","No se pudo conectar a MySQL: "+e.getMessage());q.getRequestDispatcher("/login.jsp").forward(q,r);}}
}
