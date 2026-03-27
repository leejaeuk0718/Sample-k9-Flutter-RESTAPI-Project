package com.busanit501.api5012.domain.library;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventApplication is a Querydsl query type for EventApplication
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventApplication extends EntityPathBase<EventApplication> {

    private static final long serialVersionUID = -1987995816L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventApplication eventApplication = new QEventApplication("eventApplication");

    public final DateTimePath<java.time.LocalDateTime> applyDate = createDateTime("applyDate", java.time.LocalDateTime.class);

    public final QLibraryEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final StringPath status = createString("status");

    public QEventApplication(String variable) {
        this(EventApplication.class, forVariable(variable), INITS);
    }

    public QEventApplication(Path<? extends EventApplication> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventApplication(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventApplication(PathMetadata metadata, PathInits inits) {
        this(EventApplication.class, metadata, inits);
    }

    public QEventApplication(Class<? extends EventApplication> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new QLibraryEvent(forProperty("event")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

