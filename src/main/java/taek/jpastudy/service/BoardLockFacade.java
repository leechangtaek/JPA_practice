package taek.jpastudy.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BoardLockFacade {

    private final BoardService boardService;
    private final RedissonClient redissonClient;

    public void addLikeCnt(final Long board_id){
        RLock lock = redissonClient.getLock(String.format("addLikeCnt:board:%d",board_id));
        try {
            boolean available = lock.tryLock(10,1, TimeUnit.SECONDS);
            if(!available){
                System.out.println("redisson getLock timeout");
                throw new IllegalArgumentException();
            }
            boardService.updateBoardLikeCnt(board_id);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }
}
