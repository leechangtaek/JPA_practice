package taek.jpastudy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Board {

    @Id @GeneratedValue
    private Long id;

    @NotEmpty
    private String title;
    private String content;
    private String writer;
    private Long g_num; //borad를 그룹으로 묶기
    private Long g_order;  //board그룹들의 순서
    private Long step;  //board의 계층
    private Long p_id; //부모board의 id
    private Long c_cnt; // 자식board의 수
    private Long like_cnt; // 좋아요 수

    private LocalDateTime write_dt;


}
