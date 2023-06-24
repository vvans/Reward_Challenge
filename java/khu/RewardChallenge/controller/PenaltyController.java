//package khu.RewardChallenge.controller;
//
//import khu.RewardChallenge.entity.Penalty;
//import khu.RewardChallenge.repository.PenaltyRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//@Controller
//public class PenaltyController {
//    private PenaltyRepository penaltyRepository;
//
//    @GetMapping("challenge/penalty")
//    public String penalty(){
//        System.out.println("in challenge penalty");
//        return "challenge/penalty";
//    }
//    @PostMapping("challenge/penalty")
//    public String penalty_form(@RequestParam("penalties") List<String> penaltyList) {
//
//        System.out.println(penaltyList);
//
//        for (String penalty : penaltyList) {
//            Penalty newPenalty = new Penalty();
//            newPenalty.setPenalty(penalty);
//            newPenalty.setStatus("0");
//            System.out.println(newPenalty);
//
//            penaltyRepository.save(newPenalty);
//        }
//        return "redirect:/main";
//    }
//}
