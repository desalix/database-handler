package cursos;

import java.sql.*;
import java.util.*;

public class ConsultaSimple extends ConsultaConResultado<Properties> {
    /**
     * Obtiene los profesores ordenados por apellido1
     *
     * @param conn La conexion ya abierta
     * @param data o bien ASC o bien DESC (debe ser case-insentive)
     *
     * @throws BBDDException, cuando `data` sea distinto de ACS y DESC.
     * @throws SQLException, cuando se produzca la misma al ejecutar
     *         modificar la tabla.
     */
    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        if (!data.equalsIgnoreCase("ASC") && !data.equalsIgnoreCase("DESC")) {
            throw new BBDDException(null, "ordenando");
        }
        
        resultado = new ArrayList<>();
        String sql = "SELECT nombre, apellido1, apellido2 FROM profesor ORDER BY apellido1 " + data.toUpperCase();

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resultado.add(new Properties(
                    rs.getString("nombre"),
                    rs.getString("apellido1"),
                    rs.getString("apellido2")
                ));
            }
        }
    }
}
