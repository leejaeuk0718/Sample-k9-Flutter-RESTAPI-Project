package com.busanit501.api5012.domain.library;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishBook is a Querydsl query type for WishBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishBook extends EntityPathBase<WishBook> {

    private static final long serialVersionUID = 18020146L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishBook wishBook = new QWishBook("wishBook");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final StringPath reason = createString("reason");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final StringPath wishAuthor = createString("wishAuthor");

    public final StringPath wishBookTitle = createString("wishBookTitle");

    public final StringPath wishPublisher = createString("wishPublisher");

    public QWishBook(String variable) {
        this(WishBook.class, forVariable(variable), INITS);
    }

    public QWishBook(Path<? extends WishBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishBook(PathMetadata metadata, PathInits inits) {
        this(WishBook.class, metadata, inits);
    }

    public QWishBook(Class<? extends WishBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

