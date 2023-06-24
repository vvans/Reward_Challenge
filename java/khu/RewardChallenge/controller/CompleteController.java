package khu.RewardChallenge.controller;

import khu.RewardChallenge.entity.Challenge;
import khu.RewardChallenge.entity.Photo;
import khu.RewardChallenge.entity.User;
import khu.RewardChallenge.repository.*;
import khu.RewardChallenge.service.EmailService;
import khu.RewardChallenge.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/complete")
public class CompleteController {
    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private ProgressService progressService;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OnlyChallengeRepository onlyChallengeRepository;

    @GetMapping("/my_challenge/{id}")
    private String my_challenge(@PathVariable("id") Long id, Model model) {
        Long my_challenge_id = connectionRepository.findChallenge_idById(id);

        Long c_id;
        Long f_c_id;
        System.out.println("들어옴~~~~~~~~~~~~~~~");


        c_id = connectionRepository.findChallenge_idById(id);
        f_c_id = connectionRepository.findFrinend_Challenge_idById(id);
        User my_user = userRepository.findByUsername(onlyChallengeRepository.findUsernameBychallenge_id(c_id));
        User friend_user = userRepository.findByUsername(onlyChallengeRepository.findUsernameBychallenge_id(f_c_id));
        model.addAttribute("my_name", my_user.getName());
        model.addAttribute("friend_name", friend_user.getName());
        String my_challenge_name = challengeRepository.findChallengenameBychallengeid(c_id);
        String friend_challenge_name = challengeRepository.findChallengenameBychallengeid(f_c_id);
        Challenge challenge = challengeRepository.findByChallenge_id(id);
        Long day = remain_date(challenge.getFinishdate());
        model.addAttribute("day", day);
        List<String> startdate = parseDate(challenge.getStartdate());
        List<String> finishdate = parseDate(challenge.getFinishdate());

        String start_date = startdate.get(0) + "/" + startdate.get(1) + "/" + startdate.get(2);
        String finish_date = finishdate.get(0) + "/" + finishdate.get(1) + "/" + finishdate.get(2);
        ;
        model.addAttribute("start_date", start_date);
        model.addAttribute("finish_date", finish_date);
        model.addAttribute("my_challenge_name", my_challenge_name);
        model.addAttribute("friend_challenge_name", friend_challenge_name);


        List<String> penalty = penaltyRepository.findpenaltyByChallenge_idandstatus(c_id, "1");
        List<String> reward = penaltyRepository.findpenaltyByChallenge_idandstatus(f_c_id, "1");
        model.addAttribute("penalty", penalty);
        model.addAttribute("reward", reward);

        Long date_size = size_date(challenge.getStartdate(), challenge.getFinishdate());


        List<String> my_filepath = new ArrayList<>();
        List<String> my_photo_date = new ArrayList<>();
        my_filepath = photoRepository.findFilepathById(my_challenge_id);
        my_photo_date = photoRepository.findPhotodateById(my_challenge_id);

        Long len_my = (long) my_filepath.size();
        Long my_percent = Math.round((double) len_my / date_size * 100);
        String word;
        if(my_percent==100){
            word="챌린지 달성에 성공하셨습니다.";
        }
        else{
            word="챌린지 달성에 실패하셨습니다.";
        }
        model.addAttribute("my_filepath", my_filepath);
        model.addAttribute("my_photo_date", my_photo_date);
        model.addAttribute("my_percent",my_percent);
        model.addAttribute("my_result",word);
        System.out.println(my_filepath);

        Long friend_challenge_id = connectionRepository.findFrinend_Challenge_idById(id);
        List<String> friend_filepath = new ArrayList<>();
        List<String> friend_photo_date = new ArrayList<>();
        friend_filepath = photoRepository.findFilepathById(friend_challenge_id);
        friend_photo_date = photoRepository.findPhotodateById(friend_challenge_id);

        Long len_friend = (long) friend_filepath.size();
        Long friend_percent = Math.round((double) len_friend / date_size * 100);
        String f_word;
        if(friend_percent==100){
            f_word="챌린지 달성에 성공하셨습니다.";
        }
        else{
            f_word="챌린지 달성에 실패하셨습니다.";
        }
        model.addAttribute("friend_filepath", friend_filepath);
        model.addAttribute("friend_photo_date", friend_photo_date);
        model.addAttribute("friend_percent",friend_percent);
        model.addAttribute("friend_result",f_word);
        model.addAttribute("id", id);
        System.out.println(friend_filepath);

//        System.out.println(filepath);
        return "procedure/complete";
    }

    private Long remain_date(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime targetTime = LocalDateTime.of(year, month, day, 0, 0, 0);

        Duration duration = Duration.between(currentTime, targetTime);

        long remainingDays = duration.toDays();

        return remainingDays + 1;

    }

    private Long size_date(String startdate, String finishdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(startdate, formatter);
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        LocalDateTime targetTime = LocalDateTime.of(year, month, day, 0, 0, 0);

        LocalDate date2 = LocalDate.parse(finishdate, formatter);
        int year2 = date2.getYear();
        int month2 = date2.getMonthValue();
        int day2 = date2.getDayOfMonth();

        LocalDateTime currentTime = LocalDateTime.of(year2, month2, day2, 0, 0, 0);

        Duration duration = Duration.between(targetTime, currentTime);

        long remainingDays = duration.toDays();

        return remainingDays + 1;
    }

    private List<String> parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        List<String> datelist = new ArrayList<>();
        datelist.add(Integer.toString(year));
        datelist.add(Integer.toString(month));
        datelist.add(Integer.toString(day));

        return datelist;
    }
}
