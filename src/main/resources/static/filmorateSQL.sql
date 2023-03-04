SELECT name, last_name from USERS;

SELECT name from FILMS;

SELECT FILM.name FROM LIKES
LEFT JOIN FILMS ON FILMS.filmID=LIKES.filmID
GROUP BY LIKES.filmID
ORDER BY count(LIKES.userID)
LIMIT 15

SELECT usr.name,frnd.name FROM FRIENDS
INNER JOIN USERS as usr on usr.userID=FRIENDS.friendID
INNER JOIN USERS as frnd on frnd.userID=FRIENDS.friendID
INNER JOIN FRIENDS_STATUS on STATUS.statusID=FRIENDS.statusID
where FRIENDS_STATUS.status="AFFIRMATED"


