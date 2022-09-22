package docrob.venusrestblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@Controller
public class ViewController {

    @RequestMapping({"/", "/about", "/login", "/logingoogle", "/dologin", "/home", "/posts", "/register", "/me"})
    public String showView(HttpServletRequest request) {
        // @RequestParam(name = "access_token", required = false) String accessToken
        System.out.println(request.getRequestURI());
        System.out.println(request.getQueryString());
        System.out.println(Arrays.toString(request.getParameterMap().entrySet().toArray()));
//        if(accessToken != null && accessToken.length() > 0)
//            System.out.println(accessToken);
        return "forward:/index.html";
    }
}
