package br.edu.iftm.petvida.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import br.edu.iftm.petvida.repository.AnimalRepository;

@Controller
public class ResumoController {

    private final AnimalRepository animalRepository;
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public ResumoController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @GetMapping("/resumo_52")
    public String resumo(Model model) {
        int total = animalRepository.contarAnimais();
        double media = animalRepository.mediaIdade();
        String maisVelho = animalRepository.animalMaisVelho();
        String agora = LocalDateTime.now().format(FORMATO);

        model.addAttribute("totalAnimais", String.valueOf(total));
        model.addAttribute("mediaIdade", String.format("%.2f", media));
        model.addAttribute("animalMaisVelho", maisVelho);
        model.addAttribute("dataHora", agora);
        return "resumo.html";
    }
}