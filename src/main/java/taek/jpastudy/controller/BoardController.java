package taek.jpastudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import taek.jpastudy.domain.Board;
import taek.jpastudy.domain.form.BoardForm;
import taek.jpastudy.domain.search.BoardSearch;
import taek.jpastudy.repository.board.dto.PostOneBoardResponse;
import taek.jpastudy.service.BoardService;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/boardList")
    public String board(@ModelAttribute("boardSearch") BoardSearch boardSearch, Model model ,
                        @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
        Page<PostOneBoardResponse> boards = boardService.findBoards(boardSearch,pageable);

        //페이지블럭 처리
        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
        int nowPage = boards.getPageable().getPageNumber() + 1;
        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, boards.getTotalPages()==0?1: boards.getTotalPages());

        model.addAttribute("boards", boards);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("totalPage",boards.getTotalPages());
        return "board/boardList";
    }
    @GetMapping ("/board/boardNew")
    public String boardNewForm(Model model){
        //System.out.println("p_seq = " + p_seq);
        model.addAttribute("boardForm", new BoardForm());
        return "board/boardNew";
    }
    @GetMapping ("/board/reBoardNew")
    public String reBoardNewForm(Model model ,@RequestParam("p_seq") Long p_seq){
        BoardForm boardForm = new BoardForm();
        boardForm.setP_seq(p_seq);
        model.addAttribute("boardForm",boardForm);
        return "board/boardNew";
    }

    @PostMapping("/board/boardNew")
    public String saveBoard(@Valid BoardForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "board/boardNew";
        }
        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setWriter(form.getWriter());
        board.setWrite_dt(LocalDateTime.now());
        if(form.getP_seq() != null){
            Board parent = boardService.findOne(form.getP_seq());
            board.setParent(parent);

        }
        boardService.saveBoard(board);

        return "redirect:/board/boardList";
    }
    @GetMapping("board/{seq}/edit")
    public String updateBoardForm(@PathVariable("seq") Long seq, Model model) {
        Board board = (Board) boardService.findOne(seq);

        BoardForm form = new BoardForm();
        form.setSeq(board.getSeq());
        form.setTitle(board.getTitle());
        form.setWriter(board.getWriter());
        form.setContent(board.getContent());

        model.addAttribute("form", form);
        return "board/boardUpdate";
    }
    @PostMapping("board/{seq}/edit")
    public String updateBoard(@PathVariable("seq") Long seq, @ModelAttribute("form") BoardForm form) {
        //System.out.println("form.getContent() = " + form.getContent());
        boardService.updateBoard(seq, form.getContent(),form.getTitle(),form.getWriter());
        return "redirect:/board/boardList";
    }
    @PostMapping("board/{seq}/delete")
    public String deleteBoard(@PathVariable("seq") Long seq) {
        boardService.deleteBoard(seq);
        return "redirect:/board/boardList";
    }
}
