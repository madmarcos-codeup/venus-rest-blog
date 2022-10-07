package docrob.venusrestblog.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import docrob.venusrestblog.data.User;
import docrob.venusrestblog.repository.UsersRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
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
        String [] fields = getFieldsFromToken(accessToken);
        if(fields[0] == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        String email = fields[0];
        User user = usersRepository.findByEmail(email);
        if(user == null) {
            System.out.println("User not found: " + email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        user.setProfilePic(fields[1]);
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

    private String [] getFieldsFromToken(String accessToken) {
        String [] fields = new String[2];
        RestTemplate restTemplate = new RestTemplate();

        String uri = "https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses,photos&access_token=" + accessToken;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<?> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

//        System.out.println(result.getBody());
        JsonObject jo = JsonParser.parseString(result.getBody().toString()).getAsJsonObject();
        String email = jo.get("emailAddresses").getAsJsonArray().get(0).getAsJsonObject().get("value").toString().replaceAll("\"", "");
        String photo = jo.get("photos").getAsJsonArray().get(0).getAsJsonObject().get("url").toString().replaceAll("\"", "");

        fields[0] = email;
        fields[1] = photo;
        return fields;
    }

    public User getUserFromAuthHeaderJWT(String authHeader) {
        if(authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        // grab access token
        String jwt = getAccessTokenFromHeader(authHeader);
        if(jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String [] parts = jwt.split("\\.");
        if(parts.length != 3) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT (parts != 3)");
        }
        String header = new String(Base64.getDecoder().decode(parts[0]));
        String payload = new String(Base64.getDecoder().decode(parts[1]));
        String signature = parts[2];

        // TODO: check jwt with google's public key to determine if it is ok
        if(false) {
            String jwksURI = getJWKSURI("https://accounts.google.com/.well-known/openid-configuration").trim();
            String[] sigKeys = getKeys(jwksURI);

            if (!validJWT(header, payload, signature, sigKeys)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT (keys)");
            }
        }

        // {"iss":"https://accounts.google.com","nbf":1664992935,"aud":"807309833487-uvihhc5be5ie00qj13a9d20kt9sovlp6.apps.googleusercontent.com","sub":"107139802099997183705","email":"docrob1337@gmail.com","email_verified":true,"azp":"807309833487-uvihhc5be5ie00qj13a9d20kt9sovlp6.apps.googleusercontent.com","name":"Doc Rob","picture":"https://lh3.googleusercontent.com/a-/ACNPEu_NulKPiimDFaM0ItqM9QeQzE8sRTp8Xy-Z5Myc=s96-c","given_name":"Doc","family_name":"Rob","iat":1664993235,"exp":1664996835,"jti":"b3bb8ca4faaa527340ce38b5a3082802de89e529"}
        JsonObject jo = JsonParser.parseString(payload).getAsJsonObject();
        String email = jo.get("email").toString().replaceAll("\"", "");
        User user = usersRepository.findByEmail(email);
        if(user == null) {
            System.out.println("User not found: " + email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        user.setProfilePic(jo.get("picture").toString());
        return user;
    }

    private boolean validJWT(String header, String payload, String signature, String[] sigKeys) {
        SignatureAlgorithm sa = SignatureAlgorithm.RS256;
        for(String sigKey : sigKeys) {
            try {
                KeyFactory kf = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(sigKey.getBytes());
                RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
                SecretKeySpec secretKeySpec = new SecretKeySpec(pubKey.toString().getBytes(), sa.getJcaName());
                DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
                if (validator.isValid(header + "." + payload, signature)) {
                    return true;
                }
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                // ignore and continue checking
            }
        }
        return false;
    }

    private String[] getKeys(String jwksURI) {
        ArrayList<String> keys = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<?> result = restTemplate.exchange(jwksURI, HttpMethod.GET, entity, String.class);

//        System.out.println(result.getBody());
        JsonObject jo = JsonParser.parseString(result.getBody().toString()).getAsJsonObject();
        JsonArray keyElements = jo.getAsJsonArray("keys");
        for(JsonElement el : keyElements) {
            keys.add(el.getAsJsonObject().get("n").toString().replaceAll("\"", ""));
        }
        String [] keyVals = new String[keys.size()];
        keyVals = keys.toArray(keyVals);
        return keyVals;
    }

    private String getJWKSURI(String uri) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<?> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

//        System.out.println(result.getBody());
        JsonObject jo = JsonParser.parseString(result.getBody().toString()).getAsJsonObject();
        return jo.get("jwks_uri").toString().replaceAll("\"", "");
    }
}
