package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Optional<List<ItemRequest>> findAllByUserIdOrderByCreatedDesc(Long userId);

    Optional<List<ItemRequest>> findAllByUserIdNotOrderByCreatedDesc(Long userId, Pageable pageable);

    @Query("SELECT new ru.practicum.shareit.item.model.Item(i.id, i.requestId, i.owner, i.name, i.description, i.available) " +
            "FROM Item AS i " +
            "WHERE i.requestId in (:itemRequestIds) " +
            "ORDER BY i.id ASC")
    List<Item> findAllByItemRequestIdIn(Set<Long> itemRequestIds);

}
