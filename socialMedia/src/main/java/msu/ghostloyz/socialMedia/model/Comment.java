package msu.ghostloyz.socialMedia.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Builder
@Getter
public class Comment {
    private Integer id;
    private Integer authorId;
    private Integer postId;
    private String text;
    private Timestamp crDate;
}
