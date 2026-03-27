package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.Notice;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * NoticeDTO - 공지사항 DTO
 *
 * 공지사항 목록 조회 및 상세 조회 응답에 사용하는 데이터 전송 객체입니다.
 * 첨부 이미지 목록(List<NoticeImageDTO>)을 포함합니다.
 *
 * API 엔드포인트:
 *   GET    /api/library/notices          (공지사항 목록)
 *   GET    /api/library/notices/{id}     (공지사항 상세, 이미지 포함)
 *   POST   /api/library/notices          (공지사항 등록, 관리자 전용)
 *   PUT    /api/library/notices/{id}     (공지사항 수정, 관리자 전용)
 *   DELETE /api/library/notices/{id}     (공지사항 삭제, 관리자 전용)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

    /** id - 공지사항 기본키 */
    private Long id;

    /** title - 공지사항 제목 */
    private String title;

    /** content - 공지사항 내용 */
    private String content;

    /** writer - 작성자 */
    private String writer;

    /** topFixed - 상단 고정 여부 */
    private boolean topFixed;

    /**
     * regDate - 공지사항 등록일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    /**
     * images - 첨부 이미지 목록
     * 공지사항에 첨부된 이미지 정보 목록입니다.
     * 목록 조회 시에는 빈 리스트 또는 첫 번째 이미지만 포함할 수 있습니다.
     * 상세 조회 시에는 전체 이미지 목록을 포함합니다.
     *
     * @Builder.Default : Lombok @Builder 사용 시 기본값 설정 필수
     */
    @Builder.Default
    private List<NoticeImageDTO> images = new ArrayList<>();

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - Notice 엔티티를 NoticeDTO 로 변환 (이미지 포함)
     * images 컬렉션이 LAZY 로딩이므로 트랜잭션 내에서 호출해야 합니다.
     * 또는 findWithImagesById() 로 JOIN FETCH 하여 이미 로딩된 상태여야 합니다.
     *
     * [Stream API 설명]
     * notice.getImages()          : List<NoticeImage> 를 Stream 으로 변환
     * .stream()                   : Stream 파이프라인 시작
     * .map(NoticeImageDTO::fromEntity) : 각 NoticeImage 를 NoticeImageDTO 로 변환
     * .collect(Collectors.toList())    : Stream 결과를 List 로 수집
     *
     * @param notice 변환할 Notice 엔티티
     * @return 변환된 NoticeDTO (이미지 목록 포함)
     */
    public static NoticeDTO fromEntity(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writer(notice.getWriter())
                .topFixed(notice.isTopFixed())
                .regDate(notice.getRegDate())
                .images(notice.getImages().stream()
                        .map(NoticeImageDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * fromEntityWithoutImages - Notice 엔티티를 NoticeDTO 로 변환 (이미지 제외)
     * 목록 조회 시 성능 향상을 위해 이미지를 제외하고 변환합니다.
     *
     * @param notice 변환할 Notice 엔티티
     * @return 이미지 없는 NoticeDTO
     */
    public static NoticeDTO fromEntityWithoutImages(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .writer(notice.getWriter())
                .topFixed(notice.isTopFixed())
                .regDate(notice.getRegDate())
                .build();
    }
}
