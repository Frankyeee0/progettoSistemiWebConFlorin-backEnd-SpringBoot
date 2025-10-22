package com.florin.franco.UniHub_sistemiWeb;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.florin.franco.UniHub_sistemiWeb.dto.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.dto.Universita;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.entity.Commento;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.entity.Feedback;
import com.florin.franco.UniHub_sistemiWeb.repository.FeedbackRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.ClubRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.CommentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.DipartimentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.EventoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class UniHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniHubApplication.class, args);
	}
	
	 // ✅ Metodo di inizializzazione semplificato e transazionale
    @Bean
    @Transactional
    CommandLineRunner initDatabase(
            UniversitaRepository universitaRepo,
            DipartimentoRepository dipartimentoRepo,
            AppUserRepository userRepo,
            EventoRepository eventoRepo,
            ClubRepository clubRepo,
            CommentoRepository commentoRepo,
            FeedbackRepository feedbackRepo,
            PasswordEncoder encoder
    ) {
        return args -> {
            System.out.println("\n🚀 Inizializzazione dati UniHub...\n");

            // === 🎓 UNIVERSITÀ & DIPARTIMENTI ===
            if (universitaRepo.count() == 0) {
                Universita unife = new Universita(null, "Università di Ferrara (UNIFE)", null);
                Universita unibo = new Universita(null, "Università di Bologna (UNIBO)", null);
                universitaRepo.saveAll(List.of(unife, unibo));

                Dipartimento ing = new Dipartimento(null, "Ingegneria", unife, null);
                Dipartimento eco = new Dipartimento(null, "Economia", unife, null);
                Dipartimento inf = new Dipartimento(null, "Informatica", unibo, null);
                Dipartimento fis = new Dipartimento(null, "Fisica", unibo, null);
                dipartimentoRepo.saveAll(List.of(ing, eco, inf, fis));

                System.out.println("✅ Università e Dipartimenti creati.");
            }

            // === 👤 UTENTI ===
            if (userRepo.count() == 0) {
                AppUser admin = new AppUser();
                admin.setName("System");
                admin.setSurname("Admin");
                admin.setStudentId("A000");
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("1234"));
                admin.setEmail("admin@unihub.com");
                admin.setRole(Ruolo.SUPERADMIN);

                AppUser francisc = new AppUser();
                francisc.setName("Francisc");
                francisc.setSurname("Pop");
                francisc.setStudentId("S001");
                francisc.setUsername("francisc");
                francisc.setPassword(encoder.encode("1234"));
                francisc.setEmail("francisc@student.unife.it");
                francisc.setRole(Ruolo.STUDENT);

                AppUser maria = new AppUser();
                maria.setName("Maria");
                maria.setSurname("Bianchi");
                maria.setStudentId("S002");
                maria.setUsername("maria");
                maria.setPassword(encoder.encode("1234"));
                maria.setEmail("maria@student.unibo.it");
                maria.setRole(Ruolo.STUDENT);

                AppUser luca = new AppUser();
                luca.setName("Luca");
                luca.setSurname("Rossi");
                luca.setStudentId("S003");
                luca.setUsername("luca");
                luca.setPassword(encoder.encode("1234"));
                luca.setEmail("luca@student.unibo.it");
                luca.setRole(Ruolo.STUDENT);

                userRepo.saveAll(List.of(admin, francisc, maria, luca));

                // Relazioni follow (chi segue chi)
                francisc.setSeguiti(Set.of(maria, luca));
                maria.setSeguiti(Set.of(francisc));
                luca.setSeguiti(Set.of(admin));

                userRepo.saveAll(List.of(francisc, maria, luca));

                System.out.println("✅ Utenti e follow creati.");
            }

            AppUser admin = userRepo.findByUsername("admin").get();
            AppUser francisc = userRepo.findByUsername("francisc").get();
            AppUser maria = userRepo.findByUsername("maria").get();
            AppUser luca = userRepo.findByUsername("luca").get();

            // === 🎉 EVENTI ===
            if (eventoRepo.count() == 0) {
                Evento careerDay = new Evento();
                careerDay.setTitolo("Career Day UNIFE");
                careerDay.setDescrizione("Incontro con aziende e workshop di orientamento");
                careerDay.setLuogo("Aula Magna, Ingegneria");
                careerDay.setDataInizio(LocalDateTime.of(2025, 11, 15, 9, 0));
                careerDay.setDataFine(LocalDateTime.of(2025, 11, 15, 17, 0));
                careerDay.setDeadlineIscrizione(LocalDateTime.of(2025, 11, 10, 23, 59));
                careerDay.setPostiTotali(100);
                careerDay.setCreatore(admin);
                careerDay.setIscritti(Set.of(francisc, maria));

                Evento hackathon = new Evento();
                hackathon.setTitolo("Hackathon AI 2025");
                hackathon.setDescrizione("48 ore di coding e innovazione sull’Intelligenza Artificiale");
                hackathon.setLuogo("Lab 2, Informatica");
                hackathon.setDataInizio(LocalDateTime.of(2025, 12, 10, 9, 0));
                hackathon.setDataFine(LocalDateTime.of(2025, 12, 12, 18, 0));
                hackathon.setDeadlineIscrizione(LocalDateTime.of(2025, 12, 5, 23, 59));
                hackathon.setPostiTotali(50);
                hackathon.setCreatore(admin);
                hackathon.setIscritti(Set.of(luca));

                eventoRepo.saveAll(List.of(careerDay, hackathon));
                System.out.println("✅ Eventi creati.");
            }

            // === 🏫 CLUB ===
            if (clubRepo.count() == 0) {
                Club robotica = new Club();
                robotica.setNome("Club di Robotica");
                robotica.setDescrizione("Progettazione e costruzione robot competitivi");
                robotica.setMaxMembri(50);
                robotica.setFondatore(admin);
                robotica.setMembri(Set.of(francisc, luca));

                Club fotografia = new Club();
                fotografia.setNome("Club di Fotografia");
                fotografia.setDescrizione("Workshop e uscite fotografiche nel campus");
                fotografia.setMaxMembri(40);
                fotografia.setFondatore(admin);
                fotografia.setMembri(Set.of(maria));

                clubRepo.saveAll(List.of(robotica, fotografia));
                System.out.println("✅ Club creati.");
            }

            // === 💬 COMMENTI & ⭐ FEEDBACK ===
            if (commentoRepo.count() == 0 && feedbackRepo.count() == 0) {
                Evento careerDay = eventoRepo.findAll().get(0);
                Evento hackathon = eventoRepo.findAll().get(1);
                Club robotica = clubRepo.findAll().get(0);

                Commento c1 = new Commento(null, "Bellissimo evento!", LocalDateTime.now(), francisc, careerDay, null);
                Commento c2 = new Commento(null, "Grande esperienza!", LocalDateTime.now(), luca, hackathon, null);
                Commento c3 = new Commento(null, "Bellissima community!", LocalDateTime.now(), maria, null, robotica);

                Feedback f1 = new Feedback(null, 5, "Evento ben organizzato", LocalDateTime.now(), maria, careerDay, null);
                Feedback f2 = new Feedback(null, 4, "Hackathon stimolante", LocalDateTime.now(), francisc, hackathon, null);
                Feedback f3 = new Feedback(null, 5, "Club accogliente", LocalDateTime.now(), luca, null, robotica);

                commentoRepo.saveAll(List.of(c1, c2, c3));
                feedbackRepo.saveAll(List.of(f1, f2, f3));

                System.out.println("✅ Commenti e feedback creati.");
            }

            System.out.println("\n🎯 Inizializzazione completa ✅");
        };
    }
}
