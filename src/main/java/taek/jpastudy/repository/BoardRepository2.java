package taek.jpastudy.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import taek.jpastudy.domain.Board;
import taek.jpastudy.domain.search.BoardSearch;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository2 {
    private final EntityManager em;

    public List<Board> findBoards(BoardSearch boardSearch) {
        return em.createQuery("select b from Board b", Board.class)
                .getResultList();
    }
}
