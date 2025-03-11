package com.course.java;

import com.course.java.controller.AgendaController;
import com.course.java.database.DataBase;
import com.course.java.service.AgendaService;

public class App4 {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        AgendaService agendaService = new AgendaService(dataBase);
        AgendaController controller = new AgendaController(agendaService);
        controller.iniciar();
    }
}