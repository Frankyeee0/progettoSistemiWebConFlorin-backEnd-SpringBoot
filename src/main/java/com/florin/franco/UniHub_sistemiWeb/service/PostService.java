package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.api.dto.PostCommentDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.PostCommentRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.PostCreateRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.PostDTO;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Post;
import com.florin.franco.UniHub_sistemiWeb.entity.PostComment;
import com.florin.franco.UniHub_sistemiWeb.entity.PostLike;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.PostCommentRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.PostLikeRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCommentRepository postCommentRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private AppUserRepository userRepository;

    public PostDTO createPost(Long authorId, PostCreateRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new RuntimeException("Contenuto mancante");
        }

        AppUser author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(request.getContent().trim());
        post.setImage(request.getImage());

        Post saved = postRepository.save(post);
        return toDto(saved, authorId);
    }

    public List<PostDTO> getFeed(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Set<AppUser> followed = user.getSeguiti() == null
                ? Collections.emptySet()
                : user.getSeguiti();

        List<AppUser> authors = new ArrayList<>(followed.size() + 1);
        authors.addAll(followed);
        if (!authors.contains(user)) {
            authors.add(user);
        }

        return postRepository.findByAuthorInOrderByCreatedAtDesc(authors)
                .stream()
                .map(post -> toDto(post, userId))
                .toList();
    }

    public PostDTO likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post non trovato"));
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            PostLike like = new PostLike();
            like.setPost(post);
            like.setUser(user);
            postLikeRepository.save(like);
        }

        return toDto(post, userId);
    }

    public PostDTO unlikePost(Long postId, Long userId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post non trovato"));
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        postLikeRepository.deleteByPostIdAndUserId(postId, userId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post non trovato"));
        return toDto(post, userId);
    }

    public PostCommentDTO addComment(Long postId, Long userId, PostCommentRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new RuntimeException("Commento mancante");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post non trovato"));
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setContent(request.getContent().trim());
        PostComment saved = postCommentRepository.save(comment);

        return toCommentDto(saved);
    }

    public void deleteComment(Long postId, Long commentId, Long userId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Commento non trovato"));
        if (!comment.getPost().getId().equals(postId)) {
            throw new RuntimeException("Commento non valido");
        }
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("Permesso negato");
        }
        postCommentRepository.delete(comment);
    }

    private PostDTO toDto(Post post, Long userId) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImage(post.getImage());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setAuthorId(post.getAuthor().getId());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        dto.setAuthorImage(post.getAuthor().getProfileImage());
        dto.setLikeCount(postLikeRepository.countByPost(post));
        dto.setCommentCount(postCommentRepository.findByPostOrderByCreatedAtAsc(post).size());
        dto.setUserLiked(userId != null && postLikeRepository.existsByPostIdAndUserId(post.getId(), userId));
        dto.setComments(
                postCommentRepository.findByPostOrderByCreatedAtAsc(post).stream()
                        .map(this::toCommentDto)
                        .toList()
        );
        return dto;
    }

    private PostCommentDTO toCommentDto(PostComment comment) {
        PostCommentDTO dto = new PostCommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setAuthorUsername(comment.getAuthor().getUsername());
        dto.setAuthorImage(comment.getAuthor().getProfileImage());
        return dto;
    }
}
