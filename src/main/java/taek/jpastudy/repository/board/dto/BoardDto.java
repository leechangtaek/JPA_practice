package taek.jpastudy.repository.board.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardDto {

    private Long seq;
    private String title;
    private String content;
    private String writer;

    private LocalDateTime write_dt;

    private Long parent_seq;
    private boolean hasChild;
    private Long depth;

    public BoardDto(Long seq, String title, String content, String writer, LocalDateTime write_dt, Long parent_seq, boolean hasChild, Long depth) {
        this.seq = seq;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.write_dt = write_dt;
        this.parent_seq = parent_seq;
        this.hasChild = hasChild;
        this.depth = depth;
    }
}