package sample;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.TestMethodOrder;
import sample.tables.*;

@TestMethodOrder(OrderAnnotation.class)
class Tests {
    private DBHandler db = new DBHandler();
    private Contract testContract;
    private Contract testingContract;
    private ObservableList contracts;

    @Test
    @Order(1)
    void addContractTest() {
        testContract = new Contract("2000-01-01", 15, "test.docx", "", (byte) 0);

        db.addContract(testContract);

        contracts = db.getAll();
        testingContract = (Contract) contracts.get(contracts.size() - 1);

        assertEquals("01.01.2000", testingContract.getDate());
        assertEquals(testContract.getNumber(), testingContract.getNumber());
        assertEquals(testContract.getPath(), testingContract.getPath());
        assertEquals(testContract.getDescription(), testingContract.getDescription());
        assertEquals(testContract.getStatus(), testingContract.getStatus());
    }

    @Test
    @Order(2)
    void updateContractTest() {
        contracts = db.getAll();
        testContract = (Contract) contracts.get(contracts.size() - 1);

        testContract.setPath("newTest.docx");
        testContract.setDescription("Description");
        testContract.setStatus((byte) 1);
        db.updateContract(testContract);

        contracts = db.getAll();
        testingContract = (Contract) contracts.get(contracts.size() - 1);

        assertEquals(testContract.getPath(), testingContract.getPath());
        assertEquals(testContract.getDescription(), testingContract.getDescription());
        assertEquals(testContract.getStatus(), testingContract.getStatus());
    }

    @Test
    @Order(3)
    void deleteContractTest() {
        db.deleteContract(15);

        contracts = db.getAll();

        assertTrue(contracts.isEmpty());
    }

    @Test
    @Order(4)
    void getMethodsTest() {
        db.addContract(new Contract("2000-01-01", 1, "test.docx", "", (byte) 0));
        db.addContract(new Contract("2000-01-01", 2, "test.docx", "", (byte) 0));
        db.addContract(new Contract("2000-01-01", 3, "test.docx", "", (byte) 1));

        assertEquals(3, db.getAll().size());
        assertEquals(2, db.getIncompleted().size());
        assertEquals(1, db.getCompleted().size());
    }

    @Test
    @Order(5)
    void isContractExistTest() {
        assertTrue(db.isContractExist(2));
        assertFalse(db.isContractExist(5));
    }
}