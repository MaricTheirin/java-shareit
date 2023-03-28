package ru.practicum.shareit.booking.repository;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, BookingRepositoryCustom {

    @Query("SELECT new ru.practicum.shareit.item.model.Item(it.id, u, it.name, it.description, it.available) " +
            "FROM Item AS it " +
            "JOIN User AS u ON it.owner = u " +
            "WHERE it.available = TRUE AND (" +
            "lower(it.name)        LIKE lower(concat('%', :searchQuery, '%')) OR " +
            "lower(it.description) LIKE lower(concat('%', :searchQuery, '%')) " +
            ")"
    )
    List<Item> findAllAvailableAndContainingQueryIgnoreCase(
            @Length(min = 1, message = "Запрос не может быть пустым") String searchQuery
    );

    @Query("SELECT new ru.practicum.shareit.booking.dto.BookingShortResponseDto(b.id, b.booker.id, b.start, b.end) " +
            "FROM Booking AS b " +
            "JOIN Item AS i ON b.item = i " +
            "JOIN User AS u ON b.booker = u " +
            "WHERE i.id = :itemId and b.end = (" +
                "SELECT MAX(b.end) " +
                "FROM Booking AS b " +
                "WHERE b.start < CURRENT_TIMESTAMP " +
                "AND b.item.id = :itemId " +
            ")")
    BookingShortResponseDto getLastBooking(Long itemId);

    @Query("SELECT new ru.practicum.shareit.booking.dto.BookingShortResponseDto(b.id, b.booker.id, b.start, b.end) " +
            "FROM Booking AS b " +
            "WHERE b.item.id = :itemId and b.start = (" +
                "SELECT MIN(b.start) " +
                "FROM Booking AS b " +
                "WHERE b.start > CURRENT_TIMESTAMP " +
                "AND b.item.id = :itemId " +
                "AND b.status <> ru.practicum.shareit.booking.model.BookingStatus.REJECTED " +
            ")")
    BookingShortResponseDto getNextBooking(Long itemId);

    Boolean existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(
            Long itemId,
            Long bookerId,
            BookingStatus status,
            LocalDateTime bookedBefore
    );

}
