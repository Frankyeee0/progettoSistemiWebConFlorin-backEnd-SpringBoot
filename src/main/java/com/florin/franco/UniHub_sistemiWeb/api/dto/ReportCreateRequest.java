package com.florin.franco.UniHub_sistemiWeb.api.dto;

import com.florin.franco.UniHub_sistemiWeb.utils.ReportTargetType;
import lombok.Data;

@Data
public class ReportCreateRequest {
    private ReportTargetType targetType;
    private Long targetId;
    private Long reporterId;
    private String reason;
    private String details;
}
