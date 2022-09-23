package docrob.venusrestblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@Controller
public class ViewController {
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/loginSuccess")
    public String getLoginInfo(HttpServletRequest request, Model model, OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        if(client.getAccessToken() != null) {
            System.out.println("Have an access token: " + client.getAccessToken().getTokenValue());
            request.setAttribute("access_token", client.getAccessToken().getTokenValue());
        }
        if(client.getRefreshToken() != null) {
            System.out.println("Have an refresh token");
            request.setAttribute("refresh_token", client.getRefreshToken().getTokenValue());
        }
        return "forward:/ddd.html";
    }

    @RequestMapping({"/", "/about", "/login", "doLogin", "/logingoogle", "/home", "/posts", "/register", "/me"})
    public String showView(HttpServletRequest request) {
        // @RequestParam(name = "access_token", required = false) String accessToken
//        System.out.println(request.getRequestURI());
//        System.out.println(request.getQueryString());
//        System.out.println(Arrays.toString(request.getParameterMap().entrySet().toArray()));
//        if(accessToken != null && accessToken.length() > 0)
//            System.out.println(accessToken);
        return "forward:/index.html";
    }
}
