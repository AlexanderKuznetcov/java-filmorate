package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDBStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private GenreDBStorage genreDBStorage;

    @Autowired
    public GenreService(GenreDBStorage genreDBStorage) {
        this.genreDBStorage = genreDBStorage;
    }

    public List<Genre> getAll() {
        return genreDBStorage.get();
    }

    public Genre getGenre(int genreId) {
        Genre genre = genreDBStorage.getFromId(genreId);
        if (genre == null) {
            log.warn(LogMessage.GENRE_NOT_FOUND.getLogMassage(), genreId);
            throw new ObjectNotFoundException(LogMessage.GENRE_NOT_FOUND_EXC.getLogMassage() + genreId);
        }
        return genre;
    }
}
