package sample.tables;

public class Contract {
    private String date;
    private int number;
    private String path;
    private String contract;
    private String description;
    private byte status;
    private String textStatus;

    public Contract(String date, int number, String path, String description, byte status) {
        String[] splitPath = path.split("\\\\");

        this.date = date;
        this.number = number;
        this.description = description;
        this.status = status;
        this.textStatus = (status == 0) ? "Не исполнен" : "Исполнен";
        this.path = path;
        this.contract = splitPath[splitPath.length - 1];
    }

    public void setPath(String path) {
        String[] splitPath = path.split("\\\\");

        this.path = path;
        this.contract = splitPath[splitPath.length - 1];
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(byte status) {
        this.status = status;
        this.textStatus = (status == 0) ? "Не исполнен" : "Исполнен";
    }

    public String getDate() {
        return date;
    }

    public int getNumber() {
        return number;
    }

    public String getPath() {
        return path;
    }

    public String getContract() {
        return contract;
    }

    public String getDescription() {
        return description;
    }

    public byte getStatus() {
        return status;
    }

    public String getTextStatus() {
        return textStatus;
    }
}