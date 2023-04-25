package taek.jpastudy.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import taek.jpastudy.domain.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {


}
