package awslee.v1.springawslee.config.auth;

import awslee.v1.springawslee.config.auth.dto.OAuthAttributes;
import awslee.v1.springawslee.config.auth.dto.SessionUser;
import awslee.v1.springawslee.domain.user.User;
import awslee.v1.springawslee.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //1
        //네이버인지 구글인지 구분하기 위한 아이디
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //2
        //oath2로그인시 키가 되는 필드 pk와 같은의미
        //구글의 경우 기본값으로 "sub" 제공 네이버 카카오는 기본지원 안함
        //이후 네이버 로그인과 구글 로그인을 동시 지원할때 사용됨
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        //3
        //OAuth를 이용해 가져온 attribute 를 담은 dto
        //네이버 카카오를 통해 가져온 정보도 이 dto사용
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        //4
        //세션에 저장하기 위한 dto
        httpSession.setAttribute("user",new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), attributes.getAttributes(),attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                //user가 있으면 update
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))        
                //없으면 attribute를 엔티티로 변환
                .orElse(attributes.toEntity());                                                                         
        
        //save는 없었을때 저장용 update는 더티체킹으로 업데이트 되는데 save해도 상관없음
        return userRepository.save(user);
    }
}
