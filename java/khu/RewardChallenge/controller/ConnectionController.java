package khu.RewardChallenge.controller;

import khu.RewardChallenge.entity.*;
import khu.RewardChallenge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/connection")
public class ConnectionController {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private PenaltyRepository penaltyRepository;
    @Autowired
    private OnlyChallengeRepository onlyChallengeRepository;
    @Autowired
    private ConnectionRepository connectionRepository;


    @GetMapping("/{id}/{token:[a-fA-F0-9-]+}")
    public String connection(@PathVariable("token") String token,@PathVariable("id") Long id,Model model) {

        if (isValidToken(token)) {
            return "redirect:/connection/{id}/{token}/connected";
        } else {
            // 접근 거부 처리
            return "/";
        }
    }
    @GetMapping("/{id}/{token}/connected")
    public String connected(@PathVariable("token") String token,@PathVariable("id") Long id,Model model){
        System.out.println(token);
        System.out.println(id);

        return "connection/connected";

    }

    @PostMapping("/challenge")
    public String ConnectionJoin(@RequestParam("token") String token, @RequestParam("id") Long id, User user, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println(token);
        System.out.println(id);
        User authenticatedUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            session.setAttribute("user", authenticatedUser);
            System.out.println("ok");
            redirectAttributes.addAttribute("id", id);
            redirectAttributes.addAttribute("token", token);
            return "redirect:/connection/{id}/{token}/challenge";
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("/{id}/{token:[a-fA-F0-9-]+}/challenge")
    public String con_challenge(@PathVariable("token") String token, @PathVariable("id") Long id,Model model){
        System.out.println("ok");
        Challenge challenge = challengeRepository.findByChallenge_id(id);
        User user=userRepository.findByUsername(onlyChallengeRepository.findUsernameBychallenge_id(id));
        model.addAttribute("token", token);
        model.addAttribute("id", id);// 접근 허용 처리
        model.addAttribute("name",user.getName());
        model.addAttribute("startdate",challenge.getStartdate());
        model.addAttribute("finishdate",challenge.getFinishdate());
        model.addAttribute("challengename",challenge.getChallengename());

        model.addAttribute("token", token);
        model.addAttribute("id", id);
        return "connection/challenge";
    }
    @PostMapping("/challenge/register")
    public String challenge_register(@RequestParam("token") String token, @RequestParam("id") Long id, Challenge challenge, HttpServletRequest request){
        System.out.println(challenge.getChallenge_id());
        System.out.println(id);
        Challenge challenge1 = challengeRepository.findByChallenge_id(id);
        challenge.setStartdate(challenge1.getStartdate());
        challenge.setFinishdate(challenge1.getFinishdate());
        challengeRepository.save(challenge);

//        String penalty;
//        penalty=challengeRepository.findPenaltyByChallenge_id(id);
//
//        Penalty newPenalty = new Penalty();
//        newPenalty.setChallenge_id(challenge.getChallenge_id());
//        newPenalty.setFriend_challenge_id(id);
//        newPenalty.setPenalty(challenge.getPenalty());
//        newPenalty.setStatus("0");
//        penaltyRepository.save(newPenalty);
//
//        Penalty newPenalty2 = new Penalty();
//        newPenalty2.setChallenge_id(id);
//        newPenalty2.setFriend_challenge_id(challenge.getChallenge_id());
//        newPenalty2.setPenalty(penalty);
//        newPenalty2.setStatus("0");
//        penaltyRepository.save(newPenalty2);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println(user.getUsername());

        OnlyChallenge onlyChallenge=new OnlyChallenge();
        onlyChallenge.setUsername(user.getUsername());
        onlyChallenge.setChallenge_id(challenge.getChallenge_id());
        onlyChallengeRepository.save(onlyChallenge);

        Connection connection=connectionRepository.findByChallenge_idstatus(id,"-1");
        connection.setFriend_challenge_id(challenge.getChallenge_id());
        connection.setStatus("0");
        connectionRepository.save(connection);

        Connection connection2=new Connection();
        connection2.setChallenge_id(challenge.getChallenge_id());
        connection2.setFriend_challenge_id(id);
        connection2.setStatus("0");
        connectionRepository.save(connection2);

        Token storedToken = tokenRepository.findByToken(token);
        storedToken.setStatus("1");
        tokenRepository.save(storedToken);
        return "redirect:/main";

    }
    @PostMapping("/register")
    public String connectionRegister(@RequestParam("token") String token, @RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("token", token);
        return "redirect:/connection/{id}/{token}/register";
    }
    @GetMapping("/{id}/{token:[a-fA-F0-9-]+}/register")
    public String New_User(@PathVariable("token") String token, @PathVariable("id") Long id, Model model){
        System.out.println(token);
        model.addAttribute("token", token);
        model.addAttribute("id", id);
        return "connection/register";
    }

    @PostMapping("/register/add")
    public String New_User_form(@RequestParam("token") String token, @RequestParam("id") Long id,User user, RedirectAttributes redirectAttributes) {
        System.out.println("add");
        userRepository.save(user);
        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("token", token);
        return "redirect:/connection/"+id+ "/" +token;
    }
    private boolean isValidToken(String token) {
        System.out.println(token);
        Token storedToken = tokenRepository.findByToken(token);
        return storedToken != null && storedToken.getStatus().equals("0");
    }
}
