package taek.jpastudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import taek.jpastudy.domain.Board;
import taek.jpastudy.domain.form.BoardForm;
import taek.jpastudy.domain.search.BoardSearch;
import taek.jpastudy.repository.board.dto.PostOneBoardResponse;
import taek.jpastudy.service.BoardApiService;
import taek.jpastudy.service.BoardService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    private final BoardApiService boardApiService;

//    @GetMapping("/board/api/boardList")
//    public List<PostOneBoardResponse> board(){
//        return boardApiService.findBoards();
//    }

}
