package com.example.rest.notice.service;

import com.example.common.domain.notice.Notice;
import com.example.rest.exception.NotFoundException;
import com.example.rest.file.repository.FileCustomRepositoryImpl;
import com.example.rest.file.repository.FileJpaRepository;
import com.example.rest.notice.repository.NoticeRepository;
import com.example.rest.notice.service.dto.NoticeItemResponse;
import com.example.rest.notice.service.dto.NoticeSaveRequestDTO;
import com.example.rest.notice.service.dto.NoticeUpdateRequestDTO;
import com.example.rest.notice.service.impl.NoticeServiceImpl;
import com.example.rest.notice.service.impl.NoticeViewHistoryServiceImpl;
import com.example.rest.util.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private FileCustomRepositoryImpl fileCustomRepository;

    @Mock
    private FileJpaRepository fileJpaRepository;

    @Mock
    private NoticeViewHistoryServiceImpl noticeViewHistoryServiceImpl;

    @InjectMocks
    private NoticeServiceImpl noticeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    HttpServletRequest request = new MockHttpServletRequest();
    String title = "Test Title";
    String content = "Test Content";
    LocalDateTime noticeStartDate = LocalDateTime.now();
    LocalDateTime noticeEndDate = LocalDateTime.now().plusDays(7);

    String noticeStartDateStr = noticeStartDate.toString();
    String noticeEndDateStr = noticeEndDate.toString();

    @DisplayName("공지사항 저장 테스트")
    @Test
    public void testSaveNotice() {


        NoticeSaveRequestDTO requestDTO = new NoticeSaveRequestDTO(title, content, noticeStartDateStr, noticeEndDateStr);

        Notice savedNotice = Notice.builder()
                .title(requestDTO.title())
                .content(requestDTO.content())
                .noticeStartDate(CommonUtils.stringToLocalDate(requestDTO.startDate()))
                .noticeEndDate(CommonUtils.stringToLocalDate(requestDTO.endDate()))
                .createdBy(CommonUtils.getClientIp(request))
                .lastModifiedBy(CommonUtils.getClientIp(request))
                .build();
        savedNotice.setId(1);

        when(noticeRepository.save(any())).thenReturn(savedNotice);

        Integer savedNoticeId = noticeService.saveNotice(request, requestDTO, null);

        assertEquals(savedNotice.getId(), savedNoticeId);
    }

    @DisplayName("공지사항 수정 테스트")
    @Test
    public void testUpdateNotice() {
        Integer noticeId = 1;
        NoticeUpdateRequestDTO requestDTO = new NoticeUpdateRequestDTO(title, content, noticeStartDateStr, noticeEndDateStr, null);

        Notice existingNotice = Notice.builder()
                .title("Original Title")
                .content("Original Content")
                .build();
        existingNotice.setId(noticeId);

        when(noticeRepository.findById(anyInt())).thenReturn(Optional.of(existingNotice));
        when(noticeRepository.save(any())).thenReturn(existingNotice);

        Integer updatedNoticeId = noticeService.updateNotice(request, noticeId, requestDTO, null);

        assertEquals(noticeId, updatedNoticeId);
        assertEquals(requestDTO.title(), existingNotice.getTitle());
        assertEquals(requestDTO.content(), existingNotice.getContent());
    }

    @DisplayName("공지사항 조회 & 조회정보 저장")
    @Test
    void findById_IncreasesViewCount() {
        // Given
        Integer noticeId = 1;
        Notice notice = new Notice("Test Title", "Test Content", "User", "User", LocalDateTime.now(), LocalDateTime.now());
        when(noticeRepository.findById(noticeId)).thenReturn(java.util.Optional.of(notice));
        AuditingEntityListener listener = new AuditingEntityListener();
        ObjectFactory<AuditingHandler> auditingHandlerFactory = mock(ObjectFactory.class);
        AuditingHandler auditingHandler = new AuditingHandler(mock(PersistentEntities.class)); // PersistentEntities를 Mock으로 대체하여 생성
        when(auditingHandlerFactory.getObject()).thenReturn(auditingHandler);
        AuditorAware<String> auditorAware = mock(AuditorAware.class);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("admin"));
        auditingHandler.setAuditorAware(auditorAware); // AuditorAware 설정
        DateTimeProvider dateTimeProvider = new FixedDateTimeProvider(LocalDateTime.now());
        auditingHandler.setDateTimeProvider(dateTimeProvider);
        listener.setAuditingHandler(auditingHandlerFactory);


        // When
        listener.touchForCreate(notice);

        // When
        NoticeItemResponse result = noticeService.findById(request, noticeId);

        // Then
        verify(noticeRepository, times(1)).findById(noticeId);
        verify(noticeViewHistoryServiceImpl, times(1)).insertNoticeViewHistory("VIEWER", noticeId);
        // Add more assertions if necessary
    }
    // 테스트용 DateTimeProvider 구현체
    private static class FixedDateTimeProvider implements DateTimeProvider {

        private final LocalDateTime dateTime;

        public FixedDateTimeProvider(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        public Optional<java.time.temporal.TemporalAccessor> getNow() {
            return Optional.of(dateTime);
        }
    }

    @DisplayName("NotFoundException 테스트")
    @Test
    public void testUpdateNotice_NotFound() {
        Integer noticeId = 1;
        NoticeUpdateRequestDTO requestDTO = new NoticeUpdateRequestDTO(title, content, noticeStartDateStr, noticeEndDateStr, null);

        when(noticeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> noticeService.updateNotice(request, noticeId, requestDTO, null));
    }

    @DisplayName("공지사항 수정 통합테스트")
    @Test
    public void updateNotice_ValidInput_Success() {
        // Given
        Integer noticeId = 1;
        NoticeUpdateRequestDTO requestDTO = new NoticeUpdateRequestDTO(title, content, noticeStartDateStr, noticeEndDateStr, null);

        List<MultipartFile> uploadFiles = Collections.emptyList();
        MockHttpServletRequest request = new MockHttpServletRequest();

        NoticeRepository noticeRepository = mock(NoticeRepository.class);
        FileJpaRepository fileJpaRepository = mock(FileJpaRepository.class);
        FileCustomRepositoryImpl fileCustomRepository = mock(FileCustomRepositoryImpl.class);
        NoticeViewHistoryServiceImpl noticeViewHistoryService = mock(NoticeViewHistoryServiceImpl.class);

        NoticeServiceImpl noticeService = new NoticeServiceImpl(noticeRepository, fileCustomRepository, fileJpaRepository, noticeViewHistoryService);

        Notice existingNotice = Notice.builder()
                .title("Original Title")
                .content("Original Content")
                .noticeStartDate(LocalDateTime.now().minusDays(1))
                .noticeEndDate(LocalDateTime.now().plusDays(6))
                .createdBy(CommonUtils.getClientIp(request))
                .lastModifiedBy(CommonUtils.getClientIp(request))
                .build();
        existingNotice.setId(noticeId);


        Mockito.when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));


        noticeService.updateNotice(request, noticeId, requestDTO, uploadFiles);


        verify(noticeRepository).findById(noticeId);
        verify(noticeRepository).save(any(Notice.class));
    }

    @DisplayName("존재하지 않는 공지사항 삭제 테스트")
    @Test
    public void deleteNotice_NotExistingNotice_ThrowsException() {
        // Given
        Integer noticeId = 1;
        NoticeRepository noticeRepository = mock(NoticeRepository.class);
        FileCustomRepositoryImpl fileCustomRepository = mock(FileCustomRepositoryImpl.class);
        FileJpaRepository fileJpaRepository = mock(FileJpaRepository.class);
        NoticeViewHistoryServiceImpl noticeViewHistoryService = mock(NoticeViewHistoryServiceImpl.class);

        NoticeServiceImpl noticeService = new NoticeServiceImpl(noticeRepository, fileCustomRepository, fileJpaRepository, noticeViewHistoryService);


        when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> noticeService.deleteNotice(noticeId));
        verify(noticeRepository, times(1)).findById(noticeId);
        verifyNoMoreInteractions(noticeRepository, fileCustomRepository, fileJpaRepository, noticeViewHistoryService);
    }

    @DisplayName("공지사항 삭제 성공")
    @Test
    public void deleteNotice_ExistingNotice_Success() {
        // Given
        Integer noticeId = 1;
        NoticeRepository noticeRepository = mock(NoticeRepository.class);
        FileCustomRepositoryImpl fileCustomRepository = mock(FileCustomRepositoryImpl.class);
        FileJpaRepository fileJpaRepository = mock(FileJpaRepository.class);
        NoticeViewHistoryServiceImpl noticeViewHistoryService = mock(NoticeViewHistoryServiceImpl.class);

        NoticeServiceImpl noticeService = new NoticeServiceImpl(noticeRepository, fileCustomRepository, fileJpaRepository, noticeViewHistoryService);
        Notice existingNotice = Notice.builder().build();
        existingNotice.setId(noticeId);


        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));


        noticeService.deleteNotice(noticeId);


        verify(noticeRepository, times(1)).findById(noticeId);
        verify(noticeRepository, times(1)).deleteById(noticeId);
        verifyNoMoreInteractions(noticeRepository, fileCustomRepository, fileJpaRepository, noticeViewHistoryService);
    }
}