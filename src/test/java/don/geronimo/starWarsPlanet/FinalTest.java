/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package don.geronimo.starWarsPlanet;

import don.geronimo.starWarsPlanet.model.Planet;
import don.geronimo.starWarsPlanet.repository.PlanetRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FinalTest {
    @Autowired
    private PlanetRepository repo;
    
    public FinalTest() {
    }
    
    @Before
    public void setUp() {
        repo.deleteAll();
    }
    
    @After
    public void tearDown() {
        repo.deleteAll();
    }
    
    @Test
    //Espera-se que coruscant retorne com um id e com suas aparicoes
    public void putCoruscant() throws InterruptedException{
        tearDown();
        Planet coruscant = new Planet();
        coruscant.setName("Coruscant");
        coruscant.setTerrain("urban");
        coruscant.setClimate("temperate");
        String url = "http://localhost:8080/swplanets/planet/";
        RestTemplate restTemplate = new RestTemplate();
        Planet returned = restTemplate.postForObject(url, coruscant, Planet.class);  
        Thread.sleep(100);
        assert(returned.getId()!=null);
        assert(returned.getFrequency().equals(4));
        tearDown();
    }
    //Klendathu por sua vez não tem aparições
    @Test
    public void putKlendathu() throws InterruptedException{
        tearDown();
        Planet coruscant = new Planet();
        coruscant.setName("Klendathu");
        coruscant.setTerrain("rocky");
        coruscant.setClimate("temperate");
        String url = "http://localhost:8080/swplanets/planet/";
        RestTemplate restTemplate = new RestTemplate();
        Planet returned = restTemplate.postForObject(url, coruscant, Planet.class);  
        Thread.sleep(100);
        assert(returned.getId()!=null);
        assert(returned.getFrequency().equals(0));
        tearDown();       
    }
    
    @Test
    public void getAPlanet() throws InterruptedException{
        tearDown();
        Planet klendathu = new Planet();
        klendathu.setName("Klendathu");
        klendathu.setTerrain("rocky");
        klendathu.setClimate("temperate");
        String url = "http://localhost:8080/swplanets/planet/";
        RestTemplate restTemplate = new RestTemplate();
        Planet returned = restTemplate.postForObject(url, klendathu, Planet.class);  
        Thread.sleep(100);

        ResponseEntity<Planet> gotById = restTemplate.getForEntity("http://localhost:8080/swplanets/planet/id/{id}", Planet.class, returned.getId());
        assert(klendathu.equals(gotById));
        
        ResponseEntity<Planet> gotByName =  restTemplate.getForEntity("http://localhost:8080/swplanets/planet/name/{id}", Planet.class, "Klendathu");
        assert(klendathu.equals(gotByName));
        
        tearDown();
    }
    
    @Test
    public void testPlanetNotFoundForDelete(){
        tearDown();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8080/swplanets/planet/id/{id}", Planet.class, 999);

        restTemplate.delete("http://localhost:8080/swplanets/planet/name/{id}", Planet.class, "Klendathu");
        tearDown();
    }
    
    @Test
    public void testPlanetNotFoundForGet(){
        try{
        tearDown();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Planet> gotById = restTemplate.getForEntity("http://localhost:8080/swplanets/planet/id/{id}", Planet.class, 999);
        assert(gotById.getStatusCode()==HttpStatus.NOT_FOUND);

        ResponseEntity<Planet> gotByName =  restTemplate.getForEntity("http://localhost:8080/swplanets/planet/name/{id}", Planet.class, "Klendathu");
        assert(gotByName.getStatusCode()==HttpStatus.NOT_FOUND);
        tearDown();
        }catch(Exception ex)
        {
            ex.printStackTrace();
            throw  ex;
        }
    }
}
