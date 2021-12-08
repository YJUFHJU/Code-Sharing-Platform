package platform.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import platform.bussiness.Snippet;
import platform.bussiness.SnippetService;

import javax.transaction.Transactional;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

@RestController
public class SnippetController {
    @Autowired
    SnippetService snippetService;


    @GetMapping(value = "/code/new", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView codePasteForm() {
        return new ModelAndView("paste_code");
    }

    @GetMapping(value = "/code/{uuid}", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getSnippetAsHtml(@PathVariable String uuid) {
        Snippet snippet = getSnippetAsJson(uuid).getBody();
        if (snippet == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        ModelAndView mav = new ModelAndView("show_code");
        mav.addObject("date", snippet.getDate());
        mav.addObject("views_restrict", snippet.isViewsRestrict());
        mav.addObject("views", snippet.getViews());
        mav.addObject("time_restrict", snippet.isTimeRestrict());
        mav.addObject("time", snippet.getTime());
        mav.addObject("code", snippet.getCode());

        return mav;
    }

    @GetMapping(value = "/code/latest", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getLastSnippetsAsHtml() {
        ModelAndView mav = new ModelAndView("show_codes");
        List<Snippet> lastSnippets = getLastSnippetsAsJson().getBody();
        mav.addObject("codes", lastSnippets);

        return mav;
    }

    @PostMapping("/api/code/new")
    public ResponseEntity<Map<String, String>> postCodeAsJson(@RequestBody Map<String, String> map) {
        String time = map.getOrDefault("time", "0");
        String views = map.getOrDefault("views", "0");
        Snippet snippet;

        try {
            snippet = new Snippet(map.get("code"), Long.parseLong(time), Integer.parseInt(views));
        } catch (InputMismatchException inme) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        snippetService.putSnippet(snippet);

        return ResponseEntity.ok().body(Map.of("id", snippet.getUuid()));
    }

    @Transactional
    @GetMapping("/api/code/{uuid}")
    public ResponseEntity<Snippet> getSnippetAsJson(@PathVariable String uuid) {
        Snippet snippet = snippetService.getSnippet(uuid);

        if (snippet == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().body(snippet);
    }

    @GetMapping("/api/code/latest")
    public ResponseEntity<List<Snippet>> getLastSnippetsAsJson() {
        List<Snippet> snippetList = snippetService.getLatestSnippets();

        if (snippetList == null || snippetList.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(snippetList);
    }
}
