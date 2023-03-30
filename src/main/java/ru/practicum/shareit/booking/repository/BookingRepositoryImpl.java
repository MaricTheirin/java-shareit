package ru.practicum.shareit.booking.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryImpl implements BookingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private enum SearchBy {
        USER_ITEMS,
        USER_BOOKINGS
    }

    @Override
    public List<Booking> findAllByUserItemsAndFilterByState(Long userId, BookingState state) {
        return findBookingsByUserAndState(userId, state, SearchBy.USER_ITEMS);
    }

    @Override
    public List<Booking> findAllByUserBookingsAndFilterByState(Long userId, BookingState state) {
        return findBookingsByUserAndState(userId, state, SearchBy.USER_BOOKINGS);
    }

    private List<Booking> findBookingsByUserAndState (long userId, BookingState state, SearchBy searchBy) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBooking booking = QBooking.booking;

        BooleanExpression bookingsMadeByUser = booking.booker.id.eq(userId);
        BooleanExpression itemsOwnedByUser = booking.item.owner.id.eq(userId);
        BooleanExpression searchFields = (searchBy == SearchBy.USER_BOOKINGS) ? bookingsMadeByUser : itemsOwnedByUser;

        BooleanExpression filterByState = mapBookingStateToPredicate(state);

        return queryFactory
                .selectFrom(booking)
                .where(searchFields.and(filterByState))
                .orderBy(booking.start.desc())
                .fetch();
    }

    private BooleanExpression mapBookingStateToPredicate(BookingState state) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case CURRENT: return QBooking.booking.start.before(now).and(QBooking.booking.end.after(now));
            case PAST: return QBooking.booking.end.before(now);
            case FUTURE: return QBooking.booking.start.after(now);
            case WAITING: return QBooking.booking.status.eq(BookingStatus.WAITING);
            case REJECTED: return QBooking.booking.status.eq(BookingStatus.REJECTED);
            case ALL:
            default:
                return Expressions.TRUE.isTrue();
        }
    }


}
