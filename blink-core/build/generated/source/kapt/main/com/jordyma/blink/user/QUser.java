package com.jordyma.blink.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1335380233L;

    public static final QUser user = new QUser("user");

    public final com.jordyma.blink.common.QBaseTimeEntity _super = new com.jordyma.blink.common.QBaseTimeEntity(this);

    public final StringPath aosPushToken = createString("aosPushToken");

    public final StringPath birthYear = createString("birthYear");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath iosPushToken = createString("iosPushToken");

    public final StringPath jobField = createString("jobField");

    public final EnumPath<LanguageType> language = createEnum("language", LanguageType.class);

    public final StringPath nickname = createString("nickname");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final EnumPath<SocialType> socialType = createEnum("socialType", SocialType.class);

    public final StringPath socialUserId = createString("socialUserId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

