package com.florin.franco.UniHub_sistemiWeb.api.dto;

import java.time.LocalDateTime;

import com.florin.franco.UniHub_sistemiWeb.dto.UserLiteDto;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportStatus;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportTargetType;
import lombok.Data;

@Data
public class ReportDTO {
    private Long id;
    private ReportTargetType targetType;
    private Long targetId;
    private String targetSummary;
    private ReportStatus status;
    private String reason;
    private String details;
    private LocalDateTime createdAt;
    private UserLiteDto reporter;
    private Boolean targetSuspended;
}
