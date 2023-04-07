package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDBStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> get(){
        List<Mpa> mpaList = new ArrayList<>();
        SqlRowSet mpaR = jdbcTemplate.queryForRowSet("SELECT * FROM mpas");
        while(mpaR.next()) {
            Mpa mpa = new Mpa(mpaR.getInt("mpa_id"), mpaR.getString("name"));
            mpaList.add(mpa);
        }
        return mpaList;
    }

    public Mpa getFromId(int id){
        SqlRowSet mpaR = jdbcTemplate.queryForRowSet("SELECT * FROM mpas WHERE mpa_id=?", id);
        if(mpaR.next()) {
            return new Mpa(mpaR.getInt("mpa_id"), mpaR.getString("name"));
        } else {
            return null;
        }
    }
}
