package sample.tables;

/**
 * Contains a contract's data.
 *
 * @author Vladislav
 * @version 2.0
 * @since 1.0
 */
public class Contract {
    private String date;
    private int number;
    private String path;
    private String contract;
    private String description;
    private byte status;
    private String textStatus;
    private String completionDate;

    /**
     * Defines a contract's data.
     *
     * @since 1.0
     * @param date a date of the contract signing.
     * @param number a number of the contract.
     * @param path a path to the contract.
     * @param description a brief description of the contract.
     * @param status a completion status of the contract.
     * @param completionDate a date of the contract completion.
     */
    public Contract(String date, int number, String path, String description, byte status, String completionDate) {
        String[] splitPath = path.split("\\\\");

        this.date = date;
        this.number = number;
        this.description = description;
        this.status = status;
        this.textStatus = (status == 0) ? "Не исполнен" : "Исполнен";
        this.path = path;
        this.contract = splitPath[splitPath.length - 1];
        this.completionDate = completionDate;
    }

    /**
     * Defines a path to a contract.
     *
     * @since 1.0
     * @param path the path to the contract.
     */
    public void setPath(String path) {
        String[] splitPath = path.split("\\\\");

        this.path = path;
        this.contract = splitPath[splitPath.length - 1];
    }

    /**
     * Defines a brief description of a contract.
     *
     * @since 1.0
     * @param description the brief description of the contract.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Defines a completion status of a contract.
     *
     * @since 1.0
     * @param status the completion status of the contract.
     */
    public void setStatus(byte status) {
        this.status = status;
        this.textStatus = (status == 0) ? "Не исполнен" : "Исполнен";
    }

    /**
     * Defines a completion date of a contract
     *
     * @since 2.0
     * @param completionDate the completion date of the contract.
     */
    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    /**
     * Returns current signing date of a contract.
     *
     * @since 1.0
     * @return the current contract's signing date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns current number of a contract.
     *
     * @since 1.0
     * @return the current contract's number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns current path to a contract.
     *
     * @since 1.0
     * @return the current contract's path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns current contract's name and extension.
     *
     * @since 1.0
     * @return the current contract's name and extension.
     */
    public String getContract() {
        return contract;
    }

    /**
     * Returns current brief description of a contract.
     *
     * @since 1.0
     * @return the current contract's briefing.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns current completion status of a contract.
     *
     * @since 1.0
     * @return the current contract's completion status.
     */
    public byte getStatus() {
        return status;
    }

    /**
     * Returns current completion status of a contract as a string.
     *
     * @since 1.0
     * @return the current string value of the contract's completion status.
     */
    public String getTextStatus() {
        return textStatus;
    }

    /**
     * Returns current completion date of a contract.
     *
     * @since 2.0
     * @return the current contract's completion date.
     */
    public String getCompletionDate() {
        return completionDate;
    }
}