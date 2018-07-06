package don.geronimo.starWarsPlanet.controller;

import don.geronimo.starWarsPlanet.model.Planet;
import don.geronimo.starWarsPlanet.repository.PlanetApparitionsSource;
import don.geronimo.starWarsPlanet.repository.PlanetRepository;
import java.io.IOException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    @GetMapping("/planet/all/")
    /** Retorna uma lista com todos os planetas.**/
    public List<Planet> getAll() {
        return repo.findAll();
    }
    
    
    @GetMapping("/planet/id/{id}")
    /** Pega o planeta pelo ID. Esse id não é o id do planeta no serviço de star wars, embora possa ser identico**/
    public Planet getById(@PathVariable String id) {
        final int _id = Integer.parseInt(id);
        return repo.findById(id).get();
    }

    @GetMapping("/planet/name/{name}")
    /**Pega pelo nome. O nome tem que coincidir exatamente, é case sensitive também.**/
    public Planet getByName(@PathVariable String name) {
        return repo.findByName(name).get();
    }
    
    private void setPlanetFrequency(Planet p) throws IOException{
        PlanetApparitionsSource apparitionsSource = new PlanetApparitionsSource(p.getName());
        Optional<Integer> amount = apparitionsSource.getNumberOfApparition();
        p.setFrequency(amount.orElse(0));       
    }
    
    @PutMapping("/planet/")
    /**Insere um planeta novo. Se o planeta já existir responde com erro. É nesse momento que eu consulto as
     informaçoes remotas sobre o numero de aparições nos filmes e guardo no banco. A verificação do planeta já
     existir é feita pelo nome que nem em /planet/name/{name}**/
    public ResponseEntity<Planet> put(@RequestBody Planet input) {
        Planet newPlanet = (Planet)input;
        try{
        //Se já tem um planeta com esse nome, se recusa a por, falhando com erro
        if(repo.findByName(newPlanet.getName()).isPresent()){
            ResponseEntity resp = new ResponseEntity(null, HttpStatus.NOT_ACCEPTABLE);
            return resp;
        }else{
            //Se não, pega a qtd de aparições  https://swapi.co/api/people/?search=r2
            setPlanetFrequency(newPlanet);
            //Guarda
            Planet resultingPlanet = repo.insert(newPlanet);
            //Retorna
            ResponseEntity resp = new ResponseEntity(resultingPlanet, HttpStatus.OK);
            return resp;
        }
        }
        catch(IOException ex){
            ResponseEntity resp = new ResponseEntity("erro interno", HttpStatus.INTERNAL_SERVER_ERROR);
            return resp;   
        }
    }
    
    @PostMapping("/planet/")
    /**Insere ou atualiza o planeta dado. Também consulta as informações remotas sobre o numero de aparições
     nos filmes**/
    public ResponseEntity<?> post(@RequestBody Planet input) throws IOException {
        try{
        setPlanetFrequency(input);
        Planet saved = repo.save(input);
        ResponseEntity resp = new ResponseEntity(saved, HttpStatus.OK);
        return resp;
        }
        catch(Exception ex){
            ex.printStackTrace();
            ResponseEntity resp = new ResponseEntity("erro", HttpStatus.INTERNAL_SERVER_ERROR);
            return resp;
        }
    }
    
    @DeleteMapping("/planet/id/{id}")
    /**Deleta o planeta pelo ID**/
    public ResponseEntity<?> deletePlanetById(@PathVariable String id) {
        try
        {
            Optional<Planet> p = repo.findById(id);   
            repo.delete(p.get());
            ResponseEntity resp = new ResponseEntity("deletado", HttpStatus.OK);
            return resp;
        }
        catch(NoSuchElementException ex)
        {
            ResponseEntity resp = new ResponseEntity("planeta não encontrado", HttpStatus.NOT_FOUND);
            return resp;
        }
    }
    
    @DeleteMapping("/planet/name/{name}")
    /**Deleta o planeta pelo ID**/
    public ResponseEntity<?> deletePlanetByName(@PathVariable String name) {
        try
        {
            Optional<Planet> p = repo.findByName(name);   
            repo.delete(p.get());
            ResponseEntity resp = new ResponseEntity("deletado", HttpStatus.OK);
            return resp;
        }
        catch(NoSuchElementException ex)
        {
            ResponseEntity resp = new ResponseEntity("planeta não encontrado", HttpStatus.NOT_FOUND);
            return resp;
        }
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
