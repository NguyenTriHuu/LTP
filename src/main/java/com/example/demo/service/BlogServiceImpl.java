package com.example.demo.service;

import com.amazonaws.util.IOUtils;
import com.example.demo.Entity.BlogEntity;
import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.BlogRequest;
import com.example.demo.dto.BlogResponse;
import com.example.demo.repo.*;
import com.example.demo.s3.BucketName;
import com.example.demo.s3.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogServiceImpl implements BlogService{
    private final BlogRepo blogRepo;
    private final CommentRepo commentRepo;
    private final FileStore fileStore;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final InteractRepo interactRepo;
    @Override
    public void save(BlogRequest blogRequest) {
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle(blogRequest.getTitle());
        blogEntity.setStatus(true);
        blogEntity.setContent(blogRequest.getContent());
        blogEntity.setDateCreate(LocalDateTime.now());
        blogEntity.setShortDescription(blogRequest.getShortDescription());
        String pathThumbnail;
        String thumbnailName;
        if(blogRequest.getThumbnail()!=null){
            Map<String, String> metadata= getStringStringMap(blogRequest.getThumbnail());
            pathThumbnail = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), blogRequest.getTitle());
            thumbnailName =String.format("%s-%s",blogRequest.getThumbnail().getName(),blogRequest.getTitle());
            blogEntity.setLinkThumnail(thumbnailName);
            try{
                fileStore.save(pathThumbnail,thumbnailName,Optional.of(metadata),blogRequest.getThumbnail().getInputStream());
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        CategoryEntity categoryEntity = categoryRepo.findById(blogRequest.getIdCategory()).get();
        blogEntity.setCategory(categoryEntity);
        UserEntity userEntity = userRepo.findByUsername(blogRequest.getUserName()).get();
        blogEntity.setUser(userEntity);

        try{
            blogRepo.save(blogEntity);
        }catch (Exception e){
             throw new IllegalStateException("can't save",e);
        }

    }

    @Override
    public Page<BlogResponse> getAll(int page, int size){
       return  blogRepo.getAllBlog(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreate")));
    }

    @Override
    public Page<BlogResponse> getAllByCategory(int page, int size, Long idCategory) {

        return blogRepo.getAllByCategory(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreate")),idCategory);
    }

    @Override
    public byte[] loadThumnail(Long idBlog) {
        BlogEntity blogEntity = blogRepo.findById(idBlog).get();
        String path=String.format("%s/%s",BucketName.COURSE_IMAGE.getBucketName(),blogEntity.getTitle());
        String key =blogEntity.getLinkThumnail();
        try {
            return  IOUtils.toByteArray(fileStore.getObject(path,key).getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BlogResponse getBlogById(Long id) {
        BlogResponse blogResponse = new BlogResponse();
        BlogEntity blogEntity = blogRepo.findById(id).get();
        blogResponse.setId(blogEntity.getId());
        blogResponse.setTitle((blogEntity.getTitle()));
        blogResponse.setContent(blogEntity.getContent());
        blogResponse.setDateCreate(blogEntity.getDateCreate());
        blogResponse.setIdUser(blogEntity.getUser().getId());
        blogResponse.setNameUser(blogEntity.getUser().getFullname());
        blogResponse.setNumOfComment(commentRepo.countComment(id));
        blogResponse.setNumOfInteract(interactRepo.countInteractionBlog(id));
        int liked = interactRepo.likedBlog(blogEntity.getUser().getId(),id);
        if(liked>0) blogResponse.setLiked(true);
        else blogResponse.setLiked(false);
        return blogResponse;
    }

    @Override
    public Page<BlogResponse> search(int page, int size, String search) {

        return  blogRepo.search(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreate")),"%"+search+"%");
    }

    @Override
    public List<BlogResponse> getAllBlogByUser(String userName) {
        UserEntity user = userRepo.findByUsername(userName).get();
        List<BlogResponse> list =blogRepo.getBlogByUser(user.getId());
        for(BlogResponse blogResponse: list){
            blogResponse.setNumOfComment(commentRepo.countComment(blogResponse.getId()));
            blogResponse.setNumOfInteract(interactRepo.countInteractionBlog(blogResponse.getId()));
        }
        return list;
    }

    @Override
    public void block(Long idBlog, Boolean block) {
        BlogEntity blog = blogRepo.findById(idBlog).get();
        blog.setStatus(block);
        blogRepo.save(blog);
    }

    @Override
    public List<BlogResponse> searchMyBlog(String search, String userName) {
        UserEntity user = userRepo.findByUsername(userName).get();
        List<BlogResponse> list =blogRepo.searchMyBlog(user.getId(),"%"+search+"%");
        for(BlogResponse blogResponse: list){
            blogResponse.setNumOfComment(commentRepo.countComment(blogResponse.getId()));
            blogResponse.setNumOfInteract(interactRepo.countInteractionBlog(blogResponse.getId()));
        }
        return list;
    }

    @Override
    public BlogResponse getById(Long id) {
        return blogRepo.getByIdBlog(id);
    }

    @Override
    public void update(BlogRequest blogRequest, Long idBlog) {
        BlogEntity blogEntity = blogRepo.findById(idBlog).get();
        blogEntity.setTitle(blogRequest.getTitle());
        blogEntity.setContent(blogRequest.getContent());
        blogEntity.setCategory(categoryRepo.findById(blogRequest.getIdCategory()).get());
        blogEntity.setModifiedtime(new Date(Timestamp.valueOf(LocalDateTime.now()).getTime()));
        String pathThumdnail;
        String thumdnailName;
        if(blogRequest.getThumbnail()!=null){
                Map<String, String> metadata= getStringStringMap(blogRequest.getThumbnail());
            pathThumdnail = String.format("%s/%s", BucketName.COURSE_IMAGE.getBucketName(), blogRequest.getTitle());
            thumdnailName =String.format("%s-%s",blogRequest.getThumbnail().getName(),blogRequest.getTitle());
                try{
                    if(blogEntity.getLinkThumnail()!= null)
                        fileStore.deleteFile( BucketName.COURSE_IMAGE.getBucketName(),blogEntity.getLinkThumnail());
                    fileStore.save(pathThumdnail,thumdnailName,Optional.of(metadata),blogRequest.getThumbnail().getInputStream());
                    blogEntity.setLinkThumnail(thumdnailName);
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
        blogRepo.save(blogEntity);
    }

    private static Map<String, String> getStringStringMap(MultipartFile file) {
        Map<String, String> metadata= new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));
        return metadata;
    }

}
