package trvoid.modelmapper;

public class DstMessage {
    private String name;
    private int type;
    private String summary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String toString() {
        return String.format("DstMessage[name:%s, type:%d, summary:%s]", getName(), getType(), getSummary());
    }
}
