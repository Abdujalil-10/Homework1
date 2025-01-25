package org.example.service;

import org.example.Model.Author;
import org.example.Model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBService {
    private String host = "jdbc:postgresql://localhost:5432/";
    private String user = "postgres";
    private String password = "Abdujalil2005";
    private String dbName = "homework1";

    private static DBService instance;

    private DBService() {
    }

    public static DBService getInstance() {
        if (instance == null) {
            instance = new DBService();
        }
        return instance;
    }


    public List<Book> getBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = """
                    
                SELECT
                        b.id as "b.id",
                        b.name as "b.name",
                        b.genre as "b.genre",
                        a.id as "a.id",
                        a.name as "a.name"
                    FROM book b
                             join author a on b.author_id = a.id
                    """;
        Connection conn = getConnection();
        conn.prepareStatement(query);
        ResultSet resultSet = conn.createStatement().executeQuery(query);
        while (resultSet.next()) {
           Book book = new Book();
           book.setId(resultSet.getInt("b.id"));
           book.setName(resultSet.getString("b.name"));
           book.setGenre(resultSet.getString("b.genre"));
           Author author = new Author();
           author.setId(resultSet.getInt("a.id"));
           author.setName(resultSet.getString("a.name"));
           book.setAuthor(author);
           books.add(book);
        }
        return books;
    }

    public List<Author> getAuthors() throws SQLException {
        List<Author> authors = new ArrayList<>();
        Connection conn = getConnection();
        String sql = "select * from author";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setName(rs.getString("name"));
            authors.add(author);
        }
        return authors;
    }

    public Book addBook(Book book) throws SQLException {
        Connection conn = getConnection();
        String query = "insert into book(name,genre,author_id) values(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, book.getName());
        ps.setString(2, book.getGenre());
        ps.setInt(3, book.getAuthor_id());
        ps.executeUpdate();
        conn.close();
        return book;
    }

    public void addAuthor(Author author) throws SQLException {
        Connection conn = getConnection();
        String query = "insert into author(name) values(?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, author.getName());
        ps.executeUpdate();
        conn.close();
    }


    public Connection getConnection() {
        String url = host + dbName;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
