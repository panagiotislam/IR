package txtparsing;

public class QueryDoc {

    private int id;
    private String body;

    public QueryDoc(int id, String body) {
        this.id = id;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        String ret = "CranDoc{"
                + "\n\tId: " + id
                + "\n\tBody: " + body;
        return ret + "\n}";
    }
}
