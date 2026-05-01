package cursos;

import java.sql.*;
import java.text.SimpleDateFormat;

public class InsertaUnaFilaImparte implements DataBaseTask {
    private static final String SQL =
    "INSERT INTO imparte (profesor_id, curso_id, n_modulo, aula_id, fecha) " +
    "VALUES (?, ?, ?, ?, ?)";

    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SQL)){
            int profesorId;
            int cursoId;
            int nModulo;
            int aulaId;
            java.sql.Date fecha;

            try {
                String[] campos = data.split(",");
                if (campos.length != 5) {
                    throw new IllegalArgumentException(
                        "Se esperaban 5 campos separados por coma, se han recibido " + campos.length);
                }
                profesorId = Integer.parseInt(campos[0].trim());
                cursoId    = Integer.parseInt(campos[1].trim());
                nModulo    = Integer.parseInt(campos[2].trim());
                aulaId     = Integer.parseInt(campos[3].trim());
 
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                java.util.Date parsed = sdf.parse(campos[4].trim());
                fecha = new java.sql.Date(parsed.getTime());
            } catch (Exception e) {
                throw new BBDDException(e, "Error al parsear los datos: " + e.getMessage());
            }

            stmt.setInt(1, profesorId);
            stmt.setInt(2, cursoId);
            stmt.setInt(3, nModulo);
            stmt.setInt(4, aulaId);
            stmt.setDate(5, fecha);
            stmt.executeUpdate();
        }
    }
}
