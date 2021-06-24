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
            BufferedReader bufferedReader = new BufferedReader(new FileReader("config.json"));
            String line;

            while ((line = bufferedReader.readLine()) != null) json = json.concat(line);

            bufferedReader.close();

            JSONObject jsonObject = new JSONObject(json);

            host = jsonObject.getString("host");
            port = jsonObject.getString("port");
            user = jsonObject.getString("user");
            password = jsonObject.getString("password");
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