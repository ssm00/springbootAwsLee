package awslee.v1.springawslee.config.auth.dto;

import awslee.v1.springawslee.domain.user.User;
import lombok.Getter;

import java.io.Serializable;


//인증된 사용자 정보만 필요한 sessionuser
//세션 직렬화 하여서 사용
@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
