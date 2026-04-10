package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    @Autowired
    private UserService userService;

    // --- AUTHENTICATION ---

    @GetMapping("/signup")
    public String signupPage(Model model) {    
        model.addAttribute("user", new User()); 
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signPost(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "auth/signup";
        }
        userService.saveUser(user); 
        return "auth/success";
    } 

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; 
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam("email") String email, 
                            @RequestParam("password") String password, 
                            HttpSession session, 
                            Model model) {
        User user = userService.loginUser(email, password);
        
        if (user != null) {
            session.setAttribute("loggedInUser", user); 
            // Changed from /index1 to /index
            return "redirect:/index"; 
        } else {
            model.addAttribute("loginError", "Invalid email or password.");
            return "auth/login";
        }
    }

    @GetMapping("/index") // This is the URL shown in the browser
    public String homePage() {
        return "index1"; // This refers to the actual index1.html file
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    // --- BRAND ROUTES ---
    
    @GetMapping("/GNBMW") public String showBmwPage() { return "BMW/gnbmw"; }
    @GetMapping("/GNBUGATTI") public String showBugattiPage() { return "BUGATTI/gnbugatti"; }
    @GetMapping("/GNAUDI") public String showAudiPage() { return "AUDI/gnaudi"; }
    @GetMapping("/GNMCLAREN") public String showMcLarenPage() { return "MCLAREN/gnmclaren"; }
    @GetMapping("/GNPORSCHE") public String showPorschePage() { return "PORSCHE/gnporsche"; }
    @GetMapping("/GNFERRARI") public String showFerrariPage() { return "FERRARI/gnferrari"; }
    @GetMapping("/GNASTONMARTIN") public String showAstonMartinPage() { return "ASTONMARTIN/gnastonmartin"; }
    @GetMapping("/GNTOYOTA") public String showToyotaPage() { return "TOYOTA/gntoyota"; }
    @GetMapping("/CHIRON-SS-300") public String showChironSS300() { return "BUGATTI/chironss300"; }

    // --- ADMIN RECORDS ---

    @GetMapping("/showRecords")
    public String showRecords(Model model) {
        model.addAttribute("users", userService.getAllUsers()); 
        return "auth/ShowRecords"; 
    }

    @GetMapping("/editUser/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "update_user"; 
        }
        return "redirect:/showRecords";
    }

    @PostMapping("/updateUser/{id}")
    public String updateUserInfo(@PathVariable("id") Long id, @ModelAttribute User user, RedirectAttributes ra) {
        User existingUser = userService.getUserById(id);
        if (existingUser != null) {
            existingUser.setUserName(user.getUserName());
            existingUser.setEmail(user.getEmail());
            userService.saveUser(existingUser);
            ra.addFlashAttribute("message", "Member profile updated successfully.");
        } else {
            ra.addFlashAttribute("message", "Error: Member not found.");
        }
        return "redirect:/showRecords";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        userService.deleteUserById(id);
        ra.addFlashAttribute("message", "Member successfully removed.");
        return "redirect:/showRecords";
    }

    @GetMapping("/chatbot")
    public String showChatbot() {
        return "chatbot"; 
    }

    // --- USER MODULE CORE LOGIC ---

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute("user") User user, HttpSession session, RedirectAttributes ra) {
        User existingUser = (User) session.getAttribute("loggedInUser");
        if (existingUser != null) {
            existingUser.setUserName(user.getUserName());
            existingUser.setEmail(user.getEmail());
            userService.saveUser(existingUser);
            session.setAttribute("loggedInUser", existingUser);
            ra.addFlashAttribute("message", "Profile updated successfully!");
        }
        return "redirect:/index"; // Changed to /index
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpSession session, RedirectAttributes ra) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null && loggedInUser.getPassword().equals(oldPassword)) {
            loggedInUser.setPassword(newPassword);
            userService.saveUser(loggedInUser);
            ra.addFlashAttribute("message", "Password changed successfully!");
        } else {
            ra.addFlashAttribute("error", "Incorrect current password!");
        }
        return "redirect:/index"; // Changed to /index
    }

    // --- FORGET PASSWORD LOGIC ---

    @GetMapping("/forgetPassword")
    public String showForgetPage() {
        return "forget_password"; 
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword, RedirectAttributes ra) {
        User user = userService.findUserByEmail(email); 
        
        if (user != null) {
            user.setPassword(newPassword);
            userService.saveUser(user);
            ra.addFlashAttribute("message", "Password updated! Please login.");
            return "redirect:/login";
        }
        
        ra.addFlashAttribute("error", "Email not found in our records.");
        return "redirect:/forgetPassword";
    }

    @PostMapping("/sendFeedback")
    public String receiveFeedback(@RequestParam String feedbackMsg, HttpSession session, RedirectAttributes ra) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String email = (loggedInUser != null) ? loggedInUser.getEmail() : "Guest";

        System.out.println("Feedback from " + email + ": " + feedbackMsg);

        ra.addFlashAttribute("message", "Thank you! Your feedback has been received.");
        return "redirect:/index";
    }
    
    @GetMapping("/viewFeedback")
    public String viewFeedback(Model model) {
        // model.addAttribute("feedbacks", feedbackService.getAllFeedback());
        return "/feedback_list"; 
    }
}