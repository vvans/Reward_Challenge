package khu.RewardChallenge.controller;

import khu.RewardChallenge.entity.User;
import khu.RewardChallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String register() {
        return "account/register";
    }

    @PostMapping("/register")
    public String register(User user) {
        userRepository.save(user);
        return "redirect:/";
    }
//    @PostMapping("/login")
//    public String login(@ModelAttribute User user, HttpSession session) {
//        User authenticatedUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//        if (authenticatedUser != null) {
//            session.setAttribute("user", authenticatedUser);
//            System.out.println("ok");
//            return "redirect:/main";
//        } else {
//            return "redirect:/";
//        }
//    }
//    @GetMapping("main")
//    public String go_to_main(){
//        System.out.println("ok2");
//
//        return "main";
//    }


}
