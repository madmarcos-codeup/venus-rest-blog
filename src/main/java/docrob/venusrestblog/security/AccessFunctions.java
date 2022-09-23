package docrob.venusrestblog.security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class AccessFunctions {
    public static String getAccessTokenFromHeader(String header) {
        String [] parts = header.split(" ");
        if(parts.length != 2) {
            return null;
        }
        if(!parts[0].equalsIgnoreCase("bearer")) {
            return null;
        }
        return parts[1];
    }

    public static String getEmailFromToken(String accessToken) {
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
