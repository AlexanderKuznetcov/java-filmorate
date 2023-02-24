package ru.yandex.practicum.filmorate.message;

public enum LogMessage {
    GET_FILMS("Получен запрос на получение списка фильмов"),
    GET_FILM ("Получен запрос на получение фильма с id = "),
    GET_USERS("Получен запрос на получение списка пользователей"),
    GET_USER ("Получен запрос на получение пользователя с id = "),
    ADD_FILM("Получен запрос на добавление фильма"),
    ADD_USER("Получен запрос на добавление пользователя"),
    ADD_FILM_DONE("Фильм добавлен c id = "),
    ADD_USER_DONE("Пользователь добавлен c id = "),
    UPDATE_FILM("Получен запрос на обновление фильма c id = "),
    UPDATE_USER("Получен запрос на обновление пользователя c id = "),
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
    NOT_VALID_DURATION(" Продолжительность отрицательная или 0."),
    ADD_LIKE("Получен запрос на добавление лайка фильму с id = "),
    ADD_LIKE_DONE("Лайк фильму поставлен"),
    DEL_LIKE("Получен запрос на удаление лайка фильму с id = "),
    DEL_LIKE_DONE("Лайк фильму удален"),
    GET_POPULAR("Получен запрос на получение списка популярных фильмов в количесвте = "),
    ADD_FRIEND("Получен запрос на добавление друга с id = "),
    ADD_FRIEND_DONE("Друг добавлен"),
    DEL_FRIEND("Получен запрос на удаление друга с id = "),
    DEL_FRIEND_DONE("Друг удален"),
    GET_FRIENDS("Получен запрос на получение друзей пользователя с id = "),
    GET_COMMON_FRIENDS("Получен запрос на получение общих друзей пользователей с id "),
    UNEXPECTED_ERROR("Произошла непредвиденная ошибка.");

    private String logMassage;

    LogMessage (String logMassage) {
        this.logMassage = logMassage;
    }

    public String getLogMassage() {
        return logMassage;
    }
}