package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final SavedTripRepository savedTripRepository;

    public AuthController(UserRepository userRepository,
                      BackendService backendService,
                      SavedTripRepository savedTripRepository) {
        this.userRepository = userRepository;
        this.backendService = backendService;
        this.savedTripRepository = savedTripRepository;
    }


    private final UserRepository userRepository;
    private final BackendService backendService;

    // public AuthController(UserRepository userRepository,
    //                       BackendService backendService) {
    //     this.userRepository = userRepository;
    //     this.backendService = backendService;
    // }

    // @GetMapping("/login")
    // public String loginForm() {
    //     return "login";   
    // }

    // @PostMapping("/login")
    // public String doLogin(@RequestParam String username,
    //                       @RequestParam String password,
    //                       HttpSession session,
    //                       Model model) {

    //     User user = userRepository.findByUsername(username);

    //     if (user == null || !user.getPassword().equals(password)) {
    //         model.addAttribute("error", "Invalid username or password");
    //         return "login";
    //     }

    //     session.setAttribute("userId", user.getId());
    //     return "redirect:/home";
    // }
@GetMapping("/auth/login")
public String loginForm() {
    return "redirect:/login.html";
}

@PostMapping("/auth/login")
public String doLogin(@RequestParam String username,
                      @RequestParam String password,
                      HttpSession session,
                      Model model) {

    try {
        System.out.println("Login attempt: username=" + username);

        User user = userRepository.findByUsername(username);
        System.out.println("Found user? " + (user != null));

        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userRepository.save(user);
            System.out.println("New user saved with id=" + user.getId());
        } else if (!user.getPassword().equals(password)) {
            System.out.println("Password mismatch for user " + username);
            model.addAttribute("error", "Invalid username or password");
            return "redirect:/login.html?error=bad_credentials";
        }

        session.setAttribute("userId", user.getId());
        return "redirect:/home";
    } catch (Exception e) {
        e.printStackTrace();
        // Redirect with error flag so you can see something happened
        return "redirect:/login.html?error=server";
    }
}



@GetMapping("/home")
public String home(HttpSession session) {
    if (session.getAttribute("userId") == null) {
        return "redirect:/login.html";
    }
    return "redirect:/index.html";   // if index.html is in /static
}

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    // @GetMapping("/home")
    // public String home(HttpSession session) {
    //     if (session.getAttribute("userId") == null) {
    //         return "redirect:/login";
    //     }
    //     return "index"; 
    // }

    


    @PostMapping("/trips")
    @ResponseBody
    public String saveTrip(@RequestBody String planHtml, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "NOT_AUTHENTICATED";
        }

        SavedTrip trip = new SavedTrip();
        trip.setUserId(userId);
        trip.setTitle("Trip " + System.currentTimeMillis());
        trip.setPlanHtml(planHtml);
        trip.setCreatedAt(java.time.Instant.now().toString());

        savedTripRepository.save(trip);
        return "OK";
    }

    @GetMapping("/trips")
    @ResponseBody
    public java.util.List<SavedTrip> getTrips(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return java.util.List.of();
        }
        return savedTripRepository.findByUserId(userId);
    }

    
}

