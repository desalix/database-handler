package cursos;

import java.sql.*;
import java.time.LocalDate;

public class InsertaUnaFilaImparte implements DataBaseTask {
    private static final String SQL =
        "INSERT INTO imparte (profesor_id, curso_id, n_modulo, aula_id, fecha) " +
        "VALUES (?, ?, ?, ?, ?)";

    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        int profesorId, cursoId, nModulo, aulaId;
        java.sql.Date fecha;
        try {
            String[] campos = data.split(",");
            profesorId = Integer.parseInt(campos[0].trim());
            cursoId    = Integer.parseInt(campos[1].trim());
            nModulo    = Integer.parseInt(campos[2].trim());
            aulaId     = Integer.parseInt(campos[3].trim());
            String[] partesFecha = campos[4].trim().split("/");
            int day   = Integer.parseInt(partesFecha[0].trim());
            int month = Integer.parseInt(partesFecha[1].trim());
            int year  = Integer.parseInt(partesFecha[2].trim());
            fecha = Date.valueOf(LocalDate.of(year, month, day));
        } catch (Exception e) {
            throw new BBDDException(e, "Insertando");
        }
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
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
}
