# CinemaBookingApp
Cinema Booking App which allows users to reserve a ticket to the cinema. Developed in Android Studio
Proiect BD

1.	Descrierea cerintelor
Aplicatie cu baze de date relationale avand interfata vizuala pentru rezervarea biletelor la film.
Aplicatia va fi implementata in Android Studio si va avea drept suport smartphon-urile, tabletele
si alte gadget-uri ce permit rularea sistemului de operare Android.

Aplicatia trebuie sa permita utilizatorilor sa si poata crea un cont si sa se logheze apoi cu contul creat,
dupa care acestia pot cauta prin lista de filme si mai apoi sa rezerve un bilet pentru filmul ales.
Pentru o cautare mai usoara acestia vor beneficia de anumite filtre de cautare si vor avea si posibilitatea de ordonare a filmelor.
Aplicatia va avea si o parte de administrare unde un “super-user” va avea acces la controlul deplin al bazei de date 
pentru a adauga/sterge/edita filme, cinema-uri , show uri corespunzatoare unui cinema ce gazduieste un film la un anume moment de timp,
precum si posibilitatea de a vedea rezervarile facute de alti useri si de a le accepta sau refuza.

Aplicatia va permite insert, update si delete aspura bazei de date din perspectiva unui administrator,
cat si interogari din perspectiva unui user ce doreste sa rezerve un bilet.


2.	Proiectarea bazei de date

Baza de date va avea numele de “Cinema.db” si aceasta va contine 7 tabele pentru stocarea datelor aplicatiei.
Tabelele vor fi:
•	users_table (se vor stoca date despre utilizatorii aplicatiei)
•	cinema_table(se vor stoca date despre cinema-urile disponibile)
•	movies_table(se vor stoca date despre filmele disponibile)
•	showings_table(se vor stoca date despre show-urile disponibille)
•	booking(se vor stoca date despre rezervarile facute)
•	favourite(se vor stoca date despre filmele favorite ale utilizatorilor)
•	movie_reviews_table(se vor stoca date reprezentand comentariile/parerile userilor in legatura cu un anumit film)

users_table va structura formata din 4 campuri:
(ID – INTEGER PRIMARY KEY,
 EMAIL - TEXT UNIQUE & NOT NULL,
 PASSWORD - TEXT NOT NULL,
 ADMIN - TEXT CHECK & NOT NULL)

movies_table va structura formata din 7 campuri:
(ID – INTEGER PRIMARY KEY,
 NAME - TEXT UNIQUE & NOT NULL,
 CATEGORY - TEXT NOT NULL,
 DESCRIPTION – TEXT,
 PRICE – INTEGER NOT NULL, 
 LENGTH- INTEGER NOT NULL,
 IMAGE - BLOB)


cinemas_table va structura formata din 5 campuri:
(ID – INTEGER PRIMARY KEY,
 NAME - TEXT  NOT NULL,
 MANAGER - TEXT NOT NULL,
 ADDRESS - TEXT UNIQUE & NOT NULL,
 CAPACITY – INTEGER NOT NULL)

showings _table va structura formata din 5 campuri:
(ID – INTEGER PRIMARY KEY,
 MOVIE_ID - INTEGER NOT NULL,
 CINEMA_ID – INTEGER NOT NULL,
 FROM_DATE – DATE & NOT NULL,
 TO_DATE – DATE NOT NULL
 START_TIME - TIME  NOT NULL
 FOREIGN KEY MOVIE_ID REFERENCES movies_table(ID),
 FOREIGN KEY CINEMA_ID REFERENCES cinemas_table(ID),
 UNIQUE(MOVIE_ID, CINEMA_ID)
)

bookings va structura formata din 8 campuri:
(ID – INTEGER PRIMARY KEY,
 USER_ID - INTEGER NOT NULL,
 SHOWING_ID – INTEGER NOT NULL,
 BOOKING_FOR_DATE– DATE & NOT NULL,
 BOOKING_MADE_DATE– DATE NOT NULL
 BOOKING_MADE_TIME- TIME  NOT NULL
 SEAT_COUNT- INTEGER  NOT NULL
 CONFIRMED- TEXT CHECK & NOT NULL
 FOREIGN KEY USER_ID REFERENCES users_table (ID),
 FOREIGN KEY SHOWING_ID REFERENCES showing_table(ID),
)


favourite va structura formata din 2 campuri:
(MOVIE_ID- INTEGER NOT NULL, 
 USER_ID– INTEGER NOT NULL,
 FOREIGN KEY MOVIE_ID REFERENCES movies_table(ID),
 FOREIGN KEY USER_ID REFERENCES users_table (ID),
 PRIMARY_KEY (MOVIE_ID, USER_ID),
)

movie_reviews_table va structura formata din 3 campuri:
(ID – INTEGER PRIMARY KEY,
 MOVIE_ID- INTEGER NOT NULL, 
 FOREIGN KEY MOVIE_ID REFERENCES movies_table(ID),
)



