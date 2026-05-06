package cursos;

import java.sql.*;
import java.util.*;

public class ConsultaConFiltro extends ConsultaConResultado<Properties> {
    /**
     * Obtiene los profesores que imparten un modulo cuyo titulo
     * contiene la cadena dada.
     *
     * @throws BBDDException, cuando data este vacia. Se debe fijar
     *         when a "filtro vacio"
     * @throws SQLException, cuando se produzca la misma al ejecutar
     *         modificar la tabla.
     */
    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        if (data == null || data.isEmpty()) {
            throw new BBDDException(null, "filtro vacio");
        }
        resultado = new ArrayList<>();
        String sql = "SELECT p.nombre, p.apellido1, p.apellido2, m.curso_id, m.titulo " +
                     "FROM profesor p " +
                     "JOIN imparte i ON p.id = i.profesor_id " +
                     "JOIN modulo m ON i.curso_id = m.curso_id AND i.n_modulo = m.n_modulo " +
                     "WHERE m.titulo LIKE ? " +
                     "ORDER BY p.apellido1 ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + data + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String extra = rs.getString("curso_id") + "-" + rs.getString("titulo");
                    resultado.add(new Properties(
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2"),
                        extra
                    ));
                }
            }
        }
    }
}
