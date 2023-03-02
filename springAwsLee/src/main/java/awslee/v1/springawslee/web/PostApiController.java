package awslee.v1.springawslee.web;

import awslee.v1.springawslee.service.posts.PostsService;
import awslee.v1.springawslee.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostsService postService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postService.save(requestDto);
    }


    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsSaveRequestDto requestDto) {
        return postService.update(id, requestDto);
    }
}
