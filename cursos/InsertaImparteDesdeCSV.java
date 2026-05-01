package cursos;

import java.io.FileInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
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
            
            int lineNumber = 0;
            while (sc.hasNextLine()) {
                lineNumber++;
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                insertarLinea(stmt, line, lineNumber);
            }
        } catch (SQLException e) {
            throw new BBDDException(e, "Error al insertar en la base de datos: " + e.getMessage());
        } catch (BBDDException e) {
            throw e;
        } catch (Exception e) {
            throw new BBDDException(e, "Error al leer el archivo CSV: " + e.getMessage());
        }
    }

        private void insertarLinea(PreparedStatement ps, String linea, int numLinea)
            throws BBDDException, SQLException {
 
        int profesorId;
        int cursoId;
        int nModulo;
        int aulaId;
        java.sql.Date fecha;
 
        try {
            String[] campos = linea.split(",");
            if (campos.length != 5) {
                throw new IllegalArgumentException(
                    "Se esperaban 5 campos en la linea " + numLinea
                    + ", se han encontrado " + campos.length);
            }
            profesorId = Integer.parseInt(campos[0].trim());
            cursoId = Integer.parseInt(campos[1].trim());
            nModulo = Integer.parseInt(campos[2].trim());
            aulaId = Integer.parseInt(campos[3].trim());
 
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            java.util.Date parsed = sdf.parse(campos[4].trim());
            fecha = new java.sql.Date(parsed.getTime());
        } catch (Exception e) {
            throw new BBDDException(e, "Error extrayendo los datos de la linea " + numLinea + " del CSV");
        }
 
        ps.setInt(1, profesorId);
        ps.setInt(2, cursoId);
        ps.setInt(3, nModulo);
        ps.setInt(4, aulaId);
        ps.setDate(5, fecha);
        ps.executeUpdate();
    }
}
