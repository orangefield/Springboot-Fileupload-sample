package site.metacoding.fileupload;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @GetMapping("/main")
    public String main(Model model) {
        // List<User> users = userRepository.findAll();
        User user = userRepository.findById(1).get();

        model.addAttribute("user", user);

        return "main";
    }

    @PostMapping("/join")
    public String join(JoinDto joinDto) { // 버퍼로 읽는 것 1.json 2.있는 그대로 받고 싶을 때

        UUID uuid = UUID.randomUUID();

        String requestFileName = joinDto.getFile().getOriginalFilename();
        System.out.println("전송받은 파일명:" + requestFileName);

        String imgurl = uuid + "_" + requestFileName;

        try {
            // jar 파일로 구우면 안 돌아감

            // 1. 폴더가 이미 만들어져 있어야 함
            // 2. 리눅스 / 사용, 윈도우 \ 사용
            // 윈도우 : imgUrl = C:/upload/a.png 리눅스 : imgUrl = /upload/a.png
            // 우리는 상대경로 사용할 예정
            Path filePath = Paths.get("src/main/resources/static/upload/" + imgurl);

            System.out.println(filePath);
            Files.write(filePath, joinDto.getFile().getBytes());

            userRepository.save(joinDto.toEntity(imgurl));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "joinComplete"; // ViewResolver
    }
}
