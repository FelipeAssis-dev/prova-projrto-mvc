package br.edu.iftm.petvida.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import br.edu.iftm.petvida.repository.AnimalRepository;

@Controller
public class ConsultaController {                          // <== ITEM 1

    @Autowired
    AnimalRepository repository;

    @GetMapping("consulta")                                // <== ITEM 2
    public String consulta(Model model) {
        model.addAttribute("animal", repository.buscarPorId(2));   // <== ITEM 3
        return "consulta.html";                            // <== ITEM 4
    }
}