package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.api.dto.ReportCreateRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.ReportDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.ReportUpdateRequest;
import com.florin.franco.UniHub_sistemiWeb.dto.UserLiteDto;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.entity.Commento;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.entity.Report;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.ClubRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.CommentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.EventoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.ReportRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportStatus;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportTargetType;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private CommentoRepository commentoRepository;

    @Autowired
    private ClubRepository clubRepository;

    public ReportDTO createReport(ReportCreateRequest request) {
        if (request.getTargetType() == null || request.getTargetId() == null) {
            throw new RuntimeException("Target mancante");
        }
        if (request.getReporterId() == null) {
            throw new RuntimeException("Reporter mancante");
        }
        if (request.getReason() == null || request.getReason().isBlank()) {
            throw new RuntimeException("Motivo mancante");
        }

        AppUser reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> new RuntimeException("Reporter non trovato"));

        String targetSummary = resolveTargetSummary(request.getTargetType(), request.getTargetId());

        Report report = new Report();
        report.setTargetType(request.getTargetType());
        report.setTargetId(request.getTargetId());
        report.setReporter(reporter);
        report.setReason(request.getReason());
        report.setDetails(request.getDetails());
        report.setStatus(ReportStatus.NEW);

        Report saved = reportRepository.save(report);
        return toDto(saved, targetSummary);
    }

    public List<ReportDTO> listReports(ReportStatus status, ReportTargetType type) {
        List<Report> reports;
        if (status != null && type != null) {
            reports = reportRepository.findByStatusAndTargetType(status, type);
        } else if (status != null) {
            reports = reportRepository.findByStatus(status);
        } else if (type != null) {
            reports = reportRepository.findByTargetType(type);
        } else {
            reports = reportRepository.findAll();
        }

        return reports.stream()
                .map(r -> toDto(r, resolveTargetSummary(r.getTargetType(), r.getTargetId())))
                .toList();
    }

    public ReportDTO updateStatus(Long reportId, ReportUpdateRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("Status mancante");
        }
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Segnalazione non trovata"));
        report.setStatus(request.getStatus());
        Report saved = reportRepository.save(report);
        return toDto(saved, resolveTargetSummary(saved.getTargetType(), saved.getTargetId()));
    }

    private String resolveTargetSummary(ReportTargetType type, Long targetId) {
        if (type == ReportTargetType.EVENT) {
            Evento evento = eventoRepository.findById(targetId)
                    .orElseThrow(() -> new RuntimeException("Evento non trovato"));
            return evento.getTitolo();
        }
        if (type == ReportTargetType.CLUB) {
            Club club = clubRepository.findById(targetId)
                    .orElseThrow(() -> new RuntimeException("Club non trovato"));
            return club.getNome();
        }
        Commento commento = commentoRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("Commento non trovato"));
        String testo = commento.getTesto();
        if (testo == null) return "";
        return testo.length() > 80 ? testo.substring(0, 77) + "..." : testo;
    }

    private ReportDTO toDto(Report report, String targetSummary) {
        ReportDTO dto = new ReportDTO();
        dto.setId(report.getId());
        dto.setTargetType(report.getTargetType());
        dto.setTargetId(report.getTargetId());
        dto.setTargetSummary(targetSummary);
        dto.setStatus(report.getStatus());
        dto.setReason(report.getReason());
        dto.setDetails(report.getDetails());
        dto.setCreatedAt(report.getCreatedAt());
        if (report.getTargetType() == ReportTargetType.EVENT && report.getTargetId() != null) {
            eventoRepository.findById(report.getTargetId())
                    .ifPresent(evento -> dto.setTargetHidden(evento.isHidden()));
        }
        if (report.getTargetType() == ReportTargetType.CLUB && report.getTargetId() != null) {
            clubRepository.findById(report.getTargetId())
                    .ifPresent(club -> dto.setTargetSuspended(club.isSuspended()));
        }
        if (report.getReporter() != null) {
            dto.setReporter(new UserLiteDto(
                    report.getReporter().getId(),
                    report.getReporter().getUsername(),
                    report.getReporter().getProfileImage()
            ));
        }
        return dto;
    }
}
