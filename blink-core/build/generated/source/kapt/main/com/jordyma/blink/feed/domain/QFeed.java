package com.jordyma.blink.feed.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeed is a Querydsl query type for Feed
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeed extends EntityPathBase<Feed> {

    private static final long serialVersionUID = -1771515537L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeed feed = new QFeed("feed");

    public final com.jordyma.blink.common.QBaseTimeEntity _super = new com.jordyma.blink.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final com.jordyma.blink.folder.QFolder folder;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isChecked = createBoolean("isChecked");

    public final BooleanPath isMarked = createBoolean("isMarked");

    public final ListPath<com.jordyma.blink.keyword.Keyword, com.jordyma.blink.keyword.QKeyword> keywords = this.<com.jordyma.blink.keyword.Keyword, com.jordyma.blink.keyword.QKeyword>createList("keywords", com.jordyma.blink.keyword.Keyword.class, com.jordyma.blink.keyword.QKeyword.class, PathInits.DIRECT2);

    public final StringPath memo = createString("memo");

    public final StringPath originUrl = createString("originUrl");

    public final StringPath platform = createString("platform");

    public final ListPath<com.jordyma.blink.recommend.Recommend, com.jordyma.blink.recommend.QRecommend> recommendFolders = this.<com.jordyma.blink.recommend.Recommend, com.jordyma.blink.recommend.QRecommend>createList("recommendFolders", com.jordyma.blink.recommend.Recommend.class, com.jordyma.blink.recommend.QRecommend.class, PathInits.DIRECT2);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final StringPath summary = createString("summary");

    public final StringPath thumbnailImageUrl = createString("thumbnailImageUrl");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFeed(String variable) {
        this(Feed.class, forVariable(variable), INITS);
    }

    public QFeed(Path<Feed> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeed(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeed(PathMetadata metadata, PathInits inits) {
        this(Feed.class, metadata, inits);
    }

    public QFeed(Class<? extends Feed> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.folder = inits.isInitialized("folder") ? new com.jordyma.blink.folder.QFolder(forProperty("folder"), inits.get("folder")) : null;
    }

}

