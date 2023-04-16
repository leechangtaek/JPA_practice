package taek.jpastudy.domain.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class BoardForm {

    private Long seq;
    @NotEmpty(message = "제목 항목은 필수입니다.")
    private String title;
    @NotEmpty(message = "작성자 항목은 필수입니다.")
    private String writer;
    private String content;
}

