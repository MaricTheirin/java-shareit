package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Set;

@Validated
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item getItemByIdEqualsAndOwnerIdEquals(Long id, Long ownerId);

    List<Item> findAllByOwnerIdOrderByIdAsc(Long ownerId);

    @Query("SELECT new ru.practicum.shareit.item.model.Item(it.id, it.requestId, u, it.name, it.description, it.available) " +
            "FROM Item AS it " +
            "JOIN User u ON it.owner = u " +
            "WHERE it.available = TRUE AND (" +
                "lower(it.name)        LIKE lower(concat('%', :searchQuery, '%')) OR " +
                "lower(it.description) LIKE lower(concat('%', :searchQuery, '%')) " +
            ")"
    )
    List<Item> findAllAvailableAndContainingQueryIgnoreCase(
            String searchQuery
    );

    Boolean existsItemByIdAndAvailableIsTrue(Long itemId);

    boolean existsItemByIdAndOwnerId(long itemId, long userId);

    @Query("SELECT new ru.practicum.shareit.item.model.Item(i.id, i.requestId, i.owner, i.name, i.description, i.available) " +
            "FROM Item AS i " +
            "WHERE i.requestId in (:itemRequestIds) " +
            "ORDER BY i.id ASC")
    List<Item> findAllItemsByItemRequestIdIn(Set<Long> itemRequestIds);

}
