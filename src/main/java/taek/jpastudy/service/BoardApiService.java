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
import taek.jpastudy.repository.board.BoardRepository3;
import taek.jpastudy.repository.board.dto.PostOneBoardResponse;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardApiService {


    private final BoardRepository3 boardRepository3;

    public List<PostOneBoardResponse> findBoards(){

        return boardRepository3.findBoards();
    }



}
