package akb428.maki;

import java.sql.*;
import org.h2.tools.Csv;
import org.h2.tools.SimpleResultSet;
public class H2CsvSample {
    public static void main(String[] args) throws Exception {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("NAME", Types.VARCHAR, 255, 0);
        rs.addColumn("EMAIL", Types.VARCHAR, 255, 0);
        rs.addRow("Bob Meier", "bob.meier@abcde.abc");
        rs.addRow("John Jones", "john.jones@abcde.abc");
        new Csv().write("logs/test.csv", rs, null);
    }
}