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
    USER_NOT_FOUND("В списке отсутствует пользователь с id = "),
    VALIDATION_FAIL("Валидация не прошла!"),
    NOT_VALID_EMAIL(" Электронная почта пустая или без @."),
    NOT_VALID_LOGIN(" Логин пустой или содержит пробел."),
    NOT_VALID_BIRTHDAY(" Дата рождения не может быть в будущем."),
    NOT_VALID_FILM_NAME(" Пустое имя фильма."),
    NOT_VALID_DESCRIPTION(" Описание более 200 символов."),
    NOT_VALID_RELEASE_DATE(" Слишком ранний релиз."),
    NOT_VALID_DURATION(" Продолжительность отрицательная или 0.");




    private String logMassage;

    LogMessage (String logMassage) {
        this.logMassage = logMassage;
    }

    public String getLogMassage() {
        return logMassage;
    }
}
