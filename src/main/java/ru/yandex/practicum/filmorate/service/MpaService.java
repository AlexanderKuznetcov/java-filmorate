package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDBStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;


import java.util.List;

@Slf4j
@Service
public class MpaService {
    private MpaDBStorage mpaDBStorage;

    @Autowired
    public MpaService(MpaDBStorage mpaDBStorage) {
        this.mpaDBStorage = mpaDBStorage;
    }

    public List<Mpa> getAll() {
        return mpaDBStorage.get();
    }

    public Mpa getMpa(int mpaId) {
        Mpa mpa = mpaDBStorage.getFromId(mpaId);
        if (mpa == null) {
            log.warn(LogMessage.MPA_NOT_FOUND.getLogMassage(), mpaId);
            throw new ObjectNotFoundException(LogMessage.MPA_NOT_FOUND_EXC.getLogMassage() + mpaId);
        }
        return mpa;
    }
}