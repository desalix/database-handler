package cursos;

import java.sql.*;

public class BBDDManager {

    private String user;
    private String password;

    public BBDDManager(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String url() {
        return "jdbc:mysql://localhost:3306/cursos_db";
    }

    public StringWriter run(DataBaseTask[] tasks, String[] dataArray, boolean autoCommit) {
        StringWriter result = new StringWriter();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url(), user, password);
            conn.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            result.add("Connection:" + e.getMessage() + ";");
            result.add("fin");
            return result;
        } catch (Exception e) {
            result.add("Otro:" + e.getMessage() + ";");
            result.add("fin");
            return result;
        }
        try {
            for (int i = 0; i < tasks.length; i++) {
                try {
                    tasks[i].run(conn, dataArray[i]);
                } catch (BBDDException e) {
                    result.add("Task:" + e.when() + ";" + e.getMessage() + ";");
                    if (!autoCommit) {
                        try {
                            conn.commit();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                } catch (SQLException e) {
                    result.add("SQL:" + e.getMessage() + ";");
                    if (!autoCommit) {
                        try {
                            conn.rollback();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {}
        }
        result.add("fin");
        return result;
    }
}
