--DROP TABLE ResourceExperiments;
--DROP TABLE AxiomExperiments;
--DROP TABLE ClassExperiments;
--DROP TABLE Instances;
--DROP TABLE Users;
--DROP TABLE Triples;
--DROP TABLE Axioms;
--DROP TABLE Resources;

CREATE TABLE IF NOT EXISTS Users (
    id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    isExpert BIT
);

CREATE TABLE IF NOT EXISTS Axioms (
    id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    axiom VARCHAR(500) NOT NULL,
    verbalization VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS AxiomExperiments (
    userId INT FOREIGN KEY REFERENCES Users(id) NOT NULL,
    axiomId INT FOREIGN KEY REFERENCES Axioms(id) NOT NULL,
    adequacy INT NOT NULL,
    fluency INT NOT NULL,
    CONSTRAINT pk_axiomExperiment PRIMARY KEY (userId, axiomId)
);

CREATE TABLE IF NOT EXISTS Instances (
    id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    instanceOf INT FOREIGN KEY REFERENCES Axioms NOT NULL,
    triple VARCHAR(500) NOT NULL,
    verbalization VARCHAR(500) NOT NULL,
    correctInstance BIT NOT NULL
);

CREATE TABLE IF NOT EXISTS ClassExperiments (
    userId INT FOREIGN KEY REFERENCES Users(id) NOT NULL,
    axiomId INT FOREIGN KEY REFERENCES Axioms(id) NOT NULL,
    usersChoice INT FOREIGN KEY REFERENCES Instances(id) NOT NULL,
    CONSTRAINT pk_classExperiment PRIMARY KEY (userId, axiomId)
);

CREATE TABLE IF NOT EXISTS Resources (
    id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    resource VARCHAR(500) NOT NULL, -- won't be used for experiments - just to store the actual resource
    verbalization VARCHAR(1000) NOT NULL
);

CREATE TABLE IF NOT EXISTS Triples (
    id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    aggregatesResource INT FOREIGN KEY REFERENCES Resources(id) NOT NULL,
    triple VARCHAR(500) NOT NULL,
    verbalization VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS ResourceExperiments (
    userId INT FOREIGN KEY REFERENCES Users(id) NOT NULL,
    resourceId INT FOREIGN KEY REFERENCES Resources(id),
    adequacy INT NOT NULL,
    fluency INT NOT NULL,
    completeness INT NOT NULL,
    CONSTRAINT pk_resourceExperiment PRIMARY KEY (userId, resourceId)
);

--INSERT INTO Axioms(axiom, verbalization) VALUES ('axiom1','verb1'), ('axiom2','verb2');
--INSERT INTO Instances(instanceOf, triple, verbalization, correctInstance) VALUES ((SELECT id FROM Axioms where axiom='axiom1'), 'triple1', 'verb1', 0), ((SELECT id FROM Axioms where axiom='axiom1'), 'triple2', 'verb2', 1), ((SELECT id FROM Axioms where axiom='axiom2'), 'triple3', 'verb3', 0), ((SELECT id FROM Axioms where axiom='axiom2'), 'triple4', 'verb4', 1);
--INSERT INTO Resources(verbalization) VALUES ('resverb1'), ('resverb2');
--INSERT INTO Triples(aggregatesResource, triple, verbalization) VALUES ((SELECT id FROM Resources WHERE verbalization='resverb1'), 'triple1', 'verb1'), ((SELECT id FROM Resources WHERE verbalization='resverb1'), 'triple2', 'verb2'), ((SELECT id FROM Resources WHERE verbalization='resverb2'), 'triple3', 'verb3'), ((SELECT id FROM Resources WHERE verbalization='resverb2'), 'triple3', 'verb3');
