package don.geronimo.starWarsPlanet;

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
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import utils.ParameterStringBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StarWarsPlanetApplicationTests {

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
        assert (inDb.getClimate().equals(p.getClimate()));
        assert (inDb.getName().equals(p.getName()));
        assert (inDb.getTerrain().equals(p.getTerrain()));
        assert (inDb.getFrequency().equals(p.getFrequency()));

    }

    @Test
    public void testGetPlanet() throws MalformedURLException, IOException {
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
        String result = restTemplate.getForObject("http://localhost:8080/swplanets/{id}",  String.class, "10");
    }

    @After
    public void endTest() {
        repo.deleteAll();
    }

}
