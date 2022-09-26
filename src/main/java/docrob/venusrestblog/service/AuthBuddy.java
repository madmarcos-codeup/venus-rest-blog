package docrob.venusrestblog.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import docrob.venusrestblog.data.User;
import docrob.venusrestblog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class AuthBuddy {

    @Autowired
    private UsersRepository usersRepository;

    public User getUserFromAuthHeader(String authHeader) {
        if(authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        // grab access token
        String accessToken = getAccessTokenFromHeader(authHeader);
        if(accessToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        // assume google auth. get email from google via the access token
        String email = getEmailFromToken(accessToken);
        if(email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        User user = usersRepository.findByEmail(email);
        if(user == null) {
            System.out.println("User not found: " + email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return user;
    }

    private String getAccessTokenFromHeader(String header) {
        String [] parts = header.split(" ");
        if(parts.length != 2) {
            return null;
        }
        if(!parts[0].equalsIgnoreCase("bearer")) {
            return null;
        }
        return parts[1];
    }

    private String getEmailFromToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        String uri = "https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses&access_token=" + accessToken;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<?> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

//        System.out.println(result.getBody());
        JsonObject jo = JsonParser.parseString(result.getBody().toString()).getAsJsonObject();
        String email = jo.get("emailAddresses").getAsJsonArray().get(0).getAsJsonObject().get("value").toString().replaceAll("\"", "");
        return email;
    }

}
