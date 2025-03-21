package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.example.expert.domain.common.exception.InvalidRequestException;

public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        // 쿼리 작성
        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()  // fetchJoin을 사용하여 N+1 문제 방지
                .where(todo.id.eq(todoId))
                .fetchOne();

        if (result == null) {
            // Todo가 존재하지 않을 경우 예외를 던짐
            throw new InvalidRequestException("Todo not found");
        }

        return Optional.of(result);
    }
}
