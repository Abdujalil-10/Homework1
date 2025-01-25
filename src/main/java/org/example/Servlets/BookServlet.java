package org.example.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.example.Model.Author;
import org.example.Model.Book;
import org.example.service.DBService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "book", value = "/book")
public class BookServlet extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBService dbService = DBService.getInstance();
        List<Author> authors = dbService.getAuthors();
        String s = makeHtml(authors);
        resp.getWriter().println(s);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBService dbService = DBService.getInstance();
        String bookName = req.getParameter("book");
        String Author = req.getParameter("authors");
        String genre = req.getParameter("genre");

        Book book = new Book();
        book.setName(bookName);
        book.setAuthor_id(Integer.parseInt(Author));
        book.setGenre(genre);
        dbService.addBook(book);

        List<Author> authors = dbService.getAuthors();
        List<Book> books = dbService.getBooks();
        String s = makeHtml(authors, books);
        resp.getWriter().println(s);
    }

    String makeHtml(List<Author> authors, List<Book> books) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder html = new StringBuilder();
        for (Book book : books) {
            String option = "<option value=\"%s\"> %s</option>".formatted(
                    book.getAuthor_id(), book.getAuthor().getName()
            );
            html.append(option);
            stringBuilder.append("<tr>").append("<td>").
                    append(book.getAuthor().getName()).append("</td>").append("<td>").
                    append(book.getName()).append("</td>").append("<td>").
                    append(book.getGenre()).
                    append("</td>").
                    append("</tr>").
                    append("\n");
        }
        return bigHtml.formatted(html.toString(), stringBuilder.toString());
    }

    String makeHtml(List<Author> authors) {

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder html = new StringBuilder();
        for (Author author : authors) {
            String option = "<option value=\"%s\"> %s</option>".formatted(
                    author.getId(), author.getName()
            );
            html.append(option);

            stringBuilder.append("<tr>").append("<td>").append(author.getName()).append("</td>").append("</tr>");
        }
        return bigHtml.formatted(html.toString(), stringBuilder.toString());
    }

    String bigHtml = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Book</title>
            </head>
            <body>
            <form method="post">
                <label for="book">Authorni tanlang?</label>
                <select id="book" name="authors">
                    %s
                </select>
            <label for="book1">book</label>
                <input id="book1" name="book">
            
                <label for="genre"> genre</label>
                <input id="genre" name="genre">
                <button type="submit"> submit</button>
            </form>
            
            <table border="1">
                <thead>
                <tr>
                    <th>Author</th>
                    <th>Book</th>
                    <th>Genre</th>
                </tr>
                </thead>
                <tbody>
            %s
                </tbody>
            </table>
            
            </body>
            </html>
            """;
}
