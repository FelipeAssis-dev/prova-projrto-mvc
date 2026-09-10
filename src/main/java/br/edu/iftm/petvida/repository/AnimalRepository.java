package br.edu.iftm.petvida.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import br.edu.iftm.petvida.model.Animal;
import br.edu.iftm.petvida.model.Tutor;

@Repository
public class AnimalRepository {

    private final JdbcTemplate jdbc;

    public AnimalRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // JOIN obrigatório: uma única consulta traz o animal E o tutor dele
    private RowMapper<Animal> animalComTutorMapper = (rs, rowNum) -> {
        Tutor tutor = new Tutor(
                rs.getInt("id_tutor"),
                rs.getString("t_nome"),
                rs.getString("telefone")
        );
        return new Animal(
                rs.getInt("id_animal"),
                rs.getString("a_nome"),
                rs.getString("especie"),
                rs.getInt("idade"),
                tutor
        );
    };

    public Animal buscarPorId(int id) {
        String sql = "SELECT a.id_animal, a.nome AS a_nome, a.especie, a.idade, "
                + "t.id_tutor, t.nome AS t_nome, t.telefone "
                + "FROM animal a JOIN tutor t ON a.tutor_id_tutor = t.id_tutor "
                + "WHERE a.id_animal = ?";
        return jdbc.queryForObject(sql, animalComTutorMapper, id);
    }

    public int contarAnimais() {
        String sql = "SELECT COUNT(*) FROM animal";
        return jdbc.queryForObject(sql, Integer.class);
    }

    public double mediaIdade() {
        // CAST evita a armadilha do AVG inteiro citada na prova
        String sql = "SELECT AVG(CAST(idade AS DOUBLE)) FROM animal";
        return jdbc.queryForObject(sql, Double.class);
    }

    public String animalMaisVelho() {
        String sql = "SELECT nome FROM animal ORDER BY idade DESC LIMIT 1";
        return jdbc.queryForObject(sql, String.class);
    }

    public void salvar(Animal animal) {
        String sql = "INSERT INTO animal (id_animal, nome, especie, idade, tutor_id_tutor) "
                + "VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, animal.getId(), animal.getNome(), animal.getEspecie(),
                animal.getIdade(), animal.getTutor().getId());
    }
}