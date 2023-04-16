package taek.jpastudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taek.jpastudy.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {


}
