/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import com.mycompany.controllers.AdminJpaController;
import com.mycompany.controllers.StarActorJpaController;
import com.mycompany.controllers.VideosJpaController;
import com.mycompany.controllers.exceptions.NonexistentEntityException;
import com.mycompany.models.Admin;
import com.mycompany.models.StarActor;
import com.mycompany.models.Videos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.ws.soap.MTOM;

/**
 *
 * @author michellanet
 */
@MTOM(enabled = true, threshold = 0)
@WebService
@HandlerChain(file = "MovieAppOpsService_handler.xml")
public class MovieAppOps {

    EntityManagerFactory emf = null;
    VideosJpaController videoCtrl = null;
    AdminJpaController adminCtrl = null;
    StarActorJpaController starActorCtrl = null;
    boolean isAuthorized;

    public MovieAppOps() {
        emf = Persistence.createEntityManagerFactory("videos_PU");
        videoCtrl = new VideosJpaController(emf);
        adminCtrl = new AdminJpaController(emf);
        starActorCtrl = new StarActorJpaController(emf);
        isAuthorized = false;
    }

    @WebMethod
    public boolean postVideo(@WebParam(name = "title") String title, @WebParam(name = "image") byte[] image,
            @WebParam(name = "genre") String genre, @WebParam(name = "type") String type, @WebParam(name = "year") int year,
            @WebParam(name = "starActor") StarActor starActor) throws Exception {

        Videos video = videoCtrl.findVideosEntities().stream().filter(vid -> vid.getTitle().equals(title)).findFirst().orElse(null);
        if (video != null) {
            // if video with title already exists
            return false;
        }

        //support id generation incase autoIncrement fails to auto generate
        long millisID = System.currentTimeMillis();

        video = new Videos();
        video.setId(BigDecimal.valueOf(millisID));
        video.setImage(image);
        video.setTitle(title);
        video.setGenre(genre);
        video.setType(type);
        video.setYear(BigInteger.valueOf(year));

        // if actor is already in database
        if (starActor.getId() != null) {
            video.setStarActorFk(starActor);
        } else {
            //create new actor if not already in database
            starActor.setId(BigDecimal.valueOf(millisID));
            starActorCtrl.create(starActor);

            //perform multilevel comparison to make sure actor is new on db since the new actor would not have an id for absolute comparison yet
            StarActor starActorFirstName = starActorCtrl.findStarActorEntities().stream().filter(actor
                    -> actor.getFirstname().equals(starActor.getFirstname())).findFirst().orElse(null);

            StarActor starActorLastName = starActorCtrl.findStarActorEntities().stream().filter(actor
                    -> actor.getLastname().equals(starActor.getLastname())).findFirst().orElse(null);

            if (starActorFirstName.getFirstname() != null
                    && starActorLastName.getLastname() != null
                    && starActorLastName.getFirstname() == starActorFirstName.getFirstname()
                    && starActorFirstName.getFirstname() == starActorLastName.getFirstname()) {
                starActor.setId(starActorLastName.getId());
                video.setStarActorFk(starActor);
            }
        }
        videoCtrl.create(video);
        return true;
    }

    @WebMethod
    public Videos getVideo(@WebParam(name = "title") String title) {
        Videos video = videoCtrl.findVideosEntities().stream().filter(x -> x.getTitle().equals(title)).findFirst().orElse(null);
        if (video != null) {
            return video;
        } else {
            return new Videos();
        }
    }

    @WebMethod
    public ArrayList<Videos> getVideosByType(@WebParam(name = "type") String type) {
        Iterator iterator = videoCtrl.findVideosEntities().stream().filter(x -> x.getType().equals(type)).iterator();
        ArrayList<Videos> videos = new ArrayList<>();
        while (iterator.hasNext()) {
            videos.add((Videos) iterator.next());
        }
        if (videos != null) {
            return videos;
        } else {
            return new ArrayList<Videos>();
        }
    }

