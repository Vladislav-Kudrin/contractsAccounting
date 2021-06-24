package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.lang.String;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import sample.tables.*;

/**
 * Handles database's queries.
 *
 * @author Vladislav
 * @version 2.0
 * @since 1.0
 */
class DBHandler extends DBConfig{

    /**
     * Makes a connection to a database.
     *
     * @since 1.0
     * @return the connection to the database.
     * @throws SQLException an exception of the connection issues.
     */
    private Connection connect() throws SQLException {
        String connection = "jdbc:mysql://" + host + ":" + port + "/" + DATABASE;

        return DriverManager.getConnection(connection, user, password);
    }

    /**
     * Resets a value of a database's auto_increment.
     *
     * @since 1.0
     */
    private void resetContracts() {
        PreparedStatement preparedStatement;
        String resetQuery = "ALTER TABLE " + CONTRACTS + " AUTO_INCREMENT = 1";

        try {
            preparedStatement = connect().prepareStatement(resetQuery);

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    /**
     * Adds a contract's data to a database.
     *
     * @since 1.0
     * @param contract the contract's data.
     */
    void addContract(Contract contract) {
        PreparedStatement preparedStatement;
        String insertQuery = "INSERT INTO " + CONTRACTS + "(" + DATE + ", " + NUMBER + ", " + PATH + ", " + DESCRIPTION + ", " + STATUS + ", " + COMPLETION_DATE + ") " + "VALUES(?, ?, ?, ?, ?, ?)";

        try {
            preparedStatement = connect().prepareStatement(insertQuery);

            preparedStatement.setString(1, contract.getDate());
            preparedStatement.setInt(2, contract.getNumber());
            preparedStatement.setString(3, contract.getPath());
            preparedStatement.setString(4, contract.getDescription());
            preparedStatement.setByte(5, contract.getStatus());
            preparedStatement.setString(6, contract.getCompletionDate());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    /**
     * Updates a contract's data in a database.
     *
     * @since 1.0
     * @param contract the contract's data.
     */
    void updateContract(Contract contract) {
        PreparedStatement preparedStatement;
        String updateQuery = "UPDATE " + CONTRACTS + " SET " + PATH + " = ?, " + DESCRIPTION + " = ?, " + STATUS + " = ?, " + COMPLETION_DATE + " = ? WHERE " + NUMBER + " = ?";

        try {
            preparedStatement = connect().prepareStatement(updateQuery);

            preparedStatement.setString(1, contract.getPath());
            preparedStatement.setString(2, contract.getDescription());
            preparedStatement.setByte(3, contract.getStatus());
            preparedStatement.setString(4, contract.getCompletionDate());
            preparedStatement.setInt(5, contract.getNumber());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    /**
     * Deletes a contract's data from a database.
     *
     * @since 1.0
     * @param number the current contract's number.
     */
    void deleteContract(int number) {
        PreparedStatement preparedStatement;
        String deleteQuery = "DELETE FROM " + CONTRACTS + " WHERE " + NUMBER + " = ?";

        try {
            preparedStatement = connect().prepareStatement(deleteQuery);

            preparedStatement.setInt(1, number);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    /**
     * Gets and returns all contracts' data from a database.
     *
     * @since 1.0
     * @return all contracts' data from the database.
     */
    ObservableList<Contract> getAll() {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        ObservableList<Contract> contractsList = FXCollections.observableArrayList();
        String selectQuery = "SELECT * FROM " + CONTRACTS;

        try {
            preparedStatement = connect().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String[] splitDate = resultSet.getString(DATE).split("-");
                String[] splitCompletionDate = (resultSet.getString(COMPLETION_DATE) != null) ? resultSet.getString(COMPLETION_DATE).split("-") : null;

                contractsList.add(new Contract(splitDate[2] + "." + splitDate[1] + "." + splitDate[0], resultSet.getInt(NUMBER),
                        resultSet.getString(PATH), resultSet.getString(DESCRIPTION), resultSet.getByte(STATUS),
                        (splitCompletionDate != null) ? splitCompletionDate[2] + "." + splitCompletionDate[1] + "." + splitCompletionDate[0] : null));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        if (contractsList.isEmpty()) resetContracts();

        return contractsList;
    }

    /**
     * Gets and returns incompleted contracts' data from a database.
     *
     * @since 1.0
     * @return incompleted contracts' data from the database.
     */
    ObservableList<Contract> getIncompleted() {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        ObservableList<Contract> contractsList = FXCollections.observableArrayList();
        String selectQuery = "SELECT * FROM " + CONTRACTS + " WHERE " + STATUS + " = 0";

        try {
            preparedStatement = connect().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String[] splitDate = resultSet.getString(DATE).split("-");

                contractsList.add(new Contract(splitDate[2] + "." + splitDate[1] + "." + splitDate[0], resultSet.getInt(NUMBER),
                        resultSet.getString(PATH), resultSet.getString(DESCRIPTION), resultSet.getByte(STATUS), null));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        if (contractsList.isEmpty()) resetContracts();

        return contractsList;
    }

    /**
     * Gets and returns completed contracts' data from a database.
     *
     * @since 1.0
     * @return completed contracts' data from the database.
     */
    ObservableList<Contract> getCompleted() {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        ObservableList<Contract> contractsList = FXCollections.observableArrayList();
        String selectQuery = "SELECT * FROM " + CONTRACTS + " WHERE " + STATUS + " = 1";

        try {
            preparedStatement = connect().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String[] splitDate = resultSet.getString(DATE).split("-");
                String[] splitCompletionDate = resultSet.getString(COMPLETION_DATE).split("-");

                contractsList.add(new Contract(splitDate[2] + "." + splitDate[1] + "." + splitDate[0], resultSet.getInt(NUMBER),
                        resultSet.getString(PATH), resultSet.getString(DESCRIPTION), resultSet.getByte(STATUS),
                        splitCompletionDate[2] + "." + splitCompletionDate[1] + "." + splitCompletionDate[0]));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        if (contractsList.isEmpty()) resetContracts();

        return contractsList;
    }

    /**
     * Checks a contract's existing in a database.
     *
     * @since 1.0
     * @param number the current contract's number.
     * @return true if the contract is exist in the database, false if the contract isn't exist in the database.
     */
    boolean isContractExist(int number) {
        PreparedStatement preparedStatement;
        String selectQuery = "SELECT * FROM " + CONTRACTS + " WHERE " + NUMBER + " = ?";

        try {
            preparedStatement = connect().prepareStatement(selectQuery);

            preparedStatement.setInt(1, number);

            return preparedStatement.executeQuery().next();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        return false;
    }
}