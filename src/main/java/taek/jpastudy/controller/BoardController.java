package taek.jpastudy.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import taek.jpastudy.domain.Board;
import taek.jpastudy.domain.form.BoardForm;
import taek.jpastudy.domain.search.BoardSearch;
import taek.jpastudy.service.BoardService;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    @GetMapping("/")
    public String board(@ModelAttribute("boardSearch") BoardSearch boardSearch, Model model){
        List<Board> boards = boardService.findBoards(boardSearch);
        model.addAttribute("boards", boards);
        return "board/boardList";
    }
    @GetMapping ("/board/boardNew")
    public String boardNew(Model model){
        model.addAttribute("boardForm", new BoardForm());

        return "board/boardNew";
    }

    @PostMapping("/board/boardNew")
    public String createBoard(@Valid BoardForm form, BindingResult result) {
        if (result.hasErrors()) {

            return "board/boardNew";
        }
        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setWriter(form.getWriter());
        board.setWrite_dt(LocalDateTime.now());
        boardService.saveBoard(board);
        return "redirect:/";
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
        return "redirect:/";
    }
    @PostMapping("board/{seq}/delete")
    public String deleteBoard(@PathVariable("seq") Long seq) {
        boardService.deleteBoard(seq);
        return "redirect:/";
    }
}
