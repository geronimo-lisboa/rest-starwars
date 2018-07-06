package don.geronimo.starWarsPlanet.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import don.geronimo.starWarsPlanet.model.swapi.QueriedPlanet;
import java.io.IOException;
import java.util.Optional;

public class BuildSWAPIPlanetService {
    public Optional< QueriedPlanet > buildPlanet(String swapiJson) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        QueriedPlanet p = mapper.readValue(swapiJson, QueriedPlanet.class);
        if(p.getResults().isEmpty())
            p = null;

        return Optional.ofNullable(p);
        
    }
}
