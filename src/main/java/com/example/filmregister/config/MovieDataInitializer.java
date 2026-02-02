package com.example.filmregister.config;

import com.example.filmregister.entity.Movie;
import com.example.filmregister.entity.MovieType;
import com.example.filmregister.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@Profile({"default-init", "demo"})
@RequiredArgsConstructor
@Slf4j
public class MovieDataInitializer implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) {
        // Create starter movies if database is empty
        if (movieRepository.count() == 0) {
            createStarterMovies();
            createStarterTVSeries();
            log.info("Starter content created successfully - {} items loaded", movieRepository.count());
        } else {
            log.info("Content already exists in database ({} items), skipping initialization", movieRepository.count());
        }
    }

    private void createStarterMovies() {
        // Movie 1
        Movie movie1 = new Movie();
        movie1.setTitle("The Shawshank Redemption");
        movie1.setDescription("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
        movie1.setReleaseYear(1994);
        movie1.setGenre("Drama");
        movie1.setDirector("Frank Darabont");
        movie1.setImageUrl("https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg");
        movie1.setActors("Tim Robbins, Morgan Freeman");
        movie1.setType(MovieType.MOVIE);
        movie1.setImdbRating(9.3);
        movieRepository.save(movie1);

        // Movie 2
        Movie movie2 = new Movie();
        movie2.setTitle("The Dark Knight");
        movie2.setDescription("When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.");
        movie2.setReleaseYear(2008);
        movie2.setGenre("Action");
        movie2.setDirector("Christopher Nolan");
        movie2.setImageUrl("https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg");
        movie2.setActors("Christian Bale, Heath Ledger, Aaron Eckhart");
        movie2.setType(MovieType.MOVIE);
        movie2.setImdbRating(9.0);
        movieRepository.save(movie2);

        // Movie 3
        Movie movie3 = new Movie();
        movie3.setTitle("Inception");
        movie3.setDescription("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.");
        movie3.setReleaseYear(2010);
        movie3.setGenre("Sci-Fi");
        movie3.setDirector("Christopher Nolan");
        movie3.setImageUrl("https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg");
        movie3.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page");
        movie3.setType(MovieType.MOVIE);
        movie3.setImdbRating(8.8);
        movieRepository.save(movie3);

        // Movie 4
        Movie movie4 = new Movie();
        movie4.setTitle("Pulp Fiction");
        movie4.setDescription("The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.");
        movie4.setReleaseYear(1994);
        movie4.setGenre("Crime");
        movie4.setDirector("Quentin Tarantino");
        movie4.setImageUrl("https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg");
        movie4.setActors("John Travolta, Uma Thurman, Samuel L. Jackson");
        movie4.setType(MovieType.MOVIE);
        movie4.setImdbRating(8.9);
        movieRepository.save(movie4);

        // Movie 5
        Movie movie5 = new Movie();
        movie5.setTitle("Forrest Gump");
        movie5.setDescription("The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75, whose only desire is to be reunited with his childhood sweetheart.");
        movie5.setReleaseYear(1994);
        movie5.setGenre("Drama");
        movie5.setDirector("Robert Zemeckis");
        movie5.setImageUrl("https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg");
        movie5.setActors("Tom Hanks, Robin Wright, Gary Sinise");
        movie5.setType(MovieType.MOVIE);
        movie5.setImdbRating(8.8);
        movieRepository.save(movie5);

        // Movie 6
        Movie movie6 = new Movie();
        movie6.setTitle("The Matrix");
        movie6.setDescription("A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.");
        movie6.setReleaseYear(1999);
        movie6.setGenre("Sci-Fi");
        movie6.setDirector("The Wachowskis");
        movie6.setImageUrl("https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg");
        movie6.setActors("Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss");
        movie6.setType(MovieType.MOVIE);
        movie6.setImdbRating(8.7);
        movieRepository.save(movie6);

        // Movie 7
        Movie movie7 = new Movie();
        movie7.setTitle("Goodfellas");
        movie7.setDescription("The story of Henry Hill and his life in the mob, covering his relationship with his wife Karen Hill and his mob partners Jimmy Conway and Tommy DeVito.");
        movie7.setReleaseYear(1990);
        movie7.setGenre("Crime");
        movie7.setDirector("Martin Scorsese");
        movie7.setImageUrl("https://image.tmdb.org/t/p/w500/aKuFiU82s5ISJpGZp7YkIr3kCUd.jpg");
        movie7.setActors("Robert De Niro, Ray Liotta, Joe Pesci");
        movie7.setType(MovieType.MOVIE);
        movie7.setImdbRating(8.7);
        movieRepository.save(movie7);

        // Movie 8
        Movie movie8 = new Movie();
        movie8.setTitle("Fight Club");
        movie8.setDescription("An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.");
        movie8.setReleaseYear(1999);
        movie8.setGenre("Drama");
        movie8.setDirector("David Fincher");
        movie8.setImageUrl("https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg");
        movie8.setActors("Brad Pitt, Edward Norton, Helena Bonham Carter");
        movie8.setType(MovieType.MOVIE);
        movie8.setImdbRating(8.8);
        movieRepository.save(movie8);

        // Movie 9
        Movie movie9 = new Movie();
        movie9.setTitle("The Lord of the Rings: The Return of the King");
        movie9.setDescription("Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.");
        movie9.setReleaseYear(2003);
        movie9.setGenre("Fantasy");
        movie9.setDirector("Peter Jackson");
        movie9.setImageUrl("https://image.tmdb.org/t/p/w500/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg");
        movie9.setActors("Elijah Wood, Viggo Mortensen, Ian McKellen");
        movie9.setType(MovieType.MOVIE);
        movie9.setImdbRating(9.0);
        movieRepository.save(movie9);

        // Movie 10
        Movie movie10 = new Movie();
        movie10.setTitle("Star Wars: Episode V - The Empire Strikes Back");
        movie10.setDescription("After the Rebels are brutally overpowered by the Empire on the ice planet Hoth, Luke Skywalker begins Jedi training with Yoda, while his friends are pursued across the galaxy.");
        movie10.setReleaseYear(1980);
        movie10.setGenre("Sci-Fi");
        movie10.setDirector("Irvin Kershner");
        movie10.setImageUrl("https://image.tmdb.org/t/p/w500/nNAeTmF4CtdSgMDplXTDPOpYzsX.jpg");
        movie10.setActors("Mark Hamill, Harrison Ford, Carrie Fisher");
        movie10.setType(MovieType.MOVIE);
        movie10.setImdbRating(8.7);
        movieRepository.save(movie10);

        // Movie 11
        Movie movie11 = new Movie();
        movie11.setTitle("Interstellar");
        movie11.setDescription("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.");
        movie11.setReleaseYear(2014);
        movie11.setGenre("Sci-Fi");
        movie11.setDirector("Christopher Nolan");
        movie11.setImageUrl("https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg");
        movie11.setActors("Matthew McConaughey, Anne Hathaway, Jessica Chastain");
        movie11.setType(MovieType.MOVIE);
        movie11.setImdbRating(8.7);
        movieRepository.save(movie11);

        // Movie 12
        Movie movie12 = new Movie();
        movie12.setTitle("The Silence of the Lambs");
        movie12.setDescription("A young FBI cadet must receive the help of an incarcerated and manipulative cannibal killer to help catch another serial killer.");
        movie12.setReleaseYear(1991);
        movie12.setGenre("Thriller");
        movie12.setDirector("Jonathan Demme");
        movie12.setImageUrl("https://image.tmdb.org/t/p/w500/uS9m8OBk1A8eM9I042bx8XXpqAq.jpg");
        movie12.setActors("Jodie Foster, Anthony Hopkins, Scott Glenn");
        movie12.setType(MovieType.MOVIE);
        movie12.setImdbRating(8.6);
        movieRepository.save(movie12);

        // Movie 13
        Movie movie13 = new Movie();
        movie13.setTitle("Saving Private Ryan");
        movie13.setDescription("Following the Normandy Landings, a group of U.S. soldiers go behind enemy lines to retrieve a paratrooper whose brothers have been killed in action.");
        movie13.setReleaseYear(1998);
        movie13.setGenre("War");
        movie13.setDirector("Steven Spielberg");
        movie13.setImageUrl("https://image.tmdb.org/t/p/w500/uqx37qS8W6f55IfLJUXqHcL5Zpk.jpg");
        movie13.setActors("Tom Hanks, Matt Damon, Tom Sizemore");
        movie13.setType(MovieType.MOVIE);
        movie13.setImdbRating(8.6);
        movieRepository.save(movie13);

        // Movie 14
        Movie movie14 = new Movie();
        movie14.setTitle("The Green Mile");
        movie14.setDescription("The lives of guards on Death Row are affected by one of their charges: a black man accused of child murder and rape, yet who has a mysterious gift.");
        movie14.setReleaseYear(1999);
        movie14.setGenre("Drama");
        movie14.setDirector("Frank Darabont");
        movie14.setImageUrl("https://image.tmdb.org/t/p/w500/velWPhVMQeQKcxggNEU8YmIo52R.jpg");
        movie14.setActors("Tom Hanks, Michael Clarke Duncan, David Morse");
        movie14.setType(MovieType.MOVIE);
        movie14.setImdbRating(8.6);
        movieRepository.save(movie14);

        // Movie 15
        Movie movie15 = new Movie();
        movie15.setTitle("The Godfather");
        movie15.setDescription("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.");
        movie15.setReleaseYear(1972);
        movie15.setGenre("Crime");
        movie15.setDirector("Francis Ford Coppola");
        movie15.setImageUrl("https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg");
        movie15.setActors("Marlon Brando, Al Pacino, James Caan");
        movie15.setType(MovieType.MOVIE);
        movie15.setImdbRating(9.2);
        movieRepository.save(movie15);

        // Movie 16
        Movie movie16 = new Movie();
        movie16.setTitle("Schindler's List");
        movie16.setDescription("In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis.");
        movie16.setReleaseYear(1993);
        movie16.setGenre("Drama");
        movie16.setDirector("Steven Spielberg");
        movie16.setImageUrl("https://image.tmdb.org/t/p/w500/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg");
        movie16.setActors("Liam Neeson, Ben Kingsley, Ralph Fiennes");
        movie16.setType(MovieType.MOVIE);
        movie16.setImdbRating(9.0);
        movieRepository.save(movie16);

        // Movie 17
        Movie movie17 = new Movie();
        movie17.setTitle("The Prestige");
        movie17.setDescription("After a tragic accident, two stage magicians engage in a battle to create the ultimate illusion while sacrificing everything they have to outwit each other.");
        movie17.setReleaseYear(2006);
        movie17.setGenre("Drama");
        movie17.setDirector("Christopher Nolan");
        movie17.setImageUrl("https://image.tmdb.org/t/p/w500/bdN3gXuIZYaJP7ftKK2sU0nPtEA.jpg");
        movie17.setActors("Christian Bale, Hugh Jackman, Scarlett Johansson");
        movie17.setType(MovieType.MOVIE);
        movie17.setImdbRating(8.5);
        movieRepository.save(movie17);

        // Movie 18
        Movie movie18 = new Movie();
        movie18.setTitle("Gladiator");
        movie18.setDescription("A former Roman General sets out to exact vengeance against the corrupt emperor who murdered his family and sent him into slavery.");
        movie18.setReleaseYear(2000);
        movie18.setGenre("Action");
        movie18.setDirector("Ridley Scott");
        movie18.setImageUrl("https://image.tmdb.org/t/p/w500/ty8TGRuvJLPUmAR1H1nRIsgwvim.jpg");
        movie18.setActors("Russell Crowe, Joaquin Phoenix, Connie Nielsen");
        movie18.setType(MovieType.MOVIE);
        movie18.setImdbRating(8.5);
        movieRepository.save(movie18);

        // Movie 19
        Movie movie19 = new Movie();
        movie19.setTitle("The Departed");
        movie19.setDescription("An undercover cop and a mole in the police attempt to identify each other while infiltrating an Irish gang in South Boston.");
        movie19.setReleaseYear(2006);
        movie19.setGenre("Crime");
        movie19.setDirector("Martin Scorsese");
        movie19.setImageUrl("https://image.tmdb.org/t/p/w500/nT97ifVT2J1yMQmeq20Qblg61T.jpg");
        movie19.setActors("Leonardo DiCaprio, Matt Damon, Jack Nicholson");
        movie19.setType(MovieType.MOVIE);
        movie19.setImdbRating(8.5);
        movieRepository.save(movie19);

        // Movie 20
        Movie movie20 = new Movie();
        movie20.setTitle("The Usual Suspects");
        movie20.setDescription("A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which began when five criminals met at a seemingly random police lineup.");
        movie20.setReleaseYear(1995);
        movie20.setGenre("Thriller");
        movie20.setDirector("Bryan Singer");
        movie20.setImageUrl("https://image.tmdb.org/t/p/w500/b9vGNSDv7ABFzpJHj3a3gSy2lqI.jpg");
        movie20.setActors("Kevin Spacey, Gabriel Byrne, Chazz Palminteri");
        movie20.setType(MovieType.MOVIE);
        movie20.setImdbRating(8.5);
        movieRepository.save(movie20);
    }

    private void createStarterTVSeries() {
        // TV Series 1
        Movie series1 = new Movie();
        series1.setTitle("Breaking Bad");
        series1.setDescription("A high school chemistry teacher turned methamphetamine producer partners with a former student to secure his family's future after being diagnosed with terminal cancer.");
        series1.setReleaseYear(2008);
        series1.setGenre("Crime");
        series1.setDirector("Vince Gilligan");
        series1.setImageUrl("https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg");
        series1.setActors("Bryan Cranston, Aaron Paul, Anna Gunn");
        series1.setType(MovieType.TV_SERIES);
        series1.setSeasons(5);
        series1.setEpisodes(62);
        series1.setImdbRating(9.5);
        movieRepository.save(series1);

        // TV Series 2
        Movie series2 = new Movie();
        series2.setTitle("Game of Thrones");
        series2.setDescription("Nine noble families fight for control over the lands of Westeros, while an ancient enemy returns after being dormant for millennia.");
        series2.setReleaseYear(2011);
        series2.setGenre("Fantasy");
        series2.setDirector("David Benioff, D.B. Weiss");
        series2.setImageUrl("https://image.tmdb.org/t/p/w500/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg");
        series2.setActors("Emilia Clarke, Peter Dinklage, Kit Harington");
        series2.setType(MovieType.TV_SERIES);
        series2.setSeasons(8);
        series2.setEpisodes(73);
        series2.setImdbRating(9.2);
        movieRepository.save(series2);

        // TV Series 3
        Movie series3 = new Movie();
        series3.setTitle("Stranger Things");
        series3.setDescription("When a young boy disappears, his mother, a police chief and his friends must confront terrifying supernatural forces in order to get him back.");
        series3.setReleaseYear(2016);
        series3.setGenre("Sci-Fi");
        series3.setDirector("The Duffer Brothers");
        series3.setImageUrl("https://image.tmdb.org/t/p/w500/x2LSRK2Cm7MZhjluni1msVJ3wDF.jpg");
        series3.setActors("Millie Bobby Brown, Finn Wolfhard, Winona Ryder");
        series3.setType(MovieType.TV_SERIES);
        series3.setSeasons(4);
        series3.setEpisodes(34);
        series3.setImdbRating(8.7);
        movieRepository.save(series3);

        // TV Series 4
        Movie series4 = new Movie();
        series4.setTitle("The Crown");
        series4.setDescription("Follows the political rivalries and romance of Queen Elizabeth II's reign and the events that shaped the second half of the 20th century.");
        series4.setReleaseYear(2016);
        series4.setGenre("Drama");
        series4.setDirector("Peter Morgan");
        series4.setImageUrl("https://image.tmdb.org/t/p/w500/1M876KPjulVwppEpldhdc8V4o68.jpg");
        series4.setActors("Claire Foy, Olivia Colman, Imelda Staunton");
        series4.setType(MovieType.TV_SERIES);
        series4.setSeasons(6);
        series4.setEpisodes(60);
        series4.setImdbRating(8.6);
        movieRepository.save(series4);

        // TV Series 5
        Movie series5 = new Movie();
        series5.setTitle("The Mandalorian");
        series5.setDescription("The travels of a lone bounty hunter in the outer reaches of the galaxy, far from the authority of the New Republic.");
        series5.setReleaseYear(2019);
        series5.setGenre("Sci-Fi");
        series5.setDirector("Jon Favreau");
        series5.setImageUrl("https://image.tmdb.org/t/p/w500/eU1i6eHXlzMOlEq0ku1Rzq7Y4wA.jpg");
        series5.setActors("Pedro Pascal, Gina Carano, Giancarlo Esposito");
        series5.setType(MovieType.TV_SERIES);
        series5.setSeasons(3);
        series5.setEpisodes(24);
        series5.setImdbRating(8.7);
        movieRepository.save(series5);
    }
}
