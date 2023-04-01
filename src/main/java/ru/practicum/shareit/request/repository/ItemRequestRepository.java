package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Optional<List<ItemRequest>> findAllByUserIdOrderByCreatedDesc(Long userId);

    Optional<List<ItemRequest>> findAllByUserIdNotOrderByCreatedDesc(Long userId, Pageable pageable);

}
