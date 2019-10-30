package ir.amin.HaftTeen.vasni.model.grid;

public class DynamicModel {

    private int id;
    private int parentId;
    private int noOfColumn;
    private int noOfRow;
    private String parentIcon;
    private String parentBackground;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getNoOfColumn() {
        return noOfColumn;
    }

    public void setNoOfColumn(int noOfColumn) {
        this.noOfColumn = noOfColumn;
    }

    public int getNoOfRow() {
        return noOfRow;
    }

    public void setNoOfRow(int noOfRow) {
        this.noOfRow = noOfRow;
    }

    public String getParentIcon() {
        return parentIcon;
    }

    public void setParentIcon(String parentIcon) {
        this.parentIcon = parentIcon;
    }

    public String getParentBackground() {
        return parentBackground;
    }

    public void setParentBackground(String parentBackground) {
        this.parentBackground = parentBackground;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
