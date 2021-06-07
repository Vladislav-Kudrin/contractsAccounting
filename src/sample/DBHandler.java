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

class DBHandler extends DBConfig{

    private Connection connect() throws SQLException {
        String connection = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;

        return DriverManager.getConnection(connection, USER, PASSWORD);
    }

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

    void addContract(Contract contract) {
        PreparedStatement preparedStatement;
        String insertQuery = "INSERT INTO " + CONTRACTS + "(" + DATE + ", " + NUMBER + ", " + PATH + ", " + DESCRIPTION + ", " + STATUS + ") " + "VALUES(?, ?, ?, ?, ?)";

        try {
            preparedStatement = connect().prepareStatement(insertQuery);

            preparedStatement.setString(1, contract.getDate());
            preparedStatement.setInt(2, contract.getNumber());
            preparedStatement.setString(3, contract.getPath());
            preparedStatement.setString(4, contract.getDescription());
            preparedStatement.setByte(5, contract.getStatus());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    void updateContract(Contract contract) {
        PreparedStatement preparedStatement;
        String updateQuery = "UPDATE " + CONTRACTS + " SET " + PATH + " = ?, " + DESCRIPTION + " = ?, " + STATUS + " = ? WHERE " + NUMBER + " = ?";

        try {
            preparedStatement = connect().prepareStatement(updateQuery);

            preparedStatement.setString(1, contract.getPath());
            preparedStatement.setString(2, contract.getDescription());
            preparedStatement.setByte(3, contract.getStatus());
            preparedStatement.setInt(4, contract.getNumber());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

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

                contractsList.add(new Contract(splitDate[2] + "." + splitDate[1] + "." + splitDate[0], resultSet.getInt(NUMBER),
                        resultSet.getString(PATH), resultSet.getString(DESCRIPTION), resultSet.getByte(STATUS)));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        if (contractsList.isEmpty()) resetContracts();

        return contractsList;
    }

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
                        resultSet.getString(PATH), resultSet.getString(DESCRIPTION), resultSet.getByte(STATUS)));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        if (contractsList.isEmpty()) resetContracts();

        return contractsList;
    }

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

                contractsList.add(new Contract(splitDate[2] + "." + splitDate[1] + "." + splitDate[0], resultSet.getInt(NUMBER),
                        resultSet.getString(PATH), resultSet.getString(DESCRIPTION), resultSet.getByte(STATUS)));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        if (contractsList.isEmpty()) resetContracts();

        return contractsList;
    }

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