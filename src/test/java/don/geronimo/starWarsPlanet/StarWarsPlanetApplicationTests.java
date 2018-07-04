package don.geronimo.starWarsPlanet;

import don.geronimo.starWarsPlanet.model.Planet;
import don.geronimo.starWarsPlanet.repository.PlanetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StarWarsPlanetApplicationTests {
        @Autowired
        private PlanetRepository repo;
        @Before
        public void purgeDatabase()
        {
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
            assert(inDb.getClimate().equals(p.getClimate()));
            assert(inDb.getName().equals(p.getName()));
            assert(inDb.getTerrain().equals(p.getTerrain()));
            assert(inDb.getFrequency().equals(p.getFrequency()));
           
        }
        
}
