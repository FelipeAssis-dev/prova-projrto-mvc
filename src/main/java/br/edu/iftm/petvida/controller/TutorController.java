package br.edu.iftm.petvida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import br.edu.iftm.petvida.model.Tutor;
import br.edu.iftm.petvida.repository.TutorRepository;

@Controller
public class TutorController {

    private final TutorRepository tutorRepository;

    public TutorController(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    @GetMapping("/tutor_52")
    public String tutor(Model model) {
        Tutor tutor = tutorRepository.buscarPorId(152);
        int qtd = tutorRepository.contarAnimaisDoTutor(152);
        model.addAttribute("nomeTutor", tutor.getNome());
        model.addAttribute("telefoneTutor", tutor.getTelefone());
        model.addAttribute("qtdAnimais", String.valueOf(qtd));
        return "tutor.html";
    }
}