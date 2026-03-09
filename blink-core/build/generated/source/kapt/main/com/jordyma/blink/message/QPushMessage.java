package com.jordyma.blink.message;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPushMessage is a Querydsl query type for PushMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPushMessage extends EntityPathBase<PushMessage> {

    private static final long serialVersionUID = 1484349327L;

    public static final QPushMessage pushMessage = new QPushMessage("pushMessage");

    public final StringPath body = createString("body");

    public final StringPath date = createString("date");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public QPushMessage(String variable) {
        super(PushMessage.class, forVariable(variable));
    }

    public QPushMessage(Path<PushMessage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPushMessage(PathMetadata metadata) {
        super(PushMessage.class, metadata);
    }

}

