package sample;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import sample.tables.*;

public class Controller {
    private final DBHandler DB_HANDLER = new DBHandler();
    private final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private final int MONTH = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private final int DAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private FileChooser fileChooser = new FileChooser();
    private ToggleGroup contentControl = new ToggleGroup();
    private ObservableList<String> criteriaList = FXCollections.observableArrayList("Все",
            "Дата заключения",
            "Номер",
            "Договор",
            "Краткое описание",
            "Дата исполнения");
    private ObservableList<Integer> yearsList = FXCollections.observableArrayList();
    private ObservableList<Integer> monthsList = FXCollections.observableArrayList();
    private ObservableList<Integer> daysList = FXCollections.observableArrayList();
    private ObservableList<Integer> completionMonthsList = FXCollections.observableArrayList();
    private ObservableList<Integer> completionDaysList = FXCollections.observableArrayList();
    private ObservableList<Contract> contractsList;
    private ObservableList<Contract> incompletedList;
    private ObservableList<Contract> completedList;
    private int number;
    private Contract currentContract;
    private File contract;
    private static Logger logger = Logger.getLogger(Controller.class.getName());

    static {
        try (FileInputStream cfg = new FileInputStream("cfg.config")) {
            LogManager.getLogManager().readConfiguration(cfg);
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Exception code: " + exception + ".");
            exception.printStackTrace();
        }
    }

    @FXML
    private AnchorPane dateAnchorPane;

    @FXML
    private ChoiceBox<Integer> dayChoiceBox;

    @FXML
    private ChoiceBox<Integer> monthChoiceBox;

    @FXML
    private ChoiceBox<Integer> yearChoiceBox;

    @FXML
    private CheckBox completeCheckBox;

    @FXML
    private AnchorPane completionDateAnchorPane;

    @FXML
    private ChoiceBox<Integer> completionDayChoiceBox;

    @FXML
    private ChoiceBox<Integer> completionMonthChoiceBox;

    @FXML
    private ChoiceBox<Integer> completionYearChoiceBox;

    @FXML
    private TextField numberTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button pinButton;

    @FXML
    private Label contractLabel;

    @FXML
    private Button addButton;

    @FXML
    private AnchorPane searchAnchorPane;

    @FXML
    private ChoiceBox<String> criteriaChoiceBox;

    @FXML
    private TextField filterTextField;

    @FXML
    private AnchorPane editDialogAnchorPane;

    @FXML
    private AnchorPane contentControlAnchorPane;

    @FXML
    private RadioButton allRadioButton;

    @FXML
    private RadioButton incompletedRadioButton;

    @FXML
    private RadioButton completedRadioButton;

    @FXML
    private Label counterLabel;

    @FXML
    private Button editButton;

    @FXML
    private AnchorPane deleteAnchorPane;

    @FXML
    private TextField deleteTextField;

    @FXML
    private TableView<Contract> contractsTableView;

    @FXML
    private TableColumn<Contract, String> dateTableColumn;

    @FXML
    private TableColumn<Contract, Integer> numberTableColumn;

    @FXML
    private TableColumn<Contract, String> contractTableColumn;

    @FXML
    private TableColumn<Contract, String> descriptionTableColumn;

    @FXML
    private TableColumn<Contract, String> statusTableColumn;

    @FXML
    private TableColumn<Contract, String> completionDateTableColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private Button openButton;

    @FXML
    private void initialize() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        for (int index = 2000; index <= YEAR; index++) yearsList.add(index);

        while (DB_HANDLER.isContractExist(number)) number++;

        dayChoiceBox.setItems(daysList);
        dayChoiceBox.setValue(DAY);
        monthChoiceBox.setItems(monthsList);
        monthChoiceBox.setValue(MONTH);
        yearChoiceBox.setItems(yearsList);
        yearChoiceBox.setValue(yearsList.get(yearsList.size() - 1));
        completionDayChoiceBox.setItems(completionDaysList);
        completionDayChoiceBox.setValue(DAY);
        completionMonthChoiceBox.setItems(completionMonthsList);
        completionMonthChoiceBox.setValue(MONTH);
        completionYearChoiceBox.setItems(yearsList);
        completionYearChoiceBox.setValue(yearsList.get(yearsList.size() - 1));
        criteriaChoiceBox.setItems(criteriaList);
        criteriaChoiceBox.setValue(criteriaList.get(0));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        numberTableColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        contractTableColumn.setCellValueFactory(new PropertyValueFactory<>("contract"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("textStatus"));
        completionDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("completionDate"));
        allRadioButton.setToggleGroup(contentControl);
        allRadioButton.setSelected(true);
        incompletedRadioButton.setToggleGroup(contentControl);
        completedRadioButton.setToggleGroup(contentControl);
        fileChooser.setTitle("Выберите договор");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DOC, DOCX", "*.doc", "*.docx"));

