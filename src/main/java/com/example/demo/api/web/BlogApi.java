package com.example.demo.api.web;

import com.example.demo.dto.BlogRequest;
import com.example.demo.dto.BlogResponse;
import com.example.demo.service.BlogService;
import com.example.demo.service.InteractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class BlogApi {
    private final BlogService blogService;
    private final InteractService interactService;
    @GetMapping(value ="/blog/get-all")
    public Page<BlogResponse> getAll(@RequestParam Integer page, @RequestParam Integer size){
        //int page2 = Integer.parseInt(page);
       // int size2 =Integer.parseInt(size);
        return blogService.getAll(page,size);
    }

    @GetMapping(value ="/blog/get-all-category/{id}")
    public Page<BlogResponse> getAllByCategory(@RequestParam Integer page, @RequestParam Integer size , @PathVariable("id") Long id){
        //int page2 = Integer.parseInt(page);
        // int size2 =Integer.parseInt(size);
        return blogService.getAllByCategory(page,size,id);
    }

    @PostMapping(value = "/blog/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void save(@ModelAttribute BlogRequest blogRequest){
         blogService.save(blogRequest);
    }

    @PostMapping(value = "/blog/update/{idBlog}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@ModelAttribute BlogRequest blogRequest , @PathVariable("idBlog") Long idBlog){
        blogService.update(blogRequest,idBlog);
    }

    @GetMapping(value = "/blog/{id}/thumnail")
    public byte[] loadThumnail(@PathVariable("id") Long id){
       return blogService.loadThumnail(id);
    }


    @GetMapping(value = "/blog/{id}/read")
    public ResponseEntity<BlogResponse> read(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(blogService.getBlogById(id));
    }

    @PostMapping(value = "/blog/like/{idBlog}/{idUser}")
    public void like(@PathVariable("idBlog") Long idBlog , @PathVariable("idUser") Long idUser){
        interactService.likeBlog(idBlog,idUser);
    }

    @PostMapping(value = "/blog/dis-like/{idBlog}/{idUser}")
    public void disLike(@PathVariable("idBlog") Long idBlog , @PathVariable("idUser") Long idUser){
            interactService.dislikeBlog(idBlog,idUser);
    }

    @GetMapping(value ="/blog/search")
    public Page<BlogResponse> getAll(@RequestParam Integer page, @RequestParam Integer size,@RequestParam String search){
        return blogService.search(page,size,search);
    }

    @GetMapping(value ="/blog/search/{userName}")
    public ResponseEntity<List<BlogResponse>> searchMyBlog(@PathVariable("userName") String userName, @RequestParam String search){
        return ResponseEntity.ok().body(blogService.searchMyBlog(search,userName));
    }
    @GetMapping(value = "/blog/my-blog/{userName}")
    public ResponseEntity<List<BlogResponse>> getAllBlogByUser(@PathVariable("userName") String userName){
        return ResponseEntity.ok().body(blogService.getAllBlogByUser(userName));
    }

    @PostMapping(value = "/blog/block/{idBlog}/{value}")
    public void block(@PathVariable("idBlog") Long idBlog,@PathVariable("value") Boolean block){
        blogService.block(idBlog,block);
    }

    @GetMapping(value = "/blog/get/{idBlog}")
    public ResponseEntity<BlogResponse> getBlogById(@PathVariable("idBlog") Long idBlog){
        return ResponseEntity.ok().body(blogService.getById(idBlog));
    }
}
