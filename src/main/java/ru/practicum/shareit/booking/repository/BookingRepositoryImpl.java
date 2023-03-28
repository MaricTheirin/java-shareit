package ru.practicum.shareit.booking.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookingRepositoryImpl implements BookingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingRepositoryImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    private enum SearchBy {
        USER_ITEMS,
        USER_BOOKINGS
    }

    @Override
    public List<Booking> findAllByUserItemsAndFilterByState(
            Long userId,
            BookingState state
    ) {
        return findAndFilter(userId, state, SearchBy.USER_ITEMS);
    }

    @Override
    public List<Booking> findAllByUserBookingsAndFilterByState(
            Long userId,
            BookingState state
    ) {
        return findAndFilter(userId, state, SearchBy.USER_BOOKINGS);
    }


    private List<Booking> findAndFilter(Long userId, BookingState state, SearchBy searchBy) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cq = cb.createQuery(Booking.class);

        Root<Booking> root = cq.from(Booking.class);
        List<Predicate> predicates = mapStateToPredicates(cb, root, state);

        if (searchBy.equals(SearchBy.USER_BOOKINGS)) {
            predicates.add(cb.equal(root.get("booker").get("id"), userId));
        } else {
            //В дальнейшем костыль можно будет заменить на Join
            List<Long> userItems =
                    itemRepository.findAllByOwnerIdOrderByIdAsc(userId).stream().map(Item::getId).collect(Collectors.toList());
            predicates.add(root.get("item").get("id").in(userItems));
        }

        cq.select(root)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(cb.desc(root.get("start")))
        ;
        return entityManager.createQuery(cq).getResultList();
    }

    private List<Predicate> mapStateToPredicates(CriteriaBuilder cb, Root<Booking> root, BookingState state) {
        List<Predicate> predicates = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                break;
            case CURRENT:
                predicates.add(cb.lessThan(root.get("start"), now));
                predicates.add(cb.greaterThan(root.get("end"), now));
                break;
            case PAST:
                predicates.add(cb.lessThan(root.get("end"), now));
                break;
            case FUTURE:
                predicates.add(cb.greaterThan(root.get("start"), now));
                break;
            case WAITING:
                predicates.add(cb.equal(root.get("status"), BookingStatus.WAITING));
                break;
            case REJECTED:
                predicates.add(cb.equal(root.get("status"), BookingStatus.REJECTED));
                break;
        }
        return predicates;
    }

}
