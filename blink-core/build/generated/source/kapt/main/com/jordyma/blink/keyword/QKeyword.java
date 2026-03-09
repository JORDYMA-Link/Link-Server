package com.jordyma.blink.keyword;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKeyword is a Querydsl query type for Keyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKeyword extends EntityPathBase<Keyword> {

    private static final long serialVersionUID = 1217614541L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKeyword keyword = new QKeyword("keyword");

    public final com.jordyma.blink.common.QBaseTimeEntity _super = new com.jordyma.blink.common.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final com.jordyma.blink.feed.domain.QFeed feed;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QKeyword(String variable) {
        this(Keyword.class, forVariable(variable), INITS);
    }

    public QKeyword(Path<Keyword> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKeyword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKeyword(PathMetadata metadata, PathInits inits) {
        this(Keyword.class, metadata, inits);
    }

    public QKeyword(Class<? extends Keyword> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feed = inits.isInitialized("feed") ? new com.jordyma.blink.feed.domain.QFeed(forProperty("feed"), inits.get("feed")) : null;
    }

}

