package khu.RewardChallenge.service;

import khu.RewardChallenge.entity.Connection;
import khu.RewardChallenge.entity.Photo;
import khu.RewardChallenge.repository.ConnectionRepository;
import khu.RewardChallenge.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class ProgressService {
    @Autowired
    private PhotoRepository photoRepository;


    public void upload(Long challenge_id, MultipartFile file) throws Exception{

        Photo photo = new Photo();
        System.out.print("--------------챌린지 이름");
        System.out.println(challenge_id);
        String projectPath = System.getProperty("user.dir")+"/src/main/resources/static/files";
        UUID uuid=UUID.randomUUID();
        String fileName= uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath,fileName);

        file.transferTo(saveFile);

        photo.setChallenge_id(challenge_id);
        photo.setPhoto_date(LocalDate.now().toString());
        photo.setFilepath("/files/"+fileName);

        photoRepository.save(photo);
    }
}
