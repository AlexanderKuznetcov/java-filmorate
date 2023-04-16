package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDBStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> get() {
        return jdbcTemplate.query("SELECT * FROM mpas", new MpaMapper());
    }

    public Mpa getFromId(int id) {
        return jdbcTemplate.query("SELECT * FROM mpas WHERE mpa_id=?", new Object[]{id},
                new MpaMapper()).stream().findAny().orElse(null);
    }
}