Relatiile dintre tabele sunt urmatoarele:
•	Relatie 1:N dintre tabel users_table si bookings (un user poate face niciuna,una sau mai multe rezervari.
O rezervare are doar un singur user)
•	Relatie 1:N dintre showings_table si bookings(Un show poate avea mai multe rezervari.
O rezervare contine doar un singur show)
•	Relatie 1:N dintre movies_table si movie_review_table(Un film poate avea niciunul, unul sau mai multe pareri/comentarii.
Un comentariu anume apartine unui singur film)
•	Relatie N:N dintre users_table si favourite (un user poate avea niciunul,unul sau mai multe filme favorite.
Un film poate fi favorit pentru niciunul, unul sau mai multi useri)
•	Relatie N:N dintre movies_table si cinemas_table (un cinema poate difuza niciunul,unul sau mai multe filme.
Un film poate fi difuzat de niciunul, unul sau mai multe cinemauri)


3.	Proiectarea interfetei vizuale si implementarea acesteia intr-un mediu de programare.Functionarea aplicatiei
 
Aplicatia se imparte in 2 parti:

a)	Partea de administrare, in care administratorul aplicatiei are drepturi de acces si controlul bazei de date.
Acesta poate sa adauge filme, sa adauge cinema-uri care gazduiesca filmele si sa adauge show-uri in care se specifica filmul si 
cinema-ul ce difuzeaza filmul resprectiv, precum si informatii referitoare la data, ora etc.Tot administratorul poate vedea 
rezervarile facute si sa accepte sau sa refuze o rezervare.
b)	Partea de user, care este publica, unde userii pot sa isi creeze cont si sa se logheze cu contul creat in aplicatie,
iar acestia pot vedea toate filmele disponibile, au posibilitatea de cautare cu ajutorul unor filtre pentru a gasi cu usurinta
filmul potrivit si in final au posibilitatea de rezervare a filmului dorit si nu in ultimul rand acestia pot vedea istoricul
rezervarilor si daca rezervarea/rezervarile lor au fost acceptate de catre administratorul aplicatiei



4.	Interogari

•	Afiseaza toate id-urile si numele filmelor ce apartin cel putin unui show 
SELECT m.ID , m.NAME FROM movies_table where m.ID
IN (select DISTINCT m2.ID from movies_table m2, showing_movies_table s
WHERE m2.id = s.MOVIE_ID)

•	selecteaza toate filmele ce apartin unui show unde locurile rezervate la fiecare show este peste media locurilor rezervate
la toate show-urile
select DISTINCT s.MOVIE_ID
from showing_movies_table s
where s.id in (select b.SHOWING_ID from showing_movies_table s2, bookings b
where s2.ID = b.SHOWING_ID
group by s2.ID
having sum(SEAT_COUNT) >= (select avg(SEAT_COUNT) from bookings)
)


•	Selecteaza toti userii care au cel putin o rezervare facuta 

SELECT u.ID, u.EMAIL
FROM users_table u
where u.ID 
in (select DISTINCT u2.ID from users_table u2, bookings b 
where u2.ID = b.USER_ID)

•	Selecteaza toate show-urile ce nu au rezervari facute
select s.ID, s.MOVIE_ID, S.CINEMA_ID
from showing_movies_table s
where s.id not in (select distinct s2.id
from showing_movies_table s2, bookings b\n
where s2.id = b.SHOWING_ID)



 	
•	Selecteaza toate show-urile din cinema-ul ales 
SELECT S.MOVIE_ID
FROM showing_movies_table S 
INNER JOIN cinemas_table C 
ON S.CINEMA_ID = C.ID 
WHERE C.ID = ?cinema.id 




•	Selecteaza id ul si numele filmelor din categoria precizata 
SELECT ID, NAME 
FROM movies_table 
WHERE CATEGORY = ?category 

  
•	Selecteaza toate filmele ce au pretul sub ?price
SELECT ID FROM movies_table WHERE PRICE < ?price ;


•	Selecteaza toate filmele ce se difuzeaza in cinema-ul ?cinema_id si au pretul sub ?price
SELECT DISTINCT MOVIE_ID FROM showing_movies_table S
INNER JOIN cinemas_table C
ON S.CINEMA_ID = C.ID
INNER JOIN movies_table M
ON S.MOVIE_ID = M.ID
WHERE C.ID = cinema_id  AND M.PRICE <= ?price;



•	Selecteaza toate filmele ce sunt din categoria ?category si au pretul sub ?price
SELECT * 
FROM movies_table 
WHERE CATEGORY = ?category AND PRICE < ?price;




•	Selecteaza media rating-urilor unui Film
SELECT AVG(MARK) 
FROM movies_reviews_table MR 
where MR.MOVIE_ID = ?movie_id


•	Selecteaza toate filmele ce sunt din categoria specificata si se difuzeaza in cinema-ul specificat
SELECT DISTINCT MOVIE_ID
FROM showing_movies_table S
INNER JOIN cinemas_table C
ON S.CINEMA_ID = C.ID
INNER JOIN movies_table M
ON S.MOVIE_ID = M.ID
WHERE M.CATEGORY = ?category AND C.ID = ?cinema_id




•	Selecteaza toate filmele ce se difuzeaza in cinema-ul specificat ?cinema_id, au categoria ?category si pretul sub ?price

SELECT DISTINCT MOVIE_ID 
FROM showing_movies_table S
INNER JOIN cinemas_table C
ON S.CINEMA_ID = C.ID
INNER JOIN movies_table M
ON S.MOVIE_ID = M.ID
WHERE M.CATEGORY = ?category AND C.ID = ?cinema_id AND M.PRICE < ?price


