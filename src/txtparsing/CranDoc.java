package txtparsing;

/**
 *
 * @author Tonia Kyriakopoulou
 */
public class CranDoc {

    private int id;
    private String title;
    private String author;
    private String b;
    private String body;

    public CranDoc(int id, String title, String author, String b, String body) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.b = b;
        this.body = body;
    }

    @Override
    public String toString() {
        String ret = "CranDoc{"
                + "\n\tId: " + id
                + "\n\tTitle: " + title
                + "\n\tAuthor: " + author
                + "\n\tB: " + b
                + "\n\tBody: " + body;
        return ret + "\n}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
