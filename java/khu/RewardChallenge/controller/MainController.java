package khu.RewardChallenge.controller;

import khu.RewardChallenge.entity.Connection;
import khu.RewardChallenge.entity.OnlyChallenge;
import khu.RewardChallenge.entity.User;
import khu.RewardChallenge.repository.ChallengeRepository;
import khu.RewardChallenge.repository.ConnectionRepository;
import khu.RewardChallenge.repository.OnlyChallengeRepository;
import khu.RewardChallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OnlyChallengeRepository onlyChallengeRepository;

    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private ConnectionRepository connectionRepository;

    @PostMapping("/main")
    public String login(@ModelAttribute User user, HttpSession session) {
        User authenticatedUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            session.setAttribute("user", authenticatedUser);
            System.out.println("ok");
            return "redirect:/main";
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("main")
    public String go_to_main(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        User user2=new User();

        List<Long> challenges_id=onlyChallengeRepository.findChallenge_idByUsername(user.getUsername());
        System.out.println(challenges_id);
        List<String> challenges_name= new ArrayList<>();
        String challenge_add;
        for(Long c_id : challenges_id){
            challenge_add= challengeRepository.findChallengenameBychallengeid(c_id);
            challenges_name.add(challenge_add);
        }

        List<Long> id_challenge;
        List<Long> connect_id = new ArrayList<>();
        for(Long c_id : challenges_id){
            System.out.println(c_id);
            id_challenge= connectionRepository.findIdByChallenge_id(c_id);
            System.out.println(id_challenge);
            if(id_challenge!=null){
                for(Long id : id_challenge){
                    connect_id.add(id);
                }
            }
        }



        List<String> challenges_name_n= new ArrayList<>();
        List<Long> challenges_id_n= new ArrayList<>();
        List<Long> challenges_friend_id_n= new ArrayList<>();
        List<String> challenges_username_n= new ArrayList<>();
        List<String> challenges_realname_n= new ArrayList<>();
        List<String> challenges_status= new ArrayList<>();
        List<String> challenges_friend_name_n= new ArrayList<>();
        for(Long id : connect_id){
            Connection connection = new Connection();
            connection=connectionRepository.findByID(id);
            if(connection != null && !connection.getStatus().equals("-1")){
                System.out.print("찾는값~~~~~");
                System.out.println(connection);
                challenges_name_n.add(challengeRepository.findChallengenameBychallengeid(connection.getChallenge_id()));
                challenges_friend_name_n.add(challengeRepository.findChallengenameBychallengeid(connection.getFriend_challenge_id()));
                challenges_status.add(connection.getStatus());
                challenges_id_n.add(connection.getId());
                challenges_friend_id_n.add(connection.getFriend_challenge_id());
                challenges_username_n.add(onlyChallengeRepository.findUsernameBychallenge_id(connection.getFriend_challenge_id()));
                user2=userRepository.findByUsername(onlyChallengeRepository.findUsernameBychallenge_id(connection.getFriend_challenge_id()));
                challenges_realname_n.add(user2.getName());
            }
            else{
                challenges_name_n.add(challengeRepository.findChallengenameBychallengeid(connection.getChallenge_id()));
                challenges_friend_name_n.add("0");
                challenges_status.add(connection.getStatus());
                challenges_id_n.add(connection.getId());
                challenges_friend_id_n.add(1L);
                challenges_username_n.add("0");
                challenges_realname_n.add("X");

            }
        }
        if(challenges_name != null) {
            model.addAttribute("challenges_name", challenges_name);
            model.addAttribute("challenges_id", challenges_id);
            model.addAttribute("name", user.getName());
        }
        if(challenges_name_n != null) {
            System.out.println(challenges_status);
            System.out.println(challenges_name_n);
            model.addAttribute("challenges_name_n", challenges_name_n);
            model.addAttribute("challenges_id_n", challenges_id_n);
            model.addAttribute("challenges_username_n", challenges_username_n);
            model.addAttribute("challenges_realname_n", challenges_realname_n);
            model.addAttribute("challenges_friend_id_n", challenges_friend_id_n);
            model.addAttribute("challenges_friend_name_n", challenges_friend_name_n);
            model.addAttribute("challenges_status", challenges_status);
        }
        return "main";
    }
}
