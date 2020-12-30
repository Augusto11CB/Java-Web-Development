package aug.bueno.book.repository.resolver;

import aug.bueno.book.repository.model.Author;
import aug.bueno.book.repository.model.Book;
import aug.bueno.book.repository.repository.AuthorRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;

import java.util.Optional;

public class BookResolver implements GraphQLResolver<Book> {
    private AuthorRepository authorRepository;

    public BookResolver(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Optional<Author> getAuthor(Book book) {
        return authorRepository.findById(book.getAuthor().getId());
    }
}
