package aug.bueno.book.repository.repository;

import aug.bueno.book.repository.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
