package taek.jpastudy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Board {

    @Id @GeneratedValue
    private Long seq;

    private String title;
    private String content;
    private String writer;
    private int depth;

    private LocalDateTime write_dt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_seq")
    private Board parent; // 부모 게시글

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Board> child = new ArrayList<>();
}
