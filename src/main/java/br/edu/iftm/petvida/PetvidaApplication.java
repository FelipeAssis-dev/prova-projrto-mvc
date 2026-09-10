package br.edu.iftm.petvida;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.edu.iftm.petvida.model.Animal;
import br.edu.iftm.petvida.model.Tutor;
import br.edu.iftm.petvida.repository.AnimalRepository;
import br.edu.iftm.petvida.repository.TutorRepository;

@SpringBootApplication
public class PetvidaApplication implements CommandLineRunner {

    private final TutorRepository tutorRepository;
    private final AnimalRepository animalRepository;

    public PetvidaApplication(TutorRepository tutorRepository, AnimalRepository animalRepository) {
        this.tutorRepository = tutorRepository;
        this.animalRepository = animalRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(PetvidaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // tutores da Seção 2 — gravados antes dos animais (FK exige que o tutor já exista)
        Tutor marina = new Tutor(1, "Marina Alves", "34 99101-0001");
        Tutor carlos = new Tutor(2, "Carlos Prado", "34 99101-0002");
        tutorRepository.salvar(marina);
        tutorRepository.salvar(carlos);

        // seu tutor (semente NN=52)
        Tutor felipe = new Tutor(152, "Felipe", "34 95252-5252");
        tutorRepository.salvar(felipe);

        // animais da Seção 2
        animalRepository.salvar(new Animal(2, "Mimi", "gato", 3, marina));
        animalRepository.salvar(new Animal(3, "Thor", "cao", 1, carlos));
        animalRepository.salvar(new Animal(4, "Lila", "gato", 11, carlos));

        // seu animal (semente NN=52)
        animalRepository.salvar(new Animal(152, "Pet_52", "cao", 52, felipe));
    }
}