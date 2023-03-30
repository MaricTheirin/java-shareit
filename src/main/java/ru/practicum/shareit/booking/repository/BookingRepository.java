package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.model.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingRepository extends
        JpaRepository<Booking, Long>, BookingRepositoryCustom, QuerydslPredicateExecutor<Booking> {

    @Query("SELECT new ru.practicum.shareit.booking.model.BookingShort(b.id, b.item.id, b.booker, b.start, b.end) " +
            "FROM Booking AS b " +
            "WHERE b.item.id IN (:itemIds) " +
                "AND b.start < CURRENT_TIMESTAMP " +
                "AND b.status = :statusToInclude " +
            "ORDER BY b.end DESC"
    )
    List<BookingShort> getLastBookingsForItems(Set<Long> itemIds, BookingStatus statusToInclude);

    @Query("SELECT new ru.practicum.shareit.booking.model.BookingShort(b.id, b.item.id, b.booker, b.start, b.end) " +
            "FROM Booking AS b " +
            "WHERE b.item.id IN (:itemIds) " +
                "AND b.start > CURRENT_TIMESTAMP " +
                "AND b.status = :statusToInclude " +
            "ORDER BY b.start ASC"
    )
    List<BookingShort> getNextBookingsForItems(Set<Long> itemIds, BookingStatus statusToInclude);

    @Query("SELECT new ru.practicum.shareit.booking.model.BookingShort(b.id, b.item.id, b.booker, b.start, b.end) " +
            "FROM Booking AS b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start <= CURRENT_TIMESTAMP " +
            "AND b.status = :statusToInclude " +
            "ORDER BY b.end DESC"
    )
    List<BookingShort> getLastBookingsForItem(long itemId, BookingStatus statusToInclude);

    @Query("SELECT new ru.practicum.shareit.booking.model.BookingShort(b.id, b.item.id, b.booker, b.start, b.end) " +
            "FROM Booking AS b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "AND b.status = :statusToInclude " +
            "ORDER BY b.start ASC"
    )
    List<BookingShort> getNextBookingsForItem(long itemId, BookingStatus statusToInclude);

    Boolean existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
            Long itemId,
            Long bookerId,
            BookingStatus status,
            LocalDateTime bookedBefore
    );

}
