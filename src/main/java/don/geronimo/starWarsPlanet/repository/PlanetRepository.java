package don.geronimo.starWarsPlanet.repository;
import don.geronimo.starWarsPlanet.model.Planet;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface PlanetRepository extends MongoRepository<Planet, Integer> {
    public Optional<Planet> findByName(String name);
}
