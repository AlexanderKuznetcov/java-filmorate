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

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> get() {
        List<Film> filmsList = new ArrayList<>();
        SqlRowSet idR = jdbcTemplate.queryForRowSet("SELECT film_id FROM films");
        while (idR.next()) {
            Film film = this.getFromId(idR.getInt("film_id"));
            filmsList.add(film);
        }
        return filmsList;
    }

    public List<Film> getPopular(int count) {
        List<Film> popularFilms = new ArrayList<>();
        SqlRowSet popularFilmIdR = jdbcTemplate.queryForRowSet("SELECT film_id FROM films_rate " +
                "ORDER BY rate DESC LIMIT ?", count);
        while (popularFilmIdR.next()) {
            popularFilms.add(this.getFromId(popularFilmIdR.getInt(("film_id"))));
        }
        return popularFilms;
    }

    public Film add(Film film) {
        jdbcTemplate.execute("INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES('" + film.getName() + "', '" + film.getDescription() + "', '" + film.getReleaseDate() + "', "
                + film.getDuration() + ", " + film.getMpa().getId() + ")");
        Integer newId = null;
        SqlRowSet idR = jdbcTemplate.queryForRowSet("SELECT MAX(film_id) AS max_id FROM films");
        if (idR.next()) {
            newId = idR.getInt("max_id");
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                int genreId = genre.getId();
                SqlRowSet genreIdR = jdbcTemplate.queryForRowSet("SELECT name FROM genres WHERE genre_id=?",
                        genreId);
                if (genreIdR.next()) {
                    SqlRowSet genreIdCheckR = jdbcTemplate.queryForRowSet("SELECT film_id FROM films_genres " +
                                    "WHERE film_id=? AND genre_id=?", film.getId(), genreId);
                    if (!genreIdCheckR.next()) {
                        jdbcTemplate.execute("INSERT INTO films_genres (film_id, genre_id) VALUES(" +
                                newId + ", " + genreId + ")");
                    } else {
                        log.info("Обнаружен дубликат жанра у добавляемого фильма", genreId);
                    }
                } else {
                    log.info("Жанр id=? не найден в базе жанров", genreId);
                }
            }
        }
        jdbcTemplate.execute("INSERT INTO films_rate (film_id, rate) VALUES(" + newId + ", "
                + film.getRate() + ")");
        return this.getFromId(newId);
    }

    public void addLike(int filmId) {
        SqlRowSet rateR = jdbcTemplate.queryForRowSet("SELECT rate FROM films_rate WHERE film_id = ?", filmId);
        if (rateR.next()) {
            int rate = rateR.getInt("rate");
            rate++;
            jdbcTemplate.execute("UPDATE films_rate SET rate = " + rate + " WHERE film_id = " + filmId);
        }
    }

    public void deleteLike(int filmId) {
        SqlRowSet rateR = jdbcTemplate.queryForRowSet("SELECT rate FROM films_rate WHERE film_id = ?", filmId);
        if (rateR.next() && rateR.getInt("rate") > 0) {
            int rate = rateR.getInt("rate");
            rate--;
            jdbcTemplate.execute("UPDATE films_rate SET rate = " + rate + " WHERE film_id = " + filmId);
        }
    }

    public Film update(Film film) {
        jdbcTemplate.execute("UPDATE films SET name = '" + film.getName() + "', description = '"
                + film.getDescription() + "', release_date = '" + film.getReleaseDate() + "', duration = "
                + film.getDuration() + ", mpa_id = " + film.getMpa().getId() + " WHERE film_id = " + film.getId());
        jdbcTemplate.execute("UPDATE films_rate SET rate = " + film.getRate() + " WHERE film_id = " + film.getId());
        jdbcTemplate.execute("DELETE FROM films_genres WHERE film_id = " + film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                int genreId = genre.getId();
                SqlRowSet genreIdR = jdbcTemplate.queryForRowSet("SELECT genre_id FROM genres WHERE genre_id=?",
                        genreId);
                if (genreIdR.next()) {
                    SqlRowSet genreIdCheckR = jdbcTemplate.queryForRowSet("SELECT film_id FROM films_genres " +
                        "WHERE film_id=? AND genre_id=?", film.getId(), genreId);
                    if (!genreIdCheckR.next()) {
                        jdbcTemplate.execute("INSERT INTO films_genres (film_id, genre_id) VALUES(" +
                                film.getId() + ", " + genreId + ")");
                    } else {
                        log.info("Обнаружен дубликат жанра у добавляемого фильма", genreId);
                    }
                } else {
                    log.info("Жанр id=? не найден в базе жанров", genreId);
                }
            }
        }
        return this.getFromId(film.getId());
    }

    public Film delete(Film film) {
        jdbcTemplate.execute("DELETE FROM films WHERE film_id = " + film.getId());
        jdbcTemplate.execute("DELETE FROM films_genres WHERE film_id = " + film.getId());
        jdbcTemplate.execute("DELETE FROM films_rate WHERE film_id = " + film.getId());
        return film;
    }

    public Film getFromId(int id) {
        SqlRowSet fR = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id=?", id);
        if (fR.next()) {
            log.info("Найден фильм: id={}, name={}, description={}", fR.getInt("film_id"),
                    fR.getString("name"), fR.getString("description"));
            int mpaId = fR.getInt("mpa_id");
            List<Genre> genresList = new ArrayList<>();
            SqlRowSet genreIdR = jdbcTemplate.queryForRowSet("SELECT genre_id FROM films_genres WHERE film_id=?",
                    id);
            while (genreIdR.next()) {
                int genreId = genreIdR.getInt("genre_id");
                SqlRowSet genreNameR = jdbcTemplate.queryForRowSet("SELECT name FROM genres WHERE genre_id=?",
                        genreId);
                if (genreNameR.next()) {
                    String genreName = genreNameR.getString("name");
                    genresList.add(new Genre(genreId, genreName));
                }
            }
            int rate = 0;
            SqlRowSet lR = jdbcTemplate.queryForRowSet("SELECT rate FROM films_rate WHERE " +
                            "film_id=?", id);
            if (lR.next()) {
                rate = lR.getInt("rate");
                System.out.println(rate);
            }
            String mpaName = null;
            SqlRowSet mR = jdbcTemplate.queryForRowSet("SELECT name FROM mpas WHERE mpa_id=?",
                    mpaId);
            if (mR.next()) {
                mpaName = mR.getString("name");
                System.out.println(mpaName);
            }
            Film film = new Film(fR.getInt("film_id"),
                    fR.getString("name"), fR.getString("description"),
                    fR.getDate("release_date").toLocalDate(), fR.getInt("duration"),
                    new Mpa(fR.getInt("mpa_id"), mpaName), genresList, rate);
            return film;
        } else {
            return null;
        }
    }
}