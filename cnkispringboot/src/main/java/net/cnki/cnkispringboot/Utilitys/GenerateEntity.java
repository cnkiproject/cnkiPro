package net.cnki.cnkispringboot.Utilitys;

import com.kbase.jdbc.ConnectionImpl;
import com.kbase.jdbc.PreparedStatement;
import com.kbase.jdbc.ResultSetImpl;
import kbase.struct.TPI_RETURN_RESULT;

import java.sql.DriverManager;
import java.sql.SQLException;

public class GenerateEntity {
    public static void main(String[] args) {
        try {
            String sql="SELECT * FROM sys_hotstar_system  where ISVIEW=Y  ORDER BY TABLENAME ASC";
            String type = "String";

            Class.forName("com.kbase.jdbc.Driver");
            String url = "jdbc:kbase://127.0.0.1";
            String user = "DBOWN";
            String password ="";
            ConnectionImpl conn = (ConnectionImpl) DriverManager.getConnection(url, user, password);
            StringBuffer stringBuffer = new StringBuffer();
            PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
            ResultSetImpl rs1 = (ResultSetImpl) ps.executeQuery();
            TPI_RETURN_RESULT fieldResult = rs1.KBaseGetRecordSetFieldName(0);
            String recordSetField = fieldResult.rtnBuf;
            String[] allColumns = recordSetField.split(",");
            for (int i = 0; i < allColumns.length; i++) {
                String name = allColumns[i];
                stringBuffer.append("private " + type + " " + name + ";\r\n");
            }
            String model = stringBuffer.toString();
            System.err.println(model);
            System.err.println(allColumns.length);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}