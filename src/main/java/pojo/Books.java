package pojo;

public class Books {
    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getName() {
        return name;
    }

    public String getAisle() {
        return aisle;
    }

    private String author,isbn,name,aisle;
    public Books(String author, String isbn, String name, String aisle){
        this.author = author;
        this.isbn=isbn;
        this.name = name;
        this.aisle = aisle;
    }

}
