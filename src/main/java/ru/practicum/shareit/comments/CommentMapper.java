package ru.practicum.shareit.comments;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.NewCommentRequest;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(NewCommentRequest request) {
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
