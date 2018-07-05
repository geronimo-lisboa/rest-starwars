package don.geronimo.starWarsPlanet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import don.geronimo.starWarsPlanet.model.Planet;
import don.geronimo.starWarsPlanet.repository.PlanetRepository;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.internal.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import utils.ParameterStringBuilder;

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
    public void testInsertInDb() {
        Planet p = new Planet();
        p.setClimate("foo");
        p.setId(1);
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
        newPlanet.setId(10);
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
        newPlanet.setId(10);
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
    public void testGetAll() throws MalformedURLException, IOException {
        repo.deleteAll();
        Planet a = new Planet(50,"trantor","cool","urban");
        Planet b = new Planet(100,"pandora","moist","jungle");
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
