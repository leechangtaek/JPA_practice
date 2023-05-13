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
import taek.jpastudy.service.BoardApiService;
import taek.jpastudy.service.BoardService;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final BoardApiService boardApiService;

    @GetMapping("/board/boardList")
    public String board(@ModelAttribute("boardSearch") BoardSearch boardSearch, Model model ,
                        @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
        Page<Board> boards = boardService.findBoards(boardSearch,pageable);

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
        //System.out.println("p_id = " + p_id);
        model.addAttribute("boardForm", new BoardForm());
        return "board/boardNew";
    }
    @GetMapping ("/board/reBoardNew")
    public String reBoardNewForm(Model model ,@RequestParam("p_id") Long p_id){
        BoardForm boardForm = new BoardForm();
        boardForm.setP_id(p_id);
        model.addAttribute("boardForm",boardForm);
        return "board/boardNew";
    }

    @PostMapping("/board/boardNew")
    public String saveBoard(@Valid BoardForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "board/boardNew";
        }
        boardService.saveBoard(form);

        return "redirect:/board/boardList";
    }
    @GetMapping("board/{id}/edit")
    public String updateBoardForm(@PathVariable("id") Long id, Model model) {
        Board board = (Board) boardService.findOne(id);

        BoardForm form = new BoardForm();
        form.setId(board.getId());
        form.setTitle(board.getTitle());
        form.setWriter(board.getWriter());
        form.setContent(board.getContent());

        model.addAttribute("form", form);
        return "board/boardUpdate";
    }
    @PostMapping("board/{id}/edit")
    public String updateBoard(@PathVariable("id") Long id, @ModelAttribute("form") BoardForm form) {
        //System.out.println("form.getContent() = " + form.getContent());
        boardService.updateBoard(id, form.getContent(),form.getTitle(),form.getWriter());
        return "redirect:/board/boardList";
    }
    @PostMapping("board/{id}/addLikeCnt")
    public String updateBoardLikeCnt(@PathVariable("id") Long id, @ModelAttribute("form") BoardForm form) {
        boardService.updateBoardLikeCnt(id);
        return "redirect:/board/boardList";
    }
    @PostMapping("board/{id}/delete")
    public String deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return "redirect:/board/boardList";
    }
}
