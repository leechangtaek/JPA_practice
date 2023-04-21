package taek.jpastudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taek.jpastudy.domain.Board;
import taek.jpastudy.domain.search.BoardSearch;
import taek.jpastudy.repository.BoardRepository;
import taek.jpastudy.repository.BoardRepository2;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository2 boardRepository;

    public List<Board> findBoards(BoardSearch boardSearch){
        return boardRepository.findBoards(boardSearch);
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
