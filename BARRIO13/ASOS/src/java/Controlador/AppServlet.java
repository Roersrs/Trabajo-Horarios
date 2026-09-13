package Controlador;
import Modelo.RepositorioJdbc;import jakarta.servlet.*;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.*;import java.sql.*;import java.util.*;
@WebServlet({"/dashboard","/cursos/*","/docentes/*","/asignaturas/*","/horarios/*","/horario-docente"})
public class AppServlet extends HttpServlet{
 private final RepositorioJdbc repo=new RepositorioJdbc();
 protected void service(HttpServletRequest q,HttpServletResponse r)throws ServletException,IOException{if(q.getSession(false)==null){r.sendRedirect(q.getContextPath()+"/login.jsp");return;}super.service(q,r);}
 protected void doGet(HttpServletRequest q,HttpServletResponse r)throws ServletException,IOException{try{String p=path(q);switch(p){
  case "/dashboard" -> {q.setAttribute("indicadores",repo.indicadores());view(q,r,"Dashboard","/WEB-INF/views/dashboard.jsp");}
  case "/cursos" -> {q.setAttribute("items",repo.cursos());view(q,r,"Cursos","/WEB-INF/views/cursos/list.jsp");}
  case "/cursos/nuevo" -> cursoForm(q,r,new LinkedHashMap<>()); case "/cursos/editar" -> cursoForm(q,r,repo.curso(id(q)));
  case "/docentes" -> {q.setAttribute("items",repo.docentes());view(q,r,"Docentes","/WEB-INF/views/docentes/list.jsp");}
  case "/docentes/nuevo" -> form(q,r,"Docente",new LinkedHashMap<>(),"docente","/WEB-INF/views/docentes/form.jsp"); case "/docentes/editar" -> form(q,r,"Docente",repo.docente(id(q)),"docente","/WEB-INF/views/docentes/form.jsp");
  case "/asignaturas" -> {q.setAttribute("items",repo.asignaturas());view(q,r,"Asignaturas","/WEB-INF/views/asignaturas/list.jsp");}
  case "/asignaturas/nuevo" -> form(q,r,"Asignatura",new LinkedHashMap<>(),"asignatura","/WEB-INF/views/asignaturas/form.jsp"); case "/asignaturas/editar" -> form(q,r,"Asignatura",repo.asignatura(id(q)),"asignatura","/WEB-INF/views/asignaturas/form.jsp");
  case "/horarios" -> {q.setAttribute("items",repo.horarios());view(q,r,"Horarios","/WEB-INF/views/horarios/list.jsp");}
  case "/horario-docente" -> horarioDocente(q,r);
  case "/horarios/nuevo" -> horarioForm(q,r,new LinkedHashMap<>()); case "/horarios/editar" -> horarioForm(q,r,repo.horario(id(q)));
  default -> r.sendError(404);
 }}catch(Exception e){mostrarError(q,r,e);}}
 protected void doPost(HttpServletRequest q,HttpServletResponse r)throws ServletException,IOException{q.setCharacterEncoding("UTF-8");try{String p=path(q);Integer id=opInt(q.getParameter("id"));switch(p){
  case "/cursos/guardar" -> {repo.guardarCurso(id,q.getParameter("grado"),q.getParameter("nombre"),intv(q,"numero_estudiantes"),intv(q,"jornada_id"));go(q,r,"/cursos");}
  case "/docentes/guardar" -> {repo.guardarDocente(id,q.getParameter("nombre"),q.getParameter("correo"),q.getParameter("telefono"),intv(q,"max_horas_diarias"));go(q,r,"/docentes");}
  case "/asignaturas/guardar" -> {repo.guardarAsignatura(id,q.getParameter("nombre"),intv(q,"intensidad_horaria"));go(q,r,"/asignaturas");}
  case "/horarios/guardar" -> {repo.guardarHorario(id,intv(q,"curso_id"),intv(q,"asignatura_id"),intv(q,"docente_id"),q.getParameter("dia"),q.getParameter("hora_inicio"),q.getParameter("hora_fin"));go(q,r,"/horarios");}
  case "/cursos/eliminar" -> {repo.eliminar("cursos",intv(q,"id"));go(q,r,"/cursos");}
  case "/docentes/eliminar" -> {repo.eliminar("docentes",intv(q,"id"));go(q,r,"/docentes");}
  case "/asignaturas/eliminar" -> {repo.eliminar("asignaturas",intv(q,"id"));go(q,r,"/asignaturas");}
  case "/horarios/eliminar" -> {repo.eliminar("horarios",intv(q,"id"));go(q,r,"/horarios");}
  default -> r.sendError(404);
 }}catch(Exception e){mostrarError(q,r,e);}}
 private void horarioDocente(HttpServletRequest q,HttpServletResponse r)throws Exception{
  q.setAttribute("docentes",repo.docentes());
  Integer docenteId=opInt(q.getParameter("docente_id"));
  if(docenteId!=null){
   Map<String,Object> docente=repo.docente(docenteId);
   if(docente.isEmpty()) throw new SQLException("Docente no encontrado");
   q.setAttribute("docenteSeleccionado",docente);
   q.setAttribute("horarioDocente",repo.horarioDocente(docenteId));
   q.setAttribute("docenteId",docenteId);
  }
  view(q,r,"Horario del docente","/WEB-INF/views/docentes/horario.jsp");
 }
 private void cursoForm(HttpServletRequest q,HttpServletResponse r,Map<String,Object>x)throws Exception{q.setAttribute("curso",x);q.setAttribute("jornadas",repo.jornadas());view(q,r,"Curso","/WEB-INF/views/cursos/form.jsp");}
 private void horarioForm(HttpServletRequest q,HttpServletResponse r,Map<String,Object>x)throws Exception{q.setAttribute("horario",x);q.setAttribute("cursos",repo.cursos());q.setAttribute("asignaturas",repo.asignaturas());q.setAttribute("docentes",repo.docentes());view(q,r,"Horario","/WEB-INF/views/horarios/form.jsp");}
 private void form(HttpServletRequest q,HttpServletResponse r,String t,Map<String,Object>x,String n,String jsp)throws Exception{q.setAttribute(n,x);view(q,r,t,jsp);} private void view(HttpServletRequest q,HttpServletResponse r,String t,String jsp)throws ServletException,IOException{q.setAttribute("pageTitle",t);q.getRequestDispatcher(jsp).forward(q,r);} private String path(HttpServletRequest q){return q.getServletPath()+(q.getPathInfo()==null?"":q.getPathInfo());} private int id(HttpServletRequest q){return Integer.parseInt(q.getParameter("id"));} private int intv(HttpServletRequest q,String n){return Integer.parseInt(q.getParameter(n));} private Integer opInt(String s){return s==null||s.isBlank()?null:Integer.valueOf(s);} private void go(HttpServletRequest q,HttpServletResponse r,String p)throws IOException{r.sendRedirect(q.getContextPath()+p);} private void mostrarError(HttpServletRequest q,HttpServletResponse r,Exception e)throws ServletException,IOException{q.setAttribute("error",e.getMessage());q.setAttribute("pageTitle","Error");q.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(q,r);}
}
