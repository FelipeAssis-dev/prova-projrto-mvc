package br.edu.iftm.petvida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import br.edu.iftm.petvida.model.Animal;
import br.edu.iftm.petvida.repository.AnimalRepository;

@Controller
public class FichaController {

    private final AnimalRepository animalRepository;

    public FichaController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @GetMapping("/ficha_52")
    public String ficha(Model model) {
        Animal animal = animalRepository.buscarPorId(152);
        model.addAttribute("nomeAnimal", animal.getNome());
        model.addAttribute("especie", animal.getEspecie());
        model.addAttribute("idade", String.valueOf(animal.getIdade()));
        model.addAttribute("nomeTutor", animal.getTutor().getNome());
        model.addAttribute("telefoneTutor", animal.getTutor().getTelefone());
        return "ficha.html";
    }
}