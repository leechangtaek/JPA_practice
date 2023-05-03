package taek.jpastudy.repository.board;

import com.querydsl.core.types.dsl.BooleanExpression;
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
import taek.jpastudy.repository.board.dto.BoardChildrenResponse;
import taek.jpastudy.repository.board.dto.PostOneBoardResponse;
import taek.jpastudy.repository.board.dto.QBoardChildrenResponse;
import taek.jpastudy.repository.board.dto.QPostOneBoardResponse;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BoardRepository3 {
    private final EntityManager em;

//    public List<PostOneBoardResponse> findBoards(){
//        JPAQueryFactory query = new JPAQueryFactory(em);
//        QBoard board = QBoard.board;
//
//        List<PostOneBoardResponse> boards = query
//                .select(new QPostOneBoardResponse(
//                        board.parent.seq,
//                        board.seq,
//                        board.content,
//                        board.title,
//                        board.writer,
//                        board.write_dt
//                        ))
//                .from(board)
//                .where(board.parent.seq.isNull())
//                .orderBy(board.seq.asc())
//                .fetch();
//
//        List<BoardChildrenResponse> childBoards = query
//                .select(new QBoardChildrenResponse(
//                        board.seq,
//                        board.parent.seq,
//                        board.content,
//                        board.title,
//                        board.writer,
//                        board.write_dt))
//                .from(board)
//                .where(board.parent.seq.isNotNull())
//                .fetch();
//
//        for(PostOneBoardResponse b : boards){
//            System.out.println("b.getSeq() = " + b.getSeq());
//        }
//        for(BoardChildrenResponse c : childBoards){
//            System.out.println("c.getParent_seq() = " + c.getParent_seq());
//        }
//        boards.stream()
//                .forEach(parent -> {
//                    parent.setChildren(childBoards.stream()
//                            .filter(child -> child.getParent_seq().equals(parent.getSeq()))
//                            .collect(Collectors.toList()));
//                });
//
//        for(PostOneBoardResponse b : boards){
//        System.out.println("b.getSeq() = " + b.getSeq());
//
//            for(BoardChildrenResponse c : b.getChildren()){
//                System.out.println("c.getParent_seq() = " + c.getParent_seq());
//            }
//        }
//        return boards;
//
//    }


//    private void printChildren(List<Board> children, int depth) {
//        for (Board child : children) {
//            System.out.println(String.format("%" + (depth * 2) + "s %s", "", child.getTitle()));
//            printChildren(child.getChild(), depth + 1);
//        }
//    }
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
        if (board.getId() == null) {
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
