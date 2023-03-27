package ru.practicum.shareit.booking.repository;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, BookingRepositoryCustom {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    @Query("SELECT new ru.practicum.shareit.item.model.Item(it.id, it.ownerId, it.name, it.description, it.available) "+
            "FROM Item as it " +
            "WHERE it.available = TRUE AND (" +
            "lower(it.name)        LIKE lower(concat('%', :searchQuery, '%')) OR " +
            "lower(it.description) LIKE lower(concat('%', :searchQuery, '%')) " +
            ")"
    )
    List<Item> findAllAvailableAndContainingQueryIgnoreCase(
            @Length(min = 1, message = "Запрос не может быть пустым") String searchQuery
    );

    @Query("SELECT new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.itemId, b.start, b.end, b.bookerId) " +
            "FROM Booking AS b " +
            "WHERE b.itemId = :itemId and b.end = (" +
                "SELECT MAX(b.end) " +
                "FROM Booking AS b " +
                "WHERE b.start < CURRENT_TIMESTAMP " +
                "AND b.itemId = :itemId " +
            ")")
    BookingDto getLastBooking(Long itemId);

    @Query("SELECT new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.itemId, b.start, b.end, b.bookerId) " +
            "FROM Booking AS b " +
            "WHERE b.itemId = :itemId and b.start = (" +
                "SELECT MIN(b.start) " +
                "FROM Booking AS b " +
                "WHERE b.start > CURRENT_TIMESTAMP " +
                "AND b.itemId = :itemId " +
                "AND b.status <> ru.practicum.shareit.booking.model.BookingStatus.REJECTED " +
            ")")
    BookingDto getNextBooking(Long itemId);

    Boolean existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
            Long itemId,
            Long bookerId,
            BookingStatus status,
            LocalDateTime bookedBefore
    );

}
