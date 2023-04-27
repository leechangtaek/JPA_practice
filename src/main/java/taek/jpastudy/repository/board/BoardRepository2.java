package taek.jpastudy.repository.board;

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
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;

@Repository
@RequiredArgsConstructor
public class BoardRepository2 {
    private final EntityManager em;

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
    public Page<PostOneBoardResponse> findBoards(BoardSearch boardSearch, Pageable pageable){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QBoard board = QBoard.board;
        long totalCnt = query.selectFrom(board).where(boardLike(boardSearch)).fetch().size();
        /*기존 페이징 처리 로직*/
//        List<Board> result =  query
//                                    .select(board)
//                                    .from(board)
//                                    .where(boardLike(boardSearch))
//                                    .offset(pageable.getOffset())
//                                    .limit(pageable.getPageSize())
//                                    .orderBy(board.seq.desc())
//                                    .fetch();
        /*자식이 하나인 게시글 리스트처*/
        List<PostOneBoardResponse> boards =  query
                                    .select(new QPostOneBoardResponse(
                                            board.seq,
                                            board.content,
                                            board.title,
                                            board.writer,
                                            board.write_dt
                                    )).from(board)
                                    .where(board.parent.isNull() , boardLike(boardSearch))
                                    .offset(pageable.getOffset())
                                    .limit(pageable.getPageSize())
                                    .orderBy(board.seq.asc())
                                    .fetch();
        List<BoardChildrenResponse> childBoards =  query
                .select(new QBoardChildrenResponse(
                        board.seq,
                        board.parent.seq,
                        board.content,
                        board.title,
                        board.writer,
                        board.write_dt
                )).from(board)
                .where(board.parent.seq.isNotNull())
                .fetch();

        boards.stream()
                .forEach(parent -> {
                    parent.setChildren(childBoards.stream()
                            .filter(child -> child.getParent_seq().equals(parent.getSeq()))
                            .collect(Collectors.toList()));
                });


//        List<Board> result = query
//                .selectFrom(board)
//                .leftJoin(board.child,).fetchJoin()
//                .where(board.parent.isNull())
//                .orderBy(board.seq.desc())
//                .fetch();
//
//
//        for (Board board2 : result) {
//            System.out.println(board2.getTitle());
//            //printChildren(board2.getChild(), 1);
//            int depth = 1;
//            for (Board child : board2.getChild()) {
//                System.out.println(String.format("%" + (depth * 2) + "s %s", "", child.getTitle()));
//                printChildren(child.getChild(), depth + 1);
//            }
//        }

//        List<BoardDto> result = query
//                .select(Projections.constructor(BoardDto.class,
//                        board.seq,
//                        board.title,
//                        board.content,
//                        board.writer,
//                        board.write_dt,
//                        board.parent.seq,
//                        Expressions.as(select(
//                                                Expressions.constant(1L)).from(board)
//                                        .where(board.parent.seq.eq(board.seq))
//                                        .exists(),
//                                "hasChild"
//                        ),
//                        new CaseBuilder().when(board.parent.isNull())
//                                .then(0)
//                                .otherwise(board.parent.depth.add(1))
//                                .as("depth")
//                ))
//                .from(board)
//                .leftJoin(board.parent)
//                .orderBy(board.seq.asc())
//                .fetch();
//
//        for(BoardDto b : result){
//            System.out.println(b.getDepth());
//        }

        return new PageImpl<>(boards, pageable, totalCnt);

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
