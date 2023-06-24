package khu.RewardChallenge.controller;

import khu.RewardChallenge.entity.*;
import khu.RewardChallenge.repository.*;
import khu.RewardChallenge.service.EmailService;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Controller
@RequestMapping("/challenge")
public class ChallengeController {
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Autowired
    private OnlyChallengeRepository onlyChallengeRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @GetMapping("/register")
    public String register(){
        return "challenge/register";
    }
    @PostMapping("/register")
    public String register_post(Challenge challenge,@RequestParam("email") String email, HttpServletRequest request){
        challengeRepository.save(challenge);

//        Penalty newPenalty = new Penalty();
//        newPenalty.setChallenge_id(challenge.getChallenge_id());
//        newPenalty.setPenalty(penalty);
//        newPenalty.setStatus("0");
//        penaltyRepository.save(newPenalty);
        Connection connection=new Connection();
        connection.setStatus("-1");
        connection.setChallenge_id(challenge.getChallenge_id());
        connectionRepository.save(connection);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        OnlyChallenge onlyChallenge=new OnlyChallenge();
        onlyChallenge.setUsername(user.getUsername());
        onlyChallenge.setChallenge_id(challenge.getChallenge_id());
        onlyChallengeRepository.save(onlyChallenge);
        invite_form(email,challenge.getChallenge_id(),user.getName());
        return "redirect:/main";
    }
    @GetMapping("/{id}")
    public String getChallengeDetails(@PathVariable("id") Long challenge_id, Model model) {
                // Add the challenge details to the model
        Challenge challenge = challengeRepository.findByChallenge_id(challenge_id);


        model.addAttribute("challenge", challenge);

        System.out.println(challenge);

        // Return the name of the Thymeleaf template to be rendered
        return "challenge/detail";
    }
    @GetMapping("info/{id}")
    public String getinfoChallengeDetails(@PathVariable("id") Long challenge_id, Model model) {
        // Add the challenge details to the model
        Challenge challenge = challengeRepository.findByChallenge_id(challenge_id);


        model.addAttribute("challenge", challenge);
        // Return the name of the Thymeleaf template to be rendered
        return "challenge/info";
    }
    private void invite_form(String email,Long challenge_id,String username){

        String token = generateToken();
        Challenge challenge = challengeRepository.findByChallenge_id(challenge_id);
        List<String> startdate=parseDate(challenge.getStartdate());
        List<String> finishdate=parseDate(challenge.getFinishdate());
        String challengename=challenge.getChallengename();
        String subject = "Challenge Invitation";
        String body = "안녕하세요,"+username+"님이 Challenge에 초대합니다! 아래 링크를 클릭하여 참여하세요.\n\n" +
                "-Challenge 링크: localhost:8080/connection/"+challenge_id+"/"+ token+"\n\n" +
                "-Challenge 정보\n"+
                "Challenge 이름: "+challengename+"\n\n"+
                "Challenge 시작 날짜: "+startdate.get(0)+"년"+startdate.get(1)+"월"+startdate.get(2)+"일\n\n"+
                "Challenge 끝나는 날짜: "+finishdate.get(0)+"년"+finishdate.get(1)+"월"+finishdate.get(2)+"일\n\n\n"+
                "해당 기간동안"+username+"님과 함께 본인의 Challenge를 등록해서 함께 달성해보세요!!!!\n\n";
        emailService.sendEmail(email,subject,body);
        Token newtoken = new Token();
        newtoken.setToken(token);
        newtoken.setStatus("0");

        tokenRepository.save(newtoken);

    }
    // 토큰 생성 메소드
    private String generateToken() {

        String token = UUID.randomUUID().toString();
        return token;
    }

    private List<String> parseDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        List<String> datelist=new ArrayList<>();
        datelist.add(Integer.toString(year));
        datelist.add(Integer.toString(month));
        datelist.add(Integer.toString(day));

        return datelist;
    }
}
