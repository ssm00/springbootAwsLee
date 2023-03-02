package awslee.v1.springawslee.service.posts;

import awslee.v1.springawslee.domain.posts.Posts;
import awslee.v1.springawslee.domain.posts.PostsRepository;
import awslee.v1.springawslee.web.dto.PostsResponseDto;
import awslee.v1.springawslee.web.dto.PostsSaveRequestDto;
import awslee.v1.springawslee.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id : " + id));
        //더티 체킹
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return posts.getId();
    }

    public PostsResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id : " + id));
        return new PostsResponseDto(posts);
    }

}
