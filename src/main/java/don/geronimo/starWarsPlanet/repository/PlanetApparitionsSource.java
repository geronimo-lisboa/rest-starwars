package don.geronimo.starWarsPlanet.repository;

import don.geronimo.starWarsPlanet.model.Planet;
import don.geronimo.starWarsPlanet.model.swapi.QueriedPlanet;
import don.geronimo.starWarsPlanet.services.BuildSWAPIPlanetService;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import java.io.*;
import java.net.*;
import java.util.Optional;
import java.util.logging.Level;
import javax.net.ssl.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class PlanetApparitionsSource {
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
    private final String name;
    /**Pega o json, usando api de baixo nivel*/
    private String getJsonLowLevel(){
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String urlOverHttps = "https://swapi.co/api/planets/?search="+name;
            HttpGet getMethod = new HttpGet(urlOverHttps);
            HttpResponse response = httpClient.execute(getMethod);
            ResponseHandler<String> handler = new BasicResponseHandler();
            String body = handler.handleResponse(response);
            System.out.println(body);
            return body;    
        }catch(Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }        
    }
    //Cria a fonte de apariçoes para o planeta dado.
    public PlanetApparitionsSource(String name){
        this.name = name;
    }
    
    //Pega quantas vezes o planeta aparece nos filmes
    public Optional<Integer> getNumberOfApparition() throws IOException{
        String swApiJson = getJsonLowLevel();
        Optional<QueriedPlanet> queried = new BuildSWAPIPlanetService().buildPlanet(swApiJson);
        Optional<Integer> result = Optional.empty();
        if(queried.isPresent())
            return Optional.of(queried.get().getResults().get(0).getFilms().size());
        else
            return Optional.empty();
    }
}
