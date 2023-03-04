package es.codeurjc.backend.controller;

import java.util.List;
//import java.util.Date;
import java.sql.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.backend.model.Tweet;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.TweetRepository;
import es.codeurjc.backend.repository.UserRepository;

@RestController
//@RequestMapping("/users")
public class DataBaseController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;
    /*
     * 
     */
    @PostConstruct
    public void init() {
        
        User.Builder builder = new User.Builder();

        builder.setUsername("a").setEmail("a@a.com").setEncodedPassword(passwordEncoder.encode("a"));

        User userA = builder.build();
        User userB = builder.setUsername("userB").build();

        Tweet.Builder tweetBuilder = new Tweet.Builder();

        //-------------------------------------
        tweetBuilder.setAuthor(userA).setText("This is my first tweet!")
        .addTag("halal").addTag("amogus").addTag("sus");
        
        Tweet tweet1 = tweetBuilder.build();

        tweet1.addLike(userB);
        //-------------------------------------
        tweetBuilder.setAuthor(userB).setText("I replied to userA's tweet!").setParent(tweet1);
        
        Tweet tweet2 = tweetBuilder.build();

  
        List<User> aux = userA.getFollowers();
        aux.add(userA);

        userRepository.save(userA);

        tweetRepository.save(tweet1);//TODO hacer esto cada vez que se agregue un like en el code
        tweetRepository.save(tweet2);

        //User auxUser = userRepository.findByUsername("alb014").orElse(null);
        //List<Tweet> aList = auxUser.getTweets();
        //System.out.println(aList.get(0).getText());
    }
    
}