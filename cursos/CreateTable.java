package cursos;

import java.sql.*;

public class CreateTable implements DataBaseTask {
    private final String queryImparte = "CREATE TABLE imparte (" +
        " profesor_id INT NOT NULL," +
        " curso_id INT NOT NULL," +
        " n_modulo INT NOT NULL," +
        " aula_id INT NOT NULL," +
        " fecha DATE NOT NULL," +
        " PRIMARY KEY (profesor_id, curso_id, n_modulo, aula_id, fecha)," +
        " FOREIGN KEY (profesor_id) REFERENCES profesor(id)" +
        " ON DELETE CASCADE," +
        " FOREIGN KEY (curso_id, n_modulo) REFERENCES modulo(curso_id, n_modulo)" +
        " ON DELETE CASCADE," +
        " FOREIGN KEY (aula_id) REFERENCES aula(id)" +
        " ON DELETE CASCADE" +
        ")";

    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(queryImparte);
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new BBDDException(e, "Error ejecutando el comando de creacion de la tabla Imparte");
        }
    }
}