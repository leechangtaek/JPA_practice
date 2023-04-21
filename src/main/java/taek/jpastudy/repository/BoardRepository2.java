package taek.jpastudy.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import taek.jpastudy.domain.Board;
import taek.jpastudy.domain.search.BoardSearch;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository2 {
    private final EntityManager em;

    public List<Board> findBoards(BoardSearch boardSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Board> cq = cb.createQuery(Board.class);
        Root<Board> o = cq.from(Board.class);

        List<Predicate> criteria = new ArrayList<>();

        System.out.println("boardSearch = " + boardSearch.getSearchText());
        if (StringUtils.hasText(boardSearch.getSearchText())) {
            Predicate text =
                    cb.like(o.<String>get(boardSearch.getSearchStatus()), "%" + boardSearch.getSearchText() + "%");
            criteria.add(text);

        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Board> query = em.createQuery(cq).setMaxResults(1000);


        return query.getResultList();
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

    public void deleteBorad(Board board) {
        em.remove(board);
    }
}
