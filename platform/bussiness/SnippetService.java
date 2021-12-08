package platform.bussiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.persistance.SnippetRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class SnippetService {

    @Autowired
    SnippetRepository snippetRepository;

    public Snippet getSnippet(String UUID) {
        Snippet snippet = snippetRepository.findSnippetByUuid(UUID);

        if (snippet != null && !snippet.isOpen()) {

            if (snippet.isTimeRestrict()) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expires = snippet.getExpDate();

                if (now.isAfter(expires)) {
                    deleteSnippet(snippet.getId());
                    return null;
                }

                snippet.setTime(Duration.between(now, expires).getSeconds());
            }

            if (snippet.isViewsRestrict()) {
                snippet.viewsDecr();

                snippetRepository.save(snippet);

                if (snippet.getViews() < 0) {
                    deleteSnippet(snippet.getId());
                    return null;
                }
            }

        }

        return snippet;
    }

    public List<Snippet> getLatestSnippets() {
        return snippetRepository.findTop10ByOpenTrueOrderByIdDesc();
    }

    public void putSnippet(Snippet snippet) {
        snippetRepository.save(snippet);
    }

    public void deleteSnippet(Integer id) {
        snippetRepository.deleteSnippetById(id);
    }

    public Snippet getSnippetTest(int id) {
        return snippetRepository.findSnippetById(id);
    }
}
