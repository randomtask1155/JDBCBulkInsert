package io.pivotal.support;

/**
 * Created by danl on 11/20/14.
 */


import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCBulkInsert {

    public static void usage() {
        System.err.println("Usage: ");
        System.err.println("java io.pivotal.support.JDBCBulkInsert \"jdbc:postgresql://localhost:5432/database\" user password file table\n");
    }

    public static void main(String[] args) throws Exception {
        Connection con = null;
        String driver = "org.postgresql.Driver";

        if ( args.length != 5 ) {
            System.err.println("Not enough arguments specified");
            usage();
            System.exit(2);
        }

        String url = args[0];
        String user = args[1];
        String password = args[2];
        String file = args[3];
        String table = args[4];
        String COPY_CMD = "COPY " + table + " from STDIN";

        try {
            System.out.println("~# Loading Driver " + driver);
            Class.forName(driver);

            System.out.println("~# connecting to database with url " + url);
            con = DriverManager.getConnection(url, user, password);
            CopyManager cm = new CopyManager((BaseConnection) con);

            System.out.println("~# reading in file " + file);
            FileReader fileReader = new FileReader(file);
            cm.copyIn(COPY_CMD, fileReader);
            System.out.println("~# COPY operation completed successfully");
            con.close();
        } catch (SQLException ex)
        {
            System.err.println("Failed to copy data: " + ex.getMessage());
        }
        finally
        {
            try
            {
                if (con!=null)
                    con.close();
            }
            catch (SQLException ex)
            {
                System.err.println(ex.getMessage());
            }
        }

    }
}
