package utils.networkUtil;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by kuldeep.Dwivedi on 12/1/2014.
 */
public class DBConnection {

    @SuppressLint("NewApi")
    public Connection CONN(String user, String pass, String db, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String connUrl = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connUrl = "jdbc:jtds:sqlserver://" + server + ";" + "databaseName="
                    + db + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connUrl);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException cl) {
            Log.e("ERROR", cl.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        return conn;

    }
}
