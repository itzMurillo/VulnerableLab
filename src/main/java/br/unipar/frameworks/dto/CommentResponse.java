package br.unipar.frameworks.dto;

import br.unipar.frameworks.model.Comment;

public record CommentResponse(Long id, String text, Long productId) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getProduct().getId()
        );
    }
}
