package com.jordyma.blink.recommend;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecommend is a Querydsl query type for Recommend
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommend extends EntityPathBase<Recommend> {

    private static final long serialVersionUID = 1666782579L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecommend recommend = new QRecommend("recommend");

    public final com.jordyma.blink.common.QBaseTimeEntity _super = new com.jordyma.blink.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final com.jordyma.blink.feed.domain.QFeed feed;

    public final StringPath folderName = createString("folderName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> priority = createNumber("priority", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecommend(String variable) {
        this(Recommend.class, forVariable(variable), INITS);
    }

    public QRecommend(Path<Recommend> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecommend(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecommend(PathMetadata metadata, PathInits inits) {
        this(Recommend.class, metadata, inits);
    }

    public QRecommend(Class<? extends Recommend> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feed = inits.isInitialized("feed") ? new com.jordyma.blink.feed.domain.QFeed(forProperty("feed"), inits.get("feed")) : null;
    }

}

