package msu.ghostloyz.socialMedia.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Setter
@Getter
@Builder
@ToString
public class Post {
    private Integer id;
    private Integer authorId;
    private String descrip;
    private Integer likeCnt;
    private Integer dislikeCnt;
    private Integer repostCnt;
    private Integer comCnt;
    private Timestamp crDate;
    private String category;
}
