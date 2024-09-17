package repository;

import entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    // JpaRepository를 상속받아 기본적인 CRUD 기능을 제공합니다.
    // 필요하다면 추가적인 커스텀 쿼리를 여기에 작성할 수 있습니다.
}
