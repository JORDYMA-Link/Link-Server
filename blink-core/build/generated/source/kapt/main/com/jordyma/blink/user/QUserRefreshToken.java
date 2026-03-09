package com.jordyma.blink.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserRefreshToken is a Querydsl query type for UserRefreshToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserRefreshToken extends EntityPathBase<UserRefreshToken> {

    private static final long serialVersionUID = -1688266635L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserRefreshToken userRefreshToken = new QUserRefreshToken("userRefreshToken");

    public final com.jordyma.blink.common.QBaseTimeEntity _super = new com.jordyma.blink.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final DateTimePath<java.time.LocalDateTime> tokenExpirationTime = createDateTime("tokenExpirationTime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public QUserRefreshToken(String variable) {
        this(UserRefreshToken.class, forVariable(variable), INITS);
    }

    public QUserRefreshToken(Path<UserRefreshToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserRefreshToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserRefreshToken(PathMetadata metadata, PathInits inits) {
        this(UserRefreshToken.class, metadata, inits);
    }

    public QUserRefreshToken(Class<? extends UserRefreshToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

