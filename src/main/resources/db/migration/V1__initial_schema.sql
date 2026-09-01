/* =========================================
   V1 - Initial Fitness Application Schema
   ========================================= */


/* ----------
   USERS
   ---------- */

CREATE TABLE dbo.users (
    user_id BIGINT IDENTITY(1,1) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,

    CONSTRAINT PK_users
       PRIMARY KEY (user_id),

    CONSTRAINT UQ_users_email
       UNIQUE (email),

    CONSTRAINT UQ_users_username
       UNIQUE (username)
);


/* ----------
   USER PROFILES
   ---------- */

CREATE TABLE dbo.user_profiles (
    user_id BIGINT NOT NULL,
    date_of_birth DATE NULL,
    gender VARCHAR(255) NULL,
    height_cm NUMERIC(10,2) NULL,

    CONSTRAINT PK_user_profiles
       PRIMARY KEY (user_id),

    CONSTRAINT FK_user_profiles_users
       FOREIGN KEY (user_id)
           REFERENCES dbo.users(user_id)
);


/* ----------
   WORKOUTS
   ---------- */

CREATE TABLE dbo.workouts (
    id BIGINT IDENTITY(1,1) NOT NULL,
    date DATE NOT NULL,
    day_name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT PK_workouts
      PRIMARY KEY (id),

    CONSTRAINT FK_workouts_users
      FOREIGN KEY (user_id)
          REFERENCES dbo.users(user_id)
);


/* ----------
   EXERCISES
   ---------- */

CREATE TABLE dbo.exercises (
    id BIGINT IDENTITY(1,1) NOT NULL,
    name VARCHAR(255) NULL,
    number_of_sets INT NULL,
    workout_id BIGINT NOT NULL,

    CONSTRAINT PK_exercises
       PRIMARY KEY (id),

    CONSTRAINT FK_exercises_workouts
       FOREIGN KEY (workout_id)
           REFERENCES dbo.workouts(id)
);


/* ----------
   EXERCISE SETS
   ---------- */

CREATE TABLE dbo.exercise_sets (
    id BIGINT IDENTITY(1,1) NOT NULL,
    distance NUMERIC(10,2) NULL,
    duration NUMERIC(10,2) NULL,
    number_of_reps INT NULL,
    number_of_sets INT NULL,
    weight NUMERIC(10,2) NULL,
    exercise_id BIGINT NOT NULL,

    CONSTRAINT PK_exercise_sets
       PRIMARY KEY (id),

    CONSTRAINT FK_exercise_sets_exercises
       FOREIGN KEY (exercise_id)
           REFERENCES dbo.exercises(id)
);