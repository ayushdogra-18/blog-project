package com.ayush.blogproject.service;

import com.ayush.blogproject.dto.PostBlogDto;
import com.ayush.blogproject.model.Posts;
import com.ayush.blogproject.model.Tags;
import com.ayush.blogproject.repository.PostRepository;
import com.ayush.blogproject.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class BlogServiceImp implements BlogService {

    PostRepository postRepository;
    TagRepository tagRepository;

    public BlogServiceImp(PostRepository postRepository,TagRepository tagRepository){
        this.postRepository=postRepository;
        this.tagRepository=tagRepository;
    }

    @Override
    public void savePost(Posts posts,String tagNames,String action) {
        System.out.println("from service: " + action);
        String tags[]=tagNames.split(",");
        HashSet<Tags>tagSet = new HashSet<>();
        for(String tagName:tags){
            tagName = tagName.trim();

             Tags tag=tagRepository.findByName(tagName);

             if(tag==null){
                 Tags newTag=new Tags();
                 newTag.setName(tagName);
                 tagRepository.save(newTag);
                 tagSet.add(newTag);
             }else{
                 tagSet.add(tag);
             }
        }
        posts.setTags(tagSet);
        if("publish".equals(action)){
            posts.setPublished(true);
            posts.setPublishedAt(LocalDateTime.now());
        }else{
            posts.setPublished(false);
            posts.setPublishedAt(null);
        }
        if(posts.getContent().length()>100) {
            posts.setExcerpt(posts.getContent().substring(0, 100));
        }else{
            posts.setExcerpt(posts.getContent());
        }
        postRepository.save(posts);
    }

    @Override
    public List<Posts> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Posts readMore(Integer id) {
        return postRepository.findById(id).orElse(null);
    }


}
