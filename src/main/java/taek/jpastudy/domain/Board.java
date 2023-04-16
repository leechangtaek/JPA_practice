package taek.jpastudy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Board {

    @Id @GeneratedValue
    private Long seq;

    private String title;
    private String content;
    private String writer;
    private LocalDateTime write_dt;
}
