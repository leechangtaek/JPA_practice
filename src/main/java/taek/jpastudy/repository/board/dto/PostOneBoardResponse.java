package taek.jpastudy.repository.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostOneBoardResponse {
    private Long seq;
    private String content;
    private String title;
    private String writer;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime write_dt;


    private List<BoardChildrenResponse> children = new ArrayList<>();

    @QueryProjection
    public PostOneBoardResponse(Long seq, String content, String title, String writer, LocalDateTime write_dt) {
        this.seq = seq;
        this.content = content;
        this.title = title;
        this.writer = writer;
        this.write_dt = write_dt;
    }
}
