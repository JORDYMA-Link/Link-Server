package com.jordyma.blink.common.system;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommonParameter is a Querydsl query type for CommonParameter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommonParameter extends EntityPathBase<CommonParameter> {

    private static final long serialVersionUID = -1146816565L;

    public static final QCommonParameter commonParameter = new QCommonParameter("commonParameter");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath paramCode = createString("paramCode");

    public final StringPath paramValue = createString("paramValue");

    public final DatePath<java.time.LocalDate> validEndDate = createDate("validEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> validStartDate = createDate("validStartDate", java.time.LocalDate.class);

    public QCommonParameter(String variable) {
        super(CommonParameter.class, forVariable(variable));
    }

    public QCommonParameter(Path<CommonParameter> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommonParameter(PathMetadata metadata) {
        super(CommonParameter.class, metadata);
    }

}

