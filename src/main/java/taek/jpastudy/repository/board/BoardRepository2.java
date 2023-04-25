package taek.jpastudy.repository.board;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import taek.jpastudy.repository.board.dto.BoardDto;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository2 {
    private final EntityManager em;

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
    public Page<BoardDto> findBoards(BoardSearch boardSearch, Pageable pageable){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QBoard board = QBoard.board;
        long totalCnt = query.selectFrom(board).where(boardLike(boardSearch)).fetch().size();
//        List<Board> result =  query
//                                    .select(board)
//                                    .from(board)
//                                    .where(boardLike(boardSearch))
//                                    .offset(pageable.getOffset())
//                                    .limit(pageable.getPageSize())
//                                    .orderBy(board.seq.desc())
//                                    .fetch();
//        List<Board> result =  query
//                                    .selectFrom(board).leftJoin(board.child).fetchJoin()
//                                    .where(board.parent.isNull())
//                                    .orderBy(board.seq.desc())
//                                    .fetch();
//
//        for (Board board2 : result) {
//            System.out.println(board2.getTitle());
//            printChildren(board2.getChild(), 1);
//        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@");
        List<BoardDto> result = query
                .select(Projections.constructor(BoardDto.class,
                        board.seq,
                        board.title,
                        board.content,
                        board.writer,
                        board.write_dt,
                        board.parent.seq,
                        Expressions.as(JPAExpressions.select(
                                                Expressions.constant(1L)).from(board)
                                        .where(board.parent.seq.eq(board.seq))
                                        .exists(),
                                "hasChild"
                        ),
                        Expressions.constant(0L) // depth는 0으로 시작
                ))
                .from(board)
                .leftJoin(board.parent)
                .orderBy(board.seq.desc())
                .fetch();
        System.out.println("####################################");
        for (BoardDto board3 : result) {
            System.out.println(board3.getTitle());
            System.out.println(board3.getDepth());
        }

        return new PageImpl<>(result, pageable, totalCnt);

    }
    private void printChildren(List<Board> children, int depth) {
        for (Board child : children) {
            System.out.println(String.format("%" + (depth * 2) + "s %s", "", child.getTitle()));
            printChildren(child.getChild(), depth + 1);
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
