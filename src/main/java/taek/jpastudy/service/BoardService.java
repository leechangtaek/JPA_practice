package taek.jpastudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taek.jpastudy.domain.Board;
import taek.jpastudy.domain.search.BoardSearch;
import taek.jpastudy.repository.board.BoardRepository;
import taek.jpastudy.repository.board.BoardRepository2;
import taek.jpastudy.repository.board.dto.PostOneBoardResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository2 boardRepository;

    private final BoardRepository boardRepository2;

    public Page<PostOneBoardResponse> findBoards(BoardSearch boardSearch , Pageable pageable){
        Page<PostOneBoardResponse> result = boardRepository.findBoards(boardSearch,pageable);
        return result;
    }

    public Board findOne(Long seq) {
        return boardRepository.findById(seq);
    }
    @Transactional
    public Long saveBoard(Board board) {

        boardRepository.save(board);
        return board.getSeq();
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
