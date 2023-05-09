package taek.jpastudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taek.jpastudy.domain.Board;
import taek.jpastudy.domain.form.BoardForm;
import taek.jpastudy.domain.search.BoardSearch;
import taek.jpastudy.repository.board.BoardRepository;
import taek.jpastudy.repository.board.BoardRepository2;
import taek.jpastudy.repository.board.dto.PostOneBoardResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository2 boardRepository;

    public Page<Board> findBoards(BoardSearch boardSearch , Pageable pageable){
        Page<Board> result = boardRepository.findBoards(boardSearch,pageable);
//        Page<Board> result = null;
        return result;
    }

    public Board findOne(Long seq) {
        return boardRepository.findById(seq);
    }
    @Transactional
    public Long saveBoard(BoardForm form) {

        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setWriter(form.getWriter());
        board.setWrite_dt(LocalDateTime.now());

        if(form.getP_id() == null){ //신규
            Long g_num = boardRepository.findByGroupNum();
            board.setG_num(g_num+1l);
            board.setG_order(0l);
            board.setStep(0l);
            board.setP_id(0l);
            board.setC_cnt(0l);
            boardRepository.save(board);
        }else{
            Board parentBoard = boardRepository.findById(form.getP_id());
            Long g_order = groupOrderAndUpdate(parentBoard);

            board.setG_num(parentBoard.getG_num());
            board.setG_order(g_order);
            board.setStep(parentBoard.getStep()+1l);
            board.setP_id(form.getP_id());
            board.setC_cnt(0l);
            boardRepository.save(board);

            parentBoard.setC_cnt(parentBoard.getC_cnt()+1l);
            boardRepository.updateChildCnt(parentBoard);
        }



        //boardRepository.save(board);
        return form.getP_id();
    }

    private Long groupOrderAndUpdate(Board parentBoard) {
        Long saveStep = parentBoard.getStep() +1l;
        Long g_order = parentBoard.getG_order(); //board그룹들의 순서
        Long c_cnt = parentBoard.getC_cnt(); //자식들 개수
        Long g_num = parentBoard.getG_num();  //board그룹들 넘버

        Long cCntSum = boardRepository.findBySumChildCnt(g_num); //board그룹넘버가 같은 애들 합

        Long maxStep = boardRepository.findByNvlMaxStep(g_num);  //board그룹너버의 최상

        if(saveStep < maxStep){
            return cCntSum+1l;
        }else if(saveStep == maxStep){
            boardRepository.updateGroupOrderPlus(g_num,g_order+c_cnt);
            return g_order+c_cnt+1l;
        }else if(saveStep > maxStep){
            boardRepository.updateGroupOrderPlus(g_num,g_order);
            return g_order+1l;
        }
        return null;

    }

    @Transactional
    public void updateBoard(Long seq, String content, String title, String writer) {
        Board board = boardRepository.findById(seq);
        board.setContent(content);
        board.setTitle(title);
        board.setWriter(writer);
    }

    @Transactional
    public void deleteBoard(Long seq) {
        Board board = boardRepository.findById(seq);
        boardRepository.deleteBorad(board);
    }


}
