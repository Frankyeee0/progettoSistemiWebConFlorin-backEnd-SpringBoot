package com.florin.franco.UniHub_sistemiWeb.api.dto;

import com.florin.franco.UniHub_sistemiWeb.utils.ReportStatus;
import lombok.Data;

@Data
public class ReportUpdateRequest {
    private ReportStatus status;
}
