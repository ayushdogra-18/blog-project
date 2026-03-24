package com.ayush.blogproject.service;

import com.ayush.blogproject.dto.PostBlogDto;
import com.ayush.blogproject.model.Comments;
import com.ayush.blogproject.model.Posts;
import com.ayush.blogproject.model.Tags;
import com.ayush.blogproject.model.User;
import com.ayush.blogproject.repository.CommentsRepository;
import com.ayush.blogproject.repository.PostRepository;
import com.ayush.blogproject.repository.TagRepository;
import com.ayush.blogproject.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class BlogServiceImp implements BlogService {

    PostRepository postRepository;
    TagRepository tagRepository;
    CommentsRepository commentsRepository;
    UserRepository userRepository;

    public BlogServiceImp(PostRepository postRepository, TagRepository tagRepository, CommentsRepository commentsRepository,UserRepository userRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.commentsRepository = commentsRepository;
        this.userRepository=userRepository;
    }

    @Override
   // public void savePost(Posts posts, String tagNames, String action) {
    public void savePost(Posts posts, String tagNames, String action, String author) {
        System.out.println("from service: " + action);

        User user = userRepository.findByUsername(author).orElse(null);
        System.out.println("AUTHOR INPUT: " + author);
        System.out.println("USER FOUND: " + user);
        posts.setUser(user);

        String tags[] = tagNames.split(",");
        HashSet<Tags> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagName = tagName.trim().toLowerCase();

            Tags tag = tagRepository.findFirstByName(tagName);

            if (tag == null) {
                Tags newTag = new Tags();
                newTag.setName(tagName);
                tagRepository.save(newTag);
                tagSet.add(newTag);
            } else {
                tagSet.add(tag);
            }
        }
        posts.setTags(tagSet);
        posts.setUser(user);

        if ("publish".equals(action)) {
            posts.setPublished(true);
            System.out.println("here    trueeeeeeeeeeeeeeeeeeeeeee");
            posts.setPublishedAt(LocalDateTime.now());
        } else {
            posts.setPublished(false);
            System.out.println("here    falseeeeeeeeeeeeeeeeee");
            posts.setPublishedAt(LocalDateTime.now());
        }
        if (posts.getContent().length() > 100) {
            posts.setExcerpt(posts.getContent().substring(0, 100));
        } else {
            posts.setExcerpt(posts.getContent());
        }
        postRepository.save(posts);
    }

    @Override
    public Page<Posts> getAllPosts(int page, int limit, String sortBy, String search, String author, Long tagId) {

        String userSearch = (search != null && !search.isBlank()) ? search : null;
        String authorFilter = (author != null && !author.isBlank()) ? author : null;

        // JPQL ke liye — Java field naam
        Sort jpqlSort = "publishedAt".equals(sortBy)
                ? Sort.by(Sort.Direction.DESC, "publishedAt")
                : Sort.by(Sort.Direction.ASC, sortBy.equals("content") ? "content" : sortBy.equals("author") ? "user.username" : "title");

        Pageable jpqlPageable = PageRequest.of(page, limit, jpqlSort);

        // koi filter nahi
        if (userSearch == null && authorFilter == null && tagId == null) {
            return postRepository.findAllPublished(jpqlPageable);
        }

        // Native SQL ke liye — DB column naam
        String nativeSortColumn;
        switch (sortBy) {
            case "publishedAt" -> nativeSortColumn = "published_at";
            case "author" -> nativeSortColumn = "sort_username";
            case "content" -> nativeSortColumn = "content";
            default -> nativeSortColumn = "title";
        }

        Sort nativeSort = "publishedAt".equals(sortBy)
                ? Sort.by(Sort.Direction.DESC, nativeSortColumn)
                : Sort.by(Sort.Direction.ASC, nativeSortColumn);

        Pageable nativePageable = PageRequest.of(page, limit, nativeSort);

        return postRepository.findWithFilters(userSearch, authorFilter, tagId, nativePageable);
    }


    @Override
    public List<String> getAllAuthors() {
        return postRepository.findDistinctAuthors();
    }

    @Override
    public List<Tags> getAllTags() {
        return tagRepository.findAllByOrderByNameAsc();
    }


    @Override
    public Posts getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Posts updateBlog(Posts posts, String tagNames) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        boolean isOwner = posts.getUser().getUsername().equals(userName);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Not allowed!");
        }

        Posts existing = postRepository.findById(posts.getId()).orElseThrow();
        String tags[] = tagNames.split(",");
        HashSet<Tags> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagName = tagName.trim().toLowerCase();

            Tags tag = tagRepository.findFirstByName(tagName);

            if (tag == null) {
                Tags newTag = new Tags();
                newTag.setName(tagName);
                tagRepository.save(newTag);
                tagSet.add(newTag);
            } else {
                tagSet.add(tag);
            }
        }
        posts.setUser(existing.getUser());
        posts.setTags(tagSet);
        posts.setPublished(existing.isPublished());
        posts.setPublishedAt(existing.getPublishedAt());

        if (posts.getContent().length() > 100) {
            posts.setExcerpt(posts.getContent().substring(0, 100));
        } else {
            posts.setExcerpt(posts.getContent());
        }

        return postRepository.save(posts);
    }

    @Transactional
    public void deleteBlog(Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Posts posts=postRepository.findById(id).orElse(null);
        boolean isOwner = posts.getUser().getUsername().equals(userName);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Not allowed!");
        }

        postRepository.deleteById(id);
    }

}