        refreshTable();

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void onClickCompleteCheckBox() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        if (completeCheckBox.isSelected() && !completeCheckBox.isDisabled()) {
            completionYearChoiceBox.setValue(yearsList.get(yearsList.size() - 1));
            completionMonthChoiceBox.setValue(completionMonthsList.get(completionMonthsList.size() - 1));
            completionDayChoiceBox.setValue(completionDaysList.get(completionDaysList.size() - 1));
            completionDateAnchorPane.setVisible(true);
        } else completionDateAnchorPane.setVisible(false);

        logger.log(Level.INFO, methodName + " is finished with : " + completionDateAnchorPane.isVisible() + ".");
    }

    @FXML
    private void onClickPinButton() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        contract = fileChooser.showOpenDialog(pinButton.getScene().getWindow());

        contractLabel.setText(getFileName());
        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void onClickAddButton() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        descriptionTextField.setText(descriptionTextField.getText().trim());

        number = (numberTextField.getText().isEmpty()) ? number : Integer.parseInt(numberTextField.getText());

        if (isInputCorrect()) {
            currentContract = new Contract(yearChoiceBox.getValue() + "-" + monthChoiceBox.getValue() + "-" + dayChoiceBox.getValue(),
                    number,
                    contract.getPath(),
                    descriptionTextField.getText(),
                    (byte) ((completeCheckBox.isSelected()) ? 1 : 0),
                    (completeCheckBox.isSelected()) ? completionYearChoiceBox.getValue() + "-" + completionMonthChoiceBox.getValue() + "-" + completionDayChoiceBox.getValue() : null);

            DB_HANDLER.addContract(currentContract);

            clearFields();
            refreshTable();

            logger.log(Level.INFO, methodName + " is running. Contract is added.");
        }

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void onClickSearchButton() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        if (filterTextField.getText().isEmpty()) {
            refreshTable();

            logger.log(Level.INFO, methodName + " is finishing. filterTextField is empty.");

            return;
        }

        FilteredList<Contract> filteredContracts;

        if (contentControl.getSelectedToggle() != allRadioButton) filteredContracts = new FilteredList<>((contentControl.getSelectedToggle() == incompletedRadioButton) ? incompletedList : completedList);
        else filteredContracts = new FilteredList<>(contractsList);

        switch (criteriaChoiceBox.getSelectionModel().getSelectedIndex()) {
            case 1:
                filteredContracts.setPredicate(item -> {
                    String filter = filterTextField.getText();

                    return item.getDate().contains(filter);
                });

                break;
            case 2:
                filteredContracts.setPredicate(item -> {
                    String filter = filterTextField.getText().replaceAll("\\D", "");

                    return String.valueOf(item.getNumber()).equals(filter);
                });

                break;
            case 3:
                filteredContracts.setPredicate(item -> {
                    String filter = filterTextField.getText().toLowerCase();

                    return item.getContract().toLowerCase().contains(filter);
                });

                break;
            case 4:
                filteredContracts.setPredicate(item -> {
                    String filter = filterTextField.getText().toLowerCase();

                    return item.getDescription().toLowerCase().contains(filter);
                });

                break;
            case 5:
                filteredContracts.setPredicate(item -> {
                    String filter = filterTextField.getText();

                    return (item.getCompletionDate() != null && item.getCompletionDate().contains(filter));
                });

                break;
            default:
                filteredContracts.setPredicate(item -> {
                    String filter = filterTextField.getText().toLowerCase();

                    if (item.getDate().contains(filter)) return true;
                    else if (String.valueOf(item.getNumber()).equals(filter.replaceAll("\\D", ""))) return true;
                    else if (item.getContract().toLowerCase().contains(filter)) return true;
                    else if (item.getDescription().toLowerCase().contains(filter)) return true;

                    return (item.getCompletionDate() != null && item.getCompletionDate().contains(filter));
                });
        }

        logger.log(Level.INFO, methodName + " is finishing. Selected criteria index: " + criteriaChoiceBox.getSelectionModel().getSelectedIndex() + ".");

        SortedList<Contract> sortedContracts = new SortedList<>(filteredContracts);

        sortedContracts.comparatorProperty().bind(contractsTableView.comparatorProperty());
        contractsTableView.setItems(sortedContracts);

        onSelectTableItem();

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void onClickEditButton() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        String[] date = currentContract.getDate().split("\\.");
        contract = new File(currentContract.getPath());

        dayChoiceBox.setValue(Integer.parseInt(date[0]));
        monthChoiceBox.setValue(Integer.parseInt(date[1]));
        yearChoiceBox.setValue(yearsList.get(Integer.parseInt(date[2]) - 2000));
        dateAnchorPane.setDisable(true);
        numberTextField.setText(String.valueOf(currentContract.getNumber()));
        numberTextField.setDisable(true);
        descriptionTextField.setText(currentContract.getDescription());
        completeCheckBox.setSelected(currentContract.getStatus() == 1);
        completeCheckBox.setDisable(completeCheckBox.isSelected());
        contractLabel.setText(getFileName());
        searchAnchorPane.setDisable(true);
        addButton.setVisible(false);
        editButton.setDisable(true);
        deleteAnchorPane.setDisable(true);
        refreshButton.setDisable(true);
        openButton.setDisable(true);
        editDialogAnchorPane.setVisible(true);
        contentControlAnchorPane.setDisable(true);
        contractsTableView.setDisable(true);
        errorLabel.setVisible(false);

        onClickCompleteCheckBox();
    }

    @FXML
    private void onClickConfirmButton() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        if (isInputCorrect()) {
            if (completeCheckBox.isSelected() && !completeCheckBox.isDisabled()) {
                currentContract.setCompletionDate(completionYearChoiceBox.getValue() + "-" +
                        completionMonthChoiceBox.getValue() + "-" + completionDayChoiceBox.getValue());
                logger.log(Level.INFO, methodName + " is running. Completion date is set.");
            }

            currentContract.setStatus((byte) ((completeCheckBox.isSelected()) ? 1 : 0));
            currentContract.setDescription(descriptionTextField.getText());
            currentContract.setPath(contract.getPath());

            DB_HANDLER.updateContract(currentContract);

            clearFields();
            restoreElements();
            refreshTable();

            logger.log(Level.INFO, methodName + " is finished.");
        }
    }

    @FXML
    private void onClickDiscardButton() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        clearFields();
        restoreElements();

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void onClickDeleteButton() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        if (deleteTextField.getText().isEmpty()) {
            errorLabel.setText("Введите номер выделенного договора для подтверждения!");
            errorLabel.setVisible(true);
            logger.log(Level.INFO, methodName + " is running. deleteTextField is empty.");
        } else if (Integer.parseInt(deleteTextField.getText()) == currentContract.getNumber()) {
            DB_HANDLER.deleteContract(currentContract.getNumber());

            clearFields();
            refreshTable();

            logger.log(Level.INFO, methodName + " is running. Contract is deleted.");
        } else {
            errorLabel.setText("Введенный номер не совпадает с номером выделенного договора!");
            errorLabel.setVisible(true);
            logger.log(Level.INFO, methodName + " is running. Numbers conflict.");
        }

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void onSelectContentRadioButton() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        if (contentControl.getSelectedToggle() == allRadioButton) {
            counterLabel.setText(String.valueOf(contractsList.size()));
            contractsTableView.setItems(contractsList);

            if (criteriaList.size() < 6) criteriaList.add("Дата исполнения");

            logger.log(Level.INFO, methodName + " is running. All contracts shown.");
        } else if (contentControl.getSelectedToggle() == incompletedRadioButton) {
            counterLabel.setText(String.valueOf(incompletedList.size()));
            contractsTableView.setItems(incompletedList);

            if (criteriaList.size() == 6) {
                criteriaChoiceBox.setValue((criteriaChoiceBox.getSelectionModel().getSelectedIndex() == 5 ? criteriaList.get(4) : criteriaChoiceBox.getValue()));
                criteriaList.remove(5);
            }

            logger.log(Level.INFO, methodName + " is running. Incompleted contracts shown.");
        } else {
            counterLabel.setText(String.valueOf(completedList.size()));
            contractsTableView.setItems(completedList);

            if (criteriaList.size() < 6) criteriaList.add("Дата исполнения");

            logger.log(Level.INFO, methodName + " is running. Completed contracts shown.");
        }

        onSelectTableItem();

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void onSelectTableItem() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        if ((currentContract = contractsTableView.getSelectionModel().getSelectedItem()) != null) {
            editButton.setVisible(true);
            deleteAnchorPane.setVisible(true);
            openButton.setVisible(true);
        } else resetElements();

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void refreshTable() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        contractsList = DB_HANDLER.getAll();
        incompletedList = DB_HANDLER.getIncompleted();
        completedList = DB_HANDLER.getCompleted();
        number = 1;

        descriptionTextField.setText(descriptionTextField.getText().trim());
        errorLabel.setVisible(false);
        filterTextField.clear();

        while (DB_HANDLER.isContractExist(number)) number++;

        resetElements();
        onSelectContentRadioButton();

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void onClickOpenButton() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        try {
            Desktop.getDesktop().open(new File(currentContract.getPath()));
        } catch (IOException exception) {
            errorLabel.setText("Не удалось открыть файл!");
            errorLabel.setVisible(true);
            logger.log(Level.WARNING, methodName + " is running. File not found.");
        }

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void fillMonths(ActionEvent event) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        ChoiceBox<Integer> currentMonthChoiceBox, currentYearChoiceBox;
        ObservableList<Integer> currentMonthsList;
        int choseMonth;

        if (event.getSource() == completionYearChoiceBox) {
            currentMonthChoiceBox = completionMonthChoiceBox;
            currentYearChoiceBox = completionYearChoiceBox;
            currentMonthsList = completionMonthsList;

            logger.log(Level.INFO, methodName + " is running. Completion date is changed.");
        } else {
            currentMonthChoiceBox = monthChoiceBox;
            currentYearChoiceBox = yearChoiceBox;
            currentMonthsList = monthsList;

            logger.log(Level.INFO, methodName + " is running. Date is changed.");
        }

        choseMonth = currentMonthChoiceBox.getValue();

        if (currentYearChoiceBox.getValue() == YEAR && (currentMonthsList.size() > MONTH || currentMonthsList.isEmpty())) {
            if (choseMonth > MONTH) currentMonthChoiceBox.setValue(currentMonthsList.get(MONTH - 1));

            if (currentMonthsList.isEmpty()) for (int index = 1; index <= MONTH; index++) currentMonthsList.add(index);
            else currentMonthsList.remove(MONTH, currentMonthsList.size());
        } else if (currentMonthsList.size() < 12) for (int index = currentMonthsList.size() + 1; index <= 12; index++) currentMonthsList.add(index);

        fillDays(event);

        logger.log(Level.INFO, methodName + " is running. Months list size: " + currentMonthsList.size());
    }

    @FXML
    private void fillDays(ActionEvent event) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        ChoiceBox<Integer> currentDayChoiceBox, currentMonthChoiceBox, currentYearChoiceBox;
        ObservableList<Integer> currentDaysList;
        int choseDay;

        if (event.getSource() == completionMonthChoiceBox || event.getSource() == completionYearChoiceBox) {
            currentDayChoiceBox = completionDayChoiceBox;
            currentMonthChoiceBox = completionMonthChoiceBox;
            currentYearChoiceBox = completionYearChoiceBox;
            currentDaysList = completionDaysList;

            logger.log(Level.INFO, methodName + " is running. Completion date is changed.");
        } else {
            currentDayChoiceBox = dayChoiceBox;
            currentMonthChoiceBox = monthChoiceBox;
            currentYearChoiceBox = yearChoiceBox;
            currentDaysList = daysList;

            logger.log(Level.INFO, methodName + " is running. Date is changed.");
        }

        choseDay = currentDayChoiceBox.getValue();

        currentDaysList.clear();

        if (currentMonthChoiceBox.getValue() == MONTH && !currentYearChoiceBox.getItems().isEmpty() && currentYearChoiceBox.getValue() == YEAR)
            for (int index = 1; index <= DAY; index++) currentDaysList.add(index);
        else {
            switch (currentMonthChoiceBox.getValue()) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    for (int index = 1; index <= 31; index++) currentDaysList.add(index);

                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    for (int index = 1; index <= 30; index++) currentDaysList.add(index);

                    break;
                default:
                    if (!currentYearChoiceBox.getItems().isEmpty() && currentYearChoiceBox.getValue() % 4 == 0) for (int index = 1; index <= 29; index++) currentDaysList.add(index);
                    else for (int index = 1; index <= 28; index++) currentDaysList.add(index);
            }
        }

        if (choseDay > currentDaysList.size()) currentDayChoiceBox.setValue(currentDaysList.get(currentDaysList.size() - 1));
        else currentDayChoiceBox.setValue(currentDaysList.get(choseDay - 1));

        logger.log(Level.INFO, methodName + " is finished. Days list size: " + currentDaysList.size());
    }

    @FXML
    private void cutOffLimitNumber(KeyEvent event) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        TextField textField = (TextField) event.getTarget();
        String oldNumber = textField.getText();
        String newNumber = oldNumber.replaceAll("\\D", "");

        if (!oldNumber.equals(newNumber)) {
            textField.setText(newNumber);
            textField.positionCaret(textField.getLength());
            logger.log(Level.INFO, methodName + " is running. New number is detected.");
        }

        if (textField.getLength() > 9) {
            textField.setText(textField.getText(0, 9));
            textField.positionCaret(9);
            logger.log(Level.INFO, methodName + " is running." + textField.getId() + " is trimmed.");
        }

        logger.log(Level.INFO, methodName + " is finished.");
    }

    @FXML
    private void cutOffLimitDescription() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        String trimmedString = descriptionTextField.getText().trim();

        if (trimmedString.length() > 32) {
            descriptionTextField.setText(trimmedString.substring(0, 32));
            descriptionTextField.positionCaret(32);
            logger.log(Level.INFO, methodName + " is running. descriptionTextField is trimmed.");
        }

        logger.log(Level.INFO, methodName + " is finished.");
    }

    private String getFileName() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        String[] fullPath = (contract != null && contract.exists()) ? contract.getPath().split("\\\\") : new String[] {"Договор не прикреплен!"};
        String fileName = fullPath[fullPath.length - 1];

        logger.log(Level.INFO, methodName + " is finishing with: " + fileName + ".");

        return (fileName.length() > 50) ? fileName.substring(0, 47) + "..." : fileName;
    }

    private void clearFields() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");

        contract = null;

        yearChoiceBox.setValue(yearsList.get(yearsList.size() - 1));
        monthChoiceBox.setValue(monthsList.get(monthsList.size() - 1));
        dayChoiceBox.setValue(daysList.get(daysList.size() - 1));
        numberTextField.clear();
        completeCheckBox.setSelected(false);
        descriptionTextField.clear();
        filterTextField.clear();
        deleteTextField.clear();
        contractLabel.setText("Договор не прикреплен!");

        onClickCompleteCheckBox();

        logger.log(Level.INFO, methodName + " is finished.");
    }

    private void restoreElements() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");
        dateAnchorPane.setDisable(false);
        numberTextField.setDisable(false);
        completeCheckBox.setDisable(false);
        searchAnchorPane.setDisable(false);
        editDialogAnchorPane.setVisible(false);
        addButton.setVisible(true);
        editButton.setDisable(false);
        deleteAnchorPane.setDisable(false);
        refreshButton.setDisable(false);
        openButton.setDisable(false);
        contentControlAnchorPane.setDisable(false);
        contractsTableView.setDisable(false);
        errorLabel.setVisible(false);

        onSelectTableItem();

        logger.log(Level.INFO, methodName + " is finished.");
    }

    private void resetElements() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");
        editButton.setVisible(false);
        deleteAnchorPane.setVisible(false);
        openButton.setVisible(false);
        logger.log(Level.INFO, methodName + " is finished.");
    }

    private boolean isInputCorrect() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        logger.log(Level.INFO, methodName + " is running.");
        descriptionTextField.setText(descriptionTextField.getText().trim());

        if (!numberTextField.isDisable() && DB_HANDLER.isContractExist(number)) {
            errorLabel.setText("Договор с указанным номером уже существует!");
            errorLabel.setVisible(true);
            logger.log(Level.INFO, methodName + " is finishing. Contract exist.");

            return false;
        } else if (completeCheckBox.isSelected()) {
            int date = Integer.parseInt(yearChoiceBox.getValue() +
                    ((monthChoiceBox.getValue() < 10) ? "0" : "") +monthChoiceBox.getValue() +
                    ((dayChoiceBox.getValue() < 10) ? "0" : "") + dayChoiceBox.getValue());
            int completionDate = Integer.parseInt(completionYearChoiceBox.getValue() +
                    ((completionMonthChoiceBox.getValue() < 10) ? "0" : "") + completionMonthChoiceBox.getValue() +
                    ((completionDayChoiceBox.getValue() < 10) ? "0" : "") + completionDayChoiceBox.getValue());

            if (completionDate < date) {
                errorLabel.setText("Дата исполнения меньше даты заключения!");
                errorLabel.setVisible(true);
                logger.log(Level.INFO, methodName + " is finishing. Date conflicts.");

                return false;
            }
        }

        if (contract == null || !contract.exists()) {
            contractLabel.setText("Договор не прикреплен!");
            errorLabel.setText("Необходимо прикрепить договор!");
            errorLabel.setVisible(true);
            logger.log(Level.INFO, methodName + " is finishing. Contract not found.");

            return false;
        }

        logger.log(Level.INFO, methodName + " is finishing.");

        return true;
    }
}