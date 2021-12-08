package platform.persistance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import platform.bussiness.Snippet;

import java.util.List;

@Repository
public interface SnippetRepository extends CrudRepository<Snippet, Integer> {
    Snippet findSnippetById(Integer id);

    Snippet findSnippetByUuid(String UUID);

    List<Snippet> findTop10ByOpenTrueOrderByIdDesc();

    void deleteSnippetById(Integer id);
}
