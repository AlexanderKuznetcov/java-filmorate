DROP TABLE IF EXISTS friends, users;
DROP TABLE IF EXISTS genres,films, films_genres, films_rate, mpas;

CREATE TABLE IF NOT EXISTS mpas(
    mpa_id serial PRIMARY KEY,
    mpa_name VARCHAR(10) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films(
    film_id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    mpa_id INT NOT NULL REFERENCES mpas(mpa_id)
);

CREATE TABLE IF NOT EXISTS genres(
    genre_id serial PRIMARY KEY,
    genre_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_genres(
    film_id INT NOT NULL REFERENCES films(film_id),
    genre_id INT NOT NULL REFERENCES genres(genre_id),
    PRIMARY KEY (film_id,genre_id)
);

CREATE TABLE IF NOT EXISTS users(
    user_id serial PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS films_rate(
    film_id INT NOT NULL REFERENCES films(film_id),
    rate INT NOT NULL,
    PRIMARY KEY (film_id,rate)
);

CREATE TABLE IF NOT EXISTS friends(
    user_id INT NOT NULL REFERENCES users(user_id),
    friend_id INT NOT NULL,
    PRIMARY KEY (user_id,friend_id)
);