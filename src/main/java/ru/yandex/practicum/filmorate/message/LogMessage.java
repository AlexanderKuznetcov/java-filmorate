package ru.yandex.practicum.filmorate.message;

public enum LogMessage {
    GET_FILMS("GET: Получен запрос на получение списка всех фильмов"),
    GET_MPAS("GET: Получен запрос на получение списка всех возрастных рейтингов"),
    GET_GENRES("GET: Получен запрос на получение списка всех жанров"),
    GET_FILM("GET: Получен запрос на получение фильма с id = {}"),
    GET_MPA("GET: Получен запрос на получение возрастного рейтинга с id = {}"),
    GET_GENRE("GET: Получен запрос на получение жанра с id = {}"),
    GET_USERS("GET: Получен запрос на получение списка всех пользователей"),
    GET_USER("GET: Получен запрос на получение пользователя с id = {}"),
    ADD_FILM("POST: Получен запрос на добавление нового фильма"),
    ADD_USER("POST: Получен запрос на добавление нового пользователя"),
    ADD_FILM_DONE("Фильм добавлен c id = {}"),
    ADD_USER_DONE("Пользователь добавлен c id = {}"),
    UPDATE_FILM("PUT: Получен запрос на обновление фильма c id = {}"),
    UPDATE_USER("PUT: Получен запрос на обновление пользователя c id = {}"),
    UPDATE_FILM_DONE("Фильм с id= {} обновлен"),
    UPDATE_USER_DONE("Пользователь с id= {} обновлен"),
    FILM_NOT_FOUND("В списке отсутствует фильм с id = {}"),
    USER_NOT_FOUND("В списке отсутствует пользователь с id = {}"),
    MPA_NOT_FOUND("В списке отсутствует возрастной рейтинг с id = {}"),
    GENRE_NOT_FOUND("В списке отсутствует жанр с id = {}"),
    FILM_NOT_FOUND_EXC("В списке отсутствует фильм с id = "),
    USER_NOT_FOUND_EXC("В списке отсутствует пользователь с id = "),
    MPA_NOT_FOUND_EXC("В списке отсутствует возрастной рейтинг с id = "),
    GENRE_NOT_FOUND_EXC("В списке отсутствует жанр с id = "),
    VALIDATION_FAIL("Валидация не прошла!"),
    NOT_VALID_EMAIL("Электронная почта пустая или без @"),
    NOT_VALID_LOGIN("Логин пустой или содержит пробел"),
    NOT_VALID_BIRTHDAY("Дата рождения не может быть в будущем"),
    NOT_VALID_FILM_NAME("Пустое имя фильма"),
    NOT_VALID_DESCRIPTION("Описание более 200 символов"),
    NOT_VALID_RELEASE_DATE("Слишком ранний релиз"),
    NOT_VALID_DURATION("Продолжительность отрицательная или 0"),
    ADD_LIKE("POST: Получен запрос на добавление лайка фильму с id = {}"),
    ADD_LIKE_DONE("Лайк фильму c id = {} поставлен пользователем с id = {}"),
    DEL_LIKE("DELETE: Получен запрос на удаление лайка фильму с id = {}"),
    DEL_LIKE_DONE("Лайк у фильма c id = {} удалил пользователь с id = {}"),
    GET_POPULAR("GET: Получен запрос на получение списка популярных фильмов в количестве = {}"),
    ADD_FRIEND("POST: Получен запрос на добавление пользователю с id = {} друга с id = {}"),
    ADD_FRIEND_DONE("Пользователю с id = {} добавлен друг с id = {}"),
    DEL_FRIEND("DELETE: Получен запрос на удаление у пользователя с id = {} друга с id = {}"),
    DEL_FRIEND_DONE("У пользователя с id = {} удален друг с id = {}"),
    GET_FRIENDS("GET: Получен запрос на получение друзей пользователя с id = {}"),
    GET_COMMON_FRIENDS("GET: Получен запрос на получение общих друзей пользователей с id = {} и id = {}"),
    UNEXPECTED_ERROR("Произошла непредвиденная ошибка");

    private String logMassage;

    LogMessage(String logMassage) {
        this.logMassage = logMassage;
    }

    public String getLogMassage() {
        return logMassage;
    }
}