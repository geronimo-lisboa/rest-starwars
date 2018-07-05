package don.geronimo.starWarsPlanet.controller;

import don.geronimo.starWarsPlanet.repository.PlanetRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/swplanets")
public class PlanetRestController {
    @Autowired
    private PlanetRepository repo;    
    @GetMapping()
    public List<Object> list() {
        return null;
    }
    
    @GetMapping("/id/{id}")
    public Object getById(@PathVariable String id) {
        final int _id = Integer.parseInt(id);
        return repo.findById(_id).get();
    }

    @GetMapping("/name/{name}")
    public Object getByName(@PathVariable String name) {
        return repo.findByName(name);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Object input) {
        return null;
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Object input) {
        return null;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return null;
    }
    /*
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleError() {
        
    }
*/
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason="planeta não encontrado")
    public PlanetNotFoundException handlePlanetNotFound(){
        PlanetNotFoundException m = new PlanetNotFoundException("Planeta não encontrado");
        return m;
    }
    
}
