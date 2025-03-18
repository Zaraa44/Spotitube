/* Insert Users */
INSERT INTO USERS (USERNAME, PASSWORD, TOKEN)
VALUES 
    ('user1', 'password123', NULL),
    ('user2', 'securepass456', NULL);

/* Insert Songs */
INSERT INTO TRACKS (TRACK_ID, TITLE, PERFORMER, ALBUM, PLAYCOUNT, PUBLICATIONDATE, DESCRIPTION, OFFLINEAVAILABLE, DURATION)
VALUES
    (1, 'Song A', 'Artist 1', 'Album X', 10, '2020-01-01', 'A great song', 1, 210),
    (2, 'Song B', 'Artist 2', 'Album Y', 5, '2021-05-12', 'Another great song', 0, 190),
    (3, 'Song C', 'Artist 3', NULL, 8, '2019-07-23', 'A cool track', 1, 220),
    (4, 'Song D', 'Artist 1', 'Album X', 15, '2018-11-10', 'A hit song', 0, 200),
    (5, 'Song E', 'Artist 4', 'Album Z', 3, '2022-03-15', 'A relaxing melody', 1, 250),
    (6, 'Song F', 'Artist 2', NULL, 12, '2017-09-30', 'An energetic track', 0, 180),
    (7, 'Song G', 'Artist 5', 'Album W', 7, '2023-02-10', 'A fresh release', 1, 230),
    (8, 'Song H', 'Artist 3', 'Album Y', 9, '2020-06-05', 'A deep song', 0, 210),
    (9, 'Song I', 'Artist 6', NULL, 4, '2016-12-25', 'A nostalgic tune', 1, 195),
    (10, 'Song J', 'Artist 7', 'Album V', 20, '2024-01-01', 'A chart-topper', 0, 240);
