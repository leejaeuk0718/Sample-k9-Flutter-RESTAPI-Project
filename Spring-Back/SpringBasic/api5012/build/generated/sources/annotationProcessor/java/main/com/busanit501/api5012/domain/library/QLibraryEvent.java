package com.busanit501.api5012.domain.library;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLibraryEvent is a Querydsl query type for LibraryEvent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLibraryEvent extends EntityPathBase<LibraryEvent> {

    private static final long serialVersionUID = 1120996513L;

    public static final QLibraryEvent libraryEvent = new QLibraryEvent("libraryEvent");

    public final StringPath category = createString("category");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> currentParticipants = createNumber("currentParticipants", Integer.class);

    public final DatePath<java.time.LocalDate> eventDate = createDate("eventDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxParticipants = createNumber("maxParticipants", Integer.class);

    public final StringPath place = createString("place");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final StringPath title = createString("title");

    public QLibraryEvent(String variable) {
        super(LibraryEvent.class, forVariable(variable));
    }

    public QLibraryEvent(Path<? extends LibraryEvent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLibraryEvent(PathMetadata metadata) {
        super(LibraryEvent.class, metadata);
    }

}

