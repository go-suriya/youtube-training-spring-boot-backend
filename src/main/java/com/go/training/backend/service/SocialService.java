package com.go.training.backend.service;

import com.go.training.backend.entity.Social;
import com.go.training.backend.entity.User;
import com.go.training.backend.repository.SocialRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SocialService {

    private final SocialRepository socialRepository;

    public SocialService(SocialRepository socialRepository) {
        this.socialRepository = socialRepository;
    }

    public Optional<Social> findByUser(User user) {
        return socialRepository.findByUser(user);
    }

    public Social create(User user, String facebook, String line, String instagram, String tiktok) {
        // TODO: validate

        // create
        Social entity = new Social();

        entity.setUser(user);
        entity.setFacebook(facebook);
        entity.setLine(line);
        entity.setInstagram(instagram);
        entity.setTiktok(tiktok);

        return socialRepository.save(entity);
    }

}