package Modelo;

import conf.ConexionBD;
import java.sql.*;
import java.util.*;

public class RepositorioJdbc {
    public boolean autenticar(String usuario, String clave) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE (usuario=? OR correo=?) AND clave=?";
        try (Connection c=ConexionBD.obtenerConexion(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1, usuario); p.setString(2, usuario); p.setString(3, clave);
            try (ResultSet r=p.executeQuery()) { return r.next(); }
        }
    }

    public void registrar(String usuario,String correo,String clave) throws SQLException {
        try(Connection c=ConexionBD.obtenerConexion(); PreparedStatement p=c.prepareStatement("INSERT INTO usuarios(usuario,correo,clave) VALUES(?,?,?)")){
            p.setString(1,usuario); p.setString(2,correo); p.setString(3,clave); p.executeUpdate();
        }
    }

    public Map<String,Integer> indicadores() throws SQLException {
        Map<String,Integer> m=new LinkedHashMap<>();
        m.put("Cursos", contar("cursos")); m.put("Docentes", contar("docentes")); m.put("Asignaturas", contar("asignaturas")); m.put("Horarios", contar("horarios"));
        return m;
    }
    private int contar(String tabla)throws SQLException{
        try(Connection c=ConexionBD.obtenerConexion(); Statement s=c.createStatement(); ResultSet r=s.executeQuery("SELECT COUNT(*) FROM "+tabla)){ r.next(); return r.getInt(1); }
    }

    public List<Map<String,Object>> jornadas() throws SQLException { return lista("SELECT id,nombre FROM jornadas ORDER BY id"); }
    public List<Map<String,Object>> cursos() throws SQLException { return lista("SELECT c.id,c.grado,c.nombre,c.numero_estudiantes,c.jornada_id,j.nombre jornada FROM cursos c JOIN jornadas j ON j.id=c.jornada_id ORDER BY c.grado,c.nombre"); }
    public List<Map<String,Object>> docentes() throws SQLException { return lista("SELECT id,nombre,correo,telefono,max_horas_diarias FROM docentes ORDER BY nombre"); }
    public List<Map<String,Object>> asignaturas() throws SQLException { return lista("SELECT id,nombre,intensidad_horaria FROM asignaturas ORDER BY nombre"); }
    public List<Map<String,Object>> horarios() throws SQLException { return lista("SELECT h.id,h.curso_id,h.asignatura_id,h.docente_id,h.dia,TIME_FORMAT(h.hora_inicio,'%H:%i') hora_inicio,TIME_FORMAT(h.hora_fin,'%H:%i') hora_fin,c.nombre curso,a.nombre asignatura,d.nombre docente FROM horarios h JOIN cursos c ON c.id=h.curso_id JOIN asignaturas a ON a.id=h.asignatura_id JOIN docentes d ON d.id=h.docente_id ORDER BY FIELD(h.dia,'Lunes','Martes','Miercoles','Jueves','Viernes'),h.hora_inicio"); }
    public List<Map<String,Object>> horarioDocente(int docenteId) throws SQLException {
        String sql="SELECT h.id,h.dia,TIME_FORMAT(h.hora_inicio,'%H:%i') hora_inicio,TIME_FORMAT(h.hora_fin,'%H:%i') hora_fin,a.nombre asignatura,c.nombre curso,c.grado FROM horarios h JOIN asignaturas a ON a.id=h.asignatura_id JOIN cursos c ON c.id=h.curso_id WHERE h.docente_id=? ORDER BY FIELD(h.dia,'Lunes','Martes','Miercoles','Jueves','Viernes'),h.hora_inicio";
        List<Map<String,Object>> out=new ArrayList<>();
        try(Connection c=ConexionBD.obtenerConexion();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,docenteId);
            try(ResultSet r=p.executeQuery()){
                ResultSetMetaData md=r.getMetaData();
                while(r.next()){
                    Map<String,Object> m=new LinkedHashMap<>();
                    for(int i=1;i<=md.getColumnCount();i++) m.put(md.getColumnLabel(i),r.getObject(i));
                    out.add(m);
                }
            }
        }
        return out;
    }

    public Map<String,Object> curso(int id)throws SQLException{return uno("SELECT * FROM cursos WHERE id=?",id);}    
    public Map<String,Object> docente(int id)throws SQLException{return uno("SELECT * FROM docentes WHERE id=?",id);}    
    public Map<String,Object> asignatura(int id)throws SQLException{return uno("SELECT * FROM asignaturas WHERE id=?",id);}    
    public Map<String,Object> horario(int id)throws SQLException{return uno("SELECT id,curso_id,asignatura_id,docente_id,dia,TIME_FORMAT(hora_inicio,'%H:%i') hora_inicio,TIME_FORMAT(hora_fin,'%H:%i') hora_fin FROM horarios WHERE id=?",id);}    

    public void guardarCurso(Integer id,String grado,String nombre,int estudiantes,int jornada)throws SQLException{
        if(id==null) ejecutar("INSERT INTO cursos(grado,nombre,numero_estudiantes,jornada_id) VALUES(?,?,?,?)",grado,nombre,estudiantes,jornada);
        else ejecutar("UPDATE cursos SET grado=?,nombre=?,numero_estudiantes=?,jornada_id=? WHERE id=?",grado,nombre,estudiantes,jornada,id);
    }
    public void guardarDocente(Integer id,String nombre,String correo,String telefono,int max)throws SQLException{
        if(id==null) ejecutar("INSERT INTO docentes(nombre,correo,telefono,max_horas_diarias) VALUES(?,?,?,?)",nombre,correo,telefono,max);
        else ejecutar("UPDATE docentes SET nombre=?,correo=?,telefono=?,max_horas_diarias=? WHERE id=?",nombre,correo,telefono,max,id);
    }
    public void guardarAsignatura(Integer id,String nombre,int intensidad)throws SQLException{
        if(id==null) ejecutar("INSERT INTO asignaturas(nombre,intensidad_horaria) VALUES(?,?)",nombre,intensidad);
        else ejecutar("UPDATE asignaturas SET nombre=?,intensidad_horaria=? WHERE id=?",nombre,intensidad,id);
    }

    public void guardarHorario(Integer id,int curso,int asignatura,int docente,String dia,String inicio,String fin)throws SQLException{
        if(!inicio.matches("\\d{2}:\\d{2}") || !fin.matches("\\d{2}:\\d{2}")) throw new SQLException("Hora inválida");
        if(inicio.compareTo(fin)>=0) throw new SQLException("La hora final debe ser mayor que la inicial");
        validarCruces(id,curso,docente,dia,inicio,fin);
        validarMaximoDiario(id,docente,dia,inicio,fin);
        if(id==null) ejecutar("INSERT INTO horarios(curso_id,asignatura_id,docente_id,dia,hora_inicio,hora_fin) VALUES(?,?,?,?,?,?)",curso,asignatura,docente,dia,inicio,fin);
        else ejecutar("UPDATE horarios SET curso_id=?,asignatura_id=?,docente_id=?,dia=?,hora_inicio=?,hora_fin=? WHERE id=?",curso,asignatura,docente,dia,inicio,fin,id);
    }

    private void validarCruces(Integer id,int curso,int docente,String dia,String inicio,String fin)throws SQLException{
        String sql="SELECT COUNT(*) FROM horarios WHERE dia=? AND id<>? AND hora_inicio<? AND hora_fin>? AND (docente_id=? OR curso_id=?)";
        try(Connection c=ConexionBD.obtenerConexion();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,dia); p.setInt(2,id==null?0:id); p.setString(3,fin); p.setString(4,inicio); p.setInt(5,docente); p.setInt(6,curso);
            try(ResultSet r=p.executeQuery()){r.next(); if(r.getInt(1)>0) throw new SQLException("Hay cruce de horario para el docente o el curso");}
        }
    }

    private void validarMaximoDiario(Integer id,int docente,String dia,String inicio,String fin)throws SQLException{
        double nuevas=minutos(inicio,fin)/60.0;
        String sql="SELECT COALESCE(SUM(TIMESTAMPDIFF(MINUTE,hora_inicio,hora_fin)),0) mins FROM horarios WHERE docente_id=? AND dia=? AND id<>?";
        int usados;
        try(Connection c=ConexionBD.obtenerConexion();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,docente);p.setString(2,dia);p.setInt(3,id==null?0:id);try(ResultSet r=p.executeQuery()){r.next();usados=r.getInt(1);}}
        int max=8;
        try(Connection c=ConexionBD.obtenerConexion();PreparedStatement p=c.prepareStatement("SELECT max_horas_diarias FROM docentes WHERE id=?")){p.setInt(1,docente);try(ResultSet r=p.executeQuery()){if(r.next())max=r.getInt(1);}}
        if(usados/60.0+nuevas>max) throw new SQLException("El docente supera el máximo de "+max+" horas diarias");
    }
    private int minutos(String a,String b){String[]x=a.split(":");String[]y=b.split(":");return (Integer.parseInt(y[0])*60+Integer.parseInt(y[1]))-(Integer.parseInt(x[0])*60+Integer.parseInt(x[1]));}

    public void eliminar(String tabla,int id)throws SQLException{
        if(!Set.of("cursos","docentes","asignaturas","horarios").contains(tabla)) throw new SQLException("Tabla no permitida");
        ejecutar("DELETE FROM "+tabla+" WHERE id=?",id);
    }

    private List<Map<String,Object>> lista(String sql)throws SQLException{
        List<Map<String,Object>> out=new ArrayList<>();
        try(Connection c=ConexionBD.obtenerConexion();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            ResultSetMetaData md=r.getMetaData(); while(r.next()){Map<String,Object> m=new LinkedHashMap<>(); for(int i=1;i<=md.getColumnCount();i++)m.put(md.getColumnLabel(i),r.getObject(i)); out.add(m);}
        } return out;
    }
    private Map<String,Object> uno(String sql,int id)throws SQLException{
        try(Connection c=ConexionBD.obtenerConexion();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,id);try(ResultSet r=p.executeQuery()){if(!r.next())return new LinkedHashMap<>();ResultSetMetaData md=r.getMetaData();Map<String,Object>m=new LinkedHashMap<>();for(int i=1;i<=md.getColumnCount();i++)m.put(md.getColumnLabel(i),r.getObject(i));return m;}}
    }
    private void ejecutar(String sql,Object...v)throws SQLException{
        try(Connection c=ConexionBD.obtenerConexion();PreparedStatement p=c.prepareStatement(sql)){for(int i=0;i<v.length;i++)p.setObject(i+1,v[i]);p.executeUpdate();}
    }
}
