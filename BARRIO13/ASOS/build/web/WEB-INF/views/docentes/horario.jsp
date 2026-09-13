<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>

<%@ include file="../fragments/header.jspf" %>

<style>

    .calendar-container {
        overflow-x: auto;
        margin-top: 20px;
    }

    .calendar-title {
        margin-bottom: 15px;
        padding: 15px;
        background: #0d0e10;
        border-left: 4px solid var(--acento);
        border-radius: 5px;
    }

    .calendar-title strong {
        color: var(--acento);
        font-size: 20px;
    }

    .calendar-title span {
        color: var(--muted);
        font-size: 14px;
    }

    .calendar {
        width: 100%;
        min-width: 900px;
        border-collapse: collapse;
        table-layout: fixed;
    }

    .calendar th {
        background: #111315;
        color: var(--acento);
        padding: 14px;
        text-align: center;
        border: 1px solid #333;
    }

    .calendar td {
        border: 1px solid #333;
        height: 95px;
        vertical-align: top;
        padding: 6px;
        background: #191b1e;
    }

    .calendar .hora {
        width: 100px;
        background: #111315;
        color: #fff;
        font-weight: bold;
        text-align: center;
        vertical-align: middle;
    }

    .clase {
        background: #24272b;
        border-left: 4px solid var(--acento);
        padding: 9px;
        border-radius: 5px;
        height: 100%;
        box-sizing: border-box;
    }

    .clase-materia {
        color: var(--acento);
        font-weight: bold;
        font-size: 15px;
        margin-bottom: 5px;
    }

    .clase-curso {
        color: white;
        font-size: 14px;
        margin-bottom: 5px;
    }

    .clase-hora {
        color: #aaa;
        font-size: 13px;
    }

    .sin-clase {
        text-align: center;
        color: #555;
        vertical-align: middle !important;
    }

    .selector-docente {
        margin-bottom: 25px;
    }

</style>


<section class="panel">

    <div class="toolbar">

        <span>Calendario del profesor</span>

        <a class="button secondary"
           href="${pageContext.request.contextPath}/docentes">

            Volver a docentes

        </a>

    </div>


    <!-- SELECCIONAR DOCENTE -->

    <form method="get"
          action="${pageContext.request.contextPath}/horario-docente"
          class="selector-docente">

        <div class="grid">

            <div class="field">

                <label for="docente_id">
                    Seleccione un profesor
                </label>

                <select id="docente_id"
                        name="docente_id"
                        required>

                    <option value="">
                        -- Seleccionar profesor --
                    </option>


                    <%

                        List<Map<String,Object>> docentes =
                            (List<Map<String,Object>>)
                            request.getAttribute("docentes");

                        Integer seleccionado =
                            (Integer)
                            request.getAttribute("docenteId");


                        for(Map<String,Object> d : docentes){

                            int did =
                                ((Number)d.get("id")).intValue();

                    %>


                    <option
                        value="<%=did%>"

                        <%=
                            seleccionado != null
                            && seleccionado == did
                            ? "selected"
                            : ""
                        %>
                    >

                        <%=d.get("nombre")%>

                    </option>


                    <%

                        }

                    %>

                </select>

            </div>


            <div class="form-actions"
                 style="grid-column:auto; align-items:end;">

                <button type="submit">
                    Ver calendario
                </button>

            </div>

        </div>

    </form>


    <%

        Map<String,Object> docente =
            (Map<String,Object>)
            request.getAttribute("docenteSeleccionado");


        if(docente != null){

            List<Map<String,Object>> horario =
                (List<Map<String,Object>>)
                request.getAttribute("horarioDocente");

    %>


    <!-- INFORMACION DOCENTE -->

    <div class="calendar-title">

        <strong>
            <%=docente.get("nombre")%>
        </strong>

        <br>

        <span>

            <%=docente.get("correo")%>

            · Máximo

            <%=docente.get("max_horas_diarias")%>

            horas por día

        </span>

    </div>


    <%

        if(horario == null || horario.isEmpty()){

    %>


        <p>
            Este profesor todavía no tiene clases asignadas.
        </p>


    <%

        } else {


            /*
             * Guardamos las clases organizadas
             * por DIA + HORA INICIO
             */

            Map<String, Map<String, Map<String,Object>>> calendario =
                new LinkedHashMap<>();


            String[] dias = {
                "Lunes",
                "Martes",
                "Miercoles",
                "Jueves",
                "Viernes"
            };


            for(String dia : dias){

                calendario.put(
                    dia,
                    new LinkedHashMap<>()
                );

            }


            /*
             * Guardamos también todas las horas
             * que existen en el horario.
             */

            Set<String> horas =
                new TreeSet<>();


            for(Map<String,Object> clase : horario){

                String dia =
                    String.valueOf(clase.get("dia"));

                String horaInicio =
                    String.valueOf(
                        clase.get("hora_inicio")
                    );

                horas.add(horaInicio);


                if(calendario.containsKey(dia)){

                    calendario
                        .get(dia)
                        .put(
                            horaInicio,
                            clase
                        );

                }

            }

    %>


    <!-- CALENDARIO -->

    <div class="calendar-container">

        <table class="calendar">

            <thead>

                <tr>

                    <th>
                        Hora
                    </th>

                    <th>
                        Lunes
                    </th>

                    <th>
                        Martes
                    </th>

                    <th>
                        Miércoles
                    </th>

                    <th>
                        Jueves
                    </th>

                    <th>
                        Viernes
                    </th>

                </tr>

            </thead>


            <tbody>


            <%

                for(String hora : horas){

            %>


                <tr>


                    <!-- HORA -->

                    <td class="hora">

                        <%=hora%>

                    </td>


                    <%

                        for(String dia : dias){

                            Map<String,Object> clase =
                                calendario
                                    .get(dia)
                                    .get(hora);


                            if(clase != null){

                    %>


                    <!-- CLASE -->

                    <td>

                        <div class="clase">


                            <div class="clase-materia">

                                <%=clase.get("asignatura")%>

                            </div>


                            <div class="clase-curso">

                                Curso:

                                <%=clase.get("curso")%>

                            </div>


                            <div class="clase-hora">

                                <%=clase.get("hora_inicio")%>

                                -

                                <%=clase.get("hora_fin")%>

                            </div>


                        </div>

                    </td>


                    <%

                            } else {

                    %>


                    <!-- SIN CLASE -->

                    <td class="sin-clase">

                        —

                    </td>


                    <%

                            }

                        }

                    %>


                </tr>


            <%

                }

            %>


            </tbody>

        </table>

    </div>


    <%

        }

    %>


    <%

        } else {

    %>


        <p>

            Seleccione un profesor para ver
            su calendario semanal.

        </p>


    <%

        }

    %>


</section>


<%@ include file="../fragments/footer.jspf" %>