package com.jordyma.blink.stats;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserStatistic is a Querydsl query type for UserStatistic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserStatistic extends EntityPathBase<UserStatistic> {

    private static final long serialVersionUID = 22030879L;

    public static final QUserStatistic userStatistic = new QUserStatistic("userStatistic");

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath type = createString("type");

    public QUserStatistic(String variable) {
        super(UserStatistic.class, forVariable(variable));
    }

    public QUserStatistic(Path<UserStatistic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserStatistic(PathMetadata metadata) {
        super(UserStatistic.class, metadata);
    }

}

