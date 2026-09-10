package br.edu.iftm.petvida.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import br.edu.iftm.petvida.model.Tutor;

@Repository
public class TutorRepository {

    private final JdbcTemplate jdbc;

    public TutorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<Tutor> tutorMapper = (rs, rowNum) -> new Tutor(
            rs.getInt("id_tutor"),
            rs.getString("nome"),
            rs.getString("telefone")
    );

    public Tutor buscarPorId(int id) {
        String sql = "SELECT * FROM tutor WHERE id_tutor = ?";
        return jdbc.queryForObject(sql, tutorMapper, id);
    }

    public int contarAnimaisDoTutor(int idTutor) {
        String sql = "SELECT COUNT(*) FROM animal WHERE tutor_id_tutor = ?";
        return jdbc.queryForObject(sql, Integer.class, idTutor);
    }

    public void salvar(Tutor tutor) {
        String sql = "INSERT INTO tutor (id_tutor, nome, telefone) VALUES (?, ?, ?)";
        jdbc.update(sql, tutor.getId(), tutor.getNome(), tutor.getTelefone());
    }
}