package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.dto.upload.ReseaUploadFileReqDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadFormReqDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadStatReqDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadStatsReqDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadSummaryReqDTO;
import com.ssi.ms.resea.service.ReseaUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.Map;
import java.util.Objects;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInRoleId;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

@RestController
@RequestMapping("/upload")
@Validated
@Slf4j
@CrossOrigin
public class ReseaUploadController {
    @Autowired
    private ReseaUploadService uploadService;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/form", produces = "application/json")
    public ResponseEntity uploadForm(@Valid @RequestBody
                                         @NotNull(message = "uploadReqDTO.notnull")
                                         final ReseaUploadFormReqDTO uploadReqDTO,
                                                HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Map.of("uploadId",
                uploadService.uploadForm(uploadReqDTO, getLoggedInStaffId.apply(request),
                        getLoggedInRoleId.apply(request))));
    }

    @PostMapping(path = "/file-id", produces = "application/json")
    public ResponseEntity generateFileId(@Valid @RequestBody
                                     @NotNull(message = "uploadReqDTO.notnull")
                                     final ReseaUploadFileReqDTO uploadReqDTO,
                                     HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Map.of("fileId",
                uploadService.generateFileId(uploadReqDTO, getLoggedInStaffId.apply(request),
                        getLoggedInRoleId.apply(request))));
    }

    @PostMapping(path = "/file/{uploadId}/{fileId}", produces = "application/json")
    public ResponseEntity<Map<String, String>> upload(@PathVariable("uploadId") Long uploadId,
                                                      @PathVariable("fileId") Long fileId,
                                                      @RequestParam("file") MultipartFile file,
                                                      HttpServletRequest request) {
        uploadService.uploadForRucsId(file, uploadId, fileId, request);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                Map.of("Uploaded file name", Objects.requireNonNull(file.getOriginalFilename())));
    }

    @PostMapping(path = "/summary", produces = "application/json")
    public ResponseEntity getFileUploadSummary(@Valid @RequestBody
                                                   @NotNull(message = "uploadSummaryDTO.notnull")
                                                   final ReseaUploadSummaryReqDTO uploadSummaryDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                uploadService.getFileUploadSummary(uploadSummaryDTO));
    }

    @PostMapping(path = "/uploadStat", produces = "application/json")
    public ResponseEntity getFileUploadStats(@Valid @RequestBody
                                               @NotNull(message = "uploadStatReqDTO.notnull")
                                               final ReseaUploadStatReqDTO uploadStatReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                uploadService.getFileUploadStats(uploadStatReqDTO));
    }

    @PostMapping(path = "/finalize-schedule/{uploadId}")
    public ResponseEntity finalize(@PathVariable("uploadId") Long uploadId,
                                   HttpServletRequest request) {
        uploadService.finalizeForRucsId(uploadId, request);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build();
    }

    @PostMapping(path = "/discard-schedule/{uploadId}")
    public ResponseEntity discard(@PathVariable("uploadId") Long uploadId,
                                   HttpServletRequest request) {
        uploadService.discardForRucsId(uploadId, request);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build();
    }

    @GetMapping(path = "/records/{uploadId}")
    public ResponseEntity getUploadRecords(@PathVariable("uploadId") Long uploadId,
                                           HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(uploadService.getUploadRecords(uploadId));
    }
}

