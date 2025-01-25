package org.example.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Model.Author;
import org.example.service.DBService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "author", value = "/author")
public class AuthorServlet extends HttpServlet {

    DBService dbService = DBService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Author> authors = dbService.getAuthors();
            String s = makeTable(authors);
            resp.getWriter().println(s);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Author author = new Author();
        author.setName(req.getParameter("name"));
        try {
            dbService.addAuthor(author);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String makeTable(List<Author> authors) {
        StringBuilder sb = new StringBuilder();
        for (Author author : authors) {
            sb.append("<tr>").append("<td>")
                    .append(author.getId())
                    .append("</td>").append("<td>")
                    .append(author.getName())
                    .append("</td>").append("</tr>");
        }
        return html.formatted(sb.toString());
    }

    String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Authors</title>
            </head>
            <body>
            <h3>Enter new author</h3>
            
            
            <form>
                <label for="name">Author name:</label>
                <input type="text" id="name" name="name">
                <button formmethod="post" type="submit">submit</button>
            </form>
            
            
            <table>
                <thead>
                <tr>
                    <th>id</th>
                    <th>name</th>
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
