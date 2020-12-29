package aug.bueno.book.repository.resolver;

import aug.bueno.book.repository.exception.BookNotFoundException;
import aug.bueno.book.repository.model.Author;
import aug.bueno.book.repository.model.Book;
import aug.bueno.book.repository.repository.AuthorRepository;
import aug.bueno.book.repository.repository.BookRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import java.util.Optional;

public class Mutation implements GraphQLMutationResolver {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public Mutation(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Author newAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        authorRepository.save(author);

        return author;
    }

    public Book newBook(String title, String isbn, Integer pageCount, Long authorId) {
        Book book = new Book();
        book.setAuthor(new Author(authorId));
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPageCount(pageCount != null ? pageCount : 0);

        bookRepository.save(book);

        return book;
    }

    public boolean deleteBook(Long id) {
        bookRepository.deleteById(id);
        return true;
    }

    public Book updateBookPageCount(Integer pageCount, Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isEmpty()) {
            throw new BookNotFoundException("The book to be updated was found", id);
        }
        Book book = bookOptional.get();
        book.setPageCount(pageCount);

        return bookRepository.save(book);

    }
}
