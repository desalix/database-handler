package cursos;

import java.util.ArrayList;

public class MainS3 {

    static BBDDManager cm = new BBDDManager("alumno", "bbdd-upm");

    // ----------------------------------------------------------------
    // AddColumn: adds foto BLOB NULL to edificio
    // ----------------------------------------------------------------
    static void testAddColumn() {
        System.out.println("=== AddColumn ===");
        DataBaseTask[] tasks = { new AddColumn() };
        String[] data = { "" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println(result);
        // Expected: [fin]  (empty = success)
        // If run twice: [SQL:<error about duplicate column>;, fin]
    }

    // ----------------------------------------------------------------
    // ConsultaSimple: valid ASC/DESC, invalid input
    // ----------------------------------------------------------------
    static void testConsultaSimple() {
        System.out.println("=== ConsultaSimple ASC ===");
        ConsultaSimple cs = new ConsultaSimple();
        DataBaseTask[] tasks = { cs };
        String[] data = { "ASC" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println(result);
        ArrayList<Properties> rows = cs.get();
        if (rows != null) rows.forEach(System.out::println);

        System.out.println("=== ConsultaSimple DESC ===");
        ConsultaSimple cs2 = new ConsultaSimple();
        tasks = new DataBaseTask[]{ cs2 };
        data  = new String[]{ "desc" };   // lowercase on purpose
        result = cm.run(tasks, data, true);
        System.out.println(result);
        if (cs2.get() != null) cs2.get().forEach(System.out::println);

        System.out.println("=== ConsultaSimple invalid (should get Task:ordenando) ===");
        ConsultaSimple cs3 = new ConsultaSimple();
        tasks = new DataBaseTask[]{ cs3 };
        data  = new String[]{ "RANDOM" };
        result = cm.run(tasks, data, true);
        System.out.println(result);
        // Expected: [Task:ordenando;<exception message>;, fin]
    }

    // ----------------------------------------------------------------
    // ConsultaConFiltro: valid filter, empty filter
    // ----------------------------------------------------------------
    static void testConsultaConFiltro() {
        System.out.println("=== ConsultaConFiltro with 'Prog' ===");
        ConsultaConFiltro ccf = new ConsultaConFiltro();
        DataBaseTask[] tasks = { ccf };
        String[] data = { "Prog" };     // adjust to a word in your modulo.titulo
        StringWriter result = cm.run(tasks, data, true);
        System.out.println(result);
        if (ccf.get() != null) ccf.get().forEach(System.out::println);
        // Expected: rows like 'Nombre':'Apellido1':'Apellido2':'3-Programacion Java'

        System.out.println("=== ConsultaConFiltro empty (should get Task:filtro vacio) ===");
        ConsultaConFiltro ccf2 = new ConsultaConFiltro();
        tasks = new DataBaseTask[]{ ccf2 };
        data  = new String[]{ "" };
        result = cm.run(tasks, data, true);
        System.out.println(result);
        // Expected: [Task:filtro vacio;<exception message>;, fin]
    }

    // ----------------------------------------------------------------
    // BBDDManager error paths
    // ----------------------------------------------------------------
    static void testBBDDManagerErrors() {
        System.out.println("=== Bad credentials (should get Connection:...) ===");
        BBDDManager bad = new BBDDManager("nobody", "wrong");
        DataBaseTask[] tasks = { new ConsultaSimple() };
        String[] data = { "ASC" };
        StringWriter result = bad.run(tasks, data, true);
        System.out.println(result);
        // Expected: [Connection:<msg>;, fin]

        System.out.println("=== autoCommit=false, SQL error -> rollback ===");
        // Trigger duplicate PK by inserting same row twice
        DataBaseTask[] tasks2 = {
            new InsertaUnaFilaImparte(),
            new InsertaUnaFilaImparte()   // duplicate -> SQLException -> rollback
        };
        String[] data2 = {
            "7, 3, 2, 4, 14/03/2025",
            "7, 3, 2, 4, 14/03/2025"
        };
        BBDDManager cmAC = new BBDDManager("alumno", "bbdd-upm");
        StringWriter result2 = cmAC.run(tasks2, data2, false);
        System.out.println(result2);
        // Expected: second insert produces SQL:<duplicate key msg>;, fin
        // And because autoCommit=false + rollback, nothing is persisted
    }

    public static void main(String[] args) {
        testAddColumn();
        testConsultaSimple();
        testConsultaConFiltro();
        testBBDDManagerErrors();
    }
}