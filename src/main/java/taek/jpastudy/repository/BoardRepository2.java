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

    public List<Board> findBoards(String searchText) {
        return em.createQuery("select b from Board b where b.title=:searchText", Board.class)
                .setParameter("searchText",searchText)
                .getResultList();
    }


    public void save(Board board) {
        if (board.getSeq() == null) {
            em.persist(board);
        } else {
            em.merge(board);
        }
    }

    public Board findById(Long seq){
        return em.find(Board.class,seq);
    }
}
