package com.jordyma.blink.folder;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFolder is a Querydsl query type for Folder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFolder extends EntityPathBase<Folder> {

    private static final long serialVersionUID = -45600681L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFolder folder = new QFolder("folder");

    public final com.jordyma.blink.common.QBaseTimeEntity _super = new com.jordyma.blink.common.QBaseTimeEntity(this);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isUnclassified = createBoolean("isUnclassified");

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.jordyma.blink.user.QUser user;

    public QFolder(String variable) {
        this(Folder.class, forVariable(variable), INITS);
    }

    public QFolder(Path<Folder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFolder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFolder(PathMetadata metadata, PathInits inits) {
        this(Folder.class, metadata, inits);
    }

    public QFolder(Class<? extends Folder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.jordyma.blink.user.QUser(forProperty("user")) : null;
    }

}

