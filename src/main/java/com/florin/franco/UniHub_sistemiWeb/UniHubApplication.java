package com.florin.franco.UniHub_sistemiWeb;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.entity.Commento;
import com.florin.franco.UniHub_sistemiWeb.entity.CategoriaEvento;
import com.florin.franco.UniHub_sistemiWeb.entity.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.entity.Feedback;
import com.florin.franco.UniHub_sistemiWeb.entity.Materia;
import com.florin.franco.UniHub_sistemiWeb.entity.Report;
import com.florin.franco.UniHub_sistemiWeb.entity.Universita;
import com.florin.franco.UniHub_sistemiWeb.entity.Aula;
import com.florin.franco.UniHub_sistemiWeb.entity.Lezione;
import com.florin.franco.UniHub_sistemiWeb.repository.FeedbackRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.AulaRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.ClubRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.CategoriaEventoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.CommentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.DipartimentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.EventoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.ReportRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.MateriaRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.LezioneRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportStatus;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportTargetType;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class UniHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniHubApplication.class, args);
	}
	
	 @Bean
	    @Transactional
	    CommandLineRunner initDatabase(
	            UniversitaRepository universitaRepo,
            DipartimentoRepository dipartimentoRepo,
            AppUserRepository userRepo,
            EventoRepository eventoRepo,
            ClubRepository clubRepo,
            CommentoRepository commentoRepo,
            CategoriaEventoRepository categoriaRepo,
            FeedbackRepository feedbackRepo,
            ReportRepository reportRepo,
            MateriaRepository materiaRepo,
            AulaRepository aulaRepo,
            LezioneRepository lezioneRepo,
            PasswordEncoder encoder
	    ) {
	        return args -> {
	            System.out.println("\n🚀 Inizializzazione dati UniHub...\n");

	            // === 🎓 UNIVERSITÀ & DIPARTIMENTI ===
            if (universitaRepo.count() == 0) {
	                Universita unife = new Universita(null, "Università di Ferrara (UNIFE)", null);
	                Universita unibo = new Universita(null, "Università di Bologna (UNIBO)", null);
	                Universita unimi = new Universita(null, "Università di Milano (UNIMI)", null);
	                Universita unipi = new Universita(null, "Università di Pisa (UNIPI)", null);
	                universitaRepo.saveAll(List.of(unife, unibo, unimi, unipi));

	                Dipartimento ing = new Dipartimento(null, "Ingegneria", unife, null);
	                Dipartimento eco = new Dipartimento(null, "Economia", unife, null);
	                Dipartimento inf = new Dipartimento(null, "Informatica", unibo, null);
	                Dipartimento fis = new Dipartimento(null, "Fisica", unibo, null);
	                Dipartimento med = new Dipartimento(null, "Medicina", unimi, null);
	                Dipartimento giu = new Dipartimento(null, "Giurisprudenza", unimi, null);
	                Dipartimento let = new Dipartimento(null, "Lettere", unipi, null);
	                Dipartimento mat = new Dipartimento(null, "Matematica", unipi, null);
	                dipartimentoRepo.saveAll(List.of(ing, eco, inf, fis, med, giu, let, mat));

	                System.out.println("✅ Università e Dipartimenti creati.");
	            }

            Map<String, Dipartimento> dipByName = new HashMap<>();
            dipartimentoRepo.findAll().forEach(d -> dipByName.put(d.getNome(), d));

            if (categoriaRepo.count() == 0) {
                List<String> defaults = List.of("accademico", "sport", "cultura", "carriera", "volontariato");
                List<CategoriaEvento> items = defaults.stream().map(name -> {
                    CategoriaEvento c = new CategoriaEvento();
                    c.setNome(name);
                    return c;
                }).toList();
                categoriaRepo.saveAll(items);
                System.out.println("✅ Categorie evento create.");
            }

	            // === 👤 UTENTI ===
            if (userRepo.count() == 0) {
                AppUser admin = new AppUser();
                admin.setName("System");
                admin.setSurname("Admin");
                admin.setStudentId("A000");
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setEmail("admin@unihub.com");
	                admin.setRole(Ruolo.ADMIN);
                    admin.setDipartimento(dipByName.get("Ingegneria"));

                AppUser planner = new AppUser();
                planner.setName("Paolo");
                planner.setSurname("Verdi");
                planner.setStudentId("P010");
                planner.setUsername("planner");
                planner.setPassword(encoder.encode("1234"));
                planner.setEmail("planner@unihub.com");
                planner.setRole(Ruolo.USER);
                planner.setDipartimento(dipByName.get("Economia"));

                AppUser francisc = new AppUser();
                francisc.setName("Francisc");
                francisc.setSurname("Pop");
                francisc.setStudentId("S001");
                francisc.setUsername("francisc");
                francisc.setPassword(encoder.encode("1234"));
                francisc.setEmail("francisc@student.unife.it");
                francisc.setRole(Ruolo.USER);
                francisc.setDipartimento(dipByName.get("Informatica"));

                AppUser maria = new AppUser();
                maria.setName("Maria");
                maria.setSurname("Bianchi");
                maria.setStudentId("S002");
                maria.setUsername("maria");
                maria.setPassword(encoder.encode("1234"));
                maria.setEmail("maria@student.unibo.it");
                maria.setRole(Ruolo.USER);
                maria.setDipartimento(dipByName.get("Fisica"));

                AppUser luca = new AppUser();
                luca.setName("Luca");
                luca.setSurname("Rossi");
                luca.setStudentId("S003");
                luca.setUsername("luca");
                luca.setPassword(encoder.encode("1234"));
                luca.setEmail("luca@student.unibo.it");
                luca.setRole(Ruolo.USER);
                luca.setDipartimento(dipByName.get("Matematica"));

                AppUser anna = new AppUser();
                anna.setName("Anna");
                anna.setSurname("Conti");
                anna.setStudentId("S004");
                anna.setUsername("anna");
                anna.setPassword(encoder.encode("1234"));
                anna.setEmail("anna@student.unimi.it");
                anna.setRole(Ruolo.USER);
                anna.setDipartimento(dipByName.get("Medicina"));

                AppUser marco = new AppUser();
                marco.setName("Marco");
                marco.setSurname("Gallo");
                marco.setStudentId("S005");
                marco.setUsername("marco");
                marco.setPassword(encoder.encode("1234"));
                marco.setEmail("marco@student.unipi.it");
                marco.setRole(Ruolo.USER);
                marco.setDipartimento(dipByName.get("Lettere"));

                userRepo.saveAll(List.of(admin, planner, francisc, maria, luca, anna, marco));

	                // Relazioni follow
	                francisc.setSeguiti(Set.of(maria, luca, anna));
	                maria.setSeguiti(Set.of(francisc, marco));
	                luca.setSeguiti(Set.of(admin, planner));
                    anna.setSeguiti(Set.of(maria));
                    marco.setSeguiti(Set.of(francisc));

	                userRepo.saveAll(List.of(francisc, maria, luca, anna, marco));

                System.out.println("✅ Utenti e relazioni follow creati.");
            }

            if (userRepo.findByUsername("admin").isEmpty()) {
                AppUser adminUser = new AppUser();
                adminUser.setName("System");
                adminUser.setSurname("Admin");
                adminUser.setStudentId("A000");
                adminUser.setUsername("admin");
                adminUser.setPassword(encoder.encode("admin123"));
                adminUser.setEmail("admin@unihub.com");
                adminUser.setRole(Ruolo.ADMIN);
                adminUser.setEmailNotificationsEnabled(true);
                adminUser.setDipartimento(dipByName.getOrDefault("Ingegneria", null));
                userRepo.save(adminUser);
                System.out.println("✅ Admin creato (admin/admin123).");
            }

            if (userRepo.findByUsername("superadmin").isEmpty()) {
                AppUser superAdmin = new AppUser();
                superAdmin.setName("Super");
                superAdmin.setSurname("Admin");
                superAdmin.setStudentId("A001");
                superAdmin.setUsername("superadmin");
                superAdmin.setPassword(encoder.encode("1234"));
                superAdmin.setEmail("superadmin@unihub.com");
                superAdmin.setRole(Ruolo.ADMIN);
                superAdmin.setEmailNotificationsEnabled(true);
                userRepo.save(superAdmin);
                System.out.println("✅ Super admin creato (superadmin/1234).");
            }

            if (userRepo.findByUsername("user").isEmpty()) {
                AppUser user = new AppUser();
                user.setName("Test");
                user.setSurname("User");
                user.setStudentId("U001");
                user.setUsername("user");
                user.setPassword(encoder.encode("user123"));
                user.setEmail("user@unihub.com");
                user.setRole(Ruolo.USER);
                user.setEmailNotificationsEnabled(true);
                user.setDipartimento(dipByName.getOrDefault("Informatica", null));
                userRepo.save(user);
                System.out.println("✅ User creato (user/user123).");
            }

	            AppUser admin = userRepo.findByUsername("admin").get();
	            AppUser francisc = userRepo.findByUsername("francisc").get();
	            AppUser maria = userRepo.findByUsername("maria").get();
	            AppUser luca = userRepo.findByUsername("luca").get();
	            AppUser anna = userRepo.findByUsername("anna").get();
	            AppUser marco = userRepo.findByUsername("marco").get();

	            // === 🎉 EVENTI ===
	            if (eventoRepo.count() == 0) {
	                Evento careerDay = new Evento();
	                careerDay.setTitolo("Career Day UNIFE");
	                careerDay.setDescrizione("Incontro con aziende e workshop di orientamento.");
	                careerDay.setCategoria("carriera");
	                careerDay.setUniversita("Università di Ferrara (UNIFE)");
	                careerDay.setLuogo("Aula Magna, Ingegneria");
	                careerDay.setDataInizio(LocalDateTime.of(2025, 11, 15, 9, 0));
	                careerDay.setDataFine(LocalDateTime.of(2025, 11, 15, 17, 0));
	                careerDay.setDeadlineIscrizione(LocalDateTime.of(2025, 11, 10, 23, 59));
	                careerDay.setPostiTotali(100);
	                careerDay.setCreatore(admin);
	                careerDay.setIscritti(Set.of(francisc, maria));

	                Evento hackathon = new Evento();
	                hackathon.setTitolo("Hackathon AI 2025");
	                hackathon.setDescrizione("48 ore di coding sull’Intelligenza Artificiale.");
	                hackathon.setCategoria("accademico");
	                hackathon.setUniversita("Università di Bologna (UNIBO)");
	                hackathon.setLuogo("Laboratorio 2, Informatica");
	                hackathon.setDataInizio(LocalDateTime.of(2025, 12, 10, 9, 0));
	                hackathon.setDataFine(LocalDateTime.of(2025, 12, 12, 18, 0));
	                hackathon.setDeadlineIscrizione(LocalDateTime.of(2025, 12, 5, 23, 59));
	                hackathon.setPostiTotali(50);
	                hackathon.setCreatore(admin);
	                hackathon.setIscritti(Set.of(luca));

	                Evento welcomeWeek = new Evento();
	                welcomeWeek.setTitolo("Welcome Week");
	                welcomeWeek.setDescrizione("Settimana di orientamento per le matricole.");
	                welcomeWeek.setCategoria("accademico");
	                welcomeWeek.setUniversita("Università di Milano (UNIMI)");
	                welcomeWeek.setLuogo("Campus UNIMI");
	                welcomeWeek.setDataInizio(LocalDateTime.of(2025, 10, 1, 9, 0));
	                welcomeWeek.setDataFine(LocalDateTime.of(2025, 10, 5, 18, 0));
	                welcomeWeek.setDeadlineIscrizione(LocalDateTime.of(2025, 9, 28, 23, 59));
	                welcomeWeek.setPostiTotali(200);
	                welcomeWeek.setCreatore(admin);
	                welcomeWeek.setIscritti(Set.of(anna, marco));

	                Evento mathMeetup = new Evento();
	                mathMeetup.setTitolo("Math Meetup");
	                mathMeetup.setDescrizione("Seminari e talk di matematica applicata.");
	                mathMeetup.setCategoria("accademico");
	                mathMeetup.setUniversita("Università di Pisa (UNIPI)");
	                mathMeetup.setLuogo("Aula 2, Matematica");
	                mathMeetup.setDataInizio(LocalDateTime.of(2025, 11, 20, 15, 0));
	                mathMeetup.setDataFine(LocalDateTime.of(2025, 11, 20, 19, 0));
	                mathMeetup.setDeadlineIscrizione(LocalDateTime.of(2025, 11, 19, 18, 0));
	                mathMeetup.setPostiTotali(80);
	                mathMeetup.setCreatore(admin);
	                mathMeetup.setIscritti(Set.of(luca, marco));

	                eventoRepo.saveAll(List.of(careerDay, hackathon, welcomeWeek, mathMeetup));
	                System.out.println("✅ Eventi creati.");
	            }

                var eventi = eventoRepo.findAll();
                boolean updated = false;
                for (Evento evento : eventi) {
                    if (evento.getCategoria() == null || evento.getCategoria().isBlank()) {
                        evento.setCategoria(guessCategoria(evento));
                        updated = true;
                    }
                    if (evento.getUniversita() == null || evento.getUniversita().isBlank()) {
                        evento.setUniversita(guessUniversita(evento));
                        updated = true;
                    }
                }
                if (updated) {
                    eventoRepo.saveAll(eventi);
                    System.out.println("✅ Eventi aggiornati con categoria/università.");
                }

	            // === 🏫 CLUB ===
	            if (clubRepo.count() == 0) {
	                Club robotica = new Club();
	                robotica.setNome("Club di Robotica");
	                robotica.setDescrizione("Progettazione e costruzione di robot competitivi.");
	                robotica.setMaxMembri(50);
	                robotica.setFondatore(admin);
	                robotica.setMembri(Set.of(francisc, luca));

	                Club fotografia = new Club();
	                fotografia.setNome("Club di Fotografia");
	                fotografia.setDescrizione("Workshop e uscite fotografiche nel campus.");
	                fotografia.setMaxMembri(40);
	                fotografia.setFondatore(admin);
	                fotografia.setMembri(Set.of(maria));

	                Club musica = new Club();
	                musica.setNome("Club di Musica");
	                musica.setDescrizione("Jam session e concerti in universita.");
	                musica.setMaxMembri(60);
	                musica.setFondatore(admin);
	                musica.setMembri(Set.of(anna, marco));

	                Club ecoClub = new Club();
	                ecoClub.setNome("Eco Club");
	                ecoClub.setDescrizione("Iniziative green e sostenibilita.");
	                ecoClub.setMaxMembri(45);
	                ecoClub.setFondatore(admin);
	                ecoClub.setMembri(Set.of(francisc, maria, luca));

	                clubRepo.saveAll(List.of(robotica, fotografia, musica, ecoClub));
	                System.out.println("✅ Club creati.");
	            }

	            // === 💬 COMMENTI & ⭐ FEEDBACK ===
	            if (commentoRepo.count() == 0 || feedbackRepo.count() == 0) {
	                List<Evento> eventiSeed = eventoRepo.findAll();
	                Evento careerDay = eventiSeed.get(0);
	                Evento hackathon = eventiSeed.get(1);
	                Evento welcomeWeek = eventiSeed.get(2);
	                Club robotica = clubRepo.findAll().get(0);
	                Club musica = clubRepo.findAll().get(2);

	                Commento c1 = new Commento(null, "Bellissimo evento!", LocalDateTime.now(), francisc, careerDay, null);
	                Commento c2 = new Commento(null, "Grande esperienza!", LocalDateTime.now(), luca, hackathon, null);
	                Commento c3 = new Commento(null, "Bellissima community!", LocalDateTime.now(), maria, null, robotica);
	                Commento c4 = new Commento(null, "Ottimo per le matricole!", LocalDateTime.now(), anna, welcomeWeek, null);
	                Commento c5 = new Commento(null, "Serata spettacolare!", LocalDateTime.now(), marco, null, musica);

	                Feedback f1 = new Feedback(null, 5, "Evento ben organizzato!", LocalDateTime.now(), maria, careerDay, null);
	                Feedback f2 = new Feedback(null, 4, "Hackathon stimolante!", LocalDateTime.now(), francisc, hackathon, null);
	                Feedback f3 = new Feedback(null, 5, "Club accogliente!", LocalDateTime.now(), luca, null, robotica);
	                Feedback f4 = new Feedback(null, 5, "Welcome week utilissima!", LocalDateTime.now(), anna, welcomeWeek, null);
	                Feedback f5 = new Feedback(null, 4, "Atmosfera super!", LocalDateTime.now(), marco, null, musica);

	                commentoRepo.saveAll(List.of(c1, c2, c3, c4, c5));
	                feedbackRepo.saveAll(List.of(f1, f2, f3, f4, f5));

	                System.out.println("✅ Commenti e feedback creati.");
	            }

	            if (reportRepo.count() == 0) {
	                Evento careerDay = eventoRepo.findAll().get(0);
	                Commento commento = commentoRepo.findAll().get(0);
	                Club club = clubRepo.findAll().get(0);

	                Report r1 = new Report();
	                r1.setTargetType(ReportTargetType.EVENT);
	                r1.setTargetId(careerDay.getId());
	                r1.setReporter(maria);
	                r1.setReason("Contenuto non appropriato");
	                r1.setDetails("Descrizione troppo generica");
	                r1.setStatus(ReportStatus.NEW);

	                Report r2 = new Report();
	                r2.setTargetType(ReportTargetType.COMMENT);
	                r2.setTargetId(commento.getId());
	                r2.setReporter(luca);
	                r2.setReason("Linguaggio offensivo");
	                r2.setDetails("Commento con parole offensive");
	                r2.setStatus(ReportStatus.NEW);

	                Report r3 = new Report();
	                r3.setTargetType(ReportTargetType.CLUB);
	                r3.setTargetId(club.getId());
	                r3.setReporter(francisc);
	                r3.setReason("Spam");
	                r3.setDetails("Club creato solo per pubblicita");
	                r3.setStatus(ReportStatus.IN_REVIEW);

	                reportRepo.saveAll(List.of(r1, r2, r3));
	                System.out.println("✅ Segnalazioni create.");
	            }

                // === 📚 MATERIE, AULE, LEZIONI ===
                if (materiaRepo.count() == 0) {
                    Materia analisi = new Materia();
                    analisi.setNome("Analisi 1");
                    analisi.setCodice("ANL101");
                    analisi.setCorsoDiStudi("Ingegneria");
                    analisi.setDipartimento(dipByName.get("Ingegneria"));

                    Materia programmazione = new Materia();
                    programmazione.setNome("Programmazione 1");
                    programmazione.setCodice("INF101");
                    programmazione.setCorsoDiStudi("Informatica");
                    programmazione.setDipartimento(dipByName.get("Informatica"));

                    Materia economia = new Materia();
                    economia.setNome("Economia Aziendale");
                    economia.setCodice("ECO201");
                    economia.setCorsoDiStudi("Economia");
                    economia.setDipartimento(dipByName.get("Economia"));

                    materiaRepo.saveAll(List.of(analisi, programmazione, economia));
                    System.out.println("✅ Materie create.");
                }

                if (aulaRepo.count() == 0) {
                    Universita unife = universitaRepo.findAll().stream()
                            .filter(u -> u.getNome().contains("UNIFE"))
                            .findFirst()
                            .orElse(null);
                    Universita unibo = universitaRepo.findAll().stream()
                            .filter(u -> u.getNome().contains("UNIBO"))
                            .findFirst()
                            .orElse(null);

                    Aula aulaA = new Aula();
                    aulaA.setNome("Aula A");
                    aulaA.setEdificio("Blocco Ingegneria");
                    aulaA.setCapienza(120);
                    aulaA.setUniversita(unife);

                    Aula lab1 = new Aula();
                    lab1.setNome("Laboratorio 1");
                    lab1.setEdificio("Dip. Informatica");
                    lab1.setCapienza(60);
                    lab1.setUniversita(unibo);

                    Aula aulaB = new Aula();
                    aulaB.setNome("Aula B");
                    aulaB.setEdificio("Blocco Economia");
                    aulaB.setCapienza(90);
                    aulaB.setUniversita(unife);

                    aulaRepo.saveAll(List.of(aulaA, lab1, aulaB));
                    System.out.println("✅ Aule create.");
                }

                if (lezioneRepo.count() == 0) {
                    List<Materia> materie = materiaRepo.findAll();
                    List<Aula> aule = aulaRepo.findAll();

                    Materia analisi = materie.stream().filter(m -> "ANL101".equals(m.getCodice())).findFirst().orElse(null);
                    Materia programmazione = materie.stream().filter(m -> "INF101".equals(m.getCodice())).findFirst().orElse(null);
                    Materia economia = materie.stream().filter(m -> "ECO201".equals(m.getCodice())).findFirst().orElse(null);

                    Aula aulaA = aule.stream().filter(a -> "Aula A".equals(a.getNome())).findFirst().orElse(null);
                    Aula lab1 = aule.stream().filter(a -> "Laboratorio 1".equals(a.getNome())).findFirst().orElse(null);
                    Aula aulaB = aule.stream().filter(a -> "Aula B".equals(a.getNome())).findFirst().orElse(null);

                    Lezione l1 = new Lezione();
                    l1.setMateria(analisi);
                    l1.setAula(aulaA);
                    l1.setDocente("Prof. Rossi");
                    l1.setGiornoSettimana("MON");
                    l1.setOraInizio(LocalTime.of(9, 0));
                    l1.setOraFine(LocalTime.of(11, 0));
                    l1.setNote("Lezione introduttiva");

                    Lezione l2 = new Lezione();
                    l2.setMateria(programmazione);
                    l2.setAula(lab1);
                    l2.setDocente("Prof.ssa Bianchi");
                    l2.setGiornoSettimana("TUE");
                    l2.setOraInizio(LocalTime.of(10, 0));
                    l2.setOraFine(LocalTime.of(12, 0));
                    l2.setNote("Laboratorio base");

                    Lezione l3 = new Lezione();
                    l3.setMateria(economia);
                    l3.setAula(aulaB);
                    l3.setDocente("Prof. Verdi");
                    l3.setGiornoSettimana("WED");
                    l3.setOraInizio(LocalTime.of(14, 0));
                    l3.setOraFine(LocalTime.of(16, 0));
                    l3.setNote("Modulo 1");

                    lezioneRepo.saveAll(List.of(l1, l2, l3));
                    System.out.println("✅ Lezioni create.");
                }

	            System.out.println("\n🎯 Inizializzazione completata con successo ✅");
	        };
	    }

    private static String guessCategoria(Evento evento) {
        String text = ((evento.getTitolo() == null ? "" : evento.getTitolo()) + " " +
                (evento.getDescrizione() == null ? "" : evento.getDescrizione()) + " " +
                (evento.getLuogo() == null ? "" : evento.getLuogo())).toLowerCase();
        if (text.contains("sport")) return "sport";
        if (text.contains("volontariat")) return "volontariato";
        if (text.contains("musica") || text.contains("concerto") || text.contains("cultura")) return "cultura";
        if (text.contains("career") || text.contains("job") || text.contains("lavoro")) return "carriera";
        return "accademico";
    }

    private static String guessUniversita(Evento evento) {
        String text = ((evento.getTitolo() == null ? "" : evento.getTitolo()) + " " +
                (evento.getLuogo() == null ? "" : evento.getLuogo())).toLowerCase();
        if (text.contains("unife")) return "Università di Ferrara (UNIFE)";
        if (text.contains("unibo")) return "Università di Bologna (UNIBO)";
        if (text.contains("unimi")) return "Università di Milano (UNIMI)";
        if (text.contains("unipi")) return "Università di Pisa (UNIPI)";
        if (evento.getCreatore() != null &&
                evento.getCreatore().getDipartimento() != null &&
                evento.getCreatore().getDipartimento().getUniversita() != null) {
            return evento.getCreatore().getDipartimento().getUniversita().getNome();
        }
        return "Università di Ferrara (UNIFE)";
    }
}
