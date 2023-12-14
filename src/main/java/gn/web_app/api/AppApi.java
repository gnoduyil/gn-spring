package gn.web_app.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
public class AppApi {

    @PostMapping("/get")
    public String get(@RequestBody Map<String, String> req) {
        LocalDateTime now = LocalDateTime.now();

        // 定义日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // 格式化 LocalDateTime 对象
        String formattedDateTime = now.format(formatter);

        var log = formattedDateTime + " " + req.get("key1") + "<taskId>aaabbccdd</taskId>";
        System.out.println(log);

        return log;

    }
}
