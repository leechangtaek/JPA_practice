package taek.jpastudy.repository.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taek.jpastudy.domain.Board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardChildrenResponse {
    private Long seq;
    private Long parent_seq;
    private String content;
    private String title;
    private String writer;
    private LocalDateTime write_dt;

    @QueryProjection
    public BoardChildrenResponse(Long seq, Long parent_seq, String content, String title, String writer,  LocalDateTime write_dt) {
        this.seq = seq;
        this.parent_seq = parent_seq;
        this.content = content;
        this.title = title;
        this.writer = writer;
        this.write_dt = write_dt;
    }
}
