package khu.RewardChallenge.controller;

import khu.RewardChallenge.entity.Challenge;
import khu.RewardChallenge.entity.Connection;
import khu.RewardChallenge.entity.Penalty;
import khu.RewardChallenge.entity.User;
import khu.RewardChallenge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/negotiation")
public class NegotiationController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NegotiationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Autowired
    private OnlyChallengeRepository onlyChallengeRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("main")
    public String main(){
        return "redirect:/main";
    }
    @PostMapping("/complete")
    public String complete(@RequestParam("id") Long id){
        Long m=connectionRepository.findChallenge_idById(id);
        Long f=connectionRepository.findFrinend_Challenge_idById(id);
        Long f_connection_id=connectionRepository.find_f_connection_id(f,m);
        Connection connection_f=new Connection();
        Connection connection_m=new Connection();
        connection_m=connectionRepository.findByID(id);
        connection_f=connectionRepository.findByID(f_connection_id);
        if(connection_f.getStatus().equals("0.5")){
            connection_f.setStatus("1");
            connection_m.setStatus("1");
            connectionRepository.save(connection_f);
            connectionRepository.save(connection_m);
        }
        else{
            connection_m.setStatus("0.5");
            connectionRepository.save(connection_m);

        }
        return "redirect:/main";
    }

    @PostMapping("/accept")
    public String accept(@RequestParam("my_id") Long my_id,@RequestParam("id") Long id,RedirectAttributes redirectAttributes){
        Penalty penalty = new Penalty();
        penalty=penaltyRepository.findByID(my_id);
        penalty.setStatus("1");
        penaltyRepository.save(penalty);
        redirectAttributes.addAttribute("id",id);
        status0(id);
        status0(findid(id));
        return "redirect:/negotiation/{id}";
    }
    @PostMapping("/reject")
    public String request(@RequestParam("id") Long id,@RequestParam("my_id") Long my_penalty_id,RedirectAttributes redirectAttributes){

        Penalty penalty = new Penalty();
        penalty=penaltyRepository.findByID(my_penalty_id);
        penalty.setStatus("2");
        penaltyRepository.save(penalty);
        redirectAttributes.addAttribute("id",id);
        status0(id);
        status0(findid(id));
        return "redirect:/negotiation/{id}";
    }
    @PostMapping("/request")
    public String request(@RequestParam("penalty") String penalty,@RequestParam("id") Long id,RedirectAttributes redirectAttributes){
        Long m=connectionRepository.findChallenge_idById(id);
        Long f=connectionRepository.findFrinend_Challenge_idById(id);
        Penalty new_penalty = new Penalty();
        new_penalty.setStatus("0");
        new_penalty.setPenalty(penalty);
        new_penalty.setChallenge_id(m);
        new_penalty.setFriend_challenge_id(f);
        penaltyRepository.save(new_penalty);
        redirectAttributes.addAttribute("id",id);
//        messagingTemplate.convertAndSend("/topic/negotiation-updates/" + id_socket, "update");
        System.out.print("request 요청");
        status0(id);
        status0(findid(id));
        return "redirect:/negotiation/{id}";
    }
    @PostMapping("/add")
    public String add(@RequestParam("penalty") String penalty,@RequestParam("id") Long id,RedirectAttributes redirectAttributes){
        Long m=connectionRepository.findChallenge_idById(id);
        Long f=connectionRepository.findFrinend_Challenge_idById(id);

        Penalty new_penalty = new Penalty();
        new_penalty.setStatus("0");
        new_penalty.setPenalty(penalty);
        new_penalty.setChallenge_id(f);
        new_penalty.setFriend_challenge_id(m);
        penaltyRepository.save(new_penalty);
        redirectAttributes.addAttribute("id",id);
//                messagingTemplate.convertAndSend("/topic/negotiation-updates/" + id_socket, "update");

        System.out.print("add 요청");
        status0(id);
        status0(findid(id));

        return "redirect:/negotiation/{id}";
    }
    @GetMapping("/{id}")
    public String negotiation(@PathVariable("id") Long id,Model model){
        Long c_id;
        Long f_c_id;
        c_id=connectionRepository.findChallenge_idById(id);
        f_c_id=connectionRepository.findFrinend_Challenge_idById(id);
        User my_user=userRepository.findByUsername(onlyChallengeRepository.findUsernameBychallenge_id(c_id));
        User friend_user=userRepository.findByUsername(onlyChallengeRepository.findUsernameBychallenge_id(f_c_id));
        model.addAttribute("my_name", my_user.getName());
        model.addAttribute("friend_name", friend_user.getName());
        String my_challenge_name=challengeRepository.findChallengenameBychallengeid(c_id);
        String friend_challenge_name=challengeRepository.findChallengenameBychallengeid(f_c_id);
        Challenge challenge = challengeRepository.findByChallenge_id(c_id);
        System.out.println("찾는값");
        System.out.println(challenge);
        Long day= remain_date(challenge.getStartdate());
        model.addAttribute("day", day);
        List<String> startdate=parseDate(challenge.getStartdate());
        List<String> finishdate=parseDate(challenge.getFinishdate());

        String start_date=startdate.get(0)+"/"+startdate.get(1)+"/"+startdate.get(2);
        String finish_date=finishdate.get(0)+"/"+finishdate.get(1)+"/"+finishdate.get(2);;
        model.addAttribute("start_date", start_date);
        model.addAttribute("finish_date", finish_date);
        model.addAttribute("my_challenge_name", my_challenge_name);
        model.addAttribute("friend_challenge_name", friend_challenge_name);


        List<String> penalty_my =new ArrayList<>();
        List<String> penalty_friend =new ArrayList<>();

        List<String> status_my =new ArrayList<>();
        List<String> status_friend =new ArrayList<>();

        List<Long> id_my =new ArrayList<>();
        List<Long> id_friend =new ArrayList<>();

        id_my=penaltyRepository.findidByChallenge_idandFriend_challenge_id(c_id,f_c_id);
        id_friend=penaltyRepository.findidByChallenge_idandFriend_challenge_id(f_c_id,c_id);
        for(Long myid : id_my){
            status_my.add(penaltyRepository.findStatusByid(myid));
            penalty_my.add(penaltyRepository.findPenaltyByid(myid));
        }
        for(Long fid : id_friend){
            status_friend.add(penaltyRepository.findStatusByid(fid));
            penalty_friend.add(penaltyRepository.findPenaltyByid(fid));
        }
        model.addAttribute("penalty_my", penalty_my);
        model.addAttribute("penalty_friend", penalty_friend);
        model.addAttribute("status_my", status_my);
        model.addAttribute("status_friend", status_friend);
        model.addAttribute("id_my", id_my);
        model.addAttribute("id_friend", id_friend);
        model.addAttribute("id", id);

//        messagingTemplate.convertAndSend("/topic/negotiation-updates/" + id, "update");

        System.out.println(id);
        System.out.print("get 요청");

        return "procedure/negotiation";
    }
    private Long remain_date(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime targetTime = LocalDateTime.of(year, month, day, 0, 0, 0);

        Duration duration = Duration.between(currentTime,targetTime);

        long remainingDays = duration.toDays();

        return remainingDays+1;

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
    private void status0(Long id){

        Connection connection=new Connection();
        connection=connectionRepository.findByID(id);
        connection.setStatus("0");
        connectionRepository.save(connection);


    }
    private Long findid(Long id){
        Long m=connectionRepository.findChallenge_idById(id);
        Long f=connectionRepository.findFrinend_Challenge_idById(id);
        Long f_connection_id=connectionRepository.find_f_connection_id(f,m);
        return f_connection_id;
    }

//    @GetMapping("/{id}")
//    public String negotiation(@PathVariable("id") Long id,Model model){
//        Long c_id;
//        Long f_c_id;
//        c_id=connectionRepository.findChallenge_idById(id);
//        f_c_id=connectionRepository.findFrinend_Challenge_idById(id);
//
//        List<String> penalty_my =new ArrayList<>();
//        List<String> penalty_friend =new ArrayList<>();
//
//        List<String> status_my =new ArrayList<>();
//        List<String> status_friend =new ArrayList<>();
//
//        List<Long> id_my =new ArrayList<>();
//        List<Long> id_friend =new ArrayList<>();
//
//        penalty_my=penaltyRepository.findpenaltyByChallenge_id(c_id);
//        penalty_friend=penaltyRepository.findpenaltyByChallenge_id(f_c_id);
//        for(String pm : penalty_my){
//            status_my.add(penaltyRepository.findStatusByPenalty(pm,c_id));
//            id_my.add(penaltyRepository.findIdByPenalty(pm,c_id));
//        }
//        for(String pf : penalty_friend){
//            status_friend.add(penaltyRepository.findStatusByPenalty(pf,f_c_id));
//            id_friend.add(penaltyRepository.findIdByPenalty(pf,f_c_id));
//        }
//        model.addAttribute("penalty_my", penalty_my);
//        model.addAttribute("penalty_friend", penalty_friend);
//        model.addAttribute("status_my", status_my);
//        model.addAttribute("status_friend", status_friend);
//        model.addAttribute("id_my", id_my);
//        model.addAttribute("id_friend", id_friend);
//        model.addAttribute("id", id);
//        return "procedure/negotiation";
//    }

}
