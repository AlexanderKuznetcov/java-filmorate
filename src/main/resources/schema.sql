CREATE TABLE IF NOT EXISTS mpas(
    mpa_id serial PRIMARY KEY,
    name VARCHAR(5) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films(
    film_id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    mpa_id INT REFERENCES mpas(mpa_id) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres(
    genre_id serial PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_genres(
    film_id INT REFERENCES films(film_id) NOT NULL,
    genre_id INT REFERENCES genres(genre_id) NOT NULL,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users(
    user_id serial PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS films_likes(
    user_id INT REFERENCES users(user_id) NOT NULL,
    film_id INT REFERENCES films(film_id) NOT NULL,
	PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS friends(
    user_id INT REFERENCES users(user_id) NOT NULL,
    friend_id INT NOT NULL,
	PRIMARY KEY (user_id, friend_id)
);
