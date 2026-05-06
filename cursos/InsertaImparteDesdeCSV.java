package cursos;

import java.io.FileInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class InsertaImparteDesdeCSV implements DataBaseTask {
    private static final String SQL =
        "INSERT INTO imparte (profesor_id, curso_id, n_modulo, aula_id, fecha) " +
        "VALUES (?, ?, ?, ?, ?)";

    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        try (FileInputStream fis = new FileInputStream(data);
             Scanner sc = new Scanner(fis);
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                insertarLinea(stmt, line);
            }
        } catch (BBDDException | SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new BBDDException(e, "Insertando");
        }
    }

    private void insertarLinea(PreparedStatement stmt, String linea)
            throws BBDDException, SQLException {
        int profesorId, cursoId, nModulo, aulaId;
        java.sql.Date fecha;
        try {
            String[] campos = linea.split(",");
            profesorId = Integer.parseInt(campos[0].trim());
            cursoId    = Integer.parseInt(campos[1].trim());
            nModulo    = Integer.parseInt(campos[2].trim());
            aulaId     = Integer.parseInt(campos[3].trim());
            String[] partesFecha = campos[4].trim().split("-");
            int year  = Integer.parseInt(partesFecha[0].trim());
            int month = Integer.parseInt(partesFecha[1].trim());
            int day   = Integer.parseInt(partesFecha[2].trim());
            fecha = Date.valueOf(LocalDate.of(year, month, day));
        } catch (Exception e) {
            throw new BBDDException(e, "Insertando");
        }
        stmt.setInt(1, profesorId);
        stmt.setInt(2, cursoId);
        stmt.setInt(3, nModulo);
        stmt.setInt(4, aulaId);
        stmt.setDate(5, fecha);
        int rows = stmt.executeUpdate();
        if (rows != 1) {
            throw new SQLException("Expected 1 row inserted, got " + rows);
        }
    }
}
