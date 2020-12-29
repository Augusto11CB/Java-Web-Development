package aug.bueno.book.repository.resolver;

import aug.bueno.book.repository.repository.AuthorRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import aug.bueno.book.repository.model.Author;
import aug.bueno.book.repository.model.Book;
import aug.bueno.book.repository.repository.BookRepository;

public class Query implements GraphQLQueryResolver {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public Query(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Iterable<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Iterable<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public long countBooks() {
        return bookRepository.count();
    }
    public long countAuthors() {
        return authorRepository.count();
    }
}