    @WebMethod
    public ArrayList<Videos> getVideos() {
        Iterator iterator = videoCtrl.findVideosEntities().stream().iterator();
        ArrayList<Videos> videos = new ArrayList<>();
        while (iterator.hasNext()) {
            videos.add((Videos) iterator.next());
        }
        if (videos != null) {
            return videos;
        } else {
            return new ArrayList<Videos>();
        }
    }

    @WebMethod
    public boolean putVideo(@WebParam(name = "id") BigDecimal id, @WebParam(name = "title") String title, @WebParam(name = "image") byte[] image,
            @WebParam(name = "genre") String genre,
            @WebParam(name = "type") String type, @WebParam(name = "year") int year,
            @WebParam(name = "starActor") StarActor starActor) throws Exception {
        

        Videos video = videoCtrl.findVideosEntities().stream().filter(vid -> vid.getId().equals(id)).findFirst().orElse(null);
        if (video == null) {
            // if video does not exist
            return false;
        }

        //support id generation incase autoIncrement fails to auto generate
        long millisID = System.currentTimeMillis();

        if (image != null) {
            video.setImage(image);
        }

        video.setTitle(title);
        video.setGenre(genre);
        video.setType(type);
        video.setYear(BigInteger.valueOf(year));

        if (starActor.getId() != null) {
            video.setStarActorFk(starActor);
        } else {
            starActor.setId(BigDecimal.valueOf(millisID));
            starActorCtrl.create(starActor);

            //perform multilevel comparison to make sure actor is new on db since the new actor would not have an id for absolute comparison yet
            StarActor starActorFirstName = starActorCtrl.findStarActorEntities().stream().filter(actor
                    -> actor.getFirstname().equals(starActor.getFirstname())).findFirst().orElse(null);

            StarActor starActorLastName = starActorCtrl.findStarActorEntities().stream().filter(actor
                    -> actor.getLastname().equals(starActor.getLastname())).findFirst().orElse(null);

            if (starActorFirstName.getFirstname() != null
                    && starActorLastName.getLastname() != null
                    && starActorLastName.getFirstname() == starActorFirstName.getFirstname()
                    && starActorFirstName.getFirstname() == starActorLastName.getFirstname()) {
                starActor.setId(starActorLastName.getId());
                video.setStarActorFk(starActor);
            }
        }
        videoCtrl.edit(video);
        return true;
    }

    @WebMethod
    public boolean deleteVideo(@WebParam(name = "title") String title) throws NonexistentEntityException {
        Videos video = videoCtrl.findVideosEntities().stream().filter(x -> x.getTitle().equals(title)).findFirst().orElse(null);
        if (video == null) {
            return false;
        } else {
            videoCtrl.destroy(video.getId());
            return true;
        }
    }

    @WebMethod
    public boolean getAdmin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        Admin admin = adminCtrl.findAdminEntities().stream().filter(x -> x.getUsername().equals(username)).findFirst().orElse(null);
        if (admin != null) {
            isAuthorized = admin.getPassword().equals(password);
            return isAuthorized;
        } else {
            return isAuthorized;
        }
    }

    @WebMethod
    public boolean isSessionAuthorized() {
        return isAuthorized;
    }

    @WebMethod
    public ArrayList<StarActor> getStarActors() {
        Iterator iterator = starActorCtrl.findStarActorEntities().stream().iterator();
        ArrayList<StarActor> starActors = new ArrayList<>();
        while (iterator.hasNext()) {
            starActors.add((StarActor) iterator.next());
        }
        if (starActors != null) {
            return starActors;
        } else {
            return new ArrayList<StarActor>();
        }
    }

    @WebMethod
    public StarActor getStarActor(@WebParam(name = "id") BigDecimal id) {
        StarActor starActor = starActorCtrl.findStarActor(id);
        if (starActor != null) {
            return starActor;
        } else {
            return new StarActor();
        }
    }
}
