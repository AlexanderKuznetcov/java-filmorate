package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.List;

@Component
public class FilmDbStorage implements Storage<Film> {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> get(){
        List<Film> filmsList = new ArrayList<>();
        SqlRowSet idR = jdbcTemplate.queryForRowSet("SELECT film_id FROM films");
        while(idR.next()) {
            Film film = this.getFromId(idR.getInt("film_id"));
            filmsList.add(film);
        }
        return filmsList;
    }

    public Film add(Film film){
        jdbcTemplate.execute("INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES('"+ film.getName() +"', '" + film.getDescription() + "', '" + film.getReleaseDate() + "', "
                + film.getDuration() + ", " + film.getMpa().getId() + ")");
        Integer newId = null;
        SqlRowSet idR = jdbcTemplate.queryForRowSet("SELECT MAX(film_id) AS max_id FROM films");
        if(idR.next()) {
            newId = idR.getInt("max_id");
        }
        for (Genre genre : film.getGenres()){
            int genreId = genre.getId();
            SqlRowSet genreIdR = jdbcTemplate.queryForRowSet("SELECT genre_id FROM genres WHERE genre_id=?", genreId);
            if(genreIdR.next()) {
                jdbcTemplate.execute("INSERT INTO films_genres (film_id, genre_id) VALUES(" +
                        newId + ", " + genreIdR.getInt("genre_id") + ")");
                System.out.println("жанр добавлен");
            } else {
                log.info("Жанр id=? не найден в базе жанров", genreId);
            }
        }
        SqlRowSet iR = jdbcTemplate.queryForRowSet("SELECT MAX(film_id) AS max_id FROM films");
        if(iR.next()) {
            return this.getFromId(iR.getInt("max_id"));
        } else {
            return null;
        }
    }

    public Film update(Film film){
        jdbcTemplate.execute("UPDATE films SET name = " + film.getName() + ", description = "
                + film.getDescription() + ", release_date = " + film.getReleaseDate() + ", duration = "
                + film.getReleaseDate() + " WHERE film_id = " + film.getId());
        jdbcTemplate.execute("DELETE FROM films_genres WHERE film_id = " + film.getId());
//        for (String genreName : film.getGenres()){
//            SqlRowSet genreIdR = jdbcTemplate.queryForRowSet("SELECT genre_id FROM genres WHERE name = '?'", genreName);
//            if(genreIdR.next()) {
//                jdbcTemplate.execute("INSERT INTO films_genres (film_id, genre_id) VALUES(" + film.getId() +
//                        "," + genreIdR.getInt("genre_id")+ ")");
//            }
//        }
        return this.getFromId(film.getId());
    }

    public Film delete(Film film){
        jdbcTemplate.execute("DELETE FROM films WHERE film_id = " + film.getId());
        jdbcTemplate.execute("DELETE FROM films_genres WHERE film_id = " + film.getId());
        return film;
    }

    public Film getFromId(int id) {
        SqlRowSet fR = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id=?", id);
        if(fR.next()) {
            log.info("Найден фильм: id={}, name={}, description={}", fR.getInt("film_id"),
                    fR.getString("name"), fR.getString("description"));
            List<Genre> genresList = new ArrayList<>();
            SqlRowSet gR = jdbcTemplate.queryForRowSet("SELECT genre_id FROM films_genres WHERE film_id=?",
                    fR.getInt("film_id"));
            while(gR.next()) {
                System.out.println(gR.getInt("genre_id"));
                genresList.add(new Genre(gR.getInt("genre_id")));
            }
            Film film = new Film(fR.getInt("film_id"),
                    fR.getString("name"), fR.getString("description"),
                    fR.getDate("release_date").toLocalDate(), fR.getInt("duration"),
                    new Mpa(fR.getInt("mpa_id")), genresList);
            return film;
        } else {
            return null;
        }
    }
}
