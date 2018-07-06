package don.geronimo.starWarsPlanet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import don.geronimo.starWarsPlanet.model.Planet;
import don.geronimo.starWarsPlanet.repository.PlanetRepository;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StarWarsPlanetApplicationTests {
    private void assertPlanets(Planet a, Planet b){
        assert (a.getClimate().equals(b.getClimate()));
        assert (a.getName().equals(b.getName()));
        assert (a.getTerrain().equals(b.getTerrain()));
        assert (a.getFrequency().equals(b.getFrequency()));
 
    }
    @Autowired
    private PlanetRepository repo;

    @Before
    public void beginTest() {
        repo.deleteAll();
    }
    
    @Test
    public void testNoId() throws InterruptedException
    {
        repo.deleteAll();
        Planet np1 = new Planet();
        np1.setClimate("cold");
        np1.setTerrain("mountainous");
        np1.setName("Hoth");
        String url = "http://localhost:8080/swplanets/planet/";
        RestTemplate restTemplate = new RestTemplate();
        Planet returnedPlanet1 = restTemplate.postForObject(url, np1, Planet.class);  
        Thread.sleep(100);

        Planet np2 = new Planet();
        np2.setClimate("temperate");
        np2.setTerrain("urban");
        np2.setName("Coruscant");
        Planet returnedPlanet2 = restTemplate.postForObject(url, np2, Planet.class);  
        Thread.sleep(100);
        
        
        assert(returnedPlanet1.getId()!=null);
        assert(returnedPlanet2.getId()!=null);
        assert(! returnedPlanet1.getId().equals(returnedPlanet2.getId()));
        
    }
    
    @Test
    public void testUpdate() throws InterruptedException{
        repo.deleteAll();
        Planet newPlanet = new Planet("50","Charlie", "cold", "rocky");
        String url = "http://localhost:8080/swplanets/planet/";
        RestTemplate restTemplate = new RestTemplate();
        Planet returnedPlanet = restTemplate.postForObject(url, newPlanet, Planet.class);  // postForLocation(url, newPlanet); ///(url, newPlanet);
        Thread.sleep(100);
        returnedPlanet.setTerrain("water");       
        Planet updatedPlanet = restTemplate.postForObject(url, returnedPlanet, Planet.class);  // postForLocation(url, newPlanet); ///(url, newPlanet);
        Planet dbPlanet = repo.findById("50").get();
        assert(dbPlanet.getTerrain().equals(returnedPlanet.getTerrain()));
        
    }
    
    @Test
    public void testDelete(){
        repo.deleteAll();
        Planet planetToDeleteByName = new Planet("50", "Charlie", "Papa", "Hotel");
        Planet planetToDeleteById = new Planet("100", "Easy", "Fox", "Tango");
        repo.insert(planetToDeleteById);
        repo.insert(planetToDeleteByName);
        
        RestTemplate restTemplate = new RestTemplate();
        String urlId = "http://localhost:8080/swplanets/planet/id/{id}";
        restTemplate.delete(urlId, "100");
        
        String urlName = "http://localhost:8080/swplanets/planet/name/{name}";
        restTemplate.delete(urlName, "Charlie");
        
        List<Planet> planets = repo.findAll();
        assert(planets.size()==0);
    }
    
    @Test
    public void testInsertInDb() {
        Planet p = new Planet();
        p.setClimate("foo");
        p.setId("1");
        p.setName("bar");
        p.setTerrain("quid");
        p.setFrequency(1);
        repo.insert(p);
        Planet inDb = repo.findByName("bar").get();
        assertPlanets(p, inDb);
    }

    @Test
    public void testGetPlanetById() throws MalformedURLException, IOException {
        //Bota no banco
        repo.deleteAll();
        Planet newPlanet = new Planet();
        newPlanet.setClimate("able");
        newPlanet.setId("10");
        newPlanet.setName("baker");
        newPlanet.setTerrain("charlie");
        newPlanet.setFrequency(2);
        repo.insert(newPlanet);
        //Faz um request pra pegar o dado 
        RestTemplate restTemplate = new RestTemplate();
        Planet returningPlanet = restTemplate.getForEntity("http://localhost:8080/swplanets/planet/id/{id}", Planet.class, "10").getBody();
        assertPlanets(newPlanet, returningPlanet);
    }

    @Test
    public void testGetPlanetByName() throws MalformedURLException, IOException {
        //Bota no banco
        repo.deleteAll();
        Planet newPlanet = new Planet();
        newPlanet.setClimate("able");
        newPlanet.setId("10");
        newPlanet.setName("fox");
        newPlanet.setTerrain("easy");
        newPlanet.setFrequency(1);
        repo.insert(newPlanet);
        //Faz um request pra pegar o dado 
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Planet> re = restTemplate.getForEntity("http://localhost:8080/swplanets/planet/name/{id}", Planet.class, "fox");
        Planet returningPlanet = re.getBody();
        assertPlanets(newPlanet, returningPlanet);
        
    }
    
    @Test
    public void testPutNaboo()throws Exception{
        repo.deleteAll();
        Planet planetToPost = new Planet("1000", "Naboo", "clima", "terreno");
        String url = "http://localhost:8080/swplanets/planet/";
        //-----------
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, planetToPost);
        
        Thread.sleep(100);
        
        Planet found = repo.findByName("Naboo").get();
        assert(found.getName().equals(planetToPost.getName()));
        assert(found.getFrequency().equals(4));
    }
    //A diferença entre Klendathu e Naboo é que Klendathu é do Tropas Estelares 
    //e portanto tem zero aparições nos filmes.
    @Test
    public void testPutKlendathu()throws Exception{
        repo.deleteAll();
        Planet planetToPost = new Planet("200", "Klendathu", "ugly planet", "BUG PLANET");
        String url = "http://localhost:8080/swplanets/planet/";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, planetToPost);    
        
        Thread.sleep(100);
        
        Planet found = repo.findByName("Klendathu").get();
        assert(found.getName().equals(planetToPost.getName()));
        assert(found.getFrequency().equals(0));
        repo.deleteAll();
    }
    

    @Test
    public void testGetAll() throws MalformedURLException, IOException {
        repo.deleteAll();
        Planet a = new Planet("50","trantor","cool","urban");
        Planet b = new Planet("100","pandora","moist","jungle");
        repo.insert(a);
        repo.insert(b);
        RestTemplate restTemplate = new RestTemplate();
        String jsonList = restTemplate.getForObject("http://localhost:8080/swplanets/planet/all/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Planet.class);
        List<Planet> planets = mapper.readValue(jsonList, javaType);
        boolean aOk=false;
        boolean bOk=false;
        
        for(Object _p : planets){
            Planet currentPlanet = (Planet)_p;
            if(currentPlanet.getName().equals(a.getName()))
                aOk = true;
            if(currentPlanet.getName().equals(b.getName()))
                bOk = true;
        }
        if(!aOk || !bOk)
            throw new RuntimeException("return list is wrong");
    }
    
    
    @After
    public void endTest() {
        repo.deleteAll();
    }

}
