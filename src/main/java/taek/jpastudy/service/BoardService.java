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
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> findBoards(BoardSearch boardSearch){
        return boardRepository.findAll();
    }
    @Transactional
    public Long join(Board board) {

        boardRepository.save(board);
        return board.getSeq();
    }

    public Board findOne(Long seq) {
        return boardRepository.findById(seq).get();
    }
}
