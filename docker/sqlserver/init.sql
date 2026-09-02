IF DB_ID('FitnessTracker') IS NULL
BEGIN
    CREATE DATABASE FitnessTracker;
END;
GO