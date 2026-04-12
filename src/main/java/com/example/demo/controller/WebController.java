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

    // --- AUTHENTICATION & CORE ---

    @GetMapping("/signup")
    public String signupPage(Model model) {    
        model.addAttribute("user", new User()); 
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signPost(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) return "auth/signup";
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
            return "redirect:/index"; 
        } else {
            model.addAttribute("loginError", "Invalid email or password.");
            return "auth/login";
        }
    }

    @GetMapping("/index")
    public String homePage() {
        return "index1"; 
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    // --- BRAND ROUTES ---
    
    @GetMapping("/GNBMW") public String showBmwPage() { return "BMW/gnbmw"; }
    @GetMapping("/GNBUGATTI") public String showBugattiPage() { return "BUGATTI/gnbugatti"; }
    @GetMapping("/CHIRON-SS-300") public String showBugattiexclusivePage() { return "BUGATTI/chironss300"; }
    @GetMapping("/GNAUDI") public String showAudiPage() { return "AUDI/gnaudi"; }
    @GetMapping("/GNMCLAREN") public String showMcLarenPage() { return "MCLAREN/gnmclaren"; }
    @GetMapping("/GNPORSCHE") public String showPorschePage() { return "PORSCHE/gnporsche"; }
    @GetMapping("/GNFERRARI") public String showFerrariPage() { return "FERRARI/gnferrari"; }
    @GetMapping("/GNASTONMARTIN") public String showAstonMartinPage() { return "ASTONMARTIN/gnastonmartin"; }
    @GetMapping("/GNTOYOTA") public String showToyotaPage() { return "TOYOTA/gntoyota"; }
    @GetMapping("/chatbot") public String showChatBotPage() { return "chatbot"; }

    // --- ADMIN MODULE (SECURE ACCESS) ---

    @PostMapping("/adminLogin")
    public String adminLogin(@RequestParam("adminPassword") String password, HttpSession session, RedirectAttributes ra) {
        if ("admin@123".equals(password)) {
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/showRecords";
        } else {
            ra.addFlashAttribute("error", "Access Denied: Invalid Admin Password");
            return "redirect:/index";
        }
    }

    @GetMapping("/admin/showRecords")
    public String showRecords(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) return "redirect:/login";
        // Passing the list of users for DataTables and analytics [cite: 50, 76]
        model.addAttribute("users", userService.getAllUsers()); 
        return "admin/show_records"; 
    }

    @GetMapping("/admin/editUser/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) return "redirect:/login";
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "admin/update_user"; 
        }
        return "redirect:/admin/showRecords";
    }

    @PostMapping("/admin/updateUser/{id}")
    public String updateUserInfo(@PathVariable("id") Long id, @ModelAttribute User user, HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("isAdmin") == null) return "redirect:/login";
        User existingUser = userService.getUserById(id);
        if (existingUser != null) {
            existingUser.setUserName(user.getUserName());
            existingUser.setEmail(user.getEmail());
            userService.saveUser(existingUser);
            ra.addFlashAttribute("message", "Member profile updated successfully.");
        }
        return "redirect:/admin/showRecords";
    }

    @GetMapping("/admin/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id, HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("isAdmin") == null) return "redirect:/login";
        userService.deleteUserById(id);
        ra.addFlashAttribute("message", "Member successfully removed.");
        return "redirect:/admin/showRecords";
    }

    // --- USER PROFILE & SETTINGS ---

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "auth/profile"; 
    }

    @GetMapping("/editProfile")
    public String editProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "auth/edit_profile"; 
    }

    @GetMapping("/changePassword")
    public String changePasswordPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";
        return "auth/change_password"; 
    }

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
        return "redirect:/index"; 
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
        return "redirect:/index";
    }

    // --- SYSTEM LOGIC ---

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Clears all session data for security 
        session.invalidate(); 
        return "redirect:/login";
    }

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
        ra.addFlashAttribute("message", "Thank you, " + email + "! Your feedback has been safely recorded.");
        
        return "redirect:/index"; 
    }
    
    @GetMapping("/admin/viewFeedback")
    public String viewFeedback(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) return "redirect:/login";
        
               model.addAttribute("feedbacks", userService.getAllUsers()); 
        return "admin/feedback_list"; 
    }
}