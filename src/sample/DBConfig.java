package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 * Contains addresses and names of a database and inner tables.
 *
 * @author Vladislav
 * @version 2.1
 * @since 1.0
 */
class DBConfig {
    private static String json = "";
    static String host;
    static String port;
    static String user;
    static String password;

    static {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.json"));
            String line;

            while ((line = br.readLine()) != null) json = json.concat(line);

            br.close();

            JSONObject jo = new JSONObject(json);

            host = jo.getString("host");
            port = jo.getString("port");
            user = jo.getString("user");
            password = jo.getString("password");
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Please, create or check config.json file!", "Config file is missed or corrupted", JOptionPane.ERROR_MESSAGE);
        }
    }

    static final String DATABASE = "contracts_accounting";
    static final String CONTRACTS = "contracts";
    static final String DATE = "date";
    static final String NUMBER = "number";
    static final String PATH = "path";
    static final String DESCRIPTION = "description";
    static final String STATUS = "status";
    static final String COMPLETION_DATE = "completion_date";
}