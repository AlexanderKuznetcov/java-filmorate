package ru.yandex.practicum.filmorate.message;

public enum LogMessage {
    GET_FILMS("Получен запрос на получение списка фильмов"),
    GET_USERS("Получен запрос на получение списка пользователей"),
    ADD_FILM("Получен запрос на добавление фильма"),
    ADD_USER("Получен запрос на добавление пользователя"),
    ADD_FILM_DONE("Фильм добавлен"),
    ADD_USER_DONE("Пользователь добавлен"),
    UPDATE_FILM("Получен запрос на обновление фильма"),
    UPDATE_USER("Получен запрос на обновление пользователя"),
    UPDATE_FILM_DONE("Фильм обновлен"),
    UPDATE_USER_DONE("Пользователь обновлен"),
    FILM_NOT_FOUND("В списке отсутствует фильм с id = "),
    USER_NOT_FOUNR("В списке отсутствует пользователь с id = ")

    ;

    private String logMassage;

    LogMessage (String logMassage) {
        this.logMassage = logMassage;
    }

    public String getLogMassage() { // в кейсах не хочет принимать Endpoint.GET_PRIORITIZED_TASKS.getPath()
        return logMassage;
    }
}
