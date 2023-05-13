package taek.jpastudy.Service;


import org.aspectj.apache.bcel.classfile.ExceptionTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import taek.jpastudy.domain.Board;
import taek.jpastudy.repository.board.BoardRepository2;
import taek.jpastudy.service.BoardLockFacade;
import taek.jpastudy.service.BoardService;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardLockFacade boardLockFacade;
    @Autowired
    BoardRepository2 boardRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 동시에_100명이_좋아요를_누름()throws Exception{

        Long board_id = 12l;

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for(int i=0; i<100; i++){
            executorService.submit(()->{
               try{
                   boardLockFacade.addLikeCnt(board_id);
               }finally {
                   countDownLatch.countDown();
               }
            });
        }
        countDownLatch.await();
        System.out.println("board_id = " + board_id);
        Board actual = boardRepository.findById(board_id);
        System.out.println("actual.getLike_cnt() = " + actual.getLike_cnt());
    }


    private Board createBoard(String title, String content, String writer, Long like_cnt) {
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(writer);
        board.setLike_cnt(like_cnt);
        em.persist(board);
        return board;
    }

}
