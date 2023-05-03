package taek.jpastudy.repository.board;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import taek.jpastudy.domain.Board;
import taek.jpastudy.domain.QBoard;
import taek.jpastudy.domain.search.BoardSearch;
import taek.jpastudy.repository.board.dto.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;

@Repository
@RequiredArgsConstructor
public class BoardRepository2 {
    private final EntityManager em;
    JPAQueryFactory query = new JPAQueryFactory(em);
    QBoard board = QBoard.board;

    /*criteria로 조회로직*/
//    public List<Board> findBoards(BoardSearch boardSearch, Pageable pageable) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Board> cq = cb.createQuery(Board.class);
//        Root<Board> o = cq.from(Board.class);
//
//        List<Predicate> criteria = new ArrayList<>();
//
//        System.out.println("pageable = " + pageable.getSort());
//        System.out.println("pageable = " + pageable.getPageNumber());
//        System.out.println("pageable = " + pageable.getPageSize());
////        System.out.println("boardSearch = " + boardSearch.getSearchText());
//        if (StringUtils.hasText(boardSearch.getSearchText())) {
//            Predicate text =
//                    cb.like(o.<String>get(boardSearch.getSearchStatus()), "%" + boardSearch.getSearchText() + "%");
//            criteria.add(text);
//
//        }
//        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
//        TypedQuery<Board> query = em.createQuery(cq).setMaxResults(1000);
//
//
//        return query.getResultList();
//    }
    public Page<Board> findBoards(BoardSearch boardSearch, Pageable pageable){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QBoard board = QBoard.board;
//        long totalCnt = query.selectFrom(board).where(boardLike(boardSearch)).fetch().size();
        /*기존 페이징 처리 로직*/
//        List<Board> result =  query
//                                    .select(board)
//                                    .from(board)
//                                    .where(boardLike(boardSearch))
//                                    .offset(pageable.getOffset())
//                                    .limit(pageable.getPageSize())
//                                    .orderBy(board.id.desc())
//                                    .fetch();

        List<Board> boardList = query
                .selectFrom(board)
                .where(boardLike(boardSearch))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.g_num.desc(),board.g_order.asc())
                .fetch();
        long totalCnt = boardList.size();





        return new PageImpl<>(boardList, pageable, totalCnt);

    }





    private void printChildren(List<Board> children, int depth) {
        for (Board child : children) {
            System.out.println(String.format("%" + (depth * 2) + "s %s", "", child.getTitle()));
        }
    }
    private BooleanExpression boardLike(BoardSearch boardSearch){
        if(!StringUtils.hasText(boardSearch.getSearchText())){
            return null;
        }
        String searchStatus = boardSearch.getSearchStatus();

        if(searchStatus.equals("title")){
            return QBoard.board.title.like("%"+boardSearch.getSearchText()+"%");
        }else{
            return QBoard.board.writer.like("%"+boardSearch.getSearchText()+"%");
        }
    }

    public void save(Board board) {
        em.persist(board);
    }

    public Board findById(Long id){
        return em.find(Board.class,id);
    }

    public void deleteBorad(Board board) {
        em.remove(board);
    }

    public Long findByGroupNum(Long id) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QBoard board = QBoard.board;
        return query.select(board.g_num.max().coalesce(0L)).from(board).where(board.id.eq(id)).fetchOne();
    }


    public void updateChildCnt(Board parentBoard) {
        em.merge(parentBoard);

    }

    public Long findBySumChildCnt(Long g_num) {
        return query.select(board.c_cnt.sum()).from(board).where(board.g_num.eq(g_num)).fetchOne();
    }

    public Long findByNvlMaxStep(Long g_num) {
        return query.select(board.g_num.max()).from(board).where(board.g_num.eq(g_num)).fetchOne();
    }

    public void updateGroupOrderPlus(Long g_num, Long num) {
        query.update(board).set(board.g_num,g_num+1).where(board.g_num.eq(g_num).and(board.g_order.gt(num)));

    }
}
