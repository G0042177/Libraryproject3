package ie.atu.standard;


public class Book {
    private int bookId;
    private String title;
    private String author;
    private String type;
    private int quantity;

    public Book(int bookId, String title, String author, String type, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.type = type;
        this.quantity = quantity;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        String stock = (quantity <= 0) ? "OUT OF STOCK" : (quantity + " copies");
        return bookId + " | " + title + " | " + author + " | " + type + " | " + stock;
    }
}

