package com.florin.franco.UniHub_sistemiWeb.dto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@Data
@JsonInclude(Include.NON_NULL)
public class EventoDto {
	
		private Long id;
	    private String titolo;
	    private String descrizione;
	    private LocalDateTime dataInizio;
	    private LocalDateTime dataFine;
	    private String luogo;
	    private int postiTotali;
	    private LocalDateTime deadlineIscrizione;
	    private int postiDisponibili;

	    
	    private Long creatoreId;
	    private String creatoreUsername;

	    private List<UserLiteDto> iscritti;
	    private Set<Long> commentiIds;
	    private Set<Long> feedbackIds;
}
